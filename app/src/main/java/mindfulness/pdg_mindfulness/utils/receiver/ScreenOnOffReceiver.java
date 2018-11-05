package mindfulness.pdg_mindfulness.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

public class ScreenOnOffReceiver extends BroadcastReceiver {

    public final static String SCREEN_TOTAL_TIME="SCREEN_TOTAL_TIME";
    public final static String SCREEN_ON_COUNT="SCREEN_ON_COUNT";
    public final static String SCREEN_ON_TIMESTAMP="SCREEN_ON_TIMESTAMP";
    public final static String SCREEN_TOGGLE_TAG = "SCREEN_TOGGLE_TAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        long currentTime=System.currentTimeMillis();
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(Intent.ACTION_SCREEN_OFF.equals(action))
        {
            long screenOnTimestamp = PreferenceManager.getDefaultSharedPreferences(context).getLong(SCREEN_ON_TIMESTAMP,0);
            if(screenOnTimestamp>0) {
                long sessionTime = currentTime - screenOnTimestamp;
                long totalTime = PreferenceManager.getDefaultSharedPreferences(context).getLong(SCREEN_TOTAL_TIME, 0);
                totalTime += sessionTime;
                editor.putLong(SCREEN_TOTAL_TIME, totalTime);
                editor.commit();
                Log.d(SCREEN_TOGGLE_TAG, "totalTime: "+totalTime+" currentTime:" + currentTime+ " screenOnTimestamp: "+screenOnTimestamp+ " sessionTime: "+sessionTime);
                Log.d(SCREEN_TOGGLE_TAG, "Screen is turn off. " + DateUtils.formatElapsedTime(totalTime / 1000) + " segs.");
            }
        }else if(Intent.ACTION_SCREEN_ON.equals(action))
        {
            int screenOnCount = PreferenceManager.getDefaultSharedPreferences(context).getInt(SCREEN_ON_COUNT,0);
            screenOnCount++;
            editor.putInt(SCREEN_ON_COUNT,screenOnCount);
            editor.putLong(SCREEN_ON_TIMESTAMP, currentTime);
            editor.commit();
            Log.d(SCREEN_TOGGLE_TAG, "Screen is turn on. "+screenOnCount);
        }
    }

}
