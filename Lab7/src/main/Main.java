package main;


import karatsuba.Karatsuba;

import java.math.BigInteger;
import java.util.Random;

import static karatsuba.Karatsuba.calc;

public class Main {

    public static void main(String[] args) {
        long start, stop;
        Random random = new Random();
        int N = 5000;
        BigInteger a = new BigInteger(N, random);
        BigInteger b = new BigInteger(N, random);

//        System.out.println(a + "    " + b);
        start = System.currentTimeMillis();
        BigInteger c = calc(a, b);
        stop = System.currentTimeMillis();
        System.out.println(stop - start);

        start = System.currentTimeMillis();
        BigInteger d = a.multiply(b);
        stop = System.currentTimeMillis();
        System.out.println(stop - start);

        System.out.println((c.equals(d)));
    }
}