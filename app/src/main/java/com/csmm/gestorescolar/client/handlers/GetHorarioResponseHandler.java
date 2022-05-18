package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.HorarioDTO;

public interface GetHorarioResponseHandler extends RestClientBaseResponseHandler {
    void requestDidComplete(HorarioDTO response);
}
