package mindfulness.pdg_mindfulness.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
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
        final TextView mAppEntertainmentTimeText= (TextView) view.findViewById(R.id.txt_entertainment_time);
        final TextView mAppWorkTimeText= (TextView) view.findViewById(R.id.txt_app_work_time);
        final TextView mAppHealthTimeText= (TextView) view.findViewById(R.id.txt_app_health_time);
        final TextView mAppSocialTimeText= (TextView) view.findViewById(R.id.txt_app_social_time);
        final TextView mAppInformationTimeText= (TextView) view.findViewById(R.id.txt_app_information_time);
        final TextView mAppSystemTimeText= (TextView) view.findViewById(R.id.txt_app_system_time);
        SharedPreferences sharedPreferences=getActivity().getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
        String userJson=sharedPreferences.getString("USER_STATS",null);
        final String userName=sharedPreferences.getString("USER_NAME",null);
        if(userName!=null){
            mDisplayNameTxt.setText(userName);
        }
        if(userJson!=null){
            Gson gson = new Gson();
            User user=gson.fromJson(userJson,User.class);
            mScreenOnCountTxt.setText(Long.toString(user.getScreenOnCount()));
            mScreenTotalTimeTxt.setText(formatTime(user.getScreenTotalTime()));
            mCallTotalTimeTxt.setText(formatTime(user.getCallTotalTime()));
            mCallInCountTxt.setText(Long.toString(user.getCallInCount()));
            mCallOutCountTxt.setText(Long.toString(user.getCallOutCount()));
            mAppEntertainmentTimeText.setText(formatTime(user.getAppEntertainmentTime()));
            mAppWorkTimeText.setText(formatTime(user.getAppWorkTime()));
            mAppHealthTimeText.setText(formatTime(user.getAppHealthTime()));
            mAppSocialTimeText.setText(formatTime(user.getAppSocialTime()));
            mAppInformationTimeText.setText(formatTime(user.getAppInformationTime()));
            mAppSystemTimeText.setText(formatTime(user.getAppSystemTime()));
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
