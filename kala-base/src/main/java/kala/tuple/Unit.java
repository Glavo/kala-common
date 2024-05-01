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
package kala.tuple;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

/**
 * A tuple of no elements.
 *
 * @author Glavo
 */
public final class Unit implements EmptyTuple {
    @Serial
    private static final long serialVersionUID = 0L;

    public static final int HASH_CODE = 427632945;

    public static final Unit INSTANCE = new Unit();

    public static @NotNull Unit unit() {
        return Unit.INSTANCE;
    }

    private Unit() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HASH_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "()";
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
