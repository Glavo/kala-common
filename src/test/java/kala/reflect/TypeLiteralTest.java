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
package kala.reflect;

import kala.control.Option;
import org.junit.jupiter.api.*;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class TypeLiteralTest {
    @Test
    public void test() {
        assertThrows(IllegalStateException.class, () -> {
            class C extends TypeLiteral<String> {

            }

            new C() {
            };
        });

        assertEquals(String.class, TypeLiteral.of(String.class).getType());
        assertEquals(TypeLiteral.of(String.class), new TypeLiteral<String>() {
        });

        var tpe = (ParameterizedType) new TypeLiteral<Option<String>>() {
        }.getType();

        assertEquals(Option.class, tpe.getRawType());
        assertIterableEquals(List.of(String.class), Arrays.asList(tpe.getActualTypeArguments()));
    }
}
