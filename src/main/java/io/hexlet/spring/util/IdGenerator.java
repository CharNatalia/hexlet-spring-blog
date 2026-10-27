package io.hexlet.spring.util;

@Deprecated
public class IdGenerator {
    private long nextId = 1;

    public long generateId() {
        return nextId++;
    }
}
