package mindfulness.pdg_mindfulness.utils.worker;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import mindfulness.pdg_mindfulness.dashboard.HomeActivity;
import mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver;

import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_ON_COUNT;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_TOTAL_TIME;

public class MeasurementWorker extends Worker {

    private final static int INFORMATION=1;
    private final static int SYSTEM=2;
    private final static int HEALTH=3;
    private final static int ENTERTAINMENT=4;
    private final static int SOCIAL=5;
    private final static int WORK=6;

    public final static String GOOGLE_URL = "https://play.google.com/store/apps/details?id=";
    public static final String ERROR = "ERROR";
    public static final String NOT_FOUND="NOT_FOUND";

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private int screenOnCount;

    private String screenTotalTime;

    private int callInCount;

    private int callOutCount;

    private String callTotalTime;

    private String appInformationTime;

    private String appSystemTime;

    private String appHealthTime;

    private String appEntertainmentTime;

    private String appSocialTime;

    private String appWorkTime;

    private String[] informationCategories={"Transportation","Weather","Travel & Local","News & Magazines","Shopping", "Lifestyle", "Food & Drink", "Maps & Navigation", "Beauty", "Auto & Vehicles", "House & Home", "Parenting"};
    private String[] systemCategories={"Libraries & Demo","Personalization","Live Wallpapers","Tools", "Widgets"};
    private String[] healthCategories={"Sports", "Medical", "Health & Fitness"};
    private String[] entertainmentCategories={"Photography", "Entertainment", "Music & Audio", "Media & Video","Books & Reference", "Events", "Video Players & Editors",
            "Action", "Adventure", "Arcade", "Board", "Card", "Casino", "Casual", "Educational", "Music", "Puzzle", "Racing", "Role Playing", "Simulation", "Sports", "Strategy", "Trivia", "Word" };
    private String[] socialCategories={"Communication","Social", "Dating"};
    private String[] workCategories={"Business", "Productivity", "Finance", "Education", "Art & Design"};

    private SharedPreferences sharedPreferences;
    public MeasurementWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
         sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        callInCount=0;
        callOutCount=0;
        callTotalTime="";

        screenOnCount=0;
        screenTotalTime="";

