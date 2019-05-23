package jol;

import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.IOException;
import java.util.HashMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
//@Threads(value = 4)
@Warmup(iterations = 0)//3
@Measurement(iterations = 2)//8
public class IntMapComparison {

    final static int size = 10_000_000;
    //final static int size = 100_000;
/*
    public static void main( String[] args ) {
        final int size = 1000000;
        testHashMap( size );
        testInt2DoubleOpenHashMap( size );
    }
*/

    @Benchmark
    public static void testInt2DoubleOpenHashMap(
        //final int size
    ) {
        final Int2DoubleOpenHashMap map = new Int2DoubleOpenHashMap( size );
        for ( int i = 0; i < size; ++i ) {
            map.put( i, i );
        }

    }

    //@Benchmark
    public static void testInt2LongOpenHashMap(
        //final int size
    ) {
        final Int2LongOpenHashMap map = new Int2LongOpenHashMap( size );
        for ( int i = 0; i < size; ++i ) {
            map.put( i, i );
        }

    }


    //@Benchmark
    public static void testInt2ObjectOpenHashMap(
        //final int size
    ) {
        //final Int2DoubleOpenHashMap map = new Int2DoubleOpenHashMap( size );
        final Int2ObjectOpenHashMap map = new Int2ObjectOpenHashMap( size );
        for ( int i = 0; i < size; ++i ) {
            map.put( i, (Integer)i );
        }

    }


    @Benchmark
    public static void testHashMap( /*final int size */) {
        final HashMap<Integer, Double> map = new HashMap<Integer, Double>( size );
        for ( int i = 0; i < size; ++i ) {
            map.put( i, (double) i );
        }
    }

}
