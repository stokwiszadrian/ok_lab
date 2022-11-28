package main;

import matrix.Matrix;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        int[][] m = {
                {0, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1},
                {1, 0, 1, 0, 1, 0},
                {0, 0, 1, 1, 0, 0},
                {1, 0, 1, 0, 0, 0}
        };
        Matrix newM = new Matrix(m, false);
        newM.printmatrix();
        System.out.println("DFS: \n");
        newM.DFS().printmatrix();
    }
}