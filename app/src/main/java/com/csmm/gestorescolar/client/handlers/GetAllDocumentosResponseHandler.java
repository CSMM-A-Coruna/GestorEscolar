package com.csmm.gestorescolar.client.handlers;

import com.csmm.gestorescolar.client.dtos.DocumentoDTO;

import java.util.List;

public interface GetAllDocumentosResponseHandler extends RestClientBaseResponseHandler {
    void requestDidComplete(List<DocumentoDTO> response);
}
