package kfu.ccsit.tmssks.data.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;


public class RequestSimpleString extends StringRequest {

    private Map<String, String> mParams;

    public RequestSimpleString(String url,
                               Map<String, String> params,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);

        mParams = params;
        setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
