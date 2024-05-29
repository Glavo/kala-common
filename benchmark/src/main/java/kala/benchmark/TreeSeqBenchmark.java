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

import kala.collection.immutable.ImmutableTreeSeq;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 3)
@Measurement(iterations = 5, time = 3)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class TreeSeqBenchmark {

    @Param({"1", "10", "100", "1000", "10000", "100000", "1000000"})
    private int length;

    private ImmutableTreeSeq<Integer> seq;

    @Setup
    public void setup() {
        Random random = new Random(0);
        seq = ImmutableTreeSeq.fill(length, () -> random.nextInt());
    }

    @Benchmark
    public void get(Blackhole bh) {
        for (int i = 0; i < length; i++) {
            bh.consume(seq.get(i));
        }
    }
}
