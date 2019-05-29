package Bitset;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jol.info.GraphLayout;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
//@Threads(value = 4)
@Warmup(iterations = 0)//3
@Measurement(iterations = 1)//8
public class BitSetPerf {
    private static int SIZE = 2048*1000*10;

    static int[] arr = new int[SIZE];
    static ArrayList<Integer> arl = new ArrayList<Integer>();
    static IntArrayList intArrayList = new IntArrayList(SIZE);
    static BitSet bitSetSize = new BitSet(SIZE);


    @Setup
    public static void main() {


        for(int j =0; j < SIZE; j++){
            arr[j] = j;
            arl.add(j);
            bitSetSize.set(j);
            intArrayList.add(j);
        }
    }

    @Benchmark
    public static void iterateArray(Blackhole bh){
        int sum = 0;
        for(int i: arr){
            sum += i;
        }
        bh.consume(sum);
    }

    @Benchmark
    public static void iterateArrayStream(Blackhole bh){
        int sum = 0;
        IntStream intStream1 = Arrays.stream(arr);
        sum = intStream1.sum();

        bh.consume(sum);
    }

    @Benchmark
    public static void iterateFastArrayList(Blackhole bh){
        int sum = 0;
        for(int i =0; i <SIZE ; i++){
            sum += intArrayList.getInt(i);
        }
        bh.consume(sum);
    }

    @Benchmark
    public static void iterateArrayList(Blackhole bh){
        int sum = 0;
        for(int i =0; i <SIZE ; i++){
            sum += arl.get(i);
        }
        bh.consume(sum);
    }

    @Benchmark
    public static void iterateBitSet(Blackhole bh){
        int sum = 0;
        for(int i =0; i <SIZE ; i++){
            sum += (bitSetSize.get(i)? 0:i);
        }
        bh.consume(sum);
    }


    static void printSize(Object o){
        System.out.println(
            GraphLayout.parseInstance( o ).toFootprint()
        );

    }
}
