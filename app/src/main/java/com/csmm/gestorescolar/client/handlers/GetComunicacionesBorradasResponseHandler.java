package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.ComunicacionDTO;

import java.util.List;

public interface GetComunicacionesBorradasResponseHandler extends RestClientBaseResponseHandler {
    void sessionRequestDidComplete(List<ComunicacionDTO> response);
}
