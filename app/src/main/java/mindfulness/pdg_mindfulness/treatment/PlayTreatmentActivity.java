package mindfulness.pdg_mindfulness.treatment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;
import mindfulness.pdg_mindfulness.R;

public class PlayTreatmentActivity extends AppCompatActivity {

    private static final String BODYSCANDESC = "El escaneo corporal ayuda a conocer tu cuerpo y ser conciente del mismo";
    private static final String ROUTINEDESC = "Haz esta actividad de manera conciente, disfruta plenamente el momento";
    private static final String PAUSEDESC = "Date una pausa. Respira. Relájate. Vuelve.";
    private CircleButton playSession;
    private TextView sessionDescription;
    private ImageView imageView;
    private String sessionType;
    private int dayNumber;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Map<String, Object> daysTemp;
    private MediaPlayer mediaPlayer;
    private int songProgress;
    private MaterialButton terminateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_treatment);
        playSession = findViewById(R.id.playSession);
        playSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong(v);
            }
        });
        imageView = findViewById(R.id.image_session);
        sessionDescription = findViewById(R.id.sessionDesc);
        sessionType = getIntent().getStringExtra("session");
        String sdayNumber = getIntent().getStringExtra("dayNumber");
        dayNumber = Integer.parseInt(sdayNumber);
        Log.d("CONIDAYNUMBERTREAT", getIntent().getStringExtra("dayNumber"));
        Log.d("CONISessiontype", getIntent().getStringExtra("session"));
        changeFieldValues();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mediaPlayer = new MediaPlayer();
        songProgress = 0;
        setDataSource();


        terminateButton=findViewById(R.id.terminateSessionButton);
        terminateButton.setVisibility(View.GONE);
        Log.d("conibotoninvisible", "boton invisible");
        terminateButton.setEnabled(false);
    }

    public void changeFieldValues() {

        if (sessionType.equals(TreatmentActivity.BODYSCAN)) {
            sessionDescription.setText(BODYSCANDESC);
            imageView.setImageResource(R.drawable.ic_bodyscan);
        } else if (sessionType.equals(TreatmentActivity.ROUTINE)) {
            sessionDescription.setText(ROUTINEDESC);
            if (dayNumber == 1) {
                imageView.setImageResource(R.drawable.ic_eating);
            } else if (dayNumber == 2) {
                imageView.setImageResource(R.drawable.ic_brush);
            } else if (dayNumber == 3) {
                imageView.setImageResource(R.drawable.ic_coffee);
            } else if (dayNumber == 4) {
                imageView.setImageResource(R.drawable.ic_wearing);
            } else if (dayNumber == 5) {
                imageView.setImageResource(R.drawable.ic_cooking);
            } else if (dayNumber == 6) {
                imageView.setImageResource(R.drawable.ic_eating);
            } else if (dayNumber == 7) {
                imageView.setImageResource(R.drawable.ic_shower);
            }
        } else if (sessionType.equals(TreatmentActivity.PAUSE)) {
            sessionDescription.setText(PAUSEDESC);
            imageView.setImageResource(R.drawable.ic_pauseday);
        }
    }

    public void playSong(View view) {
        if (mediaPlayer.isPlaying()) {
            playSession.setImageResource(R.drawable.ic_playsession);
            pauseSong();

        } else {
            playSession.setImageResource(R.drawable.ic_pausesession);
            mediaPlayer.seekTo(songProgress);
            mediaPlayer.start();
        }
    }

    public void pauseSong() {
        mediaPlayer.pause();
        songProgress = mediaPlayer.getCurrentPosition();
    }

    public void setDataSource() {
        try {
            daysTemp = new HashMap<>();
            daysTemp.put("day", dayNumber);
            daysTemp.put("isCompleted", false);
            daysTemp.put("isAvailable", true);
            daysTemp.put("isBodyScanDone", false);
            daysTemp.put("isRoutineDone", false);
            daysTemp.put("isPauseDone", false);
            if (sessionType.equals(TreatmentActivity.BODYSCAN)) {
                mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/03.%20Ride.mp3?alt=media&token=28870759-7b83-4cc1-bcde-9c1775c3c59d");
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer = mp;
                    }
                });
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        user = mAuth.getCurrentUser();
                        if (user != null) {
                            db.collection("BCT").document(user.getUid()).collection("days").document(dayNumber + "").update("isBodyScanDone", true);

                        }
                        playSession.setImageResource(R.drawable.ic_completed);
                        playSession.setEnabled(false);

                        terminate();

                    }
                });
            } else if (sessionType.equals(TreatmentActivity.ROUTINE)) {

                mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/03.%20Ride.mp3?alt=media&token=28870759-7b83-4cc1-bcde-9c1775c3c59d");
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer = mp;
                    }
                });
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        user = mAuth.getCurrentUser();
                        if (user != null) {
                            db.collection("BCT").document(user.getUid()).collection("days").document(dayNumber + "").update("isRoutineDone", true);

                        }
                        playSession.setImageResource(R.drawable.ic_completed);
                        playSession.setEnabled(false);
                        terminate();
                    }
                });

            } else if (sessionType.equals(TreatmentActivity.PAUSE)) {
                mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/03.%20Ride.mp3?alt=media&token=28870759-7b83-4cc1-bcde-9c1775c3c59d");
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer = mp;
                    }
                });
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        user = mAuth.getCurrentUser();
                        if (user != null) {
                            db.collection("BCT").document(user.getUid()).collection("days").document(dayNumber + "").update("isPauseDone", true);

                        }
                        playSession.setImageResource(R.drawable.ic_completed);
                        playSession.setEnabled(false);
                        terminate();

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        this.finish();
    }

    public void terminate(){
        terminateButton.setVisibility(View.VISIBLE);
        Log.d("conibotonvisible","botonvisible");
        terminateButton.setEnabled(true);
        terminateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), TreatmentActivity.class);
                intent.putExtra("dayNumber",dayNumber+"");
                startActivity(intent);
                finish();
            }
        });
    }

}
