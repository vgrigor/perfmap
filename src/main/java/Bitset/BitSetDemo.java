package Bitset;

import java.util.ArrayList;
import java.util.BitSet;
import org.openjdk.jol.info.GraphLayout;

public class BitSetDemo {

    public static void main(String[] args) {
        BitSet bitSetOne = new BitSet(8);
        BitSet bitSetTwo = new BitSet(8);

        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                bitSetOne.set(i);
            }
            if (i % 2 != 0) {
                bitSetTwo.set(i);
            }
        }

        System.out.println("bitSetOne:");
        System.out.println(bitSetOne);

        System.out.println("bitSetTwo:");
        System.out.println(bitSetTwo);

        System.out.println("=====================");

        System.out.println("bitSetOne.xor(bitSetTwo): ");
        bitSetOne.xor(bitSetTwo);
        System.out.println(bitSetOne);

        System.out.println("=====================");

        System.out.println("bitSetTwo.and(bitSetTwo): ");
        bitSetTwo.and(bitSetOne);
        System.out.println(bitSetTwo);

        System.out.println("=====================");
    }

}
