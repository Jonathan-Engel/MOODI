package teammoodi.moodi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.phone.PhoneDeviceType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.NodeClient;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Dashboard extends WearableActivity
{
    private Activity mainActivity = this;
    private static final String VOICE_TRANSCRIPTION_CAPABILITY_NAME = "voice_transcription";
    private TextView mTextView;
    Node connectedNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    private void setupVoiceTranscription() throws ExecutionException, InterruptedException
    {
        CapabilityInfo capabilityInfo = Tasks.await(Wearable.getCapabilityClient(this)
                        .getCapability(VOICE_TRANSCRIPTION_CAPABILITY_NAME,
                                CapabilityClient.FILTER_REACHABLE));

        // capabilityInfo now has the reachable nodes w/ transcription capability
        updateTranscriptionCapability(capabilityInfo);
    }

    private void updateTranscriptionCapability(CapabilityInfo capabilityInfo)
    {

    }

    public void recordButton_OnClick(View view)
    {
        Thread thread = new Thread()
        {
            public void run()
            {
                try {
                    List<Node> nodes = Tasks.await(Wearable.getNodeClient(mainActivity).getConnectedNodes());

                    // setupVoiceTranscription();
                    Log.d("IT WORKED", nodes.toString()); }
                catch (ExecutionException execException){Log.e("0001", execException.toString());}
                catch (InterruptedException interException){Log.e("0002", interException.toString());}
            }
        };

        thread.start();



        Log.d("00001", "-----Record button has been clicked--------");
        Log.d("00002", "getPhoneDeviceType(this)");

        int phoneDevType = PhoneDeviceType.getPhoneDeviceType(getApplicationContext());

        Log.d("00003", "Android device value is: " + PhoneDeviceType.DEVICE_TYPE_ANDROID);
        Log.d("00004", "phoneDevType: " + phoneDevType);
    }


    /* Within the Dashboard activity of the mobile app, add the message
     * received listener
    */
}
