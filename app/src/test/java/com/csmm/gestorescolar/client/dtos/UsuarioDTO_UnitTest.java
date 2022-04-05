package com.csmm.gestorescolar.client.dtos;

import static org.junit.Assert.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UsuarioDTO_UnitTest {
    @Test
    public void createUsuarioDTO_correct() {
        try {
            BufferedReader json = new BufferedReader(
                    new FileReader("src/test/java/com/csmm/gestorescolar/client/dtos/usuario.json"));

            String jsonString = "";
            String currentLine = json.readLine();
            while(currentLine!=null) {
                jsonString += currentLine;
                currentLine = json.readLine();
            }

            JSONObject jsonObject = new JSONObject(jsonString);
            UsuarioDTO usuarioDTO = new UsuarioDTO(jsonObject);

            assertEquals(usuarioDTO.getToken(),   "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywidXN1YXJpbyI6Im1hcmlhbWIiLCJub21icmUiOiJNYXLDrWEgZGVsIENhcm1lbiIsImFwZWxsaWRvMSI6Ik1pcmFtb250ZXMiLCJhcGVsbGlkbzIiOiJCbGFuY28iLCJuYWNpbWllbnRvIjoiMDAwMC0wMC0wMCIsImRuaSI6IjU1NTU1NTU1UCIsIm9hIjoiYSIsImFjY2Vzb3MiOjQsInRpcG9Vc3VhcmlvIjoiZmFtaWxpYXMiLCJpYXQiOjE2NDg3OTg0MTIsImV4cCI6MTY0ODg4MzAxMn0.Ay0DagysPOmIH_f5F6OlHDqpLp5hLh_QRnRfqn4Whbk");

            BufferedReader userJson = new BufferedReader(
                    new FileReader("src/test/java/com/csmm/gestorescolar/client/dtos/tokenDecoded.json"));

            String userJsonString = "";
            String currentLine2 = userJson.readLine();
            while(currentLine2!=null) {
                userJsonString += currentLine2;
                currentLine2 = userJson.readLine();
            }
            JSONObject userJsonObject = new JSONObject(userJsonString);

            //assertEquals(usuarioDTO.getTokenDecoded(), userJsonObject);
            assertEquals(usuarioDTO.getId(), 3);
            assertEquals(usuarioDTO.getUsuario(), "mariamb");
            assertEquals(usuarioDTO.getNombre(), "María del Carmen");
            assertEquals(usuarioDTO.getApellido1(), "Miramontes");
            assertEquals(usuarioDTO.getApellido2(), "Blanco");
            assertEquals(usuarioDTO.getNacimiento(), "0000-00-00");
            assertEquals(usuarioDTO.getDni(), "55555555P");
            assertEquals(usuarioDTO.getOa(), "a");
            assertEquals(usuarioDTO.getAccesos(), 4);
            assertEquals(usuarioDTO.getTipoUsuario(), 2);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
