package teammoodi.moodi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.phone.PhoneDeviceType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.NodeClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.wearable.intent.RemoteIntent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.provider.CalendarContract.CalendarCache.URI;

public class Dashboard extends WearableActivity
{
    private Activity mainActivity = this;
    private static final String VOICE_TRANSCRIPTION_CAPABILITY_NAME = "voice_transcription";
    private TextView mTextView;

    // private DataMapItem dmItem = new DataMapItem();

    Node connectedNode;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

    }

    public void recordButton_OnClick(View view)
    {
        /*
        * TODO - Create the recording functionality, set it up like Arthur has it from the mobile
        *        app. Instead of saving it, pass it through the DataItem to the mobile app.
        * */

        Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                    List<Node> nodes = Tasks.await(Wearable.getNodeClient(mainActivity)
                            .getConnectedNodes());

                    for (Node node : nodes)
                    {
                        if (node.isNearby())
                            connectedNode = node; // connectedNode is the phone
                    }

                    Log.d("Connected phone ID: ", connectedNode.toString());

                    // TODO - Put in the functionality here

                    Wearable.getDataClient(mainActivity)
                            .putDataItem
                                    (PutDataRequest
                                    .create("/MOODI/WearData")
                                    .setData(new byte[]{0,0,0}));

                }
                catch (ExecutionException execException){Log.e("0001", execException.toString());}
                catch (InterruptedException interException){Log.e("0002", interException.toString());}
            }
        };
        thread.start();
    }
}