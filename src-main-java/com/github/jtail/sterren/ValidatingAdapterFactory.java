/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jtail.sterren;

import com.github.jtail.sterren.adapters.ValidatingAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

@AllArgsConstructor
public class ValidatingAdapterFactory implements TypeAdapterFactory {
    private final Validator validator;
    private final ViolationConverter converter;

    public ValidatingAdapterFactory() {
        this(Validation.buildDefaultValidatorFactory().getValidator());
    }

    public ValidatingAdapterFactory(Validator validator) {
        this.validator = validator;
        this.converter = new ViolationConverter();
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
        BiConsumer<T, Optional<JsonElement>> validate = validator != null ? this::validate : (t, v) -> {};
        return new ValidatingAdapter<>(gson.getDelegateAdapter(this, type), validate, type);
    }

    private <T> T validate(T object, Optional<JsonElement> errors) {
        JsonElement structuralErrors = errors.orElse(new JsonObject());
        JsonElement feedback;
        if (object != null) {
            Set<ConstraintViolation<T>> violations = validator.validate(object);
            feedback = converter.merge(violations, structuralErrors);
        } else {
            feedback = structuralErrors;
        }
        if (feedback.isJsonArray() || feedback.getAsJsonObject().entrySet().isEmpty()) {
            return object;
        } else {
            throw new ObjectValidationException("Validation failed", feedback);
        }
    }


}
