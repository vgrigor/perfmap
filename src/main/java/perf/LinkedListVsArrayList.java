package perf;

import maps.FastCache;
import maps.FastCache2;
import maps.FastCache_CHM;
import maps.FastObjIntMap;
import maps.SynchronizedCache;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * https://ru.stackoverflow.com/questions/568119/%D0%9E%D1%82%D0%BB%D0%B8%D1%87%D0%B8%D0%B5-arraylist-%D0%BE%D1%82-linkedlist
 *
 *
 * https://blog.avenuecode.com/java-microbenchmarks-with-jmh-part-2
 *
 */

@Fork(1)
@Warmup(iterations = 1)//10
@Measurement(iterations = 1)//10
@BenchmarkMode(Mode.AverageTime)
@Threads(1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LinkedListVsArrayList {
    private List<Object> arrayList;
    private  List<Object> linkedList;

    private static final int count = 100_000;


/*
    public static void main(String[] args) throws Exception {
        Main.main(args);
    }
*/

    @Setup
    public void setup() {

        arrayList = new ArrayList<>(count);
        linkedList = new LinkedList<>();

        for (int i = 0; i < count; i++)
            arrayList.add(new Object());

        linkedList.addAll(arrayList);
    }

    @Benchmark
    public void removeFromLinkedList(Blackhole blackhole) throws Exception {

        Object object = new Object();
        linkedList.remove(count / 2);
        linkedList.add(count / 2, object);
    }


    @Benchmark
    public void removeFromArrayList(Blackhole blackhole) throws Exception {

        Object object = new Object();
        arrayList.remove(count / 2);
        arrayList.add(count / 2, object);
    }

    @Benchmark
    public void removeFromLinkedList_Begin(Blackhole blackhole) throws Exception {

        Object object = new Object();
        linkedList.remove(0);
        linkedList.add(0, object);
    }

    @Benchmark
    public void removeFromArrayList_Begin(Blackhole blackhole) throws Exception {

        Object object = new Object();
        arrayList.remove(0);
        arrayList.add(0, object);
    }

    @Benchmark
    public void removeFromLinkedList_Part(Blackhole blackhole) throws Exception {

        Object object = new Object();
        linkedList.remove(count / 10);
        linkedList.add(count / 10, object);
    }

    @Benchmark
    public void removeFromArrayList_Part(Blackhole blackhole) throws Exception {

        Object object = new Object();
        arrayList.remove(count / 10);
        arrayList.add(count / 10, object);


    }
}