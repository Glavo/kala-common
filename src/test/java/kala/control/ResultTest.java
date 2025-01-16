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
package kala.control;

import kala.SerializationUtils;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public final class ResultTest {

    private static final Result<String, ?> ok = Result.ok("value");
    private static final Result<?, Integer> err = Result.err(10);

    @Test
    public void factoryTest() {
        assertTrue(ok.isDefined());
        assertFalse(err.isDefined());
        assertFalse(ok.isErr());
        assertTrue(err.isErr());
    }

    @Test
    public void getTest() {
        assertEquals("value", ok.get());
        assertThrows(NoSuchElementException.class, ok::getErr);
        assertThrows(NoSuchElementException.class, err::get);
        assertEquals(10, err.getErr());
    }

    @Test
    public void mapTest() {
        assertEquals(Result.ok(5), ok.map(String::length));
        assertSame(ok, ok.mapErr(it -> null));

        assertSame(err, err.map(it -> null));
        assertEquals(Result.err("10"), err.mapErr(String::valueOf));
    }

    @Test
    public void flatMapTest() {
        assertEquals(Result.ok(5), ok.flatMap(it -> Result.ok(it.length())));
        // assertEquals(Result.err(5), ok.flatMap(it -> (Result<String, Integer>) Result.err(it.length())));
    }

    @Test
    public void toEitherTest() {
        assertEquals(Either.right("value"), ok.toEither());
        assertEquals(Either.left(10), err.toEither());
    }

    @Test
    public void serializationTest() throws IOException, ClassNotFoundException {
        assertEquals(ok, SerializationUtils.writeAndRead(ok));
        assertEquals(err, SerializationUtils.writeAndRead(err));
    }
}
