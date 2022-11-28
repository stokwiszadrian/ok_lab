package main;

import matrix.Matrix;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Matrix newM = new Matrix(5, 5);
        newM.printmatrix();
        System.out.println("multi: "+newM.c3multi());
        System.out.println("naive: "+newM.hasCycle());
    }
}