package perf;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayDeque;
import java.util.LinkedList;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
//@Threads(value = 4)
@Warmup(iterations = 0)//3
@Measurement(iterations = 2)//8

public class LinkedListPerfFirsts {
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
    public void benchmark_LinkedList(Blackhole bh){

        final Integer val = 1;

        //start measuring time here<br/>
        for ( int i = 0; i < LOOP_COUNT; ++i )
        {
            for ( int j = 0; j < 5; ++j )
                lst.addFirst( val );
            for ( int j = 0; j < 5; ++j )
                lst.removeFirst();

        }
        bh.consume(lst.size());
    }

    @Benchmark
    public void benchmark_ArrayDeque(Blackhole bh){

        final Integer val = 1;

        //start measuring time here<br/>
        for ( int i = 0; i < LOOP_COUNT; ++i )
        {
            for ( int j = 0; j < 5; ++j )
                ast.addFirst( val );
            for ( int j = 0; j < 5; ++j )
                ast.removeFirst();
        }
        bh.consume(lst.size());
    }

/*    public static void main(String[] args) throws RunnerException {

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

    }*/

}
