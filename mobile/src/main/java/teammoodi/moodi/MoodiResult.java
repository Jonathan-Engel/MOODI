package teammoodi.moodi;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;

/**
 * Created by ahb5190 on 3/24/18.
 */

public class MoodiResult {
    private String sadness;
    private String fear;
    private String joy;
    private String analytical;
    private String confident;
    private String tentative;
    private String transcript;
    private String confidence;
    private String anger;

    public MoodiResult() {

    }

    private String primaryEmotion = "None";
    private Double primaryEmotionScore = 0.0;

    public String getPrimaryEmotion() {
        return primaryEmotion;
    }

    public String getPrimaryEmotionScore() {
        return primaryEmotionScore.toString();
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) { this.confidence = confidence; }

    public String getAnger() {

        return anger;
    }

    public void setAnger(String anger) {
        if (!Double.valueOf(anger).equals(0) && Double.compare(Double.valueOf(anger), primaryEmotionScore) > 0) {
            primaryEmotion = "Anger";
            primaryEmotionScore = Double.valueOf(anger);
        }
        this.anger = anger;
    }

    public String getSadness() {
        return sadness;
    }

    public void setSadness(String sadness) {
        if (!Double.valueOf(sadness).equals(0) && Double.compare(Double.valueOf(sadness), primaryEmotionScore) > 0) {
            primaryEmotion = "Sadness";
            primaryEmotionScore = Double.valueOf(sadness);
        }
        this.sadness = sadness;
    }

    public String getFear() {
        return fear;
    }

    public void setFear(String fear) {
        if (!Double.valueOf(fear).equals(0) && Double.compare(Double.valueOf(fear), primaryEmotionScore) > 0) {
            primaryEmotion = "Fear";
            primaryEmotionScore = Double.valueOf(fear);
        }
        this.fear = fear;
    }

    public String getJoy() {
        return joy;
    }

    public void setJoy(String joy) {
        if (!Double.valueOf(joy).equals(0) && Double.compare(Double.valueOf(joy), primaryEmotionScore) > 0) {
            primaryEmotion = "Joy";
            primaryEmotionScore = Double.valueOf(joy);
        }
        this.joy = joy;
    }

    public String getAnalytical() {
        return analytical;
    }

    public void setAnalytical(String analytical) {
        if (!Double.valueOf(analytical).equals(0) && Double.compare(Double.valueOf(analytical), primaryEmotionScore) > 0) {
            primaryEmotion = "Analytical";
            primaryEmotionScore = Double.valueOf(analytical);
        }
        this.analytical = analytical;
    }

    public String getConfident() {
        return confident;
    }

    public void setConfident(String confident) {
        if (!Double.valueOf(confident).equals(0) && Double.compare(Double.valueOf(confident), primaryEmotionScore) > 0) {
            primaryEmotion = "Confident";
            primaryEmotionScore = Double.valueOf(confident);
        }
        this.confident = confident;
    }

    public String getTentative() {
        return tentative;
    }

    public void setTentative(String tentative) {
        if (!Double.valueOf(tentative).equals(0) && Double.compare(Double.valueOf(tentative), primaryEmotionScore) > 0) {
            primaryEmotion = "Tentative";
            primaryEmotionScore = Double.valueOf(tentative);
        }
        this.tentative = tentative;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

}
