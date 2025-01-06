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

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.*;

public final class TryTest {

    @Test
    void throwExceptionTest() {
        MyException ex = new MyException();

        assertThrows(MyException.class, () -> Try.throwException(ex));
        assertThrows(MyException.class, () -> Try.sneakyThrow(ex));
    }

    @Test
    void maybeThrowsTest() {
        try {
            Try.<IOException>maybeThrows();
        } catch (IOException ignored) {
            // empty
        }
    }

    @Test
    void ofTest() {
        assertEquals(Try.success("foo"), Try.of(() -> "foo"));
        assertEquals(Try.success("foo"), Try.ofCallable(() -> "foo"));
        assertEquals(Try.success(null), Try.runCatching(() -> {
        }));
        assertEquals(Try.success(null), Try.runRunnable(() -> {
        }));

        MyException ex = new MyException();
        assertEquals(Try.failure(ex), Try.of(() -> Try.throwException(ex)));
        assertEquals(Try.failure(ex), Try.ofCallable(() -> Try.throwException(ex)));
        assertEquals(Try.failure(ex), Try.runCatching(() -> Try.throwException(ex)));
        assertEquals(Try.failure(ex), Try.runRunnable(() -> Try.throwException(ex)));

        assertTrue(Try.success("foo").isSuccess());
        assertTrue(Try.failure(ex).isFailure());
    }

    @Test
    void runTest() {
        assertDoesNotThrow(() -> Try.runIgnoreException(() -> {
            throw new MyException();
        }));
        assertThrows(MyException.class, () -> Try.runUnchecked(() -> Try.throwException(new MyException())));

    }

    @Test
    void usingTest() {
        class CloseableImpl implements AutoCloseable {
            final LongAdder adder;

            CloseableImpl(LongAdder adder) {
                this.adder = adder;
            }

            @Override
            public void close() {
                adder.increment();
            }
        }

        final LongAdder adder0 = new LongAdder();
        final LongAdder adder1 = new LongAdder();
        final LongAdder adder2 = new LongAdder();

        final MyException ex = new MyException();

        assertEquals(
                Try.success("foo"),
                Try.using(new CloseableImpl(adder0), r0 -> {
                    assertEquals(0, adder0.sum());

                    assertSame(adder0, r0.adder);

                    return "foo";
                })
        );
        assertEquals(1, adder0.sumThenReset());

        assertEquals(
                Try.failure(ex),
                Try.using(new CloseableImpl(adder0), r0 -> {
                    assertEquals(0, adder0.sum());

                    assertSame(adder0, r0.adder);

                    throw ex;
                })
        );
        assertEquals(1, adder0.sumThenReset());

        assertEquals(
                Try.success("foo"),
                Try.using(new CloseableImpl(adder0), new CloseableImpl(adder1), (r0, r1) -> {
                    assertEquals(0, adder0.sum());
                    assertEquals(0, adder1.sum());

                    assertSame(adder0, r0.adder);
                    assertSame(adder1, r1.adder);

                    return "foo";
                })
        );
        assertEquals(1, adder0.sumThenReset());
        assertEquals(1, adder1.sumThenReset());

        assertEquals(
                Try.failure(ex),
                Try.using(new CloseableImpl(adder0), new CloseableImpl(adder1), (r0, r1) -> {
                    assertEquals(0, adder0.sum());
                    assertEquals(0, adder1.sum());

                    assertSame(adder0, r0.adder);
                    assertSame(adder1, r1.adder);

                    throw ex;
                })
        );
        assertEquals(1, adder0.sumThenReset());
        assertEquals(1, adder1.sumThenReset());

        assertEquals(
                Try.success("foo"),
                Try.using(new CloseableImpl(adder0), new CloseableImpl(adder1), new CloseableImpl(adder2), (r0, r1, r2) -> {
                    assertEquals(0, adder0.sum());
                    assertEquals(0, adder1.sum());
                    assertEquals(0, adder2.sum());

                    assertSame(adder0, r0.adder);
                    assertSame(adder1, r1.adder);
                    assertSame(adder2, r2.adder);

                    return "foo";
                })
        );
        assertEquals(1, adder0.sumThenReset());
        assertEquals(1, adder1.sumThenReset());
        assertEquals(1, adder2.sumThenReset());

        assertEquals(
                Try.failure(ex),
                Try.using(new CloseableImpl(adder0), new CloseableImpl(adder1), new CloseableImpl(adder2), (r0, r1, r2) -> {
                    assertEquals(0, adder0.sum());
                    assertEquals(0, adder1.sum());
                    assertEquals(0, adder2.sum());

                    assertSame(adder0, r0.adder);
                    assertSame(adder1, r1.adder);
                    assertSame(adder2, r2.adder);

                    throw ex;
                })
        );
        assertEquals(1, adder0.sumThenReset());
        assertEquals(1, adder1.sumThenReset());
        assertEquals(1, adder2.sumThenReset());
    }

