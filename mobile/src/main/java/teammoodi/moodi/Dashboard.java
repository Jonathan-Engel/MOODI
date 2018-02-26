package teammoodi.moodi;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class Dashboard extends AppCompatActivity {

    private Button SignUpButton;
    private Button LogInButton;
    private Button GotoRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        /* Receive message from Wear
        MessageClient.OnMessageReceivedListener onMessageReceivedListener =
                (new MessageClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(@NonNull MessageEvent messageEvent)
            {
                if (messageEvent.getPath().equalsIgnoreCase("/moodiWear_Message"))
                {
                    Log.d("Message received from wear", messageEvent.getData().toString());

                    System.out.println(messageEvent.getData());
                }
            }
        });*/
        DataClient.OnDataChangedListener onDataChangedListener = (new DataClient.OnDataChangedListener() {
            @Override
            public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
                Log.d("WEAR MESSAGE RECEIVED", dataEventBuffer
                                                    .get(0)
                                                    .getDataItem()
                                                    .getData()
                                                    .toString());
            }
        });

        Wearable.getDataClient(this).addListener(onDataChangedListener);

        // Done with wear messaging

        SignUpButton = findViewById(R.id.SignUpBotton);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignUp();
            }
        });

        LogInButton = findViewById(R.id.LogInButton);
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLogIn();
            }
        });

        GotoRecord = findViewById(R.id.gotorecordbutton);
        GotoRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Record.class);
                startActivity(intent);
            }
        });
    }

    private void launchSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void launchLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
