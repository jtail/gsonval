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
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
public class ValidatingAdapterFactory implements TypeAdapterFactory {
    private final Validator validator;
    private final Function<Iterable<? extends ConstraintViolation<?>>, JsonObject> converter;

    public ValidatingAdapterFactory() {
        this(Validation.buildDefaultValidatorFactory().getValidator());
    }

    public ValidatingAdapterFactory(Validator validator) {
        this.validator = validator;
        this.converter = new ViolationConverter();
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {
        Consumer<T> validate = validator != null ? this::validate : t -> {};
        return new ValidatingAdapter<>(gson.getDelegateAdapter(this, type), validate, type);
    }

    private <T> T validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (violations.isEmpty()) {
            return object;
        } else {
            JsonObject feedback = converter.apply(violations);
            throw new ObjectValidationException("Constraints failed: " + violations.size(), feedback);
        }
    }


}
