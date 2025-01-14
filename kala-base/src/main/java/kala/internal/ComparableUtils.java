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
package kala.internal;

import kala.annotations.StaticClass;

import java.util.Comparator;

@StaticClass
@SuppressWarnings({"rawtypes", "unchecked"})
public class ComparableUtils {
    private ComparableUtils() {
    }

    public static <E> Comparator<E> naturalOrder() {
        return (Comparator<E>) Comparator.naturalOrder();
    }

    public static int compare(Object o1, Object o2) {
        return ((Comparable<Object>) o1).compareTo(o2);
    }

    public static int compare(Object o1, Object o2, Comparator comparator) {
        if (comparator == null) {
            return compare(o1, o2);
        } else {
            return comparator.compare(o1, o2);
        }
    }

    public static boolean comparatorEquals(Comparator comparator1, Comparator comparator2) {
        if (comparator1 == null) {
            return comparator2 == null || comparator2.equals(Comparator.naturalOrder());
        }
        //noinspection ReplaceNullCheck
        if (comparator2 == null) {
            return comparator1.equals(Comparator.naturalOrder());
        }
        return comparator1.equals(comparator2);
    }
}
