package org.glavo.kala.control;

import org.glavo.kala.annotations.Covariant;

import java.io.Serializable;

public abstract class AnyOption<@Covariant T> implements Serializable {
    protected AnyOption() {
    }
}
