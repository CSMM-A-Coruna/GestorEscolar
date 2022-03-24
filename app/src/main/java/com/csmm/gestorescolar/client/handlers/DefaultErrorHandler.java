package com.csmm.gestorescolar.client.handlers;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class DefaultErrorHandler implements Response.ErrorListener {
    private RestClientBaseResponseHandler baseHandler;

    public DefaultErrorHandler(RestClientBaseResponseHandler baseHandler) {
        this.baseHandler = baseHandler;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        int errorCode;
        if (error.networkResponse != null) {
            errorCode = error.networkResponse.statusCode;
        } else {
            error.printStackTrace();
            errorCode = -1;
        }
        this.baseHandler.requestDidFail(errorCode);
    }
}
