package String;

import java.io.BufferedReader;
    import java.io.FileNotFoundException;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.Scanner;

public class ConcatTest {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader("onegin.txt"));

        StringBuilder sb = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();

        //sb = sb.append(sb).append(sb).append(sb).append(sb).append(sb).append(sb) ;

        String[] words = sb.toString().split("\\s+");
        System.out.println("Total words:" + words.length);
        //waitEnter();



        String buff = "";
        StringBuffer buffSBf = new StringBuffer();
        StringBuilder buffSBu = new StringBuilder();


        long ts = System.nanoTime();
        //Sting
        for (String word : words) {
            buff += word + " ";
            //buff.append(word).append(" ");
        }

        long te =System.nanoTime();
        System.out.println("Complete String PLUS   , lenght:" + buff.length() + " elapsed time:" + (te - ts)/1e6 + "ms");
//------------------------

         ts = System.nanoTime();
        //Sting
        for (String word : words) {
            //buff += word + " ";
            buffSBf.append(word).append(" ");
        }

        te =System.nanoTime();


        System.out.println("Complete StringBuffer, lenght:" + buffSBf.length() + " elapsed time:" + (te - ts)/1e6 + "ms");

//------------------------

        ts = System.nanoTime();
        //Sting
        for (String word : words) {
            //buff += word + " ";
            buffSBu.append(word).append(" ");
        }

        te =System.nanoTime();

        System.out.println("Complete StringBuilder, lenght:" + buffSBu.length() + " elapsed time:" + (te - ts)/1e6 + "ms");


    }

    private static void waitEnter() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Press Enter key.");
        scan.nextLine();
    }

}