    @Test
    void getTest() {
        MyException ex = new MyException();
        Exception defaultValue = new Exception();

        Try<String> success = Try.success("foo");
        Try<String> failure = Try.failure(ex);


        assertEquals("foo", success.get());
        assertEquals("foo", success.getOrNull());
        assertEquals("foo", success.getOrDefault("bar"));
        assertEquals("foo", success.getOrElse(() -> "bar"));
        assertEquals("foo", success.getOrThrow());
        assertEquals("foo", success.getOrThrow(IllegalArgumentException::new));
        assertEquals("foo", success.getOrThrowException(new IllegalArgumentException()));
        assertEquals(Option.some("foo"), success.getOption());

        assertThrows(UnsupportedOperationException.class, success::getCause);
        assertNull(success.getCauseOrNull());
        assertEquals(Option.none(), success.getCauseOption());
        assertSame(defaultValue, success.getCauseOrDefault(defaultValue));
        assertSame(defaultValue, success.getCauseOrElse(() -> defaultValue));


        assertThrows(NoSuchElementException.class, failure::get);
        assertNull(failure.getOrNull());
        assertEquals("bar", failure.getOrDefault("bar"));
        assertEquals("bar", failure.getOrElse(() -> "bar"));
        assertThrows(MyException.class, failure::getOrThrow);
        assertThrows(IllegalArgumentException.class, () -> failure.getOrThrow(IllegalArgumentException::new));
        assertThrows(IllegalArgumentException.class, () -> failure.getOrThrowException(new IllegalArgumentException()));
        assertEquals(Option.none(), failure.getOption());

        assertSame(ex, failure.getCause());
        assertSame(ex, failure.getCauseOrNull());
        assertEquals(Option.some(ex), failure.getCauseOption());
        assertSame(ex, failure.getCauseOrDefault(defaultValue));
        assertSame(ex, failure.getCauseOrElse(() -> defaultValue));
    }

    @Test
    void recoverTest() {
        MyException ex0 = new MyException();
        IllegalArgumentException ex1 = new IllegalArgumentException();

        Try<String> success = Try.success("foo");
        Try<String> failure = Try.failure(ex0);

        Try<String> recoverValue = Try.success("bar");

        assertSame(success, success.recover(e -> null));
        assertEquals(Try.success("bar"), failure.recover(e -> "bar"));
        assertEquals(Try.failure(ex1), failure.recover(e -> Try.throwException(ex1)));

        assertSame(success, success.recoverWith(e -> null));
        assertSame(recoverValue, failure.recoverWith(e -> recoverValue));
        assertEquals(Try.failure(ex1), failure.recoverWith(e -> Try.throwException(ex1)));

        assertSame(success, success.recover(MyException.class, e -> null));
        assertEquals(Try.success("bar"), failure.recover(MyException.class, e -> "bar"));
        assertSame(failure, failure.recover(IllegalArgumentException.class, e -> "bar"));
        assertEquals(Try.failure(ex1), failure.recover(MyException.class, e -> Try.throwException(ex1)));
        assertSame(failure, failure.recover(IllegalArgumentException.class, e -> Try.throwException(ex1)));

        assertSame(success, success.recoverWith(MyException.class, e -> null));
        assertSame(recoverValue, failure.recoverWith(MyException.class, e -> recoverValue));
        assertSame(failure, failure.recoverWith(IllegalArgumentException.class, e -> recoverValue));
        assertEquals(Try.failure(ex1), failure.recoverWith(MyException.class, e -> Try.throwException(ex1)));
        assertSame(failure, failure.recoverWith(IllegalArgumentException.class, e -> Try.throwException(ex1)));
    }

