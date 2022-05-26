package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.LlaveroDTO;

import java.util.List;

public interface GetLlaveroByIdAlumnoResponseHandler extends RestClientBaseResponseHandler {
    void requestDidComplete(List<LlaveroDTO> response);
}
