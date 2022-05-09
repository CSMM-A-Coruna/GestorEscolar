package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.PreferencesDTO;


public interface GetPreferencesResponseHandler extends RestClientBaseResponseHandler {
    void requestDidComplete(PreferencesDTO response);
}