    @Test
    void rethrowTest() {
        MyException ex = new MyException();

        Try<String> success = Try.success("foo");
        Try<String> failure = Try.failure(ex);

        assertSame(success, success.rethrow());
        assertSame(success, success.rethrow(MyException.class));
        assertSame(success, success.rethrow(IllegalArgumentException.class));
        assertSame(success, success.rethrowFatal());

        assertThrows(MyException.class, failure::rethrow);
        assertThrows(MyException.class, () -> failure.rethrow(MyException.class));
        assertDoesNotThrow(() -> failure.rethrow(IllegalArgumentException.class));

        assertDoesNotThrow(failure::rethrowFatal);
        assertThrows(OutOfMemoryError.class, () -> Try.failure(new OutOfMemoryError()).rethrowFatal());
    }

    @Test
    void toEitherTest() {
        MyException ex = new MyException();

        assertEquals(Either.right("foo"), Try.success("foo").toEither());
        assertEquals(Either.left(ex), Try.failure(ex).toEither());
    }

    @Test
    void toResultTest() {
        MyException ex = new MyException();

        assertEquals(Result.ok("foo"), Try.success("foo").toResult());
        assertEquals(Result.err(ex), Try.failure(ex).toResult());
    }

    @Test
    void mapTest() {
        MyException ex = new MyException();

        assertEquals(Try.success(3), Try.success("foo").map(String::length));
        assertEquals(Try.failure(ex), Try.failure(ex).map(arg -> Try.throwException(ex)));
        assertEquals(Try.failure(ex), Try.success("foo").map(str -> Try.throwException(ex)));
    }

    @Test
    void flatMapTest() {
        MyException ex = new MyException();

        Try<String> success = Try.success("foo");
        Try<String> failure = Try.failure(ex);

        Try<String> resultValue = Try.success("bar");

        assertEquals(Try.success(3), success.flatMap(it -> Try.success(it.length())));
        assertSame(failure, success.flatMap(it -> failure));
        assertEquals(failure, success.flatMap(it -> Try.throwException(ex)));

        //noinspection AssertBetweenInconvertibleTypes
        assertSame(failure, failure.flatMap(it -> Try.success(it.length())));
        assertSame(failure, failure.flatMap(it -> failure));
        assertSame(failure, failure.flatMap(it -> Try.throwException(ex)));
    }

    @Test
    void iteratorTest() {
        assertIterableEquals(List.of("foo"), Try.success("foo"));
        assertIterableEquals(List.of(), Try.failure(new MyException()));
    }

    @Test
    void forEachTest() {
        MyException ex = new MyException();

        Try<String> success = Try.success("foo");
        Try<String> failure = Try.failure(ex);

        LongAdder adder = new LongAdder();

        failure.forEach(value -> fail());
        assertEquals(0, adder.sumThenReset());

        success.forEach(value -> {
            assertEquals("foo", value);
            adder.increment();
        });
        assertEquals(1, adder.sumThenReset());
    }

    @Test
    void serializationTest() throws IOException, ClassNotFoundException {
        assertEquals(Try.success("foo"), SerializationUtils.writeAndRead(Try.success("foo")));
        assertInstanceOf(MyException.class, SerializationUtils.writeAndRead(Try.failure(new MyException())).getCause());
    }

    private static final class MyException extends RuntimeException {
    }
}
