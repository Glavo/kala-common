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

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public final class BooleanOptionTest {
    @Test
    public void serializationTest() {
        assertAll(Stream.of(BooleanOption.True, BooleanOption.False, BooleanOption.None).map(opt -> () -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream(512);
            new ObjectOutputStream(out).writeObject(opt);
            byte[] buffer = out.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(buffer);
            Object obj = new ObjectInputStream(in).readObject();
            assertSame(opt, obj);
        }));
    }
}
