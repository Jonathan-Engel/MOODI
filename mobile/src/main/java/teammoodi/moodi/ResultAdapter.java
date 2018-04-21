package teammoodi.moodi;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

        if(pos == 0)
            holder.cardviewcontents.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        else
            holder.cardviewcontents.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition((ViewGroup) v.findViewById(R.id.cardcard));
                mExpandedPosition = isExpanded ? -1 : pos;
                notifyItemChanged(pos);
            }
        });

        holder.primaryTextView.setText(
                dataList.get(position).getPrimaryEmotion() + ": " +
                        (Math.round(Double.parseDouble(dataList.get(position).getPrimaryEmotionScore()) * 100)) + "%");

        String primaryEmotion = dataList.get(position).getPrimaryEmotion();

        switch (primaryEmotion) {
            case "Joy":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#FFC107"));
                holder.primaryTextView.setTextColor(Color.BLACK);
                holder.datetimeTextView.setTextColor(Color.BLACK);
                break;
            case "None":
                holder.cardviewheader.setBackgroundColor(Color.LTGRAY);
                holder.primaryTextView.setTextColor(Color.BLACK);
                holder.datetimeTextView.setTextColor(Color.BLACK);
                break;
            case "Anger":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#FF5722"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                holder.datetimeTextView.setTextColor(Color.WHITE);
                break;
            case "Sadness":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#2196F3"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                holder.datetimeTextView.setTextColor(Color.WHITE);
                break;
            case "Fear":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#673AB7"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                holder.datetimeTextView.setTextColor(Color.WHITE);
                break;
            case "Analytical":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#00BCD4"));
                holder.primaryTextView.setTextColor(Color.BLACK);
                holder.datetimeTextView.setTextColor(Color.BLACK);
                break;
            case "Confident":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#4CAF50"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                holder.datetimeTextView.setTextColor(Color.WHITE);
                break;
            case "Tentative":
                holder.cardviewheader.setBackgroundColor(Color.parseColor("#795548"));
                holder.primaryTextView.setTextColor(Color.WHITE);
                holder.datetimeTextView.setTextColor(Color.WHITE);
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
                    holder.firstTextView.setText(temp[1] + ":");
                    holder.firstProgressBar.setProgress((int) Math.round(Double.parseDouble(temp[0])));
                    break;
                case 1:
                    holder.secondTextView.setText(temp[1] + ":");
                    holder.secondProgressBar.setProgress((int) Math.round(Double.parseDouble(temp[0])));
                    break;
                case 2:
                    holder.thirdTextView.setText(temp[1] + ":");
                    holder.thirdProgressBar.setProgress((int) Math.round(Double.parseDouble(temp[0])));
                    break;
                case 3:
                    holder.forthTextView.setText(temp[1] + ":");
                    holder.forthProgressBar.setProgress((int) Math.round(Double.parseDouble(temp[0])));
                    break;
                case 4:
                    holder.fifthTextView.setText(temp[1] + ":");
                    holder.fifthProgressBar.setProgress((int) Math.round(Double.parseDouble(temp[0])));
                    break;
                case 5:
                    holder.sixthTextView.setText(temp[1] + ":");
                    holder.sixthProgressBar.setProgress((int) Math.round(Double.parseDouble(temp[0])));
                    break;
                case 6:
                    holder.seventhTextView.setText(temp[1] + ":");
                    holder.seventhProgressBar.setProgress((int) Math.round(Double.parseDouble(temp[0])));
                    break;
            }
        }

        holder.transcriptTextView.setText(dataList.get(position).getTranscript() + "\n");
        holder.confidenceTextView.setText("\nTotal confidence: " + String.format("%.2f", Double.parseDouble(dataList.get(position).getConfidence()) * 100) + "%\n");

        String datetime = dataList.get(position).getTimestamp();

        try {
            SimpleDateFormat fromDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            datetime = (String.valueOf(fromDateFormat.parse(datetime)).substring(0, 19));

        } catch (ParseException exp) {
            exp.printStackTrace();
        }

        holder.datetimeTextView.setText(datetime);
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView firstTextView, secondTextView, thirdTextView, forthTextView, fifthTextView,
                sixthTextView, seventhTextView, transcriptTextView, confidenceTextView, primaryTextView, datetimeTextView;

        //https://github.com/daimajia/NumberProgressBar
        NumberProgressBar firstProgressBar, secondProgressBar, thirdProgressBar, forthProgressBar,
                fifthProgressBar, sixthProgressBar, seventhProgressBar;

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
            datetimeTextView = view.findViewById(R.id.datetimeTextView);

            firstProgressBar = view.findViewById(R.id.firstProgressBar);
            secondProgressBar = view.findViewById(R.id.secondProgressBar);
            thirdProgressBar = view.findViewById(R.id.thirdProgressBar);
            forthProgressBar = view.findViewById(R.id.forthProgressBar);
            fifthProgressBar = view.findViewById(R.id.fifthProgressBar);
            sixthProgressBar = view.findViewById(R.id.sixthProgressBar);
            seventhProgressBar = view.findViewById(R.id.seventhProgressBar);
        }
    }
}