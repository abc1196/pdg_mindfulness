package mindfulness.pdg_mindfulness.treatment;

import java.util.ArrayList;

public class Treatment {

    private String user_id;
    private ArrayList<TreatmentDay> days;

    public Treatment(String user_id) {
        this.user_id = user_id;
        days=new ArrayList<>();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<TreatmentDay> getDays() {
        return days;
    }

    public void setDays(ArrayList<TreatmentDay> days) {
        this.days = days;
    }
}
