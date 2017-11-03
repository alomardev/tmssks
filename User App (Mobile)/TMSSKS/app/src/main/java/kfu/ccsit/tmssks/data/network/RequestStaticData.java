package kfu.ccsit.tmssks.data.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RequestStaticData extends StringRequest {

    private Map<String, String> mParams;

    public RequestStaticData(int id, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(Method.POST, NetworkUtils.API_GET_STATIC_DATA, listener, errorListener);

        mParams = new HashMap<>();
        if (id > -1) mParams.put("user_id", id + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
