package com.github.jtail.sterren;

import com.google.gson.JsonElement;

import javax.validation.ValidationException;

@SuppressWarnings("unused")
public class ObjectValidationException extends ValidationException {
    private JsonElement feedback;

    public ObjectValidationException(String message, JsonElement feedback) {
        super(message);
        this.feedback = feedback;
    }

    public ObjectValidationException() {
    }

    public JsonElement getFeedback() {
        return feedback;
    }
}
