package com.google.gson;

public abstract class PackageHack extends JsonElement {
    public static <T extends JsonElement> T deepCopy(T object) {
        return (T) object.deepCopy();
    }
}
