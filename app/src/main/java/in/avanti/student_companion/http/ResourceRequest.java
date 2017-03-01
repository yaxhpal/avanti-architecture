package in.avanti.student_companion.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import in.avanti.student_companion.StudentCompanionApp;

/**
 * Generic class for consuming Avanti API
 * Created on 11/2/17
 * @author      Yashpal Meena <yaxhpal@gmail.com>
 * @version     1.0
 * @since       1.0
 */
public class ResourceRequest  extends StringRequest {

    public ResourceRequest (String resourceUrl, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, resourceUrl, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + StudentCompanionApp.getInstance().getToken());
        return headers;
    }
}