package main;


import karatsuba.Karatsuba;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        long newres = Karatsuba.karatsuba(12345, 6789);
        long res = 12345 * 6789;
        System.out.println("Normal multiplication: " + res);
        System.out.println("Karatsuba multiplication: " + newres);
        System.out.println("mine: " + Karatsuba.);
    }
}