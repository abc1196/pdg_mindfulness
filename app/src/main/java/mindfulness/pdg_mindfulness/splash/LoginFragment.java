package mindfulness.pdg_mindfulness.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mindfulness.pdg_mindfulness.utils.others.BaseFragment;
import mindfulness.pdg_mindfulness.utils.interfaces.NavigationHost;
import mindfulness.pdg_mindfulness.R;


/**
 * A simple {@link Fragment} subclass
 */
public class LoginFragment extends BaseFragment {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final TextInputLayout emailTextInput = view.findViewById(R.id.email_text_input);
        final TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);

        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);

        MaterialButton loginButton = view.findViewById(R.id.login_button);
        MaterialButton loginBackButton = view.findViewById(R.id.login_back_button);
        MaterialButton registerButton = view.findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new RegisterFragment(), true); // Navigate to the next Fragment
            }
        });

        loginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ((NavigationHost) getActivity()).loginUser(email, password);
            }
        });

        return view;
    }

    @Override
    public void onBackPressed() {}

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }


    private boolean isEmailValid(@Nullable Editable text) {
        matcher = pattern.matcher(text.toString());
        return matcher.matches();
    }
}
