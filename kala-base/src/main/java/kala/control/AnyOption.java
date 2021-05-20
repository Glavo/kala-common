package kala.control;

import kala.annotations.Covariant;

import java.io.Serializable;

public abstract class AnyOption<@Covariant T> implements Serializable {
    protected AnyOption() {
    }
}
