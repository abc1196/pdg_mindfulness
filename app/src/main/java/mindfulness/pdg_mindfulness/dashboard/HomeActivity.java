package mindfulness.pdg_mindfulness.dashboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.State;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;
import mindfulness.pdg_mindfulness.dashboard.data.User;
import mindfulness.pdg_mindfulness.utils.others.BaseFragment;
import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.splash.SplashActivity;
import mindfulness.pdg_mindfulness.treatment.TreatmentActivity;
import mindfulness.pdg_mindfulness.utils.interfaces.DashboardNavigationHost;
import mindfulness.pdg_mindfulness.utils.service.ScreenOnOffBackgroundService;
import mindfulness.pdg_mindfulness.utils.worker.MeasurementWorker;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Process.myUid;

import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_ON_COUNT;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_ON_TIMESTAMP;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_TOGGLE_TAG;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_TOTAL_TIME;


public class HomeActivity extends AppCompatActivity  implements DashboardNavigationHost {

    private final static int MY_PERMISSIONS_REQUEST_READ_CALL_LOG=0;
    private final static String SERVICES_ON="SERVICES_ON";

    private WorkManager mWorkManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private BottomNavigationView bottomNavigationView;
    private List<Fragment> fragments = new ArrayList<>(3);
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        checkCurrentUser(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setPermissions();

        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        User user=getUserStats();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragment = fragments.get(0);
                        break;
                    case R.id.menu_hrv:
                        fragment = fragments.get(1);
                        break;
                    case R.id.menu_profile:
                        fragment = fragments.get(2);
                        break;
                }
                replaceFragment(fragment);
                return true;
            }
        });
        buildFragmentsList();
        setInitialFragment();
    }

    private void setInitialFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_placeholder, new HomeFragment());
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }

    private void buildFragmentsList() {
        HomeFragment homeFragment = new HomeFragment();
        HealthFragment healthFragment = new HealthFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        fragments.add(homeFragment);
        fragments.add(healthFragment);
        fragments.add(profileFragment);
    }

    @Override
    public void goToTreatment(int dayNumber){
        Intent intent = new Intent(getApplicationContext(), TreatmentActivity.class);
        intent.putExtra("dayNumber",dayNumber+"");
        this.finish();
        startActivity(intent);
    }

    @Override
    public void logout() {
        Log.d("ALEJOTAG","LOGOUT");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.logout);
