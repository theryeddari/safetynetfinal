package com.openclassrooms.safetynet.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.Instantiatable;
import com.fasterxml.jackson.core.util.Separators;


import java.io.IOException;

import java.io.Serial;
import java.io.Serializable;

public class OutputFormatIndentationJsonData implements PrettyPrinter, Instantiatable<OutputFormatIndentationJsonData>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    protected Separators _separators;
    private transient int _nesting = 0;


    public OutputFormatIndentationJsonData() {
        this._separators = DEFAULT_SEPARATORS;
    }
    public void writeRootValueSeparator(JsonGenerator jsonGenerator) {
    }

    public void writeStartObject(JsonGenerator jsonGenerator) throws IOException {
        if (this._nesting == 0){
            jsonGenerator.writeRaw("{"+ System.lineSeparator() + "  ");
            this._nesting = 1;
        }else {
            jsonGenerator.writeRaw(System.lineSeparator() +"\t{ " );
            this._nesting = 2;
        }
    }

    public void writeEndObject(JsonGenerator jsonGenerator, int i) throws IOException {
        if (this._nesting == 2){
        jsonGenerator.writeRaw(" }");
        this._nesting = 1;
        }else
            jsonGenerator.writeRaw(System.lineSeparator() + "}");
    }

    public void writeObjectEntrySeparator(JsonGenerator jsonGenerator) throws IOException {
        if (this._nesting == 1){
            jsonGenerator.writeRaw("," + System.lineSeparator() +"  ");
        }else
            jsonGenerator.writeRaw(", ");
    }

    public void writeObjectFieldValueSeparator(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeRaw(":");
    }

    public void writeStartArray(JsonGenerator jsonGenerator) throws IOException {
        if (this._nesting == 1) {
            jsonGenerator.writeRaw(" [");
            this._nesting = 2;
        }else
            jsonGenerator.writeRaw("[");
    }

    public void writeEndArray(JsonGenerator jsonGenerator, int i) throws IOException {
        if (this._nesting == 2) {
            jsonGenerator.writeRaw("]");
        }else
            jsonGenerator.writeRaw(System.lineSeparator() + "  ]");
    }

    public void writeArrayValueSeparator(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeRaw(",");
    }

    public void beforeArrayValues(JsonGenerator jsonGenerator) {
    }

    public void beforeObjectEntries(JsonGenerator jsonGenerator) {
    }

    public OutputFormatIndentationJsonData createInstance() {
        return new OutputFormatIndentationJsonData();
    }
}
