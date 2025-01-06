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
package kala.collection.base;

import kala.annotations.Covariant;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractIterator<@Covariant E> implements Iterator<E> {

    /// @throws NoSuchElementException when `hasNext()` returns `false`.
    protected void checkStatus() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException("The iterator has no more elements");
        }
    }

    @Override
    public String toString() {
        if (!hasNext()) {
            return super.toString() + "[]";
        } else {
            return super.toString() + "[<not computed>]";
        }
    }
}
