package teammoodi.moodi;

import android.graphics.Color;
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
    private ArrayList<MoodiResult> dataList;

    private int mExpandedPosition = -1;

    public ResultAdapter(ArrayList<MoodiResult> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        final int pos = position;
        final boolean isExpanded = position == mExpandedPosition;
        holder.cardviewcontents.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AutoTransition autoTransition = new AutoTransition();
                //autoTransition.setDuration(3000);
                //TransitionManager.beginDelayedTransition((ViewGroup) v.findViewById(R.id.cardcard), autoTransition);
                mExpandedPosition = isExpanded ? -1 : pos;
                notifyDataSetChanged();
            }
        });

        holder.primaryTextView.setText(
                dataList.get(position).getPrimaryEmotion() + ": " + dataList.get(position).getPrimaryEmotionScore());

        String primaryEmotion = dataList.get(position).getPrimaryEmotion();

        switch (primaryEmotion) {
            case "Joy":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#efcb1d"));
                holder.primaryTextView.setTextColor(Color.BLACK);
                break;
            case "None":
                holder.cardviewheader.setBackgroundColor(Color.LTGRAY);
                holder.primaryTextView.setTextColor(Color.BLACK);
                break;
            case "Anger":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#a50e13"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                break;
            case "Sadness":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#44889f"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                break;
            case "Fear":
                holder.cardviewheader.setBackgroundColor(Color.DKGRAY);
                holder.primaryTextView.setTextColor(Color.WHITE);
                break;
            case "Analytical":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#edae9c"));
                holder.primaryTextView.setTextColor(Color.BLACK);
                break;
            case "Confident":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#f37028"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                break;
            case "Tentative":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#3db68b"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                break;
        }

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

    class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView angerTextView, sadnessTextView, fearTextView, joyTextView, analyticalTextView,
                confidentTextView, tentativeTextView, transcriptTextView, confidenceTextView, primaryTextView;

        View cardviewcontents;
        View cardviewheader;

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

            cardviewcontents = view.findViewById(R.id.cardview_contents);
            cardviewheader = view.findViewById(R.id.cardview_header);
        }
    }
}