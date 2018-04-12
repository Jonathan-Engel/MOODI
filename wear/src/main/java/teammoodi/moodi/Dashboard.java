package teammoodi.moodi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.icu.text.AlphabeticIndex;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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

        AudioSavePathInDevice = getApplicationContext().getFilesDir().getAbsolutePath();
        AudioSavePathInDevice += "/WearAudio.m4a";

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
                    if (connectedNode != null)
                        Log.d("Connected phone ID ", connectedNode.toString());
                    else
                        Log.d("Connected phone NULL", "No connected phone");
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

            Toast.makeText(mainActivity, "Recording completed", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), ReceiveSignal.class);
            startActivity(intent);

            try {
                FileInputStream fileInputStream = new FileInputStream(AudioSavePathInDevice);
                Log.d("FILE_INPUT", ".available(): " + fileInputStream.available());

                byte[] bArr = new byte[fileInputStream.available()];
                Log.d("FILE_INPUT", "bArr.toString(): " + bArr.toString());
                Log.d("FILE_INPUT", "bArr.length(BEFORE): " + bArr.length);

                fileInputStream.read(bArr, 0, bArr.length);
                Log.d("FILE_INPUT", "bArr.length(AFTER): " + bArr.length);
                Log.d("FILE_INPUT", "bArr.toString(AFTER)" + bArr.toString());

                Wearable.getMessageClient(this)
                        .sendMessage(connectedNode.getId(),
                        "/teammoodi/moodi/wear", bArr);

                Log.d("MESSAGE_CLIENT", "Message sent");
            }catch (Exception e){
                e.printStackTrace();
            }
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
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(256000);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
        Log.d("MEDIA_RECORDER_READY", "MediaRecorderReady() complete");
    }

    public void temp_onClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ReceiveSignal.class);
        startActivity(intent);
    }
}