package com.openclassrooms.safetynet.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.Instantiatable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public final class OutputFormatIndentationJsonData implements PrettyPrinter, Instantiatable<OutputFormatIndentationJsonData>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Manages the level in the Json to adapt the formatting
    private transient int _nesting = 0;
    private static final OutputFormatIndentationJsonData INSTANCE = new OutputFormatIndentationJsonData();

    private static final Logger logger = LogManager.getLogger(OutputFormatIndentationJsonData.class);

    // Private constructor to prevent instantiation
    private OutputFormatIndentationJsonData() {
    }

    public static OutputFormatIndentationJsonData getInstance() {
        return INSTANCE;
    }

    // Method to write the root value separator
    public void writeRootValueSeparator(JsonGenerator jsonGenerator) {
        // Not implemented
    }

    // Method to write the start of an object
    public void writeStartObject(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Start of writeStartObject with nesting level: {}", this._nesting);
        if (this._nesting == 0) {
            // Start of the root object
            jsonGenerator.writeRaw("{" + System.lineSeparator() + "  ");
            this._nesting = 1;
        } else {
            // Start of a nested object
            jsonGenerator.writeRaw(System.lineSeparator() + "\t{ ");
            this._nesting = 2;
        }
        logger.debug("End of writeStartObject with nouveau nesting level: {}", this._nesting);
    }

    // Method to write the end of an object
    public void writeEndObject(JsonGenerator jsonGenerator, int i) throws IOException {
        logger.debug("Start of writeEndObject with nesting level: {}", this._nesting);
        if (this._nesting == 2) {
            // End of a nested object
            jsonGenerator.writeRaw(" }");
            this._nesting = 1;
        } else {
            // End of the root object
            jsonGenerator.writeRaw(System.lineSeparator() + "}");
        }
        logger.debug("End of writeEndObject with nouveau nesting level: {}", this._nesting);
    }

    // Method to write the separator between object entries
    public void writeObjectEntrySeparator(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Start of writeObjectEntrySeparator with nesting level: {}", this._nesting);
        if (this._nesting == 1) {
            // For entries in the root object
            jsonGenerator.writeRaw("," + System.lineSeparator() + "  ");
        } else {
            // For entries in nested objects
            jsonGenerator.writeRaw(", ");
        }
        logger.debug("End of writeObjectEntrySeparator");
    }

    // Method to write the separator between object field names and values
    public void writeObjectFieldValueSeparator(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Write of separator name/value de l'objet");
        jsonGenerator.writeRaw(":");
    }

    // Method to write the start of an array
    public void writeStartArray(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Start of writeStartArray with nesting level: {}", this._nesting);
        if (this._nesting == 1) {
            // Start of the root array
            jsonGenerator.writeRaw(" [");
            this._nesting = 2;
        } else {
            // Start of a nested array
            jsonGenerator.writeRaw("[");
        }
        logger.debug("End of writeStartArray with nouveau nesting level: {}", this._nesting);
    }

    // Method to write the end of an array
    public void writeEndArray(JsonGenerator jsonGenerator, int i) throws IOException {
        logger.debug("Start of writeEndArray with nesting level: {}", this._nesting);
        if (this._nesting == 2) {
            // End of a nested array
            jsonGenerator.writeRaw("]");
        } else {
            // End of the root array
            jsonGenerator.writeRaw(System.lineSeparator() + "  ]");
        }
        logger.debug("End of writeEndArray with nesting level: {}", this._nesting);
    }

    // Method to write the separator between array values
    public void writeArrayValueSeparator(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Write of separator values of arrays");
        jsonGenerator.writeRaw(",");
    }

    // Methods for handling array and object entries (not used)
    public void beforeArrayValues(JsonGenerator jsonGenerator) {
        // Not implemented
    }

    public void beforeObjectEntries(JsonGenerator jsonGenerator) {
        // Not implemented
    }

    // Method to create a new instance of OutputFormatIndentationJsonData
    public OutputFormatIndentationJsonData createInstance() {
        logger.info("Create new instance of OutputFormatIndentationJsonData");
        return new OutputFormatIndentationJsonData();
    }
}
