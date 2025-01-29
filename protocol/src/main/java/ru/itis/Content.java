package ru.itis;

import java.io.Serializable;

public interface Content extends Serializable {
    byte[] toByteArray();
}