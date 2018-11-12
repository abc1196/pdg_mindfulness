package mindfulness.pdg_mindfulness.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.dashboard.data.User;
import mindfulness.pdg_mindfulness.utils.interfaces.DashboardNavigationHost;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
}

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView mDisplayNameTxt= (TextView)view.findViewById(R.id.txt_display_name);
        final TextView mScreenOnCountTxt= (TextView)view.findViewById(R.id.txt_screen_on_count);
        final TextView mScreenTotalTimeTxt= (TextView)view.findViewById(R.id.txt_screen_total_time);
        final TextView mCallTotalTimeTxt= (TextView)view.findViewById(R.id.txt_call_total_time);
        final TextView mCallInCountTxt= (TextView)view.findViewById(R.id.txt_call_in_count);
        final TextView mCallOutCountTxt= (TextView)view.findViewById(R.id.txt_call_out_count);
        /*
        final TextView mAppEntertainmentTimeText= (TextView) view.findViewById(R.id.txt_entertainment_time);
        final TextView mAppWorkTimeText= (TextView) view.findViewById(R.id.txt_app_work_time);
        final TextView mAppHealthTimeText= (TextView) view.findViewById(R.id.txt_app_health_time);
        final TextView mAppSocialTimeText= (TextView) view.findViewById(R.id.txt_app_social_time);
        final TextView mAppInformationTimeText= (TextView) view.findViewById(R.id.txt_app_information_time);
        final TextView mAppSystemTimeText= (TextView) view.findViewById(R.id.txt_app_system_time);
        */
        TextView textAppUsage=(TextView) view.findViewById(R.id.text_layout_three);
        PieChartView pieChartView = (PieChartView) view.findViewById(R.id.chart);
        SharedPreferences sharedPreferences=getActivity().getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
        String userJson=sharedPreferences.getString("USER_STATS",null);
        final String userName=sharedPreferences.getString("USER_NAME",null);
        if(userName!=null){
            mDisplayNameTxt.setText(userName);
        }
        if(userJson!=null){
            textAppUsage.setVisibility(View.VISIBLE);
            pieChartView.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            User user=gson.fromJson(userJson,User.class);
            mScreenOnCountTxt.setText(Long.toString(user.getScreenOnCount()));
            mScreenTotalTimeTxt.setText(formatTime(user.getScreenTotalTime()));
            mCallTotalTimeTxt.setText(formatTime(user.getCallTotalTime()));
            mCallInCountTxt.setText(Long.toString(user.getCallInCount()));
            mCallOutCountTxt.setText(Long.toString(user.getCallOutCount()));
            List<SliceValue> pieData = getPieData(user);
            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true).setValueLabelTextSize(12);
            pieChartView.setPieChartData(pieChartData);

            /*
            mAppEntertainmentTimeText.setText(formatTime(user.getAppEntertainmentTime()));
            mAppWorkTimeText.setText(formatTime(user.getAppWorkTime()));
            mAppHealthTimeText.setText(formatTime(user.getAppHealthTime()));
            mAppSocialTimeText.setText(formatTime(user.getAppSocialTime()));
            mAppInformationTimeText.setText(formatTime(user.getAppInformationTime()));
            mAppSystemTimeText.setText(formatTime(user.getAppSystemTime()));
            */
        }else{
            textAppUsage.setVisibility(View.GONE);
            pieChartView.setVisibility(View.GONE);
        }

        ImageButton logoutButton =(ImageButton) view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashboardNavigationHost) getActivity()).logout(); // Navigate to the next Fragment
            }
        });

        return view;
    }

    private List<SliceValue> getPieData(User user){
        List<SliceValue> pieData=new ArrayList<>();
        double totalHours=0;
        String entertainmentTime=formatTime(user.getAppEntertainmentTime());
        String workTime=formatTime(user.getAppWorkTime());
        String healthTime=formatTime(user.getAppHealthTime());
        String socialTime=formatTime(user.getAppSocialTime());
        String informationTime=formatTime(user.getAppInformationTime());
        String systemTime=formatTime(user.getAppSystemTime());

        double entertainmentHours=getHours(entertainmentTime);
        double workHours=getHours(workTime);
        double healthHours=getHours(healthTime);
        double socialHours=getHours(socialTime);
        double informationHours=getHours(informationTime);
        double systemHours=getHours(systemTime);

        totalHours+=entertainmentHours+workHours+healthHours+socialHours+informationHours+systemHours;

        pieData.add(new SliceValue((float) (entertainmentHours/totalHours), Color.parseColor("#673ab7") ).setLabel("Entretenimiento: "+entertainmentTime));
        pieData.add(new SliceValue((float) (workHours/totalHours),Color.parseColor("#BDBDBD")).setLabel("Trabajo: "+workTime));
        pieData.add(new SliceValue((float) (healthHours/totalHours),Color.parseColor("#E1BEE7")).setLabel("Salud: "+healthTime));
        pieData.add(new SliceValue((float) (socialHours/totalHours), Color.parseColor("#5A3559") ).setLabel("Social: "+socialTime));
        pieData.add(new SliceValue((float) (informationHours/totalHours), Color.parseColor("#4527a0") ).setLabel("Información: "+informationTime));
        pieData.add(new SliceValue((float) (systemHours/totalHours),Color.parseColor("#9E9E9E") ).setLabel("Sistema: "+systemTime));

        return  pieData;
    }

    private double getHours(String time){
        double hours=0;
        if(!time.equalsIgnoreCase("-")){
            if(time.charAt(time.length()-1)== 'h'){
                hours=Double.parseDouble(time.substring(0,time.length()-1));
            }else if(time.charAt(time.length()-1)== 'm'){
                hours=(Double.parseDouble(time.substring(0,time.length()-1)))/60;
            }else if(time.charAt(time.length()-1)=='s'){
                hours=(Double.parseDouble(time.substring(0,time.length()-1)))/3600;
            }
        }
        return hours;
    }

    private String formatTime(String time){
        String format="";
        String[] timeValues=time.split(":");
        if(timeValues.length==3){
            format=timeValues[0]+"h";
        }else if(timeValues.length==2){
            if(!timeValues[0].equals("00")){
                if(timeValues[0].charAt(0)=='0') {
                format = timeValues[0].substring(1) + "m";
            }else{
                format = timeValues[0] + "m";
            }
            }else if(!timeValues[1].equals("00")){
                if(timeValues[1].charAt(0)=='0') {
                    format = timeValues[1].substring(1) + "s";
                }else{
                    format = timeValues[1] + "s";
                }
            }else{
                format="-";
            }
        }
        return format;
    }
}
