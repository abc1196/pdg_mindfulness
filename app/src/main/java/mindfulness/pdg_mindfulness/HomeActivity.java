package mindfulness.pdg_mindfulness;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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
