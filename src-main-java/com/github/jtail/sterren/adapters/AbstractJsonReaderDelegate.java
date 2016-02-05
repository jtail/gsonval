package com.github.jtail.sterren.adapters;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.Reader;

/**
 * Encapsulates boilerplate code to delegate a json reader.
 */
public abstract class AbstractJsonReaderDelegate extends JsonReader {
    private static final Reader UNREADABLE_READER = new Reader() {
        @Override public int read(char[] buffer, int offset, int count) throws IOException {
            throw new AssertionError();
        }
        @Override public void close() throws IOException {
            throw new AssertionError();
        }
    };
    

    private JsonReader delegate;

    public AbstractJsonReaderDelegate(JsonReader delegate) {
        super(UNREADABLE_READER);
        this.delegate = delegate;
    }

    @Override
    public void beginArray() throws IOException {
        delegate.beginArray();
    }

    @Override
    public void beginObject() throws IOException {
        delegate.beginObject();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    @Override
    public void endArray() throws IOException {
        delegate.endArray();
    }

    @Override
    public void endObject() throws IOException {
        delegate.endObject();
    }

    @Override
    public String getPath() {
        return delegate.getPath();
    }

    @Override
    public boolean hasNext() throws IOException {
        return delegate.hasNext();
    }

    @Override
    public boolean nextBoolean() throws IOException {
        return delegate.nextBoolean();
    }

    @Override
    public double nextDouble() throws IOException {
        return delegate.nextDouble();
    }

    @Override
    public int nextInt() throws IOException {
        return delegate.nextInt();
    }

    @Override
    public long nextLong() throws IOException {
        return delegate.nextLong();
    }

    @Override
    public String nextName() throws IOException {
        //        crumb = name;
        return delegate.nextName();
    }

    @Override
    public void nextNull() throws IOException {
        delegate.nextNull();
    }

    @Override
    public String nextString() throws IOException {
        return delegate.nextString();
    }

    @Override
    public JsonToken peek() throws IOException {
        return delegate.peek();
    }

    @Override
    public void skipValue() throws IOException {
        delegate.skipValue();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
