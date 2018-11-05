package mindfulness.pdg_mindfulness.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import mindfulness.pdg_mindfulness.dashboard.WelcomeActivity;
import mindfulness.pdg_mindfulness.utils.others.BaseFragment;
import mindfulness.pdg_mindfulness.dashboard.HomeActivity;
import mindfulness.pdg_mindfulness.utils.interfaces.NavigationHost;
import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver;
import mindfulness.pdg_mindfulness.utils.service.ScreenOnOffBackgroundService;
import mindfulness.pdg_mindfulness.utils.worker.MeasurementWorker;

import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_ON_COUNT;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_ON_TIMESTAMP;
import static mindfulness.pdg_mindfulness.utils.receiver.ScreenOnOffReceiver.SCREEN_TOTAL_TIME;

public class SplashActivity extends AppCompatActivity  implements NavigationHost {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private  Map<String, Object> newUser;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        checkCurrentUser(currentUser);
        db= FirebaseFirestore.getInstance();
        newUser = new HashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new SplashFragment())
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
    public void registerUser(final String  name, String email, String password) {
        Map<String, Object> userTmp = new HashMap<>();
        userTmp.put("name",name);
        userTmp.put("email",email);
        userTmp.put("isFirstLogin",true);
        userTmp.put("currentTime",System.currentTimeMillis());
        newUser=userTmp;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ALEJOTAG", "Authentication succesful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName((String)newUser.get("name"))
                                    .setPhotoUri(null)
                                    .build();
                            db.collection("users").document(user.getUid()).set(newUser);
                            newUser=new HashMap<>();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mAuth.getCurrentUser().sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                                                    editor.putString("USER_NAME",name);
                                                                    editor.commit();
                                                                    Toast.makeText(getApplicationContext(),"Revisa tu correo electrónico y confirma tu cuenta.", Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("ALEJOTAG", "Authentication failed");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

    @Override
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ALEJOTAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference docRef = db.collection("users").document(user.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("ALEJOTAG", "DocumentSnapshot data: " + document.getData());
                                            boolean isFirstLogin=(boolean)document.getData().get("isFirstLogin");
                                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREFERENCES",Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor=sharedPreferences.edit();
                                            editor.putString("USER_NAME",(String)document.getData().get("name"));
                                            editor.commit();
                                            if(isFirstLogin){
                                                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        } else {
                                            Log.d("ALEJOTAG", "No such document");
                                        }
                                    } else {
                                        Log.d("ALEJOTAG", "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("ALEJOTAG", "signInWithEmail:failure");
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        tellFragments();
        super.onBackPressed();
    }


    private void tellFragments(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if(f != null && f instanceof BaseFragment)
                ((BaseFragment)f).onBackPressed();
        }
    }

    private void checkCurrentUser(FirebaseUser currentUser){
        if(currentUser!=null){
            boolean verifiedEmail=currentUser.isEmailVerified();
            if(!verifiedEmail){
                Toast.makeText(getApplicationContext(),"Por favor confirma tu cuenta. Revisa tu correo electrónico.", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
