package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.UsuarioDTO;

public interface LoginResponseHandler extends RestClientBaseResponseHandler {
    void sessionRequestDidComplete(UsuarioDTO dto);
}
