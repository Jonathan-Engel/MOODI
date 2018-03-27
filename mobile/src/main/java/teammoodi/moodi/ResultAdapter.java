package teammoodi.moodi;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ahb5190 on 3/24/18.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    private RecyclerView rview;

    ArrayList<MoodiResult> dataList;

    int mExpandedPosition = -1;

    public ResultAdapter(ArrayList<MoodiResult> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);
        rview = view.findViewById(R.id.results_recycler_view);
        return new ResultViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {

        final int pos = position;
        final boolean isExpanded = position == mExpandedPosition;
        holder.cardview.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : pos;
                TransitionManager.beginDelayedTransition(rview);
                notifyDataSetChanged();
            }
        });
        //TODO: FIX THIS SHITTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT

        holder.primaryTextView.setText(
                dataList.get(position).getPrimaryEmotion() + ": " + dataList.get(position).getPrimaryEmotionScore());

        holder.angerTextView.setText("Anger: " + dataList.get(position).getAnger() + "%");
        holder.sadnessTextView.setText("Sadness: " + dataList.get(position).getSadness() + "%");
        holder.fearTextView.setText("Fear: " + dataList.get(position).getFear() + "%");
        holder.joyTextView.setText("Joy: " + dataList.get(position).getJoy() + "%");
        holder.analyticalTextView.setText("Analytical: " + dataList.get(position).getAnalytical() + "%");
        holder.confidentTextView.setText("Confident: " + dataList.get(position).getConfident() + "%");
        holder.tentativeTextView.setText("Tentative: " + dataList.get(position).getTentative() + "%");
        holder.transcriptTextView.setText("Transcript: " + dataList.get(position).getTranscript());
        holder.confidenceTextView.setText("Total confidence: " + dataList.get(position).getConfidence() + "%\n");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView angerTextView, sadnessTextView, fearTextView, joyTextView, analyticalTextView,
                confidentTextView, tentativeTextView, transcriptTextView, confidenceTextView, primaryTextView;

        View cardview;

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
            primaryTextView = view.findViewById(R.id.primaryTextView);
            cardview = view.findViewById(R.id.cardview_contents);
        }
    }
}