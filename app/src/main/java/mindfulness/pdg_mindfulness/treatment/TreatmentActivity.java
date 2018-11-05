package mindfulness.pdg_mindfulness.treatment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import at.markushi.ui.CircleButton;
import mindfulness.pdg_mindfulness.R;

public class TreatmentActivity extends AppCompatActivity {
    private CircleButton playSongButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

    }

    public void playSong(View view) {
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/13%20-%20Un%20Mill%C3%B3n%20De%20A%C3%B1os%20Luz.mp3?alt=media&token=5ddbbb28-5c42-40a4-a6ef-9d8d46564fb3");
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
