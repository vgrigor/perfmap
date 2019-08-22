package perf;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

@Fork(1)
@Warmup(iterations = 0)//10
@Measurement(iterations = 1)//10
public class MyBenchmark {

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"1000000", "10000000", "100000000"})
        public int listSize;

        public List<Integer> testList;

        @Setup(Level.Trial)
        public void setUp() {
            //System.out.println("SETUP");
            testList = new Random()
                    .ints()
                    .limit(listSize)
                    .boxed()
                    .collect(Collectors.toList());
        }
    }

    public static class VolatileLong {
        private volatile long value = 0;

        public synchronized void add(long amount) {
            this.value += amount;
        }

        public long getValue() {
            return this.value;
        }
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void longAdder(Blackhole blackhole, BenchmarkState state) {
        LongAdder adder = new LongAdder();
        state.testList.parallelStream().forEach(adder::add);
        blackhole.consume(adder.sum());
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void atomicLong(Blackhole blackhole, BenchmarkState state) {
        AtomicLong atomicLong = new AtomicLong();
        state.testList.parallelStream().forEach(atomicLong::addAndGet);
        blackhole.consume(atomicLong.get());
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void volatileLong(Blackhole blackHole, BenchmarkState state) {
        VolatileLong volatileLong = new VolatileLong();
        state.testList.parallelStream().forEach(volatileLong::add);
        blackHole.consume(volatileLong.getValue());
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void longStreamSum(Blackhole blackHole, BenchmarkState state) {
        long sum = state.testList.parallelStream().mapToLong(s -> s).sum();
        blackHole.consume(sum);
    }

}