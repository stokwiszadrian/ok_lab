package main;


import karatsuba.Karatsuba;

import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random rnd = new Random();
        BigInteger n1 = new BigInteger(300, rnd);
        BigInteger n2 = new BigInteger(300, rnd);
        System.out.println(n1 + "  " + n2);
        BigInteger res = n1.multiply(n2);
        BigInteger karatsuba = Karatsuba.calc(n1, n2);
        System.out.println("Normal multiplication: " + res);
        System.out.println("Karatsuba multiplication: " + karatsuba);
        System.out.println(res.equals(karatsuba));
    }
}