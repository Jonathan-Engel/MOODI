package teammoodi.moodi;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class Dashboard extends AppCompatActivity {

    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        
        button2 = findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchActivity();
            }
        });


    }

    private void launchActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /* TRYING OUT THE MESSAGE PASSING BETWEEN THE WEAR AND THE PHONE
    *
    * Added
    *   implements MessageClient.OnMessageReceivedListener
    *   the code below
    *
    public static final String VOICE_TRANSCRIPTION_MESSAGE_PATH = "/voice_transcription";

    @Override
    public void onMessageReceived(MessageEvent messageEvent)
    {
        if (messageEvent.getPath().equals(VOICE_TRANSCRIPTION_MESSAGE_PATH))
        {
            Intent startIntent = new Intent(this, Dashboard.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.putExtra("VOICE_DATA", messageEvent.getData());
            startActivity(startIntent);
        }
    } */

    // DataClient dataClient = Wearable.getDataClient(this);

    // 192.168.1.176:5555


}
