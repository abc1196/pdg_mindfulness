package mindfulness.pdg_mindfulness.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import mindfulness.pdg_mindfulness.utils.others.BaseFragment;
import mindfulness.pdg_mindfulness.measurement.HRVActivity;
import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.splash.SplashActivity;
import mindfulness.pdg_mindfulness.utils.interfaces.DashboardNavigationHost;


public class HomeActivity extends AppCompatActivity  implements DashboardNavigationHost {
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
}

/**
public class HomeActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "selected_item";
    private BottomNavigationView bottomNavigationView;
    private int mSelectedItem;
    BottomNavigationView botNavView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        botNavView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        botNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // handle desired action here
                // One possibility of action is to replace the contents above the nav bar
                // return true if you want the item to be displayed as the selected item
                selectActivity(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = botNavView.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = botNavView.getMenu().getItem(0);
        }
        selectActivity(selectedItem);
    }
    private void selectActivity(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_home:
                openPSTActivity();
                break;
        }


    }
    public void openPSTActivity(){
        Intent intent=new Intent(this, PSTActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed(){
        MenuItem homeItem = botNavView.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
                    // select home item
            selectActivity(homeItem);
        } else {
        super.onBackPressed();
        }
    }
}
**/