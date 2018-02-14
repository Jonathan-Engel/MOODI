package teammoodi.moodi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import java.util.Random;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Record extends AppCompatActivity{

    Random random;

    private Button recordButton;
    private Button backButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        random = new Random();

        recordButton = findViewById(R.id.recordbutton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mehhhh
            }
        });

        stopButton = findViewById(R.id.stopbutton);
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mehhhh
            }
        });

        backButton = findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
            }
        });
    }
}
