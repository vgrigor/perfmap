package jol;

import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import java.io.IOException;
import java.util.HashMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public final class JOLTester {

    static final boolean showClassLayeot = false;
    public static void main( String[] args ) {
        final int size = 1_000_000;
        //final int size = 100_000;
        testHashMap( size );
        testInt2DoubleOpenHashMap( size );
    }

    private static void testInt2DoubleOpenHashMap(
        final int size
    ) {
        final Int2DoubleOpenHashMap map = new Int2DoubleOpenHashMap( size );
        for ( int i = 0; i < size; ++i ) {
            map.put( i, i );
        }

        if(showClassLayeot)
        System.out.println(
            ClassLayout
                .parseClass( map.getClass() )
                .toPrintable()
        );
        System.out.println(
            GraphLayout.parseInstance( map ).toFootprint()
        );

        toImage(map,"Int2DoubleOpenHashMap.jpg");
    }

    private static void testHashMap( final int size ) {
        final HashMap<Integer, Double> map = new HashMap<Integer, Double>( size );
        for ( int i = 0; i < size; ++i ) {
            map.put( i, (double) i );
        }

        if(showClassLayeot)
        System.out.println(
            ClassLayout
                .parseClass( map.getClass() )
                .toPrintable()
        );
        System.out.println(
            GraphLayout.parseInstance( map ).toFootprint()
        );
        toImage(map,"HashMap.jpg");

    }

    static void toImage(Object o, String name){
        try {
            GraphLayout.parseInstance( o ).toImage(name);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}