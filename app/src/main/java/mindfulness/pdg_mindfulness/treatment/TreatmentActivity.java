package mindfulness.pdg_mindfulness.treatment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import at.markushi.ui.CircleButton;
import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.dashboard.HomeActivity;

public class TreatmentActivity extends AppCompatActivity {
    public final static String BODYSCAN = "BODYSCAN";
    public final static String ROUTINE = "ROUTINE";
    public final static String PAUSE = "PAUSE";
    private CircleButton playSongButton;
    private MaterialButton bodyScanButton;
    private MaterialButton dailyActivityButton;
    private MaterialButton pauseButton;
    private TextView textView;
    private String dayNumber;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private boolean isAvailable;
    private boolean isCompleted;
    private boolean isBodyScanDone;
    private boolean isPauseDone;
    private boolean isRoutineDone;
    private CircleButton completeDayButton;
    private ImageView bodyScanImage;
    private ImageView routineImage;
    private ImageView pauseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);
        textView = (TextView) findViewById(R.id.dayNumber);
        dayNumber = getIntent().getStringExtra("dayNumber");
        Log.d("conidaynumfragment", dayNumber + "");
        String currentText = textView.getText().toString();
        textView.setText(currentText + "" + dayNumber);
        bodyScanImage=findViewById(R.id.imageBodyScanSession);
        routineImage=findViewById(R.id.imageRoutineSession);
        pauseImage=findViewById(R.id.imagePauseSession);
        bodyScanButton = (MaterialButton) findViewById(R.id.bodyScan_button);
        dailyActivityButton = (MaterialButton) findViewById(R.id.daily_button);
        pauseButton = (MaterialButton) findViewById(R.id.pauseDay_button);
        completeDayButton=findViewById(R.id.completeDayButton);
        completeDayButton.setEnabled(false);
        completeDayButton.setVisibility(View.GONE);

        setListeners();
        checkSessions();

    }

    public void checkSessions() {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        //days = new ArrayList<>();
        user = mAuth.getCurrentUser();
        //treatment = new Treatment(user.getUid());
        if (user != null) {
            db.collection("BCT").document(user.getUid()).collection("days").document(dayNumber + "").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document2 = task.getResult();
                                int day = ((Long) document2.get("day")).intValue();
                                isAvailable = (boolean) document2.getData().get("isAvailable");
                                isCompleted = (boolean) document2.getData().get("isCompleted");
                                isBodyScanDone = (boolean) document2.getData().get("isBodyScanDone");
                                isPauseDone = (boolean) document2.getData().get("isPauseDone");
                                isRoutineDone = (boolean) document2.getData().get("isRoutineDone");

                                if(isBodyScanDone){
                                    bodyScanButton.setEnabled(false);
                                    bodyScanImage.setBackgroundResource(R.color.colorDisable);
                                }
                                if(isPauseDone){
                                    pauseButton.setEnabled(false);
                                    pauseImage.setBackgroundResource(R.color.colorDisable);
                                }
                                if(isRoutineDone){
                                    dailyActivityButton.setEnabled(false);
                                    routineImage.setBackgroundResource(R.color.colorDisable);
                                }
                                if(isBodyScanDone&&isPauseDone&&isRoutineDone){
                                    completeDayButton.setVisibility(View.VISIBLE);
                                    completeDayButton.setEnabled(true);
                                    db.collection("BCT").document(user.getUid()).collection("days").document(dayNumber + "").update("isCompleted", true);
                                    Log.d("CONIDAYCOMPLETED","El dia se completo");
                                    int nextDay=day+1;
                                    String next=nextDay+"";
                                    db.collection("BCT").document(user.getUid()).collection("days").document(next+"").update("isAvailable", true);
                                    Log.d("CONInextdayavialable","El siguiente dia esta disponible");

                                }

                            }
                        }
                    });
        }
    }

    public void setListeners(){
        bodyScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayTreatmentActivity.class);
                intent.putExtra("session", BODYSCAN);
                intent.putExtra("dayNumber", dayNumber + "");
                startActivity(intent);
            }
        });
        dailyActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayTreatmentActivity.class);
                intent.putExtra("session", ROUTINE);
                intent.putExtra("dayNumber", dayNumber + "");
                startActivity(intent);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayTreatmentActivity.class);
                intent.putExtra("session", PAUSE);
                intent.putExtra("dayNumber", dayNumber + "");
                startActivity(intent);
            }
        });

        completeDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

}
