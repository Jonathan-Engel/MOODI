package teammoodi.moodi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.phone.PhoneDeviceType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeClient;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Dashboard extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mTextView = findViewById(R.id.text);



        // Enables Always-on
        setAmbientEnabled();
    }

    public void recordButton_OnClick(View view)
    {
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
