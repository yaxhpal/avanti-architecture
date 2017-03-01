package in.avanti.app.data.source.remote;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class extends the VolleyRequest class to get Authorization token
 * Created on 9/2/17
 * @author      Yashpal Meena <yaxhpal@gmail.com>
 * @version     1.0
 * @since       1.0
 */

public class TokenRequest extends StringRequest {
    private String mEmail;
    private String mPassword;
    private String mUrl;
    private final static String TOKEN_URL = "http://service.staging.peerlearning.com/oauth/token";

    public TokenRequest(String email, String password, Listener<String> listener, ErrorListener errorListener) {
        super(Request.Method.POST, TOKEN_URL, listener, errorListener);
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("email", mEmail);
        params.put("password", mPassword);
        return params;
    }
}