package com.openclassrooms.safetynet.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class OutputFormatIndentationJsonDataTest {

        @MockBean
        JsonGenerator jsonGenerator;
        @InjectMocks
        OutputFormatIndentationJsonData outputFormatIndentationJsonData;

        void set_nestingReflexion(int _nestingReflexion) throws NoSuchFieldException, IllegalAccessException {
            Field nestingField = OutputFormatIndentationJsonData.class.getDeclaredField("_nestingLevel");
            nestingField.setAccessible(true);
            nestingField.setInt(outputFormatIndentationJsonData,_nestingReflexion);
        }
    @Test
    void writeStartObjectTest() throws IOException {
        //nesting = 0
        outputFormatIndentationJsonData.writeStartObject(jsonGenerator);
        Mockito.verify(jsonGenerator).writeRaw("{"+ System.lineSeparator() + "  ");
        //nesting = 1
        outputFormatIndentationJsonData.writeStartObject(jsonGenerator);
        Mockito.verify(jsonGenerator).writeRaw(System.lineSeparator() +"\t{ ");
    }
    @Test
    void writeEndObjectTest() throws IOException, NoSuchFieldException, IllegalAccessException {
        //nesting ==2
        set_nestingReflexion(2);
        outputFormatIndentationJsonData.writeEndObject(jsonGenerator,0);
        Mockito.verify(jsonGenerator).writeRaw(" }");
        //else
        outputFormatIndentationJsonData.writeEndObject(jsonGenerator,0);
        Mockito.verify(jsonGenerator).writeRaw(System.lineSeparator() + "}");
    }
    @Test
    void writeObjectEntrySeparatorTest() throws NoSuchFieldException, IllegalAccessException, IOException {
            //nesting 1
        set_nestingReflexion(1);
        outputFormatIndentationJsonData.writeObjectEntrySeparator(jsonGenerator);
        Mockito.verify(jsonGenerator).writeRaw("," + System.lineSeparator() +"  ");
        //else
        set_nestingReflexion(2);
        outputFormatIndentationJsonData.writeObjectEntrySeparator(jsonGenerator);
        Mockito.verify(jsonGenerator).writeRaw(", ");
    }
    @Test
    void writeObjectFieldValueSeparatorTest() throws IOException {
        outputFormatIndentationJsonData.writeObjectFieldValueSeparator(jsonGenerator);
        Mockito.verify(jsonGenerator).writeRaw(":");

    }
    @Test
    void writeStartArrayTest() throws IOException, NoSuchFieldException, IllegalAccessException {
            //nesting 1
            set_nestingReflexion(1);
            outputFormatIndentationJsonData.writeStartArray(jsonGenerator);
            Mockito.verify(jsonGenerator).writeRaw(" [");
            outputFormatIndentationJsonData.writeStartArray(jsonGenerator);
            Mockito.verify(jsonGenerator).writeRaw("[");
    }
    @Test
    void writeEndArrayTest() throws NoSuchFieldException, IllegalAccessException, IOException {
        //nesting 2
        set_nestingReflexion(2);
        outputFormatIndentationJsonData.writeEndArray(jsonGenerator, 0);
        Mockito.verify(jsonGenerator).writeRaw("]");
        //else
        set_nestingReflexion(1);
        outputFormatIndentationJsonData.writeEndArray(jsonGenerator, 0);
        Mockito.verify(jsonGenerator).writeRaw(System.lineSeparator() + "  ]");
    }
    @Test
    void writeArrayValueSeparator() throws IOException {
            outputFormatIndentationJsonData.writeArrayValueSeparator(jsonGenerator);
            Mockito.verify(jsonGenerator).writeRaw(",");
    }
}
