package mindfulness.pdg_mindfulness.dashboard;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mindfulness.pdg_mindfulness.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {
    private MaterialButton playSongButton;
    public HomeFragment() {}
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        playSongButton = (MaterialButton) view.findViewById(R.id.playSongButton);
        playSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong(v);
            }
        });

        return view;
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
