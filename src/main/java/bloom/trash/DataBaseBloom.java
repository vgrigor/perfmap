package bloom.trash;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;
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

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
//@Threads(value = 4)
@Warmup(iterations = 0)//3
@Measurement(iterations = 1)//8
public class DataBaseBloom {
    private static int SIZE = 2048*1000*10;
    private static int QUANT = 1000;
    private static int MIN_ID = 5000;
    private static int MAX_ID = 5100;
    ObjectArrayList iList = new ObjectArrayList();
    static BloomFilter<Integer> filter;
    BitSet bitSet = new BitSet(SIZE);

    static class Item{
        public Item(int id, String name){
            this.id = id;
            this.name = name;
        }
        int id;
        String name;
    }

    @Setup
    public void Setup(){
        fill();
        setupBloom();
    }


    void fill(){
        for(int i =0; i < SIZE; i++) {
            iList.add(new Item(i, new String("" + i)));
        }
    }

    public void setupBloom(){
        filter = BloomFilter.create(
            Funnels.integerFunnel(),
            20,//SIZE/QUANT,
            0.01);

        //for(int i =0; i < SIZE/100; i++){
        filter.put(5);
        //filter.put(6);

        bitSet.set(5);
    }

    @Benchmark
    public void searchStandart(Blackhole bh){
        int sum = 0;
        for(int i =0; i < SIZE; i++){
            Item item = (Item)iList.get(i);

            sum += ThreadLocalRandom.current().nextInt();

            if(item.id > MIN_ID && item.id < MAX_ID){
                sum += item.id ;
            }


        }

        bh.consume(sum);
        //System.out.println("sum = " + sum);

    }

    @Benchmark
    public void searchBloomOnly(Blackhole bh) {
        int sum = 0;
        int printed = 0;

        for (int i = 0; i < SIZE / QUANT; i++) {

            if (filter.mightContain(i)) {
                sum ++;
            }
        }
        bh.consume(sum);
    }

    @Benchmark
    public void searchBloom(Blackhole bh){
        int sum = 0;
        int printed = 0;

        for(int i =0; i < SIZE/QUANT; i++){

            //if(i == 5 ){
            if(bitSet.get(i)){
            //if(filter.mightContain(i)){

/*
                if(i!= printed) {
                    System.out.println("i= " + i);
                    printed = i;
                }
*/


                for(int j =0; j < QUANT; j++) {
                    Item item = (Item)iList.get(i * QUANT + j);
                    //System.out.println("i = " + item.id);

                    sum += ThreadLocalRandom.current().nextInt();

                    if(item.id > MIN_ID && item.id < MAX_ID){

                        sum += item.id ;
                    }
                }
            }

        }
        bh.consume(sum);
        //System.out.println("sum = " + sum);


    }


}
