package com.fintect.Banking_Engine.core.dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Pure Java Data Structure using a ring buffer to store and poll real-time alerts without memory leaks.
 */
public class CircularBufferEngine<T> {

    private final Object[] buffer;
    private int head = 0;
    private int tail = 0;
    private int size = 0;
    private final int capacity;

    public CircularBufferEngine(int capacity) {
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public synchronized void push(T item) {
        buffer[tail] = item;
        tail = (tail + 1) % capacity;
        if (size < capacity) {
            size++;
        } else {
            head = (head + 1) % capacity; // Overwrite oldest
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized List<T> pollAll() {
        List<T> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            items.add((T) buffer[(head + i) % capacity]);
        }
        return items;
    }
}