        appInformationTime="";
        appSystemTime="";
        appHealthTime="";
        appEntertainmentTime="";
        appSocialTime="";
        appWorkTime="";
    }

    @NonNull
    @Override
    public Result doWork() {
        getScreenValues();
       getApplicationsUsage();
       getCallUsage();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(checkCurrentUser(currentUser)){
            Calendar calendar = Calendar.getInstance();
            String created = String.valueOf(calendar.getTime());
            Map<String, Object> msrTmp = new HashMap<>();
            msrTmp.put("callInCount",callInCount);
            msrTmp.put("callOutCount",callOutCount);
            msrTmp.put("callTotalTime",callTotalTime);
            msrTmp.put("screenOnCount",screenOnCount);
            msrTmp.put("screenTotalTime",screenTotalTime);
            msrTmp.put("appInformationTime",appInformationTime);
            msrTmp.put("appSystemTime",appSystemTime);
            msrTmp.put("appHealthTime",appHealthTime);
            msrTmp.put("appEntertainmentTime",appEntertainmentTime);
            msrTmp.put("appSocialTime",appSocialTime);
            msrTmp.put("appWorkTime",appWorkTime);
            msrTmp.put("created",created);
            db= FirebaseFirestore.getInstance();
            db.collection("measurements").document(currentUser.getUid()).collection("smartphoneUsages").add(msrTmp);
            db.collection("measurements").document(currentUser.getUid()).set(msrTmp);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SCREEN_ON_COUNT,0);
            editor.putLong(SCREEN_TOTAL_TIME,0);
            editor.commit();
        }
        return Result.SUCCESS;
    }

    private void getScreenValues(){
        screenOnCount=sharedPreferences.getInt(SCREEN_ON_COUNT,0);
        long totalTime=sharedPreferences.getLong(SCREEN_TOTAL_TIME,0);
        screenTotalTime=DateUtils.formatElapsedTime(totalTime / 1000);
        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "sessionTime: "+ screenTotalTime +" screenOnCount: "+ screenOnCount);
    }

    private void getCallUsage(){
        try {
            StringBuffer sb = new StringBuffer();
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
            Uri callUri = Uri.parse("content://call_log/calls");
            Calendar calendar = Calendar.getInstance();
            String toDate = String.valueOf(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, -1);
            String fromDate = String.valueOf(calendar.getTimeInMillis());
            String[] whereValue = {fromDate, toDate};

            Cursor cur = getApplicationContext().getContentResolver().query(callUri, null, android.provider.CallLog.Calls.DATE + " BETWEEN ? AND ?", whereValue, strOrder);

            int totalDuration = 0;
            // loop through cursor
            while (cur.moveToNext()) {
                String callNumber = cur.getString(cur
                        .getColumnIndex(android.provider.CallLog.Calls.NUMBER));

                String callName = cur
                        .getString(cur
                                .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));

                String callDate = cur.getString(cur
                        .getColumnIndex(android.provider.CallLog.Calls.DATE));
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMM-yyyy HH:mm");
                String dateString = formatter.format(new Date(Long
                        .parseLong(callDate)));

                String callType = cur.getString(cur
                        .getColumnIndex(android.provider.CallLog.Calls.TYPE));
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callOutCount++;
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callInCount++;
                        dir = "INCOMING";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }

                String isCallNew = cur.getString(cur
                        .getColumnIndex(android.provider.CallLog.Calls.NEW));

                String duration = cur.getString(cur
                        .getColumnIndex(android.provider.CallLog.Calls.DURATION));
                int secs = Integer.parseInt(duration);
                totalDuration += secs;
                sb.append("\nPhone Number:--- " + callNumber + " \nName:--- " + callName + " \nCall Type dir:--- " + dir + " \nCall Date:--- " + dateString + " \n duration in sec :--- " + duration);
                sb.append("\n----------------------------------");
            }
            callTotalTime = DateUtils.formatElapsedTime(totalDuration);
            Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "totalDuration: " + totalDuration);
            Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "callTotalTime: " + callTotalTime);
            Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "callInCount: " + callInCount);
            Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "callOutCount: " + callOutCount);
        }catch (Exception e){
            Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "Exception: " + e.getMessage());
        }
    }


    private void getApplicationsUsage(){
        long informationTime=0;
        long systemTime=0;
        long healthTime=0;
        long entertainmentTime=0;
        long socialTime=0;
        long workTime=0;

        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        long start = calendar.getTimeInMillis();
        long end = System.currentTimeMillis();
        Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(start, end);

        for (String key : stats.keySet()) {
            UsageStats usageStats=stats.get(key);
            if(usageStats.getTotalTimeInForeground()>0) {
                String query_url = GOOGLE_URL + usageStats.getPackageName()+"&hl=en";
                String category= getCategory(query_url);
                if(!category.equals(ERROR)&&!category.equals(NOT_FOUND)){
                    int globalCategory=getGlobalCategory(category);
                   // Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "app: "+usageStats.getPackageName()+ " cat: "+ category);
                    switch (globalCategory){
                        case INFORMATION:
                            informationTime+=usageStats.getTotalTimeInForeground();
                            break;
                        case SYSTEM:
                            systemTime+=usageStats.getTotalTimeInForeground();
                            break;
                        case HEALTH:
                            healthTime+=usageStats.getTotalTimeInForeground();
                            break;
                        case ENTERTAINMENT:
                            entertainmentTime+=usageStats.getTotalTimeInForeground();
                            break;
                        case SOCIAL:
                            socialTime+=usageStats.getTotalTimeInForeground();
                            break;
                        case WORK:
                            workTime+=usageStats.getTotalTimeInForeground();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        appInformationTime=DateUtils.formatElapsedTime(informationTime / 1000);
        appSystemTime= DateUtils.formatElapsedTime(systemTime / 1000);
        appHealthTime= DateUtils.formatElapsedTime(healthTime / 1000);
        appEntertainmentTime=DateUtils.formatElapsedTime(entertainmentTime / 1000);
        appSocialTime=DateUtils.formatElapsedTime(socialTime / 1000);
        appWorkTime= DateUtils.formatElapsedTime(workTime / 1000);
        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG,"INFORMATION: "+appInformationTime );
        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG,"SYSTEM: "+appSystemTime);
        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG,"HEALTH: "+appHealthTime);
        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG,"ENTERTAINMENT: "+ appEntertainmentTime);
        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG,"SOCIAL: "+ appSocialTime);
        Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG,"WORK: "+appWorkTime);
    }

    private int getGlobalCategory(String category){
        for(int i=0; i< informationCategories.length; i++){
            if(category.equalsIgnoreCase(informationCategories[i])){
                return  INFORMATION;
            }
        }
        for(int i=0; i< systemCategories.length; i++){
            if(category.equalsIgnoreCase(systemCategories[i])){
                return  SYSTEM;
            }
        }
        for(int i=0; i< healthCategories.length; i++){
            if(category.equalsIgnoreCase(healthCategories[i])){
                return  HEALTH;
            }
        }
        for(int i=0; i< entertainmentCategories.length; i++){
            if(category.equalsIgnoreCase(entertainmentCategories[i])){
                return  ENTERTAINMENT;
            }
        }
        for(int i=0; i< socialCategories.length; i++){
            if(category.equalsIgnoreCase(socialCategories[i])){
                return  SOCIAL;
            }
        }
        for(int i=0; i< workCategories.length; i++){
            if(category.equalsIgnoreCase(workCategories[i])){
                return  WORK;
            }
        }
        return 0;
    }

    private String getCategory(String query_url) {
        try {
            Document doc = Jsoup.connect(query_url).timeout(5000).ignoreHttpErrors(true).followRedirects(true).userAgent("Mozilla").get();
            Element link = doc.select("a[itemprop=genre]").first();
            if(link!=null) {
                return link.text();
            }else{
                return NOT_FOUND;
            }
        } catch (Exception e) {
            Log.d(ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, e.getMessage());
            return ERROR;
        }
    }

    private boolean checkCurrentUser(FirebaseUser currentUser){
        return currentUser != null;
    }
}
