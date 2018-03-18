package teammoodi.moodi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.icu.text.AlphabeticIndex;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.phone.PhoneDeviceType;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.NodeClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.wearable.intent.RemoteIntent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.CalendarContract.CalendarCache.URI;

public class Dashboard extends WearableActivity
{
    public static final int REQUESTPERMISSION = 1;

    private Activity mainActivity = this;
    private TextView recordingTextView;

    private boolean isRecording = false;

    MediaRecorder mediaRecorder;
    String AudioSavePathInDevice = null;

    Node connectedNode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recordingTextView = findViewById(R.id.recordingText);

        AudioSavePathInDevice = getFilesDir().getAbsolutePath();
        AudioSavePathInDevice += "/WearAudio.mp4";

        Log.d("MEDIA_RECORDER", "AudioSavePathInDevice - " + AudioSavePathInDevice);

        Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                    List<Node> nodes = Tasks.await(Wearable.getNodeClient(mainActivity)
                            .getConnectedNodes());

                    for (Node node : nodes)
                    {
                        if (node.isNearby())
                            connectedNode = node; // connectedNode is the phone
                    }

                    Log.d("Connected phone ID: ", connectedNode.toString());
                }
                catch (ExecutionException execException){Log.e(
                        "EXECUTION_EXCEPTION", execException.toString());}
                catch (InterruptedException interException){Log.e
                        ("INTERRUPTED_EXCEPTION", interException.toString());}
            }
        };
        thread.start();
    }

    public void recordButton_OnClick(View view)
    {
        if (!isRecording) {
            if (checkPermission()) {
                MediaRecorderReady();

                try {
                    mediaRecorder.prepare();
                    Log.d("MEDIA_RECORDER", "prepare() complete");
                    mediaRecorder.start();
                    Log.d("MEDIA_RECORDER", "start() complete");
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.d("MEDIA_RECORDER", Uri.parse(AudioSavePathInDevice).toString());

                recordingTextView.setText(R.string.stop_recording);
                isRecording = true;
                Toast.makeText(mainActivity, "Recording started", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
        else
        {
            mediaRecorder.stop();
            recordingTextView.setText(R.string.start_recording);
            isRecording = false;

            Asset asset2 = Asset.createFromRef(AudioSavePathInDevice);
            Log.d("MEDIA_RECORDER_2-----", asset2.toString());

            Task<Integer> sendTask = Wearable.getMessageClient(this).sendMessage(
                    connectedNode.getId(), "/wear", asset2.getData());

            Toast.makeText(mainActivity, "Recording completed", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), ReceiveSignal.class);
            startActivity(intent);

            /*
            *  Using a put request to send the data instead of a message
            *    Not being used currently, here just in case it can be used
            *    in the future.


            PutDataMapRequest dataMap = PutDataMapRequest.create(AudioSavePathInDevice);
            Log.d("MEDIA_RECORDER", "PutDataMapRequest created - " +
                  dataMap.getUri().toString());

            dataMap.getDataMap().putAsset("wearRecording", asset2);
            Log.d("MEDIA_RECORDER", "dataMap asset put - " +
                  dataMap.getDataMap().toString());

            PutDataRequest request = dataMap.asPutDataRequest().setUrgent();
            Log.d("MEDIA_RECORDER----", "PutDataRequest done - " +
                  request.toString());

            Task<DataItem> putTask = Wearable
                    .getDataClient(this)
                    .putDataItem(request);

            putTask.addOnCompleteListener(new OnCompleteListener<DataItem>() {
                @Override
                public void onComplete(@NonNull Task<DataItem> task) {
                    Log.d("MEDIA_RECORDER", "putTask complete");
                    Log.d("PUT_TASK_RESULT", putTask.getResult().toString());
                }
            });

            putTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("MEDIA_FAILURE", "putTask failed");
                }
            });

            putTask.addOnSuccessListener(new OnSuccessListener<DataItem>() {
                @Override
                public void onSuccess(DataItem dataItem) {
                    Log.d("MEDIA_SUCCESS", "putTask succeeded - " +
                         dataItem.toString());
                }
            });
            */
        }
    }


    private void requestPermission() {
        Log.d("REQUEST_PERMISSION", "requestPermission() begun");
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, REQUESTPERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUESTPERMISSION:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(mainActivity, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mainActivity, "Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        Log.d("CHECK_PERMISSION", "checkPermission() begun");
        return (ContextCompat.checkSelfPermission(mainActivity, WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(mainActivity, RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED);
    }

    public void MediaRecorderReady()
    {
        mediaRecorder= new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
        Log.d("MEDIA_RECORDER_READY", "MediaRecorderReady() complete");
    }
}