package Bitset;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.BitSet;
import org.openjdk.jol.info.GraphLayout;

public class BitSetSize {
    private static int SIZE = 2048*1000;

    public static void main(String[] args) {


        int[] arr = new int[SIZE];
        ArrayList arl = new ArrayList<Integer>();
        BitSet bitSetSize = new BitSet(SIZE);
        IntArrayList intArrayList = new IntArrayList(SIZE);

        for(int j =0; j < SIZE; j++){
            arr[j] = j;
        }
        printSize(arr);

        for(int j =0; j < SIZE; j++){
            arl.add(j);
        }
        printSize(arl);

        for(int j =0; j < SIZE; j++){
            bitSetSize.set(j);
        }
        printSize(bitSetSize);

        for(int j =0; j < SIZE; j++){
            intArrayList.add(j);
        }
        printSize(intArrayList);
    }

    static void printSize(Object o){
        System.out.println(
            GraphLayout.parseInstance( o ).toFootprint()
        );
        System.gc();

    }
}
