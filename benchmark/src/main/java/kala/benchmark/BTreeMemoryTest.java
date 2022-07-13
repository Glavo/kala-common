package kala.benchmark;

import kala.collection.internal.tree.BTree8;
import org.openjdk.jol.info.*;

import java.util.Arrays;
import java.util.TreeSet;

public class BTreeMemoryTest extends BTree8<Integer> {


    private static void test(int n) {
        System.out.println("---------- Elements: " + n + " ----------");

        Integer[] values = new Integer[n];
        for (int i = 0; i < n; i++) {
            values[i] = i;
        }

        long baseSize = GraphLayout.parseInstance((Object[]) values).totalSize();

        TreeSet<Integer> rbt = new TreeSet<>(Arrays.asList(values));
        BTreeMemoryTest bTree = new BTreeMemoryTest();
        for (int i = 0; i < n; i++) {
            bTree.insert(values[i]);
        }

        long rbtSize = GraphLayout.parseInstance(rbt).totalSize() - baseSize;
        long bTreeSize = GraphLayout.parseInstance(bTree).totalSize() - baseSize;

        System.out.printf("java.util.TreeSet:                       %s bytes%n", rbtSize);
        System.out.printf("kala.collection.internal.tree.BTree8:    %s bytes%n", bTreeSize);

    }

    public static void main(String[] args) {
        int[] testData = {0, 1, 5, 10, 100, 1000, 10000, 100000, 1000000};
        for (int data : testData) {
            test(data);
        }
    }
}
