package teammoodi.moodi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahb5190 on 3/24/18.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<MoodiResult> dataList;

    public ResultAdapter(ArrayList<MoodiResult> dataList) {
        this.dataList = dataList;
    }

//    public ResultAdapter(ArrayList<MoodiResult> dataList, Context context, Cursor cursor) {
//        this.dataList = dataList;
//        this.context = context;
//        this.cursor = cursor;
//    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        holder.angerTextView.setText(dataList.get(position).getAnger());
        holder.sadnessTextView.setText(dataList.get(position).getSadness());
        holder.fearTextView.setText(dataList.get(position).getFear());
        holder.joyTextView.setText(dataList.get(position).getJoy());
        holder.analyticalTextView.setText(dataList.get(position).getAnalytical());
        holder.confidentTextView.setText(dataList.get(position).getConfident());
        holder.tentativeTextView.setText(dataList.get(position).getTentative());
        holder.transcriptTextView.setText(dataList.get(position).getTranscript());
        holder.confidenceTextView.setText(dataList.get(position).getConfidence());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView angerTextView, sadnessTextView, fearTextView, joyTextView, analyticalTextView,
                confidentTextView, tentativeTextView, transcriptTextView, confidenceTextView;

        ResultViewHolder(View view) {
            super(view);
            angerTextView = view.findViewById(R.id.angerTextView);
            sadnessTextView = view.findViewById(R.id.sadnessTextView);
            fearTextView = view.findViewById(R.id.fearTextView);
            joyTextView = view.findViewById(R.id.joyTextView);
            analyticalTextView = view.findViewById(R.id.analyticalTextView);
            confidentTextView = view.findViewById(R.id.confidentTextView);
            tentativeTextView = view.findViewById(R.id.tentativeTextView);
            transcriptTextView = view.findViewById(R.id.transcriptTextView);
            confidenceTextView = view.findViewById(R.id.confidenceTextView);
        }
    }
}