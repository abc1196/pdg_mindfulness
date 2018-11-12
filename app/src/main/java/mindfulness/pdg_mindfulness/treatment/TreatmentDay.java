package mindfulness.pdg_mindfulness.treatment;

public class TreatmentDay {

    private int day;


    private boolean isCompleted,isAvailable, isBodyScanDone, isPauseDone, isRoutineDone;

    public TreatmentDay(int day, boolean isCompleted, boolean isAvailable, boolean isBodyScanDone, boolean isPauseDone, boolean isRoutineDone) {
        this.day = day;
        this.isAvailable = isAvailable;
        this.isCompleted = isCompleted;
        this.isBodyScanDone = isBodyScanDone;
        this.isPauseDone = isPauseDone;
        this.isRoutineDone = isRoutineDone;
    }
    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isBodyScanDone() {
        return isBodyScanDone;
    }

    public void setBodyScanDone(boolean bodyScanDone) {
        isBodyScanDone = bodyScanDone;
    }

    public boolean isPauseDone() {
        return isPauseDone;
    }

    public void setPauseDone(boolean pauseDone) {
        isPauseDone = pauseDone;
    }

    public boolean isRoutineDone() {
        return isRoutineDone;
    }

    public void setRoutineDone(boolean routineDone) {
        isRoutineDone = routineDone;
    }
}
