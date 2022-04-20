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

            assertEquals(usuarioDTO.getToken(),   "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwidXN1YXJpbyI6ImFsYmVydG9nbCIsIm5vbWJyZSI6IkFsYmVydG8iLCJhcGVsbGlkbzEiOiJHYXJjw61hIiwiYXBlbGxpZG8yIjoiTG9wZXoiLCJuYWNpbWllbnRvIjoiMDAwMC0wMC0wMCIsImRuaSI6IjU1NTU1NTU1UCIsIm9hIjoibyIsImFjY2Vzb3MiOjQsInRpcG9Vc3VhcmlvIjoiZmFtaWxpYXMiLCJhbHVtbm9zQXNvY2lhZG9zIjpbeyJpZCI6MSwibm9tYnJlIjoiTWFyaW8gR2FyY8OtYSBQw6lyZXoiLCJyZWxhY2lvbiI6IlBhZHJlIn0seyJpZCI6Niwibm9tYnJlIjoiUGVwZSBHYXJjw61hIFDDqXJleiIsInJlbGFjaW9uIjoiUGFkcmUifV0sImlhdCI6MTY1MDQ0NjE0OCwiZXhwIjoxNjUwNTMwNzQ4fQ.XKWgvSk476eLM9et8oIuskSTD_wc1IXgxjAoUUjeu_k");

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
            assertEquals(usuarioDTO.getId(), 1);
            assertEquals(usuarioDTO.getUsuario(), "albertogl");
            assertEquals(usuarioDTO.getNombre(), "Alberto");
            assertEquals(usuarioDTO.getApellido1(), "García");
            assertEquals(usuarioDTO.getApellido2(), "Lopez");
            assertEquals(usuarioDTO.getNacimiento(), "0000-00-00");
            assertEquals(usuarioDTO.getDni(), "55555555P");
            assertEquals(usuarioDTO.getOa(), "o");
            assertEquals(usuarioDTO.getAccesos(), 4);
            assertEquals(usuarioDTO.getTipoUsuario(), 2);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
