package mindfulness.pdg_mindfulness.dashboard;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.utils.interfaces.DashboardNavigationHost;
import mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver;

import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_ON_COUNT;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_TOGGLE_TAG;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_TOTAL_TIME;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {

    public HomeFragment() {}
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MaterialButton mButton= view.findViewById(R.id.app_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count= PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(SCREEN_ON_COUNT,0);
                long sessionTime=PreferenceManager.getDefaultSharedPreferences(getContext()).getLong(SCREEN_TOTAL_TIME,0);
                Log.d(SCREEN_TOGGLE_TAG,"COUNT: "+count);
            }
        });
        return view;
    }

}
