package kfu.ccsit.tmssks.data.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RequestAuthorizeUser extends StringRequest {

    private Map<String, String> mParams;

    public RequestAuthorizeUser(int id, String hash,
                                Response.Listener<String> listener,
                                Response.ErrorListener errorListener) {
        this(id, hash, "", "", listener, errorListener);
    }

    public RequestAuthorizeUser(String username, String password,
                                Response.Listener<String> listener,
                                Response.ErrorListener errorListener) {
        this(-1, "", username, password, listener, errorListener);
    }

    public RequestAuthorizeUser(int id, String hash, String username, String password,
                                Response.Listener<String> listener,
                                Response.ErrorListener errorListener) {
        super(Method.POST, NetworkUtils.API_AUTHORIZE_USER_URL, listener, errorListener);

        mParams = new HashMap<>();
        if (id > -1) mParams.put("user_id", id + "");
        if (!hash.isEmpty()) mParams.put("id_hash", hash);
        if (!username.isEmpty()) mParams.put("username", username);
        if (!password.isEmpty()) mParams.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
