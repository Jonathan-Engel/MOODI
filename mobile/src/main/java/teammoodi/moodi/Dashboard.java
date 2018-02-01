package teammoodi.moodi;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    // Sample rate to record the audio at
    private int SAMPLE_RATE = 8000;

    TextView mTextView;

    // Get the min buffer size needed for recording
    int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

    // Make a new short[] to store the audio data in
    short[] audioData = new short[minBufferSize];

    AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBufferSize);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mTextView = (TextView) findViewById(R.id.dashText);
    }


    /* Record audio when the button is pressed.
    * AudioRecord(Mic input, 8000hz sample rate, MONO channel configuration,
    *             16bit encoding, min buffer size)
    *
    * */
    public void recordAudio(View view)
    {
        if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING)
        {
            audioRecord.stop();

            mTextView.setText(R.string.stopped_recording);

            Button recordButton = (Button) findViewById(R.id.recordButton);
            recordButton.setText(R.string.start_recording_button);
        }

        // If audioRecord object got all of its resources successfully
        if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED)
        {
            audioRecord.startRecording();

            // Store data as it is being recorded?
            mTextView.setText(R.string.started_recording);

            Button recordButton = (Button) findViewById(R.id.recordButton);
            recordButton.setText(R.string.stop_recording_button);
        }
        else
        {
            mTextView.setText(R.string.error_recording);
            // Throw an error.
        }
    }
}
