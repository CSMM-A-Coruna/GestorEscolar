package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.UsuarioDTO;

public interface PostLoginResponseHandler extends RestClientBaseResponseHandler {
    void sessionRequestDidComplete(UsuarioDTO dto);
}
