package com.example;

import java.io.IOException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int a = Integer.valueOf(sc.nextLine());
        switch (a) {
            case 1: {
                int index = 1;
                System.out.println(index);
                break;
            }
            case 2:
                int index = 0;
                System.out.println(index);
                break;
            default:
                break;
        }

    }

    void t1() {
        double a = 0.0;
        double b = Double.NaN;
        if (a > b || a < b || a == b) {

        } else {
            System.out.print("HelloWorld");
        }
    }

}
