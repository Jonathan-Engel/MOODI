package teammoodi.moodi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnRecordFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public static final int REQUESTPERMISSION = 1;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;

    private OnRecordFragmentInteractionListener mListener;
    private LottieAnimationView signal;

    String sendLocation;
    String frenchpref = "false";
    String profanpref = "false";

    Activity curActivity;
    Node wearNode;
    int nodeCount = 0;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        curActivity = getActivity();

        try {
            List<Node> nodes = Tasks.await(Wearable.getNodeClient(curActivity)
                    .getConnectedNodes());

            for (Node node : nodes) {
                if (node.isNearby())
                    wearNode = node;
            }
        }
        catch (ExecutionException executionException){}
        catch (InterruptedException interruptedException){}

        Wearable.getMessageClient(curActivity).addListener(new MessageClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(@NonNull MessageEvent messageEvent) {
                Log.d("MESSAGE_EVENT", "Message from wear received");
                Log.d("MESSAGE_EVENT", "Path: " + messageEvent.getPath());
                Log.d("MESSAGE_EVENT", "Data: " + messageEvent.getData());

                try {
                    OutputStream out = new FileOutputStream(Environment
                            .getExternalStorageDirectory()
                            .getAbsolutePath() + "/moodiWearRecording.m4a");
                    out.write(messageEvent.getData());
                    out.close();

                    LocationManager lm = (LocationManager) curActivity.getSystemService(Context.LOCATION_SERVICE);
                    @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null)
                        sendLocation = "";
                    else
                        sendLocation = location.getLongitude() + " " + location.getLatitude();

                    new AsyncProcessAudio().execute(Environment
                            .getExternalStorageDirectory()
                            .getAbsolutePath() + "/moodiWearRecording.m4a");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MESSAGE_LISTENER", "Message listener successfully registered");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        LottieAnimationView animationView = v.findViewById(R.id.animation_view);
        animationView.setOnClickListener(this);
        signal = v.findViewById(R.id.processing_view);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecordFragmentInteractionListener) {
            mListener = (OnRecordFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.animation_view:
                ProcessAudio(view);
                break;
            default:
                break;
        }
    }

    private boolean bIsProcessing = false;

    public void ProcessAudio(View v) {
        LottieAnimationView view = (LottieAnimationView) v;
        if (bIsProcessing) {
            view.setRepeatCount(1);
            mediaRecorder.stop();

            LocationManager lm = (LocationManager) v.getContext().getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null)
                sendLocation = "";
            else
                sendLocation = location.getLatitude() + " " + location.getLongitude();

            bIsProcessing = false;
            Toast.makeText(getActivity(), "Processing", Toast.LENGTH_SHORT).show();

            frenchpref = String.valueOf(PreferenceManager.getDefaultSharedPreferences(
                    this.getContext()).getBoolean(getString(R.string.prefFrench), false));
            profanpref = String.valueOf(PreferenceManager.getDefaultSharedPreferences(
                    this.getContext()).getBoolean(getString(R.string.prefProfanity), false));

            new AsyncProcessAudio()
                    .execute(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "MOODIAudioRecording.m4a");

            return;
        }
        if(checkPermission()) {

            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "MOODIAudioRecording.m4a";

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            view.playAnimation();
            Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_SHORT).show();
            bIsProcessing = true;
            signal.playAnimation();
            view.playAnimation();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(curActivity, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUESTPERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUESTPERMISSION:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean Gps1 = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean Gps2 = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission && Gps1 && Gps2) {
                        Toast.makeText(getActivity(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(curActivity, WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(curActivity, RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(curActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(curActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    public void MediaRecorderReady(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(256000);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecordFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class AsyncProcessAudio extends AsyncTask<String, ContentValues, ContentValues>
    {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected ContentValues doInBackground(String... params) {
            Map<String, String> p = new HashMap<>(2);
            Map<String, Double> tonesMap = new HashMap<String, Double>();

            p.put("Location", sendLocation);
            p.put("French", frenchpref);
            p.put("Profanity", profanpref);

            String result = multipartRequest("http://34.217.90.146/ProcessAudio", p, params[0], "audio_sample", "audio/mp4");

            ContentValues values = new ContentValues();

            try {
                JSONObject mainMoodiObj = new JSONObject(result);
                if(mainMoodiObj != null){
                    Integer id = mainMoodiObj.getInt("id");
                    values.put("id", id);

                    JSONObject moodiObj = mainMoodiObj.getJSONObject("moodi");
                    if(moodiObj != null){

                        JSONObject transcriptObj = moodiObj.getJSONObject("transcript");
                        if (transcriptObj != null) {
                            String text = transcriptObj.getString("text");
                            values.put("transcript", text);

                            Double confidence = transcriptObj.getDouble("confidence");
                            values.put("confidence", confidence);
                        }

                        JSONObject emotionObj = moodiObj.getJSONObject("emotion");
                        if (emotionObj != null){
                            JSONObject document_tone1 = emotionObj.getJSONObject("document_tone");
                            JSONObject document_tone2 = document_tone1.getJSONObject("document_tone");
                            if(document_tone2 != null){
                                JSONArray tonesArr = document_tone2.getJSONArray("tones");
                                for (int i = 0; i < tonesArr.length(); i++) {
                                    values.put(
                                            tonesArr.getJSONObject(i).getString("tone_id"),
                                            tonesArr.getJSONObject(i).getDouble("score")
                                    );
                                }
                            }
                        }

                        String geoText = moodiObj.getString("geo");
                        values.put("location", geoText);

                        String timeText = moodiObj.getString("created");
                        values.put("timestamp", timeText);
                    }

                    return values;
                }
            } catch (JSONException e){
                Log.e("Moodi-App", "Failed to parse response/upload it to the database", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ContentValues result) {
            if (result == null) {
                Toast.makeText(getContext(), "Audio length is too short. Please try again.", Toast.LENGTH_LONG).show();
                signal.setRepeatMode(0);

                if (wearNode != null)
                {
                    Wearable.getMessageClient(curActivity).sendMessage(wearNode.getId(),"/moodiResult",
                            new byte[]{99});
                    // Send message back to wear saying that the audio length is too short.
                }
                return;
            }

            EmotionalResponseDB db = EmotionalResponseDB.getInstance(getActivity());
            db.AddResult(result);
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager == null)
                return;
            else
                fragmentManager.beginTransaction().replace(R.id.content_frame, new HistoryFragment()).commit();

                // Sending the message back to the watch with the result
                Thread thread = new Thread() {
                    public void run() {
                            String resultForWear = EmotionalResponseDB.getInstance(getActivity())
                                    .GetResults(1)
                                    .get(0)
                                    .getPrimaryEmotion();

                            byte resultByte;
                            switch (resultForWear) {
                                case "Anger":
                                    resultByte = 0;
                                    break;
                                case "Sadness":
                                    resultByte = 1;
                                    break;
                                case "Fear":
                                    resultByte = 2;
                                    break;
                                case "Joy":
                                    resultByte = 3;
                                    break;
                                case "Analytical":
                                    resultByte = 4;
                                    break;
                                case "Confident":
                                    resultByte = 5;
                                    break;
                                case "Tentative":
                                    resultByte = 6;
                                    break;
                                default:
                                    resultByte = 10;
                                    break;
                            }

                            Wearable.getMessageClient(curActivity)
                                    .sendMessage(wearNode.getId(), "/moodiResult", new byte[]{resultByte})
                                    .addOnSuccessListener(new OnSuccessListener<Integer>() {
                                        @Override
                                        public void onSuccess(Integer integer) {
                                            Log.d("MESSAGE_TO_WEAR", "Message successfully sent");
                                        }
                                    });
                    }
                };
                if (wearNode != null)
                    thread.start();
        }
    }


    public String multipartRequest(String urlTo, Map<String, String> parmas, String filepath, String filefield, String fileMimeType) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = parmas.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = parmas.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            if (200 != connection.getResponseCode()) {
                return "error";
            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {

            Log.d("MULTIPARTREQUEST", "Error: " + e);
            return "error";
        }

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}