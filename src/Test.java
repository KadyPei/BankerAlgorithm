import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Collections;

public class Test {
    public static void main(String[] args) throws FileNotFoundException{
        FIFO fifo = new FIFO();
        System.out.println("FIRST IN FIRST OUT: \n");
        Bank banker1 = new Bank(args[0]);
        fifo.fiforun(banker1).printBank();
        System.out.println("\n \n BANKER:");
        BankerAlg banker = new BankerAlg();
        Bank banker2 = new Bank(args[0]);
        banker.run(banker2).printBank();
    }



}