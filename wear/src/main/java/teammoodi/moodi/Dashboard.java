package teammoodi.moodi;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class Dashboard extends WearableActivity {

    private TextView mTextView;
    public static final String VOICE_TRANSCRIPTION_MESSAGE_PATH = "/voice_transcription";
    private static final String VOICE_TRANSCRIPTION_CAPABILITY_NAME = "voice_transcription";

    private String transcriptionNodeID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
    }

    private void setupVoiceTranscription() {
        CapabilityInfo capabilityInfo = Tasks.await(
                Wearable.getCapabilityClient(context).getCapability(
                        VOICE_TRANSCRIPTION_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE));

        // capabilityInfo now has the reachable nodes(wear) w/ the transcription capability
        updateTranscriptionCapability(capabilityInfo);

        CapabilityClient.OnCapabilityChangedListener capabilityChangedListener =
                capabilityInfo -> {
                    updateTranscriptionCapability(capabilityInfo);
                };
        Wearable.getCapabilityClient(context).addListener(capabilityChangedListener,
                VOICE_TRANSCRIPTION_CAPABILITY_NAME);
    }

    // Update the correct node to use for the transcription
    private void updateTranscriptionCapability(CapabilityInfo capInfo) {
        Set<Node> nodes = capInfo.getNodes();
        transcriptionNodeID = getBestNodeID(nodes);
    }

    // Get the closest(best) node that is connected to the wearable
    private String getBestNodeID(Set<Node> nodes) {
        String bestNode = null;

        for (Node node : nodes) {
            // Find nearest node
            if (node.isNearby()) {
                return node.getId();
            }
        }
        // If no nodes are connected
        return null;
    }

    private void requestTranscription(byte[] voiceData) {
        if (transcriptionNodeID != null) {
            Task<Integer> sendTask = Wearable.getMessageClient(context).sendMessage(
                    transcriptionNodeID, VOICE_TRANSCRIPTION_MESSAGE_PATH, voiceData);

            // Add success listener
            //sendTask.addOnSuccessListener();
            // Add failure listener
            //sendTask.addOnFailureListener();
            // Or call Tasks.await() and catch ExecutionException
        } else {
            // Unable to retrieve node w/ transcription capability
        }
    }

    /* Within the Dashboard activity of the mobile app, add the message
     * received listener

    @Override
    public void onMessageReceived(MessageEvent messageEvent)
    {
        if (messageEvent.getPath().equals(VOICE_TRANSCRIPTION_MESSAGE_PATH))
        {
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.putExtra("VOICE_DATA", messageEvent.getData());
            startActivity(startIntent);
        }
    }

    */
}
