package mindfulness.pdg_mindfulness.measurement;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.dashboard.HomeActivity;
import mindfulness.pdg_mindfulness.dashboard.data.User;

public class PSTScoreActivity extends AppCompatActivity {

    public static final String PST_SCORE_LOW_URL="https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/pst_leves%2Fpst_score_low.jpg?alt=media&token=78ccf9d4-f7e0-422e-93fd-f84993db76ad";

    public static final String PST_SCORE_MEDIUM_URL="https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/pst_leves%2Fpst_score_medium.jpg?alt=media&token=78ccf9d4-f7e0-422e-93fd-f84993db76ad";

    public static final String PST_SCORE_HIGH_URL="https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/pst_leves%2Fpst_score_high.jpg?alt=media&token=78ccf9d4-f7e0-422e-93fd-f84993db76ad";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private MaterialButton scoreButton;
    private ProgressBar loadingBar;
    private ImageView scoreImage;
    private TextView scoreText;
    private LinearLayout scoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pstscore);
        mAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        Intent intent= getIntent();
        final int score=intent.getIntExtra("PST_SCORE",0);
        Log.d("ALEJOTAG","SCORE: "+score);
        scoreText=(TextView)findViewById(R.id.score_text);
        String text="";
        final SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        final String userName=sharedPreferences.getString("USER_NAME",null);
        if(userName!=null){
            text+=userName+", "+"\n"+"\n";
            text+="Tu puntaje es de "+score+" puntos. Esto significa que ";
        }
        scoreImage=(ImageView)findViewById(R.id.score_image);
        if (score<=18){
            Picasso.get().load(PST_SCORE_LOW_URL).into(scoreImage);
        }else if(score>18&&score<=36){
            Picasso.get().load(PST_SCORE_MEDIUM_URL).into(scoreImage);
        }else{
            Picasso.get().load(PST_SCORE_HIGH_URL).into(scoreImage);
        }
        scoreText.setText(text);
        loadingBar= (ProgressBar)findViewById(R.id.loadingBar);
        scoreLayout=(LinearLayout)findViewById(R.id.score_layout);
        loadingBar.setVisibility(View.GONE);
        scoreLayout.setVisibility(View.VISIBLE);
        scoreButton=(MaterialButton)findViewById(R.id.score_button);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null){
                    Calendar calendar = Calendar.getInstance();
                    String toDate = String.valueOf(calendar.getTimeInMillis());
                    calendar.add(Calendar.DATE, 7);
                    String nextPST = String.valueOf(calendar.getTimeInMillis());
                    db.collection("users").document(user.getUid()).update("lastPST",score, "nextPST",nextPST)
                            .addOnSuccessListener(new OnSuccessListener< Void >() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    boolean isFirstScore=sharedPreferences.getBoolean("FIRST_SCORE",false);
                                    if(!isFirstScore) {
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putBoolean("FIRST_SCORE",true);
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                    }else{
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
        }
    }
