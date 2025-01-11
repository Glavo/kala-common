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
package kala.collection;

import kala.collection.factory.CollectionFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("unchecked")
public interface CollectionTestTemplate extends CollectionLikeTestTemplate {

    <E> CollectionFactory<E, ?, ? extends Collection<? extends E>> factory();

    @Override
    default <E> Collection<E> of(E... elements) {
        return (Collection<E>) factory().from(elements);
    }

    @Override
    default <E> Collection<E> from(E[] elements) {
        return (Collection<E>) factory().from(elements);
    }

    @Override
    default <E> Collection<E> from(Iterable<? extends E> elements) {
        return (Collection<E>) factory().from(elements);
    }

    default Class<?> collectionType() {
        final String testClassName = this.getClass().getName();
        assertTrue(testClassName.endsWith("Test"));

        final String className = testClassName.substring(0, testClassName.length() - 4);

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail(e);
            return null;
        }
    }
}
