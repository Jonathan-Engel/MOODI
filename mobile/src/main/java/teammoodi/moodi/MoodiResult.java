package teammoodi.moodi;

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

    public MoodiResult(String confidence, String anger, String sadness, String fear, String joy, String analytical, String confident, String tentative, String transcript) {
        this.confidence = confidence;
        this.anger = anger;
        this.sadness = sadness;
        this.fear = fear;
        this.joy = joy;
        this.analytical = analytical;
        this.confident = confident;
        this.tentative = tentative;
        this.transcript = transcript;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getAnger() {
        return anger;
    }

    public void setAnger(String anger) {
        this.anger = anger;
    }

    public String getSadness() {
        return sadness;
    }

    public void setSadness(String sadness) {
        this.sadness = sadness;
    }

    public String getFear() {
        return fear;
    }

    public void setFear(String fear) {
        this.fear = fear;
    }

    public String getJoy() {
        return joy;
    }

    public void setJoy(String joy) {
        this.joy = joy;
    }

    public String getAnalytical() {
        return analytical;
    }

    public void setAnalytical(String analytical) {
        this.analytical = analytical;
    }

    public String getConfident() {
        return confident;
    }

    public void setConfident(String confident) {
        this.confident = confident;
    }

    public String getTentative() {
        return tentative;
    }

    public void setTentative(String tentative) {
        this.tentative = tentative;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }




}
