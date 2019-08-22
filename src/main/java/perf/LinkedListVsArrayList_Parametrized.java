package perf;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * https://ru.stackoverflow.com/questions/568119/%D0%9E%D1%82%D0%BB%D0%B8%D1%87%D0%B8%D0%B5-arraylist-%D0%BE%D1%82-linkedlist
 *
 *
 * https://blog.avenuecode.com/java-microbenchmarks-with-jmh-part-2
 *
 */

@Fork(1)
@Warmup(iterations = 0)//10
@Measurement(iterations = 1)//10
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LinkedListVsArrayList_Parametrized {


    private static final int count = 100_000;


    @State(Scope.Benchmark)
    public static class BenchmarkState {

        @Param({"2", "4", "8", "16", "32", "64"})
        public int divisor;

        public  List<Object> arrayList;
        public  List<Object> linkedList;

        @Setup(Level.Trial)
        public void setUp() {

            System.out.println("SETUP");
            arrayList = new ArrayList<>(count);
            linkedList = new LinkedList<>();

            for (int i = 0; i < count; i++)
                arrayList.add(new Object());

            linkedList.addAll(arrayList);
        }
    }



/*
    public static void main(String[] args) throws Exception {
        Main.main(args);
    }
*/

    //@Setup
    public static void setup(BenchmarkState state) {

        state.arrayList = new ArrayList<>(count);
        state.linkedList = new LinkedList<>();

        for (int i = 0; i < count; i++)
            state.arrayList.add(new Object());

        state.linkedList.addAll(state.arrayList);
    }

    //@Benchmark
    public void removeFromLinkedList(Blackhole blackhole, BenchmarkState state) throws Exception {

        Object object = new Object();
        state.linkedList.remove(count / 2);
        state.linkedList.add(count / 2, object);
    }




    //@Benchmark
    public void removeFromArrayList(Blackhole blackhole, BenchmarkState state) throws Exception {

        Object object = new Object();
        state.arrayList.remove(count / 2);
        state.arrayList.add(count / 2, object);
    }

    //@Benchmark
    public void removeFromLinkedList_Begin(Blackhole blackhole, BenchmarkState state) throws Exception {

        Object object = new Object();
        state.linkedList.remove(0);
        state.linkedList.add(0, object);
    }

    //@Benchmark
    public void removeFromArrayList_Begin(Blackhole blackhole, BenchmarkState state) throws Exception {

        Object object = new Object();
        state.arrayList.remove(0);
        state.arrayList.add(0, object);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @BenchmarkMode(Mode.AverageTime)
    public void removeFromLinkedList_Part(Blackhole blackhole, BenchmarkState state) throws Exception {

        Object object = new Object();
        state.linkedList.remove(count / state.divisor);
        state.linkedList.add(count / 10, object);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @BenchmarkMode(Mode.AverageTime)
    public void removeFromArrayList_Part(Blackhole blackhole, BenchmarkState state) throws Exception {

        Object object = new Object();
        state.arrayList.remove(count / state.divisor);
        state.arrayList.add(count / state.divisor, object);


    }
}