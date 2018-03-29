package teammoodi.moodi;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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

        //if(pos == 0)
        //    holder.cardviewcontents.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        //else
            holder.cardviewcontents.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AutoTransition autoTransition = new AutoTransition();
                //autoTransition.setDuration(3000);
                //TransitionManager.beginDelayedTransition((ViewGroup) v.findViewById(R.id.cardcard), autoTransition);
                //TransitionManager.beginDelayedTransition(cardview);
                mExpandedPosition = isExpanded ? -1 : pos;
                notifyDataSetChanged();
            }
        });

        holder.primaryTextView.setText(
                dataList.get(position).getPrimaryEmotion() + ": " + String.format
                        ("%.2f", Double.parseDouble(dataList.get(position).getPrimaryEmotionScore()) * 100) + "%");

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

        String[] emotions = new String[]{
        Double.parseDouble(dataList.get(position).getAnger()) * 100 + ":Anger",
        Double.parseDouble(dataList.get(position).getSadness()) * 100 + ":Sadness",
        Double.parseDouble(dataList.get(position).getFear()) * 100 + ":Fear",
        Double.parseDouble(dataList.get(position).getJoy()) * 100 + ":Joy",
        Double.parseDouble(dataList.get(position).getAnalytical()) * 100 + ":Analytical",
        Double.parseDouble(dataList.get(position).getConfident()) * 100 + ":Confident",
        Double.parseDouble(dataList.get(position).getTentative()) * 100 + ":Tentative"
        };

        Arrays.sort(emotions, Collections.reverseOrder());
        for(int i = 0; i < emotions.length; i++)
        {
            String[] temp = emotions[i].split(":");

            switch (i) {
                case 0:
                    holder.firstTextView.setText(temp[1] + ": " + String.format("%.2f", Double.parseDouble(temp[0])) + "%");
                    break;
                case 1:
                    holder.secondTextView.setText(temp[1] + ": " + String.format("%.2f", Double.parseDouble(temp[0])) + "%");
                    break;
                case 2:
                    holder.thirdTextView.setText(temp[1] + ": " + String.format("%.2f", Double.parseDouble(temp[0])) + "%");
                    break;
                case 3:
                    holder.forthTextView.setText(temp[1] + ": " + String.format("%.2f", Double.parseDouble(temp[0])) + "%");
                    break;
                case 4:
                    holder.fifthTextView.setText(temp[1] + ": " + String.format("%.2f", Double.parseDouble(temp[0])) + "%");
                    break;
                case 5:
                    holder.sixthTextView.setText(temp[1] + ": " + String.format("%.2f", Double.parseDouble(temp[0])) + "%");
                    break;
                case 6:
                    holder.seventhTextView.setText(temp[1] + ": " + String.format("%.2f", Double.parseDouble(temp[0])) + "%\n");
                    break;
            }
        }

        holder.transcriptTextView.setText("Transcript: " + dataList.get(position).getTranscript());
        holder.confidenceTextView.setText("Total confidence: " + String.format("%.2f", Double.parseDouble(dataList.get(position).getConfidence()) * 100) + "%\n");
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView firstTextView, secondTextView, thirdTextView, forthTextView, fifthTextView,
                sixthTextView, seventhTextView, transcriptTextView, confidenceTextView, primaryTextView;

        View cardviewcontents;
        View cardviewheader;

        ResultViewHolder(View view) {
            super(view);
            firstTextView = view.findViewById(R.id.firstTextView);
            secondTextView = view.findViewById(R.id.secondTextView);
            thirdTextView = view.findViewById(R.id.thirdTextView);
            forthTextView = view.findViewById(R.id.forthTextView);
            fifthTextView = view.findViewById(R.id.fifthTextView);
            sixthTextView = view.findViewById(R.id.sixthTextView);
            seventhTextView = view.findViewById(R.id.seventhTextView);
            transcriptTextView = view.findViewById(R.id.transcriptTextView);
            confidenceTextView = view.findViewById(R.id.confidenceTextView);
            primaryTextView = view.findViewById(R.id.primaryTextView);

            cardviewcontents = view.findViewById(R.id.cardview_contents);
            cardviewheader = view.findViewById(R.id.cardview_header);
        }
    }
}