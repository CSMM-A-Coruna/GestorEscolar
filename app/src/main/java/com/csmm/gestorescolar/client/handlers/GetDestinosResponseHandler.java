package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.DestinoDTO;

import java.util.List;

public interface GetDestinosResponseHandler extends RestClientBaseResponseHandler {
    void requestDidComplete(List<DestinoDTO> response);
}
