package kala.reflect;

import kala.SerializationUtils;
import kala.control.Option;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TypeLiteralTest {
    @Test
    public void test() {
        assertThrows(IllegalArgumentException.class, () -> {
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
