package com.jeffcardillo;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * BruteForceZipPassword - This class will sequentially generate passwords and attempt to extract files from a password
 * protected zip file. The characters allowed to appear in the password, as well as a range of password length can be
 * specified.
 *
 * @author Jeffrey Cardillo
 * created February 2018
 */

public class BruteForceZipPassword {

    private char[] chars;
    private long startTimeInMillis = System.currentTimeMillis();

    /**
     * Attempt to brute force the zip file at the specified path, extract contents to the specified destination
     *
     * @param sourceFilePath - path to source zip file
     * @param extractedContentsPath - path to extract contents of zip file once password is cracked
     * @param min - the minimum length password to generate
     * @param max - the maximum length password to generate
     * @param verbose - true will show the passwords that are generated
     * @return
     * @throws ZipException
     */
    public String bruteForceZipPassword(String sourceFilePath, String extractedContentsPath, int min, int max, char[] characters, boolean verbose) throws ZipException, FileNotFoundException {
        chars = characters;

        // check if source file exists
        File source = new File(sourceFilePath);
        if (!source.exists()) {
            throw new FileNotFoundException();
        }

        // create a zip file object from source path
        ZipFile zipFile = new ZipFile(sourceFilePath);

        String password = null;
        for (int i=min; i<=max; i++) {
            password = findPasswordForLength(i, zipFile, extractedContentsPath, verbose);

            if (password != null) {
                continue;
            }
        }

        return password;
    }

    /**
     * Tries all password combinations for the given password length
     *
     * @param length - the length password to generate
     * @param zipFile
     * @param extractedContentsPath
     * @return
     */
    private String findPasswordForLength(int length, ZipFile zipFile, String extractedContentsPath, boolean verbose) {

        // create password structure
        char[] password = new char[length];

        // how many combinations are there - for percent done calc
        double totalCombinations = Math.pow(chars.length, length);

        // sequential password based on a counter
        double count = 0;

        boolean done = false;
        while (!done) {
            // check to see if we've reached the maximum count for this password length. If so, bail
            if ((count / (Math.pow(chars.length, (length-1)))) >= chars.length) {
                done = true;
                continue;
            }

            // cleverly set the value of each character in password based on the counter
            for (int i=0; i<length; i++) {
                password[i] = chars[(int)((count / (Math.pow(chars.length, i))) % chars.length)];
            }

            try {
                if (verbose) {
                    System.out.println("Attempt: \"" + String.valueOf(password) + "\" " + count + " of " + totalCombinations + " possibilities");
                }

                // attempt to open the zip file with password.
                attemptPasswordOnZip(zipFile, extractedContentsPath, String.valueOf(password));

                // will only reach this far if password successful, otherwise exception thrown and loop again
                return String.valueOf(password);
            } catch (ZipException e) { }

            // after a large chunk, output percent done
            if (count % 10000 == 0) {
                System.out.println("Attempting passwords of length " + length + ": "
                        + (String.format("%.3f", (count / totalCombinations)*100)) + "% Complete "
                        + ((System.currentTimeMillis() - startTimeInMillis) / (60000)) + " mins elapsed");
            }

            count ++;
        }

        System.out.println("Attempting passwords of length " + length + ": 100% Complete");

        return null;
    }

    /**
     * Attempt to extract contents of zipped file with provided password. Throw exception upon failure
     *
     * @param zipFile
     * @param extractedContentsPath
     * @param password
     * @throws ZipException
     */
    private void attemptPasswordOnZip(ZipFile zipFile, String extractedContentsPath, String password) throws ZipException {
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password);
        }
        zipFile.extractAll(extractedContentsPath);
    }
}
