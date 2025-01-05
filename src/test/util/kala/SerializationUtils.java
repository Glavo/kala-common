/*
 * Copyright 2025 Glavo
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
package kala;

import java.io.*;

public final class SerializationUtils {
    private SerializationUtils() {
    }


    public static byte[] writeObject(Object obj) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (final ObjectOutputStream out = new ObjectOutputStream(stream)) {
            out.writeObject(obj);
        }
        return stream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObject(byte[] arr) throws IOException, ClassNotFoundException {
        try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(arr))) {
            return (T) stream.readObject();
        }
    }

    public static <T> T writeAndRead(T obj) throws IOException, ClassNotFoundException {
        return readObject(writeObject(obj));
    }
}
