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
package kala.benchmark;

import kala.text.StringFormat;
import org.openjdk.jmh.annotations.*;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 5, time = 3)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringFormatBenchmark {

    @Benchmark
    public String printfFormat() {
        return String.format("%s is %s years old", "Glavo", "5");
    }

    @Benchmark
    public String messageFormat() {
        return MessageFormat.format("{0} is {1} years old", "Glavo", "5");
    }

    private static final MessageFormat compiledMessageFormat = new MessageFormat("{0} is {1} years old");

    @Benchmark
    public String compiledMessageFormat() {
        return compiledMessageFormat.format(new Object[]{"Glavo", "5"});
    }

    @Benchmark
    public String stringFormat1() {
        return StringFormat.format("{} is {} years old", "Glavo", "5");
    }

    @Benchmark
    public String stringFormat2() {
        return StringFormat.format("{0} is {1} years old", "Glavo", "5");
    }
}
