package com.csmm.gestorescolar.client.handlers;

import org.json.JSONObject;

public interface PostSendComunicacionResponseHandler extends RestClientBaseResponseHandler{
    void requestDidComplete(String id);
}
