package in.avanti.app.login;

import in.avanti.app.BasePresenter;
import in.avanti.app.BaseView;

/**
 * Listens to user actions from the UI ({@link LoginFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public interface ILoginContract {

    interface View extends BaseView<Presenter> {

        void login();

        void register();

        void logout();

        void showProfile();

        void isLoggedIn();

        void showProgress(final boolean show);

        void showMainActivity();

        void showMessage(Throwable error);
    }

    interface Presenter extends BasePresenter {

//        void saveToken(String token);
//
//        void saveProfile(String profile);

        void getToken(String emailId, String password);
    }
}
