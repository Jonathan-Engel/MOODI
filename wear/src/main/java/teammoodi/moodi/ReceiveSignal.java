package teammoodi.moodi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class ReceiveSignal extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_signal);

        // Enables Always-on
        setAmbientEnabled();

        ImageView emotionalColor = findViewById(R.id.background_color);
        TextView emotionalText = findViewById(R.id.emotion_text);
        TextView processingText = findViewById(R.id.processing_text);

        Wearable.getMessageClient(this).addListener(new MessageClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(@NonNull MessageEvent messageEvent) {
                byte[] messageArr = messageEvent.getData();
                switch(messageArr[0]){
                    case 0:
                        emotionalColor.setImageResource(R.color.response_anger);
                        emotionalText.setText(R.string.emotion_anger);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Anger");
                        break;
                    case 1:
                        emotionalColor.setImageResource(R.color.response_sadness);
                        emotionalText.setText(R.string.emotion_sadness);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Sadness");
                        break;
                    case 2:
                        emotionalColor.setImageResource(R.color.response_fear);
                        emotionalText.setText(R.string.emotion_fear);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Fear");
                        break;
                    case 3:
                        emotionalColor.setImageResource(R.color.response_joy);
                        emotionalText.setText(R.string.emotion_joy);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Joy");
                        break;
                    case 4:
                        emotionalColor.setImageResource(R.color.response_analytical);
                        emotionalText.setText(R.string.emotion_analytical);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Analytical");
                        break;
                    case 5:
                        emotionalColor.setImageResource(R.color.response_confident);
                        emotionalText.setText(R.string.emotion_confident);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Confident");
                        break;
                    case 6:
                        emotionalColor.setImageResource(R.color.response_tentative);
                        emotionalText.setText(R.string.emotion_tentative);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Tentative");
                        break;
                    case 10:
                        emotionalColor.setImageResource(R.color.response_none);
                        emotionalText.setText(R.string.emotion_none);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "None");
                        break;
                    case 99:
                        Toast.makeText(getApplicationContext(),
                                "Audio length too short, please retry.",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(intent);
                        break;
                    default:
                        emotionalColor.setImageResource(R.color.black);
                        emotionalText.setText(R.string.emotion_error);
                        processingText.setVisibility(View.INVISIBLE);
                        emotionalText.setVisibility(View.VISIBLE);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "emotion default");
                        break;
                }
            }
        });
    }

    public void backButton_onClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
    }
}