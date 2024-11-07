/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.control.primitive;

import kala.annotations.ReplaceWith;
import kala.control.Option;
import kala.collection.base.primitive.BooleanIterator;
import kala.collection.base.primitive.BooleanTraversable;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.NoSuchElementException;

public final class BooleanOption implements PrimitiveOption<Boolean>, BooleanTraversable {
    @Serial
    private static final long serialVersionUID = 3226319253753655469L;
    private static final int HASH_MAGIC = 773806848;

    public static final BooleanOption True = new BooleanOption(true);
    public static final BooleanOption False = new BooleanOption(false);
    public static final BooleanOption None = new BooleanOption();

    private final boolean isDefined;
    private final boolean value;
    private final transient int hashCode;
    private final transient String name;

    private BooleanOption() {
        this.isDefined = false;
        this.value = false;
        this.name = "OptionBoolean.None";
        this.hashCode = NONE_HASH;
    }

    private BooleanOption(boolean value) {
        this.isDefined = true;
        this.value = value;
        this.name = value ? "OptionBoolean.True" : "OptionBoolean.False";
        this.hashCode = HASH_MAGIC + Boolean.hashCode(value);
    }

    public static @NotNull BooleanOption some(boolean value) {
        return value ? True : False;
    }

    public static @NotNull BooleanOption none() {
        return None;
    }

    @Deprecated
    @ReplaceWith("some(boolean)")
    public static @NotNull BooleanOption of(boolean value) {
        return value ? True : False;
    }

    @Deprecated
    @ReplaceWith("ofNullable(Boolean)")
    public static @NotNull BooleanOption of(@Nullable Boolean value) {
        return ofNullable(value);
    }

    public static @NotNull BooleanOption ofNullable(@Nullable Boolean value) {
        if (value == null) {
            return None;
        }
        return value ? True : False;
    }

    public static @NotNull BooleanOption fromOption(@NotNull Option<? extends Boolean> option) {
        if (option.isDefined()) {
            return some(option.get());
        } else {
            return None;
        }
    }

    public boolean isDefined() {
        return isDefined;
    }

    public boolean isEmpty() {
        return !isDefined;
    }

    /**
     * Returns the {@code Option}'s value.
     *
     * @return the {@code Option}'s value
     * @throws NoSuchElementException if the {@code Option} is empty
     */
    @Flow(sourceIsContainer = true)
    public boolean get() {
        if (isDefined) {
            return value;
        } else {
            throw new NoSuchElementException("OptionBoolean.None.get()");
        }
    }

    public @Nullable Boolean getOrNull() {
        return isDefined ? value : null;
    }

    public @NotNull BooleanOption getOption() {
        return this;
    }

    @Override
    public @NotNull BooleanIterator iterator() {
        return isDefined ? BooleanIterator.of(value) : BooleanIterator.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Option<?> other) {
            if (this.isEmpty()) return other.isEmpty();
            if (other.isEmpty()) return false;

            Object v = other.get();
            return v instanceof Boolean && get() == (Boolean) v;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return name;
    }

    @Serial
    private Object readResolve() {
        if (isDefined) {
            return value ? True : False;
        } else {
            return None;
        }
    }
}

