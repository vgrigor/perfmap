package perf;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 3, jvmArgs = {"-Xms2G", "-Xmx2G"})
//@Threads(value = 4)
@Warmup(iterations = 0)//3
@Measurement(iterations = 8)//8

public class LinkedListPerf {
    final static int PREFILL_COUNT = 100_000;
    final static int LOOP_COUNT = 100_000_000;
    final static LinkedList<Integer> lst = new LinkedList<Integer>();
    final static ArrayDeque<Integer> ast = new ArrayDeque<Integer>();


    @Setup
    public static void setup() {

        for ( int i = 0; i < PREFILL_COUNT; ++i )
            lst.add( 35 );

        for ( int i = 0; i < PREFILL_COUNT; ++i )
            ast.add( 35 );

    }

    @Benchmark
    public void benchmark_LinkedList(){

        final Integer val = 1;

        //start measuring time here<br/>
        for ( int i = 0; i < LOOP_COUNT; ++i )
        {
            for ( int j = 0; j < 5; ++j )
                lst.addFirst( val );
            for ( int j = 0; j < 5; ++j )
                lst.removeLast();
        }

    }

    @Benchmark
    public void benchmark_ArrayDeque(){

        final Integer val = 1;

        //start measuring time here<br/>
        for ( int i = 0; i < LOOP_COUNT; ++i )
        {
            for ( int j = 0; j < 5; ++j )
                ast.addFirst( val );
            for ( int j = 0; j < 5; ++j )
                ast.removeLast();
        }

    }

    public static void main(String[] args) throws RunnerException {

        Options options = new OptionsBuilder()
            .include(SimpleBenchmark.class.getSimpleName())
            .forks(2)
            .threads(4)//40
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .jvmArgs("-server")
            .warmupIterations(0)//2
            .measurementIterations(1)//3
            .build();

        new Runner(options).run();

    }

}
