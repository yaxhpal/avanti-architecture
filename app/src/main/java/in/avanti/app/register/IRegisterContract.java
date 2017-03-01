package in.avanti.app.register;

import in.avanti.app.BasePresenter;
import in.avanti.app.BaseView;

/**
 * Listens to user actions from the UI ({@link RegisterFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public interface IRegisterContract {

    interface View extends BaseView<IRegisterContract.Presenter> {

        void register();

        void showProgress(final boolean show);
    }

    interface Presenter extends BasePresenter {

        void getToken(String emailId, String password);
    }
}
