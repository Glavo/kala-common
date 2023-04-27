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
