package com.github.jtail.sterren.jersey;

import com.github.jtail.sterren.ObjectValidationException;
import com.github.jtail.sterren.ValidatingAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;


@Slf4j
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonMessageBodyHandler implements MessageBodyWriter<Object>, MessageBodyReader<Object> {
    private final static Gson plain = new GsonBuilder().registerTypeAdapterFactory(new ValidatingAdapterFactory(null)).create();
    private final static Gson validating = new GsonBuilder().registerTypeAdapterFactory(new ValidatingAdapterFactory()).create();

    @Override
    public long getSize(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(
            Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream
    ) throws IOException, WebApplicationException {
        try (InputStreamReader reader = new InputStreamReader(entityStream, UTF_8)) {
            Gson parser = Stream.of(annotations).anyMatch(a -> a instanceof Valid) ? validating : plain;
            try {
                return parser.fromJson(reader, calculateType(type, genericType));
            } catch (ObjectValidationException e) {
                log.debug("Validation error: [{}]", e.getMessage());
                throw new WebApplicationException(buildErrorResponse(e.getFeedback()));
            }
        }
    }

    private Response buildErrorResponse(JsonElement feedback) {
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(feedback).build();
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(
            Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream
    ) throws IOException, WebApplicationException {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8)) {
            plain.toJson(object, calculateType(type, genericType), writer);
        }
    }

    private Type calculateType(Class<?> type, Type genericType) {
        return type.equals(genericType) ? type : genericType;
    }


}