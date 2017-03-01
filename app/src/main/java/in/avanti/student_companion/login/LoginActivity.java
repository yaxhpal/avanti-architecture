package in.avanti.student_companion.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.avanti.student_companion.R;
import in.avanti.student_companion.utils.ActivityUtils;

/**
 * Displays an add or edit task screen.
 */
public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    private LoginPresenter mLoginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);

        LoginFragment loginFragment =
                (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String taskId = getIntent().getStringExtra(LoginFragment.ARGUMENT_LOGIN_ID);

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.contentFrame);
        }

        // Create the presenter
        mLoginPresenter = new LoginPresenter(taskId, "", loginFragment);

        loginFragment.setPresenter(mLoginPresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
