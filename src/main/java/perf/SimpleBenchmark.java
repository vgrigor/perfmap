package perf;

import maps.FastCache;
import maps.FastCache2;
import maps.FastCache_CHM;
import maps.FastObjIntMap;
import maps.SynchronizedCache;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.*;

import static org.openjdk.jmh.annotations.Scope.Benchmark;

/**
 * Created by Grigoryev-VN
 */

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
//@OutputTimeUnit(TimeUnit.SECONDS)
public class SimpleBenchmark {

    static SynchronizedCache<Integer, Integer> syncCache = new SynchronizedCache<>();
    static FastCache<Integer, Integer> cache2 = new FastCache<>();
    static FastCache2<Integer, Integer> cache3 = new FastCache2<>();
    static FastObjIntMap<Integer> cacheF = new FastObjIntMap<>();
    static FastCache_CHM<Integer,Integer> cache_CHM = new FastCache_CHM<>();

    static int NUM_READS=100;
    static int CACHE_SIZE=1000;
    @Setup
    public static void setup() {
        for (int i = 0; i <= CACHE_SIZE; i++) {
            syncCache.put(i, i);
            cache2.put(i, i);
            cache3.put(i, i);
            cacheF.put(i, i);
        }
    }

    //@Benchmark
    public void testReadWriteLockGet(Blackhole bh) {
        int toGet = ThreadLocalRandom.current().nextInt(100);
        int toPut = ThreadLocalRandom.current().nextInt(50000);
        //cache2.put(toPut, toPut);
        for(int j=0; j < NUM_READS ;j++) {
             Integer integer = cache2.get(toGet, 5_000);
            bh.consume(integer);
        }

    }

    //@Benchmark
    public void testReadWriteLockGet_2(Blackhole bh) {

            int toGet = 1;
            int toPut = 1;
            //cache2.put(toPut, toPut);
            for(int j=0; j < NUM_READS ;j++) {
                Integer integer = cache2.get(toGet, 5_000);
                bh.consume(integer);
            }
    }

    @Benchmark
    public void testReadWriteLockGet_3(Blackhole bh) {

        int toGet = 1;
        int toPut = 1;
        //cache3.put(toPut, toPut);
        for(int j=0; j < NUM_READS ;j++) {
            Integer integer = cache3.get(toGet, 5_000);
            bh.consume(integer);
        }
    }

    //Небезопасная карта
    @Benchmark
    public void testReadWriteLockGet_4(Blackhole bh) {

        int toGet = 1;
        int toPut = 1;
        //cache3.put(toPut, toPut);
        for(int j=0; j < NUM_READS ;j++) {
            Integer integer = cacheF.get(toGet, 5_000);
            bh.consume(integer);
        }
    }


    @Benchmark
    public void testReadWriteLockGet_CHM(Blackhole bh) {

        int toGet = 1;
        int toPut = 1;
        //cache3.put(toPut, toPut);
        for(int j=0; j < NUM_READS ;j++) {
            Integer integer = cache_CHM.get(toGet, 5_000);
            bh.consume(integer);
        }
    }

    @Benchmark
    public void testSynchMapGet(Blackhole bh) {
        int toGet = ThreadLocalRandom.current().nextInt(100);
        int toPut = ThreadLocalRandom.current().nextInt(50000);
        //cache1.put(toPut, toPut);
        for(int j=0; j < NUM_READS ;j++) {
            Integer integer = syncCache.get(toGet, 5_000);
            bh.consume(integer);
        }

    }

    public static void main(String[] args) throws RunnerException {

        Options options = new OptionsBuilder()
                .include(SimpleBenchmark.class.getSimpleName())
                .forks(1)
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


