package teammoodi.moodi;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

public class SignalReceiver extends WearableActivity
{
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
        setContentView(R.layout.activity_dashboard);
    }

    // Need to set the image to visibility=visible to make it able to be clicked
    public void emotionalImage_OnClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
    }
}