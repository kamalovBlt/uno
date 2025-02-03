package ru.itis.unogame.util;

public interface LoopedList<T> {

    int size();
    T getCurrent();
    void next();
    void reverse();
    void add(T value);
    boolean isReversed();
}
