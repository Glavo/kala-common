package org.glavo.kala.control;

import org.glavo.kala.annotations.Covariant;

import java.io.Serializable;

public abstract class OptionAny<@Covariant T> implements Serializable {
    OptionAny() {
    }

}
