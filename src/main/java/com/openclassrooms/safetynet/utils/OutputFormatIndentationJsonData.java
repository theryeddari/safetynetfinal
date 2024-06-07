package com.openclassrooms.safetynet.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.Instantiatable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

/**
 *  class which manages all operations related to a person information (updating, deleting, adding and get)
 */
public final class OutputFormatIndentationJsonData implements PrettyPrinter, Instantiatable<OutputFormatIndentationJsonData>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Manages the nesting level in the JSON to adapt the formatting.
     */
    private transient int _nestingLevel = 0;
    private static final OutputFormatIndentationJsonData INSTANCE = new OutputFormatIndentationJsonData();

    private static final Logger logger = LogManager.getLogger(OutputFormatIndentationJsonData.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private OutputFormatIndentationJsonData() {
    }

    /**
     * Gets the single instance of this class.
     *
     * @return the single instance of OutputFormatIndentationJsonData
     */
    public static OutputFormatIndentationJsonData getInstance() {
        return INSTANCE;
    }

    /**
     * Writes the root value separator.
     *
     * @param jsonGenerator the JSON generator
     */
    public void writeRootValueSeparator(JsonGenerator jsonGenerator) {
        // Not implemented
    }

    /**
     * Writes the start of an object.
     *
     * @param jsonGenerator the JSON generator
     * @throws IOException if writing fails
     */
    public void writeStartObject(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Start of writeStartObject with nesting level: {}", this._nestingLevel);
        if (this._nestingLevel == 0) {
            // Start of the root object
            jsonGenerator.writeRaw("{" + System.lineSeparator() + "  ");
            this._nestingLevel = 1;
        } else {
            // Start of a nested object
            jsonGenerator.writeRaw(System.lineSeparator() + "\t{ ");
            this._nestingLevel = 2;
        }
        logger.debug("End of writeStartObject with new nesting level: {}", this._nestingLevel);
    }

    /**
     * Writes the end of an object.
     *
     * @param jsonGenerator the JSON generator
     * @param i             the index
     * @throws IOException if writing fails
     */
    public void writeEndObject(JsonGenerator jsonGenerator, int i) throws IOException {
        logger.debug("Start of writeEndObject with nesting level: {}", this._nestingLevel);
        if (this._nestingLevel == 2) {
            // End of a nested object
            jsonGenerator.writeRaw(" }");
            this._nestingLevel = 1;
        } else {
            // End of the root object
            jsonGenerator.writeRaw(System.lineSeparator() + "}");
        }
        logger.debug("End of writeEndObject with new nesting level: {}", this._nestingLevel);
    }

    /**
     * Writes the separator between object entries.
     *
     * @param jsonGenerator the JSON generator
     * @throws IOException if writing fails
     */
    public void writeObjectEntrySeparator(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Start of writeObjectEntrySeparator with nesting level: {}", this._nestingLevel);
        if (this._nestingLevel == 1) {
            // For entries in the root object
            jsonGenerator.writeRaw("," + System.lineSeparator() + "  ");
        } else {
            // For entries in nested objects
            jsonGenerator.writeRaw(", ");
        }
        logger.debug("End of writeObjectEntrySeparator");
    }

    /**
     * Writes the separator between object field names and values.
     *
     * @param jsonGenerator the JSON generator
     * @throws IOException if writing fails
     */
    public void writeObjectFieldValueSeparator(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Write of object name/value separator");
        jsonGenerator.writeRaw(":");
    }

    /**
     * Writes the start of an array.
     *
     * @param jsonGenerator the JSON generator
     * @throws IOException if writing fails
     */
    public void writeStartArray(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Start of writeStartArray with nesting level: {}", this._nestingLevel);
        if (this._nestingLevel == 1) {
            // Start of the root array
            jsonGenerator.writeRaw(" [");
            this._nestingLevel = 2;
        } else {
            // Start of a nested array
            jsonGenerator.writeRaw("[");
        }
        logger.debug("End of writeStartArray with new nesting level: {}", this._nestingLevel);
    }

    /**
     * Writes the end of an array.
     *
     * @param jsonGenerator the JSON generator
     * @param i             the index
     * @throws IOException if writing fails
     */
    public void writeEndArray(JsonGenerator jsonGenerator, int i) throws IOException {
        logger.debug("Start of writeEndArray with nesting level: {}", this._nestingLevel);
        if (this._nestingLevel == 2) {
            // End of a nested array
            jsonGenerator.writeRaw("]");
        } else {
            // End of the root array
            jsonGenerator.writeRaw(System.lineSeparator() + "  ]");
        }
        logger.debug("End of writeEndArray with nesting level: {}", this._nestingLevel);
    }

    /**
     * Writes the separator between array values.
     *
     * @param jsonGenerator the JSON generator
     * @throws IOException if writing fails
     */
    public void writeArrayValueSeparator(JsonGenerator jsonGenerator) throws IOException {
        logger.debug("Write of array values separator");
        jsonGenerator.writeRaw(",");
    }

    /**
     * Handles before array values (not used).
     *
     * @param jsonGenerator the JSON generator
     */
    public void beforeArrayValues(JsonGenerator jsonGenerator) {
        // Not implemented
    }

    /**
     * Handles before object entries (not used).
     *
     * @param jsonGenerator the JSON generator
     */
    public void beforeObjectEntries(JsonGenerator jsonGenerator) {
        // Not implemented
    }

    /**
     * Creates a new instance of OutputFormatIndentationJsonData.
     *
     * @return a new instance of OutputFormatIndentationJsonData
     */
    public OutputFormatIndentationJsonData createInstance() {
        logger.info("Create new instance of OutputFormatIndentationJsonData");
        return new OutputFormatIndentationJsonData();
    }
}

