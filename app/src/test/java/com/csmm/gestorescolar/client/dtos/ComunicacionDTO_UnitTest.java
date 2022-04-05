package com.csmm.gestorescolar.client.dtos;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ComunicacionDTO_UnitTest {
    @Test
    public void createDto_correct() {

        try {
            BufferedReader json = new BufferedReader(
                    new FileReader("src/test/java/com/csmm/gestorescolar/client/dtos/comunicacion.json"));

            String jsonString = "";
            String currentLine = json.readLine();
            while(currentLine!=null) {
                jsonString += currentLine;
                currentLine = json.readLine();
            }

            JSONObject jsonObject = new JSONObject(jsonString);

            ComunicacionDTO  dto = new ComunicacionDTO(jsonObject);

            assertEquals(dto.getIdComunicacion(), 3);
            assertEquals(dto.getIdRemite(), 2);
            assertEquals(dto.getIdDestino(), 2);
            assertEquals(dto.getTipoRemite(), 2);
            assertEquals(dto.getTipoDestino(), 3);
            assertEquals(dto.getIdAlumnoAsociado(), 4);
            assertEquals(dto.getAsunto(), "Calificaciones 2º Trimestre");
            assertEquals(dto.getTexto(), "Prueba...");
            assertFalse(dto.isImportante());
            assertEquals(dto.getFecha(), "18:53");
            assertEquals(dto.getLeida(), "null");
            assertEquals(dto.getEliminado(), "null");
            assertEquals(dto.getNombreAlumnoAsociado(), "Mario García Pérez");
            assertEquals(dto.getNombreRemite(), "Juana María Pérez");
            assertEquals(dto.getNombreDestino(), "Pedro López");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
