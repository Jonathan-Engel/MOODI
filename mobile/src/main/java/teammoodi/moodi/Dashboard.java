package teammoodi.moodi;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.rtp.AudioStream;
import android.os.Environment;
import android.os.Message;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Dashboard extends AppCompatActivity {

    private Activity curActivity = this;
    private Button SignUpButton;
    private Button LogInButton;
    private Button GotoRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /*
        Listen for wear sending a message
        */

        Wearable.getMessageClient(this).addListener(new MessageClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(@NonNull MessageEvent messageEvent) {
                Log.d("MESSAGE_RECEIVED", "Got message");
                Log.d("MESSAGE_RECEIVED", "MessageEvent - getData() - " +
                        messageEvent.getData().toString());
                Log.d("MESSAGE_RECEIVED", "MessageEvent - getPath() - " +
                        messageEvent.getPath());
                Uri uri = Uri.parse(messageEvent.getPath());
                try
                {
                    MediaStore.Audio.Media.getContentUriForPath(messageEvent.getPath());
                    FileWriter fw = new FileWriter(
                            Environment.getExternalStorageDirectory()
                                    .getAbsolutePath() + "/WearAudio.mp4");
                    fw.write(uri.toString());
                    fw.close();
                }catch (IOException e)
                {
                    Log.e("FILE_WRITER", "IO Exception - " + e);
                }
            }
        })
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("MESSAGE_LISTENER", "Message listener complete");
            }
        });

        /* Done with wear messaging */

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
