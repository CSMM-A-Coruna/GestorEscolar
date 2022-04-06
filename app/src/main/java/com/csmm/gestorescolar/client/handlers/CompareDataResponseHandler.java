package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.UsuarioDTO;

public interface CompareDataResponseHandler extends RestClientBaseResponseHandler {
    void sessionRequestDidComplete(UsuarioDTO dto);
}
