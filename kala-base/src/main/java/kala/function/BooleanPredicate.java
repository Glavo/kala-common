/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package kala.function;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

@FunctionalInterface
public interface BooleanPredicate {

    BooleanPredicate IS_TRUE = new BooleanPredicates.IsTrue();

    BooleanPredicate IS_FALSE = new BooleanPredicates.IsFalse();

    boolean test(boolean v);

    default @NotNull BooleanPredicate and(BooleanPredicate other) {
        Objects.requireNonNull(other);
        return (BooleanPredicate & Serializable) b -> test(b) && other.test(b);
    }

    default @NotNull BooleanPredicate negate() {
        return (BooleanPredicate & Serializable) b -> !test(b);
    }

    default @NotNull BooleanPredicate or(@NotNull BooleanPredicate other) {
        Objects.requireNonNull(other);
        return (BooleanPredicate & Serializable) b -> test(b) || other.test(b);
    }

    static @NotNull BooleanPredicate isEqual(boolean target) {
        return target ? IS_TRUE : IS_FALSE;
    }

    static @NotNull BooleanPredicate not(@NotNull BooleanPredicate target) {
        return target.negate();
    }
}
