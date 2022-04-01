package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.ComunicacionDTO;
import com.csmm.gestorescolar.client.dtos.UsuarioDTO;

import java.util.List;

public interface GetComunicacionesRecibidasResponseHandler extends RestClientBaseResponseHandler {
    void sessionRequestDidComplete(List<ComunicacionDTO> response);
}
