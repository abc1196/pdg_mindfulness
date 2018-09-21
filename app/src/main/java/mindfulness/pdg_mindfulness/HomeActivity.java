package mindfulness.pdg_mindfulness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class HomeActivity extends AppCompatActivity  implements  DashboardNavigationHost {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
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

        //mAuth.addAuthStateListener(mAuthListner);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.dashboard_container, new HomeFragment())
                    .addToBackStack(null)
                    .commit();
        }
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
        Intent intent = new Intent(getApplicationContext(), PSTActivity.class);
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
