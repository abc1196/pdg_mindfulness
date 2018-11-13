package mindfulness.pdg_mindfulness.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import mindfulness.pdg_mindfulness.dashboard.HealthFragment;
import mindfulness.pdg_mindfulness.measurement.PSTScoreActivity;
import mindfulness.pdg_mindfulness.utils.others.BaseFragment;
import mindfulness.pdg_mindfulness.utils.interfaces.NavigationHost;
import mindfulness.pdg_mindfulness.R;


/**
 * A simple {@link Fragment} subclass
 */
public class SplashFragment extends BaseFragment {

    private static final String SPLASH_ICON_URL="https://firebasestorage.googleapis.com/v0/b/pdg-mindfulness.appspot.com/o/splash_icon.gif?alt=media&token=f50b1035-e0c0-4da0-8e2f-3aaae8317b7a";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);

        ImageView splashImage=(ImageView)view.findViewById(R.id.splash_image);
        //Glide.with(this).asGif().load(SPLASH_ICON_URL).into(splashImage);
        Picasso.get().load(HealthFragment.PST_SCORE_HIGH_URL).into(splashImage);

        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        MaterialButton registerButton = view.findViewById(R.id.register_button);
        MaterialButton signinButton = view.findViewById(R.id.signin_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new RegisterFragment(), true); // Navigate to the next Fragment
            }
        });


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), true); // Navigate to the next Fragment
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
