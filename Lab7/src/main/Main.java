package main;


import karatsuba.Karatsuba;

public class Main {

    public static void main(String[] args) {
        long n1 = 2344444;
        long n2 = 6789634;
        long res = n1 * n2;
        System.out.println("Normal multiplication: " + res);
        System.out.println("Karatsuba multiplication: " + Karatsuba.calc(n1, n2));
    }
}