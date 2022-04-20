package com.csmm.gestorescolar.client.handlers;

public interface PostEstadoComunicacionHandler extends RestClientBaseResponseHandler {
    void sessionRequestDidComplete(boolean update);
}

