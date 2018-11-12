package mindfulness.pdg_mindfulness.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.dashboard.data.User;
import mindfulness.pdg_mindfulness.measurement.PSTActivity;
import mindfulness.pdg_mindfulness.measurement.PSTScoreActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFragment extends Fragment {

    public static final String PST_SCORE_LOW_URL="https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/pst_leves%2Fpst_score_low.png?alt=media&token=703e3666-ec98-4d59-bfac-96a3615f2c08";

    public static final String PST_SCORE_MEDIUM_URL="https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/pst_leves%2Fpst_score_medium.png?alt=media&token=a7203297-f3d8-42f6-bed9-62ad5af88705";

    public static final String PST_SCORE_HIGH_URL="https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/pst_leves%2Fpst_score_high.png?alt=media&token=3d464718-19a9-4a0c-baf7-0340330c6f9d";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health, container, false);
        SharedPreferences sharedPreferences=getActivity().getApplicationContext().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        Long lastPST=sharedPreferences.getLong("USER_LAST_PST",0);
        String nextPST=sharedPreferences.getString("USER_NEXT_PST",null);
        Calendar currentDate=Calendar.getInstance();
        Calendar nextDate=Calendar.getInstance();
        nextDate.setTimeInMillis(Long.parseLong(nextPST));
        int daysLeft=daysBetween(currentDate,nextDate);
        String text="";
        String userName=sharedPreferences.getString("USER_NAME",null);
        if(userName!=null){
            text+=userName+", "+"\n"+"\n";
            text+="Tu puntaje fue de "+lastPST+" puntos.";
        }
        TextView scoreText=(TextView)view.findViewById(R.id.score_text);
        TextView nextPstText=(TextView)view.findViewById(R.id.next_pst_text);
        scoreText.setText(text);
        nextPstText.setText(daysLeft+" días hasta la próxima medición.");
        ImageView scoreImage=(ImageView)view.findViewById(R.id.score_image);
        if (lastPST<=18){
            Picasso.get().load(PST_SCORE_LOW_URL).into(scoreImage);
        }else if(lastPST>18&&lastPST<=36){
            Picasso.get().load(PST_SCORE_MEDIUM_URL).into(scoreImage);
        }else{
            Picasso.get().load(PST_SCORE_HIGH_URL).into(scoreImage);
        }
        MaterialButton newPstButton = view.findViewById(R.id.new_pst_button);
        if(daysLeft<=0){
            newPstButton.setVisibility(View.VISIBLE);
            nextPstText.setVisibility(View.GONE);
        }
        newPstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PSTActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    public static int daysBetween(Calendar day1, Calendar day2){
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }
}
