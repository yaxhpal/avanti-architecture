package in.avanti.app.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.avanti.app.R;
import in.avanti.app.utils.ActivityUtils;

/**
 * Displays an add or edit task screen.
 */
public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    private in.avanti.app.login.LoginPresenter mLoginPresenter;


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
        mLoginPresenter = new in.avanti.app.login.LoginPresenter(taskId, "", loginFragment);

        loginFragment.setPresenter(mLoginPresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
