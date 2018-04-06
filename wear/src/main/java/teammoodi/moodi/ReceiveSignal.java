package teammoodi.moodi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class ReceiveSignal extends WearableActivity {

/*
    *
    * Once the user stops recording, they get sent to this page.
    * Once at this page, they must wait until a response is given from the phone.
    * Once the response is given, an image will appear depending on the content of the response.
    * Once the user taps on the image, they get taken back to the previous page where they can
    *   record again.
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_signal);

        // Enables Always-on
        setAmbientEnabled();

        ImageView emotionalColor = findViewById(R.id.emotion_color);
        TextView emotionalText = findViewById(R.id.emotion_text);

        Wearable.getMessageClient(this).addListener(new MessageClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(@NonNull MessageEvent messageEvent) {
                byte[] messageArr = messageEvent.getData();
                switch(messageArr[0]){
                    case 0:
                        Log.d("EMOTION", "Anger");
                        emotionalColor.setImageResource(R.color.response_anger);
                        emotionalText.setText(R.string.emotion_anger);
                        emotionalColor.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        emotionalColor.setImageResource(R.color.response_sadness);
                        emotionalText.setText(R.string.emotion_sadness);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Sadness");
                        break;
                    case 2:
                        emotionalColor.setImageResource(R.color.response_fear);
                        emotionalText.setText(R.string.emotion_fear);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Fear");
                        break;
                    case 3:
                        emotionalColor.setImageResource(R.color.response_joy);
                        emotionalText.setText(R.string.emotion_joy);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Joy");
                        break;
                    case 4:
                        emotionalColor.setImageResource(R.color.response_analytical);
                        emotionalText.setText(R.string.emotion_analytical);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Analytical");
                        break;
                    case 5:
                        emotionalColor.setImageResource(R.color.response_confident);
                        emotionalText.setText(R.string.emotion_confident);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Confident");
                        break;
                    case 6:
                        emotionalColor.setImageResource(R.color.response_tentative);
                        emotionalText.setText(R.string.emotion_tentative);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "Tentative");
                        break;
                    case 10:
                        emotionalColor.setImageResource(R.color.response_none);
                        emotionalText.setText(R.string.emotion_none);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "None");
                        break;
                    default:
                        emotionalColor.setImageResource(R.color.black);
                        emotionalText.setText(R.string.emotion_error);
                        emotionalColor.setVisibility(View.VISIBLE);
                        Log.d("EMOTION", "emotion default");
                        break;
                }
            }
        });
    }

    // Need to set the image to visibility=visible to make it able to be clicked
    public void emotionalImage_OnClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
    }
}
