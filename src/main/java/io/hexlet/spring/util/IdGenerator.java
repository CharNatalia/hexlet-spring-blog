package io.hexlet.spring.util;

public class IdGenerator {
    private long nextId = 1;

    public long generateId() {
        return nextId++;
    }
}
