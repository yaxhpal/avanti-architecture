package in.avanti.app.register;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.avanti.app.R;
import in.avanti.app.utils.ActivityUtils;

/**
 * Displays an add or edit task screen.
 */
public class RegisterActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    private RegisterPresenter mRegisterPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_act);

        RegisterFragment registerFragment = (RegisterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.registerContentFrame);

        if (registerFragment == null) {
            registerFragment = RegisterFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    registerFragment, R.id.registerContentFrame);
        }

        // Create the presenter
        mRegisterPresenter = new RegisterPresenter();

        registerFragment.setPresenter(mRegisterPresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

}
