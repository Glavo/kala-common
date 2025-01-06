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

import kala.annotations.Covariant;
import org.jetbrains.annotations.NotNull;

/**
 * The base class of heterogeneous lists.
 * When the number of elements is 9 or less, use TupleN, otherwise use TupleXXL to store values in an array.
 * Elements are compactly stored in any case.
 *
 * <p>{@code TupleN<T1, T2, ..., Tn>} is equivalent to
 * {@code TList<T1, ? extends TList<T2, ? extends TList<..., , ? extends TList<Tn, Tuple0>>>>}.
 * The latter type notation can type-safely reference a tuple of unlimited length.
 *
 * @param <H> the type of the head element
 * @param <T>
 * @author Glavo
 */
public sealed interface HList<@Covariant H, @Covariant T extends Tuple> extends NonEmptyTuple
        permits Tuple1, Tuple2, Tuple3, Tuple4, Tuple5, Tuple6, Tuple7, Tuple8, Tuple9, TupleXXL {
    /**
     * Returns the head of this heterogeneous list.
     *
     * @return the head of this heterogeneous list
     */
    H head();

    /**
     * Returns the tail of this heterogeneous list.
     *
     * @return the tail of this heterogeneous list
     */
    @NotNull
    T tail();

    @Override
    <HH> @NotNull HList<HH, ? extends HList<H, T>> cons(HH head);
}