// Add the buttons
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                FirebaseUser currentUser=mAuth.getCurrentUser();
                if(currentUser!=null){
                    mAuth.signOut();
                    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putLong("USER_LAST_PST",0);
                    editor.putString("USER_NEXT_PST",null);
                    editor.commit();


                    SharedPreferences sharedPreferences2= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                    editor2.putLong(SCREEN_ON_TIMESTAMP, 0);
                    editor2.putLong(SCREEN_TOTAL_TIME, 0);
                    editor2.putInt(SCREEN_ON_COUNT,0);
                    editor2.putBoolean(SERVICES_ON,false);
                    editor2.commit();

                    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    manager.killBackgroundProcesses(ScreenOnOffBackgroundService.class.getName());

                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public User getUserStats(){
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            Log.d("ALEJOTAG", "NAME: "+user.getDisplayName());
            final User currentUser=new User(user.getDisplayName());
            DocumentReference docRef =db.collection("measurements").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("ALEJOTAG", "DocumentSnapshot data: " + document.getData());
                            currentUser.setScreenOnCount((Long)document.getData().get("screenOnCount"));
                            currentUser.setScreenTotalTime((String)document.getData().get("screenTotalTime"));
                            currentUser.setCallTotalTime((String)document.getData().get("callTotalTime"));
                            currentUser.setCallInCount((Long)document.getData().get("callInCount"));
                            currentUser.setCallOutCount((Long)document.getData().get("callOutCount"));
                            currentUser.setAppInformationTime((String)document.getData().get("appInformationTime"));
                            currentUser.setAppHealthTime((String)document.getData().get("appHealthTime"));
                            currentUser.setAppSystemTime((String)document.getData().get("appSystemTime"));
                            currentUser.setAppEntertainmentTime((String)document.getData().get("appEntertainmentTime"));
                            currentUser.setAppSocialTime((String)document.getData().get("appSocialTime"));
                            currentUser.setAppWorkTime((String)document.getData().get("appWorkTime"));
                            currentUser.setCreated((String)document.getData().get("created"));
                            Gson gson = new Gson();
                            String userInfJsonString = gson.toJson(currentUser);

                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("USER_STATS",userInfJsonString);
                            editor.commit();
                        } else {
                            Log.d("ALEJOTAG", "No such document");
                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("USER_STATS",null);
                            editor.commit();
                        }
                    } else {
                        Log.d("ALEJOTAG", "get failed with ", task.getException());
                    }
                }
            });
            return currentUser;
        }else{
            return new User();
        }

    }


    @Override
    public void onBackPressed() {
        tellFragments();
        super.onBackPressed();
    }

    private void tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f != null && f instanceof BaseFragment)
                ((BaseFragment) f).onBackPressed();
        }
    }

    private void checkCurrentUser(FirebaseUser currentUser) {
        if (currentUser == null) {

            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        } else if(currentUser!=null){
            boolean verifiedEmail=currentUser.isEmailVerified();
            if(!verifiedEmail){
                Toast.makeText(getApplicationContext(),"Por favor confirma tu cuenta. Revisa tu correo electrónico.", Toast.LENGTH_LONG).show();
            }
            DocumentReference docRef =db.collection("users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            Long lastPST=(Long)document.getData().get("lastPST");
                            String nextPST=(String)document.getData().get("nextPST");
                            if(lastPST!=null&&nextPST!=null) {
                                editor.putLong("USER_LAST_PST", lastPST);
                                editor.putString("USER_NEXT_PST", nextPST);
                                editor.commit();
                            }
                        } else {
                            Log.d("ALEJOTAG", "No such document");
                        }
                    } else {
                        Log.d("ALEJOTAG", "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    private void setPermissions(){
        // Here, thisActivity is the current activity
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean servicesOn=sharedPreferences.getBoolean(SERVICES_ON,false);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALL_LOG},
                        MY_PERMISSIONS_REQUEST_READ_CALL_LOG);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        }else if(!servicesOn){

            if (checkForPermission()){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SERVICES_ON,true);
                editor.commit();
                setScreenOnOffBackgroundService();
                setMeasurementWorker();
            }else {
                Toast.makeText(this,
                        getString(R.string.allow_usage_permissions),
                        Toast.LENGTH_LONG).show();
                final AppOpsManager appOps = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
                appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        getApplicationContext().getPackageName(),
                        new AppOpsManager.OnOpChangedListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.KITKAT)
                            public void onOpChanged(String op, String packageName) {
                                int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                        android.os.Process.myUid(), getPackageName());
                                if (mode == AppOpsManager.MODE_ALLOWED) {
                                    Log.d("ELTAGOSE", "SEPUD2O");
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(SERVICES_ON, true);
                                    editor.commit();
                                    setScreenOnOffBackgroundService();
                                    setMeasurementWorker();
                                    appOps.stopWatchingMode(this);
                                }
                            }
                        });
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

            }
        }else {
            if(!isMyServiceRunning(ScreenOnOffBackgroundService.class)){
                setScreenOnOffBackgroundService();
            }
            /**
            if(isWorkScheduled("jobTag")){
                setMeasurementWorker();
            }
             **/
            Log.d(SCREEN_TOGGLE_TAG, "WORK: "+isWorkScheduled("jobTag"));
            Log.d(SCREEN_TOGGLE_TAG, "service: "+isMyServiceRunning(ScreenOnOffBackgroundService.class));
        }
    }

    private boolean isWorkScheduled(String tag) {
        WorkManager instance = WorkManager.getInstance();
        if (instance == null) return false;
        LiveData<List<WorkStatus>> statuses = instance.getStatusesByTag(tag);
        if (statuses.getValue() == null) return false;
        boolean running = false;
        for (WorkStatus workStatus : statuses.getValue()) {
            running = workStatus.getState() == State.RUNNING | workStatus.getState() == State.ENQUEUED;
        }
        return running;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALL_LOG: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if(checkForPermission()){
                        setScreenOnOffBackgroundService();
                        setMeasurementWorker();
                    }else{
                        Toast.makeText(this,
                                getString(R.string.allow_usage_permissions),
                                Toast.LENGTH_LONG).show();
                        final AppOpsManager appOps = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
                        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                getApplicationContext().getPackageName(),
                                new AppOpsManager.OnOpChangedListener() {
                                    @Override
                                    @TargetApi(Build.VERSION_CODES.KITKAT)
                                    public void onOpChanged(String op, String packageName) {
                                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                                android.os.Process.myUid(), getPackageName());
                                        if (mode == AppOpsManager.MODE_ALLOWED) {
                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(SERVICES_ON, true);
                                            editor.commit();
                                            setScreenOnOffBackgroundService();
                                            setMeasurementWorker();
                                            appOps.stopWatchingMode(this);
                                        }
                                    }
                                });
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    private boolean checkForPermission() {
        AppOpsManager appOps = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), getApplicationContext().getPackageName());
        return mode == MODE_ALLOWED;
    }

    private void setScreenOnOffBackgroundService(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(SCREEN_ON_TIMESTAMP, 0);
        editor.putLong(SCREEN_TOTAL_TIME, 0);
        editor.putInt(SCREEN_ON_COUNT,0);
        editor.commit();
        Intent backgroundService = new Intent(this, ScreenOnOffBackgroundService.class);
        startService(backgroundService);
    }

    private void  setMeasurementWorker(){

        mWorkManager = WorkManager.getInstance();

        PeriodicWorkRequest.Builder myWorkBuilder =
                new PeriodicWorkRequest.Builder(MeasurementWorker.class, 1440, TimeUnit.MINUTES);

        PeriodicWorkRequest myWork = myWorkBuilder.build();
        WorkManager.getInstance()
                .enqueueUniquePeriodicWork("jobTag", ExistingPeriodicWorkPolicy.KEEP, myWork);

    }

}

