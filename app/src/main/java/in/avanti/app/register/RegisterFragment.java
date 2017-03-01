package in.avanti.app.register;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import in.avanti.app.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by yashpal on 1/3/17.
 */

public class RegisterFragment extends Fragment implements IRegisterContract.View {


    //region Declarations

    private IRegisterContract.Presenter mPresenter;

    private EditText mFullname;

    private EditText mEmailId;

    private EditText mPassword;

    private EditText mConfirmPassword;

    private ProgressDialog mProgressDialog;


    //endregion

    @Override
    public void register() {

    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void setPresenter(IRegisterContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button signIn = (Button) getActivity().findViewById(R.id.register_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register_frag, container, false);
        mFullname = (EditText) root.findViewById(R.id.fullname);
        mEmailId = (EditText) root.findViewById(R.id.email);
        mPassword = (EditText) root.findViewById(R.id.password);
        mConfirmPassword = (EditText) root.findViewById(R.id.confirm_password);
        mProgressDialog = new ProgressDialog(getActivity());
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }
}
