package in.avanti.student_companion.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyLog;

import in.avanti.student_companion.R;
import in.avanti.student_companion.activities.MainActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the login screen. Users can attempt login using email and password
 */
public class LoginFragment extends Fragment implements ILoginContract.View {

    public static final String ARGUMENT_LOGIN_ID = "LOGIN_ID";

    //region Declarations

    private ILoginContract.Presenter mPresenter;

    private EditText mEmailId;

    private EditText mPassword;

    private ProgressDialog mProgressDialog;

    private TextView mStatusMessage;

    //endregion


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button signIn = (Button) getActivity().findViewById(R.id.email_sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_frag, container, false);
        mEmailId = (EditText) root.findViewById(R.id.email);
        mPassword = (EditText) root.findViewById(R.id.password);
        mStatusMessage =  (TextView) root.findViewById(R.id.login_status_message);

        mProgressDialog = new ProgressDialog(getActivity());

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    @Override
    public void login() {
        // Reset errors.
        mEmailId.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailId.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailId.setError(getString(R.string.error_field_required));
            focusView = mEmailId;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailId.setError(getString(R.string.error_invalid_email));
            focusView = mEmailId;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mPresenter.getToken(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with valid logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with valid logic
        return password.length() > 4;
    }


    @Override
    public void showProgress(final boolean show) {
        if (show) {
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Authenticating...");
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void register() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void showProfile() {

    }

    @Override
    public void isLoggedIn() {

    }

    @Override
    public void showMainActivity() {
        Intent myIntent = new Intent(getContext(), MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }

    @Override
    public void showMessage(Throwable error) {
        showProgress(false);
        VolleyLog.e("Error: ", error.getMessage());
        if (error instanceof TimeoutError) {
            mStatusMessage.setText("Error: Connection timeout.");
        } else {
            mStatusMessage.setText("");
            mPassword.setError(getString(R.string.error_incorrect_password));
            mPassword.requestFocus();
        }
    }

}
