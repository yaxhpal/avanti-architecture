package in.avanti.student_companion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import in.avanti.student_companion.R;
import in.avanti.student_companion.StudentCompanionApp;
import in.avanti.student_companion.http.TokenRequest;
import in.avanti.student_companion.http.Token;


/**
 * A login screen that offers login via email/password.
 * @author Yashpal Meena < yaxhpal @ gmail.com >
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * File name which stores the token.
     */
    public static final String TOKEN_FILENAME = "w143kxnu.idj";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressDialog mProgressDialog;
    private TextView mStatusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // First need to delete old token.
        StudentCompanionApp.getInstance().deleteToken();

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressDialog = new ProgressDialog(LoginActivity.this);

        mStatusMessage =  (TextView) findViewById(R.id.login_status_message);
        mStatusMessage.setText("");

        // Set message if any
        setMessage();

    }

    @Override
    public void onBackPressed() {}


    private void setMessage() {
        String message  = getIntent().getStringExtra("errorMsg");
        if (message != null){
            mStatusMessage.setText(message);
        } else{
            mStatusMessage.setText("");
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute();
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


    private void showProgress(final boolean show) {
        if (show) {
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Authenticating...");
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Represents a login/registration task used to authenticate the user.
     */
    public class UserLoginTask {

        private final String mEmail;
        private final String mPassword;
        private final Token mToken;

        UserLoginTask(String email, String password) {
            mEmail    = email;
            mPassword = password;
            mToken    = new Token();
        }

        public void execute() {
            TokenRequest request = new TokenRequest(mEmail, mPassword, getResponseListener(), getErrorListener());

            // Add the request to the RequestQueue. Requests are processed in this queue automatically
            StudentCompanionApp.getInstance().addToRequestQueue(request);
        }

        private Response.Listener<String> getResponseListener() {
            return new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mAuthTask = null;
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        mToken.setToken(jsonResponse.getString("access_token"));
                        VolleyLog.v("Token: %s", mToken.getToken());
                    } catch (JSONException err) {
                        VolleyLog.wtf(err, "Token request parsing failed: %s", err.getMessage());
                    } finally {
                        if (mToken.getToken() != null) {
                            FileOutputStream fos = null;
                            try {
                                // Write token into private file
                                fos = openFileOutput(LoginActivity.TOKEN_FILENAME, Context.MODE_PRIVATE);
                                fos.write(mToken.getToken().getBytes());
                                fos.close();

                                // Start Splash activity if we are done with token request
                                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myIntent);
                            } catch (FileNotFoundException err) {
                                VolleyLog.wtf(err, "Can not find file: %s", err.getMessage());
                            } catch (IOException err) {
                                VolleyLog.wtf(err, "Can not write into file %s: %s", LoginActivity.TOKEN_FILENAME, err.getMessage());
                            }
                            LoginActivity.this.finish();
                        }
                    }
                    showProgress(false);
                }
            };
        }

        private Response.ErrorListener getErrorListener () {
            return new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mAuthTask = null;
                    showProgress(false);
                    VolleyLog.e("Error: ", error.getMessage());
                    if (error instanceof TimeoutError) {
                        mStatusMessage.setText("Error: Connection timeout.");
                    } else {
                        mStatusMessage.setText("");
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }
            };
        }
    }
}