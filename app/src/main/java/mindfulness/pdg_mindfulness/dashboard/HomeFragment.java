package mindfulness.pdg_mindfulness.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import at.markushi.ui.CircleButton;
import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.treatment.Treatment;
import mindfulness.pdg_mindfulness.treatment.TreatmentDay;
import mindfulness.pdg_mindfulness.utils.interfaces.DashboardNavigationHost;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ;
    private MaterialButton goTreatment;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Treatment treatment;
    private ArrayList<TreatmentDay> days;
    private int dayNumber;
    private CircleButton day1Button;
    private CircleButton day2Button;
    private CircleButton day3Button;
    private CircleButton day4Button;
    private CircleButton day5Button;
    private CircleButton day6Button;
    private CircleButton day7Button;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        day1Button=view.findViewById(R.id.day1Button);
        day2Button=view.findViewById(R.id.day2Button);
        day3Button=view.findViewById(R.id.day3Button);
        day4Button=view.findViewById(R.id.day4Button);
        day5Button=view.findViewById(R.id.day5Button);
        day6Button=view.findViewById(R.id.day6Button);
        day7Button=view.findViewById(R.id.day7Button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        days = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        treatment = new Treatment(user.getUid());
        if (user != null) {
            db.collection("BCT").document(user.getUid()).collection("days").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document2 : task.getResult()) {
                            if (document2 != null) {
                                int day = ((Long) document2.get("day")).intValue();
                                boolean isAvailable = (boolean) document2.getData().get("isAvailable");
                                boolean isCompleted = (boolean) document2.getData().get("isCompleted");
                                boolean isBodyScanDone = (boolean) document2.getData().get("isBodyScanDone");
                                boolean isPauseDone = (boolean) document2.getData().get("isPauseDone");
                                boolean isRoutineDone = (boolean) document2.getData().get("isRoutineDone");

                                TreatmentDay newDay = new TreatmentDay(day, isCompleted, isAvailable, isBodyScanDone, isPauseDone, isRoutineDone);
                                days.add(newDay);

                                Log.d("CONITAGDAY", day+"");
                                Log.d("CONIisAvailable", isAvailable+"");
                                Log.d("CONIisCompleted", isCompleted+"");
                                Log.d("CONIisBodyScanDone", isBodyScanDone+"");
                                Log.d("CONIisPauseDone", isPauseDone+"");
                                Log.d("CONIisRoutineDone",isRoutineDone+"");

                            }
                        }
                        treatment.setDays(days);
                        activateButtons();
                        addListeners();
                    } else {
                        Log.w("ERRORGETTINGFIREBASE", "Error getting days.", task.getException());
                    }

                }
            });
        }

        return view;
    }

    public void activateButtons() {
        ArrayList<TreatmentDay> week = treatment.getDays();

        for (int i = 0; i < week.size(); i++) {
            TreatmentDay currentDay = week.get(i);
            Log.d("CONIcurrentday",currentDay.getDay()+"");
            Log.d("CONIisavailable",currentDay.isAvailable()+"");
            if (currentDay.isCompleted()) {
                if (currentDay.getDay() == 1) {
                    day1Button.setImageResource(R.drawable.ic_completed);
                    Log.d("CONIcompleted","cambio de icono a completed");
                    day1Button.setEnabled(false);
                } else if (currentDay.getDay() == 2) {
                    day2Button.setImageResource(R.drawable.ic_completed);
                    Log.d("CONIcompleted","cambio de icono a completed");
                    day2Button.setEnabled(false);
                } else if (currentDay.getDay() == 3) {
                    day3Button.setImageResource(R.drawable.ic_completed);
                    Log.d("CONIcompleted","cambio de icono a completed");
                    day3Button.setEnabled(false);
                } else if (currentDay.getDay() == 4) {
                    day4Button.setImageResource(R.drawable.ic_completed);
                    Log.d("CONIcompleted","cambio de icono a completed");
                    day4Button.setEnabled(false);
                } else if (currentDay.getDay() == 5) {
                    day5Button.setImageResource(R.drawable.ic_completed);
                    Log.d("CONIcompleted","cambio de icono a completed");
                    day5Button.setEnabled(false);
                } else if (currentDay.getDay() == 6) {
                    day6Button.setImageResource(R.drawable.ic_completed);
                    Log.d("CONIcompleted","cambio de icono a completed");
                    day6Button.setEnabled(false);
                } else if (currentDay.getDay() == 7) {
                    day7Button.setImageResource(R.drawable.ic_completed);
                    Log.d("CONIcompleted","cambio de icono a completed");
                    day7Button.setEnabled(false);
                }

            } else {
                if (currentDay.isAvailable()) {
                    if (currentDay.getDay() == 1) {

                        day1Button.setImageResource(R.drawable.ic_health);
                        Log.d("CONIavailable","cambio de icono a meditado");
                        day1Button.setEnabled(true);
                    } else if (currentDay.getDay() == 2) {
                        day2Button.setImageResource(R.drawable.ic_health);
                        Log.d("CONIavailable","cambio de icono a meditado");
                        day2Button.setEnabled(true);
                    } else if (currentDay.getDay() == 3) {
                        day3Button.setImageResource(R.drawable.ic_health);
                        Log.d("CONIavailable","cambio de icono a meditado");
                        day3Button.setEnabled(true);
                    } else if (currentDay.getDay() == 4) {
                        day4Button.setImageResource(R.drawable.ic_health);
                        Log.d("CONIavailable","cambio de icono a meditado");
                        day4Button.setEnabled(true);
                    } else if (currentDay.getDay() == 5) {
                        day5Button.setImageResource(R.drawable.ic_health);
                        Log.d("CONIavailable","cambio de icono a meditado");
                        day5Button.setEnabled(true);
                    } else if (currentDay.getDay() == 6) {
                        day6Button.setImageResource(R.drawable.ic_health);
                        Log.d("CONIavailable","cambio de icono a meditado");
                        day6Button.setEnabled(true);
                    } else if (currentDay.getDay() == 7) {
                        day7Button.setImageResource(R.drawable.ic_health);
                        Log.d("CONIavailable","cambio de icono a meditado");
                        day7Button.setEnabled(true);
                    }
                } else {
                    if (currentDay.getDay() == 1) {

                        day1Button.setImageResource(R.drawable.ic_lock);
                        Log.d("CONIlock","cambio de icono a lock");
                        day1Button.setEnabled(false);
                    } else if (currentDay.getDay() == 2) {
                        day2Button.setImageResource(R.drawable.ic_lock);
                        Log.d("CONIlock","cambio de icono a lock");
                        day2Button.setEnabled(false);
                    } else if (currentDay.getDay() == 3) {
                        day3Button.setImageResource(R.drawable.ic_lock);
                        Log.d("CONIlock","cambio de icono a lock");
                        day3Button.setEnabled(false);
                    } else if (currentDay.getDay() == 4) {
                        day4Button.setImageResource(R.drawable.ic_lock);
                        Log.d("CONIlock","cambio de icono a lock");
                        day4Button.setEnabled(false);
                    } else if (currentDay.getDay() == 5) {
                        day5Button.setImageResource(R.drawable.ic_lock);
                        Log.d("CONIlock","cambio de icono a lock");
                        day5Button.setEnabled(false);
                    } else if (currentDay.getDay() == 6) {
                        day6Button.setImageResource(R.drawable.ic_lock);
                        Log.d("CONIlock","cambio de icono a lock");
                        day6Button.setEnabled(false);
                    } else if (currentDay.getDay() == 7) {
                        day7Button.setImageResource(R.drawable.ic_lock);
                        Log.d("CONIlock","cambio de icono a lock");
                        day7Button.setEnabled(false);
                    }
                }
            }
        }
        days=new ArrayList<>();
    }

    public void addListeners(){
        day1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber=1;
                Log.d("conidaynumfragment",dayNumber+"");
                ((DashboardNavigationHost) getActivity()).goToTreatment(dayNumber);
            }
        });
        day2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber=2;
                Log.d("conidaynumfragment",dayNumber+"");
                ((DashboardNavigationHost) getActivity()).goToTreatment(dayNumber);
            }
        });
        day3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber=3;
                Log.d("conidaynumfragment",dayNumber+"");
                ((DashboardNavigationHost) getActivity()).goToTreatment(dayNumber);
            }
        });
        day4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber=4;
                Log.d("conidaynumfragment",dayNumber+"");
                ((DashboardNavigationHost) getActivity()).goToTreatment(dayNumber);
            }
        });
        day5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber=5;
                Log.d("conidaynumfragment",dayNumber+"");
                ((DashboardNavigationHost) getActivity()).goToTreatment(dayNumber);
            }
        });
        day6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber=6;
                Log.d("conidaynumfragment",dayNumber+"");
                ((DashboardNavigationHost) getActivity()).goToTreatment(dayNumber);
            }
        });
        day7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber=7;
                Log.d("conidaynumfragment",dayNumber+"");
                ((DashboardNavigationHost) getActivity()).goToTreatment(dayNumber);
            }
        });
    }
}


