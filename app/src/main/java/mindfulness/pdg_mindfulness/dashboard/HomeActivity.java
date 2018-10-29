package mindfulness.pdg_mindfulness.dashboard;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import mindfulness.pdg_mindfulness.utils.others.BaseFragment;
import mindfulness.pdg_mindfulness.measurement.HRVActivity;
import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.splash.SplashActivity;
import mindfulness.pdg_mindfulness.utils.interfaces.DashboardNavigationHost;
import mindfulness.pdg_mindfulness.utils.service.ScreenOnOffBackgroundService;
import mindfulness.pdg_mindfulness.utils.worker.MeasurementWorker;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Process.myUid;

import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_ON_COUNT;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_ON_TIMESTAMP;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_TOTAL_TIME;


public class HomeActivity extends AppCompatActivity  implements DashboardNavigationHost {

    private final static int MY_PERMISSIONS_REQUEST_READ_CALL_LOG=0;
    private final static String SERVICES_ON="SERVICES_ON";

    private WorkManager mWorkManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private BottomNavigationView bottomNavigationView;
    private List<Fragment> fragments = new ArrayList<>(3);
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        checkCurrentUser(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setPermissions();
        setScreenOnOffBackgroundService();
        setMeasurementWorker();

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
        HRVFragment hrvFragment = new HRVFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        fragments.add(homeFragment);
        fragments.add(hrvFragment);
        fragments.add(profileFragment);
    }

    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void newPST() {
        Intent intent = new Intent(getApplicationContext(), HRVActivity.class);
        this.finish();
        startActivity(intent);
    }

    @Override
    public void logout() {
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();

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
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        }
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
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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

