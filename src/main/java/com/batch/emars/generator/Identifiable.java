package com.batch.emars.generator;

import java.io.Serializable;

public interface Identifiable<T extends Serializable> {
    T getId();
}
