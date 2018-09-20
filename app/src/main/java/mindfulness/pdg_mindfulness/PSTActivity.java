package mindfulness.pdg_mindfulness;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PSTActivity extends AppCompatActivity implements NavigationHost {
    private final static String PST_TEST="PST_TEST";
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pst);

        Log.d("ALEJOTAG", "HOLA");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ALEJOTAG", "HOLA2");
        db.collection("PST_Test")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("ALEJOTAG", document.get("question").toString());
                            }
                        } else {
                            Log.w("ALEJOTAG", "Error getting documents.", task.getException());
                        }
                    }
                });

/**
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.dashboard_container, new HomeFragment())
                    .addToBackStack(null)
                    .commit();
        }
         */
    }
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
    public void registerUser(String name, String email, String password) {

    }

    @Override
    public void loginUser(String email, String password) {

    }
}
