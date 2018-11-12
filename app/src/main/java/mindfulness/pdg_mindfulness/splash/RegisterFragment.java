package mindfulness.pdg_mindfulness.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mindfulness.pdg_mindfulness.utils.others.BaseFragment;
import mindfulness.pdg_mindfulness.utils.interfaces.NavigationHost;
import mindfulness.pdg_mindfulness.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment {

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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        final TextInputLayout nameTextInput = view.findViewById(R.id.name_text_input);
        final TextInputEditText nameEditText = view.findViewById(R.id.name_edit_text);

        final TextInputLayout emailTextInput = view.findViewById(R.id.email_text_input);
        final TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        emailTextInput.setHelperText(getResources().getString(R.string.register_info_email));

        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        passwordTextInput.setHelperText(getResources().getString(R.string.register_info_password));

        final MaterialButton registerBackButton = view.findViewById(R.id.register_back_button);
        final MaterialButton registerButton=view.findViewById(R.id.register_button);
        registerButton.setEnabled(false);
        // Set an error if the password is less than 8 characters.
        registerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ((NavigationHost) getActivity()).registerUser(name, email, password);
                //progressBar.setVisibility(View.VISIBLE)
            }

        });


        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError("La contraseña debe tener al menos 8 carácteres"); //Clear the error
                }else{
                    passwordEditText.setError(null);
                }
                if(isEmailValid(emailEditText.getText())&&isPasswordValid(passwordEditText.getText())){
                    emailEditText.setError(null);
                    passwordEditText.setError(null);
                    registerButton.setEnabled(true);
                }else{
                    registerButton.setEnabled(false);
                }
                return false;
            }
        });

        // Clear the error once more than 8 characters are typed.
        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(!isEmailValid(emailEditText.getText())) {
                    emailEditText.setError("Ingresa un correo válido");
                }else{
                    emailEditText.setError(null);
                }
                if(isEmailValid(emailEditText.getText())&&isPasswordValid(passwordEditText.getText())){
                    emailEditText.setError(null);
                    passwordEditText.setError(null);
                    registerButton.setEnabled(true);
                }else{
                    registerButton.setEnabled(false);
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onBackPressed(){}

    /*
   In reality, this will have more complex logic including, but not limited to, actual
   authentication of the username and password.
*/
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }


    private boolean isEmailValid(@Nullable Editable text) {
        matcher = pattern.matcher(text.toString());
        return matcher.matches();
    }
}
