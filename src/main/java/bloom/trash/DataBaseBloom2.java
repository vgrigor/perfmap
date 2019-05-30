package bloom.trash;

/*
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
*/
import bloom.BloomFilter;
import java.util.ArrayList;
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
import org.openjdk.jol.info.GraphLayout;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 0)//3
@Measurement(iterations = 1)//8
//@Threads(value = 4)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)

public class DataBaseBloom2 {
    //Размер массива данных
    private static int SIZE = 1000*1000*1;

    //Размер чанка, SS-табле, на самом деле храним в большом массиве с отступом размером QUANT,
    // потому что в памяти нет необходимости иметь отдельные куски и так проще писать для демонстрации - не для прома!
    private static int QUANT = 1000;

    //Запрос- найти значения от и до
    private static int MIN_ID = 5000;
    private static int MAX_ID = 5100;

    private static boolean TEST_MEMORY = false;
    private static boolean TEST_PRINT = false;

    //Слегка более быстрая реализация массива объектов
    //ObjectArrayList iList = new ObjectArrayList();
    //Стандартная реализация массива объектов
    ArrayList<Item> iList = new ArrayList<Item>();
    static BloomFilter[] filters = new BloomFilter[SIZE/QUANT];

    public static void main(String[] args) {
        DataBaseBloom2 test = new DataBaseBloom2();
        test.Setup();
        test.searchBloomArray(null);
        test.searchStandart(null);
    }

    /**
     * Обьект, над которыми делаем поиск
     */
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
        //fillData();
        fillDataSortedRandom();
        fillBloomArray();
    }

    /**
     * Заполняем данные просто отсортированными значениями
     */
    void fillData(){
        for(int i =0; i < SIZE; i++) {
            iList.add(new Item(i, new String("" + i)));
        }
    }

    /**
     * Заполняем данные     отсортированными значениями, но каждый под-массив начинается со случайного числа,
     * чтобы смоделировать SS-табле
     */
    void fillDataSortedRandom(){

        System.out.println("Fill data array");
        int firstVal = ThreadLocalRandom.current().nextInt(0, SIZE);
        for(int i =0; i < SIZE; i++) {
            if(i%QUANT == 0)
                firstVal = ThreadLocalRandom.current().nextInt(0, QUANT);

            iList.add(new Item(firstVal + i, new String("" + i)));
        }
        System.out.println("Filled data array");

        if(TEST_MEMORY)
            printMemory(iList);
    }

    /**
     * Заполняем индекс построенный на BloomFilter
     */
    void fillBloomArray(){
        filters = new BloomFilter[SIZE/QUANT];

        for(int i = 0; i < SIZE; i++) {

            int bloomIndex = i / QUANT;
            if (i % QUANT == 0) {
                //System.out.println("bloomIndex = " + bloomIndex);
                filters[bloomIndex] = new BloomFilter(1000, 10000);
            }
        }

        for(int i = 0; i < SIZE; i++) {

            int bloomIndex = i/QUANT;
            BloomFilter nextFilter = filters[bloomIndex];

            Item item = (Item )iList.get(i);
            if(item != null)
                if(nextFilter != null)
            nextFilter.add(item.id);
        }
        if(TEST_MEMORY)
            printMemory(filters);

    }

    /**
     * Проходим по всему массиву обьектов чтобы найти удовлетворяющие условию
     * @param bh
     */
    @Benchmark
    public void searchStandart(Blackhole bh){
        long sum = 0;
        for(int i =0; i < SIZE; i++){
            Item item = (Item)iList.get(i);

            if(item.id > MIN_ID && item.id < MAX_ID){
                sum += item.id ;
            }
        }
        if(bh != null)
        bh.consume(sum);

        if(TEST_PRINT)
            System.out.println("sum = " + sum + " (searchStandart)");
    }

    /**
     * Проходим по выборке из массиву обьектов чтобы найти удовлетворяющие условию
     * Выборка происходит по индексу на основе BloomFilter
     * @param bh
     */

    @Benchmark
    public void searchBloomArray(Blackhole bh){
        long sum = 0;
        int printed = 0;

        //Идем сразу по чанкам, и только в отсеянных чанках пройдем по данным
        for(int i =0; i < SIZE/QUANT; i++){

            int bloomIndex = i;
            BloomFilter nextFilter = filters[bloomIndex];
            //if(i == 5 ){
            if(nextFilter.contains(MIN_ID)){
                if(TEST_PRINT) System.out.println("search bloomIndex = " + i);
                for(int j =0; j < QUANT; j++) {
                    Item item = (Item)iList.get(i * QUANT + j);

                    if(item.id > MIN_ID && item.id < MAX_ID){

                        sum += item.id ;
                    }
                }
            }
        }

        if(TEST_PRINT)
            System.out.println("sum = " + sum + " (searchBloom)");

        if(bh != null)
            bh.consume(sum);
    }


    static void printMemory(Object o){
        System.out.println(
            GraphLayout.parseInstance( o ).toFootprint()
        );
    }

}
