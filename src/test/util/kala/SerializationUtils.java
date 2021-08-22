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
