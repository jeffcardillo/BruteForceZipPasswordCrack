package com.jeffcardillo;

import net.lingala.zip4j.exception.ZipException;

import java.io.FileNotFoundException;

/**
 * @author Jeffrey Cardillo
 * created February 2018
 */

public class Main {

    public static void main(String[] args) {
        // default values
        String sourcePath = "";
        String destinationPath = "";
        String charactersStr = new String("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*_-=+");
        char[] characters = charactersStr.toCharArray();
        boolean verbose = false;

        int min = 6;
        int max = 10;

        try {
            // process program arguments, most key/value pairs
            for (int i = 0; i < args.length; i += 2) {
                if (args.length > (i + 1)) {
                    String key = args[i];
                    String value = args[i + 1];

                    switch (key) {
                        case "-s":
                            sourcePath = value;
                            break;
                        case "-d":
                            destinationPath = value;
                            break;
                        case "-min":
                            min = Integer.valueOf(value);
                            break;
                        case "-max":
                            max = Integer.valueOf(value);
                            break;
                        case "-characters":
                            charactersStr = value;
                            characters = value.toCharArray();
                            break;
                        case "-v":
                        case "-verbose":
                            verbose = true;
                            // decrement i since this is a single key
                            i--;
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid arguments");
        }

        System.out.println("Brute Force Zip Password Cracker\n--------------------------------");
        System.out.println("Source: " + sourcePath);
        System.out.println("Destination: " + destinationPath);
        System.out.println("Characters: " + charactersStr);
        System.out.println("Min Length: " + min);
        System.out.println("Max Length: " + max);
        System.out.println("");

        try {
            BruteForceZipPassword brute = new BruteForceZipPassword();
            String password = brute.bruteForceZipPassword(sourcePath, destinationPath, min, max, characters, verbose);
            if (password == null) {
                System.out.println("No password found :(");
            } else {
                System.out.println("Password found: " + password);
            }
        } catch (ZipException e) {
            System.out.println("Something went wrong with ZipFile");
        } catch (FileNotFoundException e) {
            System.out.println("Zip file not found");
        }
    }
}
