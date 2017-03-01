package in.avanti.app;

/**
 * Created by yashpal on 26/2/17.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
