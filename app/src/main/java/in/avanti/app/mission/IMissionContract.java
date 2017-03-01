package in.avanti.app.mission;

import in.avanti.app.BasePresenter;
import in.avanti.app.BaseView;

/**
 * Listens to user actions from the UI ({@link MissionsFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public interface IMissionContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
