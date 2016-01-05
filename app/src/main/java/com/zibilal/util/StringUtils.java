package com.zibilal.util;

import java.util.Random;

/**
 * Created by Bilal on 12/31/2015.
 */
public class StringUtils {
    public static String getSaltString(int len) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while(sb.length() < len) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            sb.append(SALTCHARS.charAt(index));
        }
        return sb.toString();
    }

    public static String getNumberString(int len) {
        String SALTCHARS="1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while(sb.length() < len) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            sb.append(SALTCHARS.charAt(index));
        }
        return sb.toString();
    }

    public static int getRandomInt(int len) {
        Random rnd = new Random();
        return rnd.nextInt(len);
    }

    public static void main(String[] args) {
        for (int i=0; i < 100; i++) {
            System.out.println("Number : "  + StringUtils.getRandomInt(15) );
        }
    }
}
