package org.example;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Main {

    /*
    Variables must be organized in such a way:
    minterms <- the numbers on the onset
    dontcares <- the values with the output "-"
    essentialminterms <- the values that either don't have a cover, or make it to the end

    ---

    mintermcovers <- numbers will be held here after every iteration of the covers
     */
    static ArrayList<String> minterms = new ArrayList<>();
    static ArrayList<String> dontcares = new ArrayList<>();
    static ArrayList<String> essentialminterms = new ArrayList<>();
    static ArrayList<String> mintermscover = new ArrayList<>();
    static ArrayList<String> allterms = new ArrayList<>();

    //---------------------------------------------------------

    static ArrayList<String> left = new ArrayList<>();
    static ArrayList<String> right = new ArrayList<>();

    //---------------------------------------------------------
    static ArrayList<String> processed = new ArrayList<>();

    //---------------------------------------------------------
    static ArrayList<String> headers = new ArrayList<>();
    static ArrayList<String> titles = new ArrayList<>();
    static int headerWeight = 0;

    static int maxTest = 0;
    static boolean moreMatch = true;

    //---------------------------------------------------------
    //Column and Row Dominance


    //---------------------------------------------------------
    //Final expression
    static ArrayList<String> finalExpression = new ArrayList<>();

    public static void main(String[] args) {
        // Path to the input .pla file
        String filePath = "C:\\Users\\Erica\\IdeaProjects\\QM_MyWAY\\src\\main\\resources\\input.pla";

        /*
        There are 3 formats to be prepared for
        101 <- binary
        1-1 <- wild card digits - essentially starting at the second step of QM
        ~0~ <- inverted digits - the tildes are the opposite of the visible numbers
        */

        // Determine the maximum number of 1s across all terms
        try {
            readPlaFile(filePath);
            allterms.addAll(minterms);
            allterms.addAll(dontcares);
            int maxOnes = calculateMaxOnes(allterms);

            while (true) {
                boolean matchesMade = false; // Track if any matches are made in this cycle

                for (int i = 1; i <= maxOnes; i++) {
                    int initialSize = mintermscover.size();
                    hammerTime(allterms, i - 1, i);

                    // Check if `hammerTime` added new terms
                    if (mintermscover.size() > initialSize) {
                        matchesMade = true;
                    }
                }

                // If no matches were made in this cycle, terminate the loop
                if (!matchesMade) {
                    break;
                }

                // Prepare for the next cycle
                allterms.clear();
                allterms.addAll(mintermscover);
                mintermscover.clear();
                headerWeight++;
            }


            essentialminterms.addAll(allterms);

            for (int i = 0; i < essentialminterms.size(); i++) {
                int dashCounter = 0;
                //System.out.println(essentialminterms.get(i));
                for (int j = 0; j < essentialminterms.get(i).length(); j++) {
                    if (essentialminterms.get(i).charAt(j) == '-') {
                        dashCounter++;
                    }
                }
                /*
                if (dashCounter == headerWeight) {

                    headers.add(essentialminterms.get(i));
                }

                 */

                headers.add(essentialminterms.get(i));
            }

            HashSet<String> set = new HashSet<>(headers);
            headers = new ArrayList<>(set);


            //columns of prime implicants

            //row of minterms


            System.out.println("The size is: " + essentialminterms.size());
            System.out.println("The header weight is: " + headerWeight);
            printFormattedTable();

            // Apply column dominance and update the table
            columnDominance();

            // Perform row dominance next
            rowDominance();

            finalRun();

            System.out.println("\nFinal expression:");
            ArrayList<String> output = new ArrayList<>(new HashSet<>(finalExpression));
            System.out.println(finalOutput(output));
            String outputFilePath = "output.pla";
            writePlaFile(outputFilePath);
            System.out.println("PLA file written successfully!");
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    //this method parses the input .pla
    public static void readPlaFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                //disregards the PLA calls for input that contain "." and "#"
                if (!line.contains(".") && !line.contains("#") && !line.equals("")) {
                    //line for debugging to make sure everything is being parsed properly
                    //System.out.println(line);

                    //this method handles the inverted inputs. The digits are flipped based on the numbers that are not tildes
                    if (line.contains("~")) {
                        line = inverterMethod(line);
                    }

                    //splits the input from the output. The first member of linetoo is the number, and the second is the output associated
                    String[] linetoo = line.split(" ");

                    //if a number has an output of 1, will be added to the minterms arraylist; if a number has an output of zero it will just be ignored
                    if (linetoo[1].equals("1")) {
                        minterms.add(linetoo[0]);
                        //System.out.println("The minterm was: " + linetoo[0]);
                    }
                    //if a number has an output of -, will be added to the dontcares arraylist
                    else if (linetoo[1].equals("-")) {
                        dontcares.add(linetoo[0]);
                        //System.out.println("The dontcare was: " + linetoo[0]);
                    }

                } else {
                    continue;
                }
            }
        }
    }

    //convert tildes to inverse value of present digits
    public static String inverterMethod(String number) {

        if (number.contains("0")) {
            number = number.replace('~', '1');
        } else if (number.contains("1")) {
            number = number.replace('~', '0');
        }
        return number;
    }

    //this method counts the number of 1s in a number

    public static void hammerTime(ArrayList<String> terms, int index1, int index2) {
        int matchesMade = 0;
        // Organize the terms into left and right sets based on the number of 1s
        organizer(terms, index1, index2);

        // Check each term in the left set
        for (int i = 0; i < left.size(); i++) {
            int noMatch = 0;
            // Compare it with each term in the right set
            for (int k = 0; k < right.size(); k++) {
                int same = 0;
                String hold = "";

                // Compare each character in the two terms
                for (int j = 0; j < left.get(i).length(); j++) {
                    if (left.get(i).charAt(j) == right.get(k).charAt(j)) {
                        same++;
                        hold += left.get(i).charAt(j);
                    } else {
                        hold += '-';
                    }
                }

                // If there's a match (differ by only one bit), store the new term
                if (same == left.get(i).length() - 1) {
                    mintermscover.add(hold);
                    processed.add(left.get(i));
                    processed.add(right.get(k));
                    noMatch++;
                    matchesMade++;
                }
            }

            // If no match was found for this term, it is essential, add to the final expression
            if (noMatch == 0 && !processed.contains(left.get(i))) {
                essentialminterms.add(left.get(i));  // Add to essential terms

                //finalExpression.add();

            }
        }

        // If no matches were made, end the loop
        if (matchesMade == 0) {
            moreMatch = false;
        }



        // Clear the left and right sets for the next iteration
        left.clear();
        right.clear();
    }


    //this method compares the digits based on two arraylists, left and right,
    public static void organizer(ArrayList<String> terms, int index1, int index2) {
        for (int i = 0; i < terms.size(); i++) {
            int internal = 0;
            for (int j = 0; j < terms.get(i).length(); j++) {
                if (terms.get(i).charAt(j) == '1') {
                    internal++;
                }
            }

            if (internal == index1) {
                //System.out.println("A match! And it is: " + terms.get(i) + " with " + index1);
                left.add(terms.get(i));
                //System.out.println("While populating the left index is: " + left.getFirst());
            } else if (internal == index2) {
                //System.out.println("A match! And it is: " + terms.get(i) + " with " + index2);
                right.add(terms.get(i));
                //System.out.println("While populating the right index is: " + right.getFirst());
            }
            //System.out.println(internal);
        }
    }

    private static int calculateMaxOnes(ArrayList<String> terms) {
        int maxOnes = 0;
        for (String term : terms) {
            int count = 0;
            for (char c : term.toCharArray()) {
                if (c == '1') count++;
            }
            maxOnes = Math.max(maxOnes, count);
        }
        return maxOnes;
    }

    private static void printFormattedTable() {
        int columnWidth = 8; // Width for formatting columns

        ArrayList<String> miniTerms = new ArrayList<>();
        miniTerms.addAll(miniTerms);

        // Print the header row
        System.out.print(padRight("", columnWidth)); // Empty cell for minterm titles
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            System.out.print(padRight(i + ":" + header, columnWidth)); // Add indices before header
        }
        System.out.println();

        // Printing separator row
        System.out.print(padRight("", columnWidth).replace(" ", "-"));
        for (int i = 0; i < headers.size(); i++) {
            System.out.print(padRight("", columnWidth).replace(" ", "-"));
        }
        System.out.println();

        // Printing the rows for minterms and their coverage
        for (int i = 0; i < minterms.size(); i++) {
            String minterm = minterms.get(i);
            System.out.print(padRight(i + ":" + minterm, columnWidth)); // Add indices before minterm

            int numMatch = 0; // Count the number of matches for the current minterm
            for (String header : headers) {
                if (matches(minterm, header)) {
                    System.out.print(padRight("x", columnWidth)); // Covered by this header term
                    int index = i;
                    numMatch++;
                } else {
                    System.out.print(padRight("-", columnWidth)); // Not covered by this header term
                }
            }

            // Mark rows with only one match
            if (numMatch == 1) {
                System.out.print("ONLY ONE MATCH");

            }


            System.out.println(); // Move to the next row
        }
    }


    public static String padRight(String str, int width) {
        return String.format("%-" + width + "s", str); // Padding each string to ensure proper alignment
    }

    public static boolean matches(String minterm, String header) {
        for (int i = 0; i < minterm.length(); i++) {
            if (header.charAt(i) != '-' && header.charAt(i) != minterm.charAt(i)) {

                return false;
            }
        }
        return true;
    }

    private static void columnDominance() {
        boolean columnRemoved = true;

        // Loop until no column is removed
        while (columnRemoved) {
            columnRemoved = false;

            // Map to store headers and the rows they cover
            ArrayList<String> rowsToRemove = new ArrayList<>();
            ArrayList<String> headersToRemove = new ArrayList<>();

            // Check for rows with a single match
            for (String minterm : minterms) {
                int matchCount = 0;
                String matchingHeader = null;

                for (String header : headers) {
                    if (matches(minterm, header)) {
                        matchCount++;
                        matchingHeader = header; // Track the matching header
                    }
                }

                // If exactly one match, mark the header and its rows for removal
                if (matchCount == 1 && matchingHeader != null) {
                    if (!headersToRemove.contains(matchingHeader)) {
                        headersToRemove.add(matchingHeader);
                        finalExpression.add(matchingHeader);
                    }
                    rowsToRemove.add(minterm); // Mark the row for removal
                    columnRemoved = true; // Indicate a change
                }
            }

            // Remove identified headers
            headers.removeAll(headersToRemove);

            // Remove rows satisfied by removed headers
            for (String header : headersToRemove) {
                for (String minterm : minterms) {
                    if (matches(minterm, header)) {
                        rowsToRemove.add(minterm); // Mark for removal
                    }
                }
            }

            minterms.removeAll(rowsToRemove); // Remove rows from minterms

            // Reprint the table after modifications
            if (columnRemoved) {
                System.out.println("\nColumn dominance");
                printFormattedTable();
            }
        }
    }

    private static void rowDominance() {
        ArrayList<String> rowsToRemove = new ArrayList<>();

        for (int i = 0; i < minterms.size(); i++) {
            for (int j = 0; j < minterms.size(); j++) {
                if (i != j && isRowCovering(minterms.get(i), minterms.get(j))) {
                    rowsToRemove.add(minterms.get(i)); // Add the covering row (parent) for removal

                    break; // Move to the next row once a covering relation is found
                }
            }
        }

        // Remove all "parent" rows that cover others
        minterms.removeAll(rowsToRemove);

        // Print the updated table
        System.out.println("\nRow dominance");
        printFormattedTable();
    }

    // Helper method to check if one row covers another
    private static boolean isRowCovering(String parentRow, String childRow) {
        for (int col = 0; col < headers.size(); col++) {
            boolean parentMatches = matches(parentRow, headers.get(col));
            boolean childMatches = matches(childRow, headers.get(col));

            if (childMatches && !parentMatches) {
                return false; // The parent row does not cover the child row in this column
            }
        }
        return true; // The parent row covers the child row entirely
    }

    private static void finalRun() {

        ArrayList<String> selectedHeaders = new ArrayList<>();
        HashSet<String> alreadyChosenHeaders = new HashSet<>();  // This tracks headers that have already been selected

        // For each minterm, find the first matching header
        for (String row : minterms) {
            for (String header : headers) {
                if (matches(row, header) && !alreadyChosenHeaders.contains(header)) {
                    selectedHeaders.add(header);  // Select this header
                    alreadyChosenHeaders.add(header);  // Mark the header as chosen
                    break;  // Move to the next row after selecting the first matching header
                }
            }
        }

        // Now, only add the selected headers to the final expression
        for (String header : selectedHeaders) {
            finalExpression.add(header);
        }
    }


    // Helper method to convert a header (binary with '-') to an expression
    private static String finalOutput(ArrayList<String> binaries) {
        StringBuilder finalExpression = new StringBuilder();

        for (int i = 0; i < binaries.size(); i++) {
            String binary = binaries.get(i);
            StringBuilder expression = new StringBuilder();
            char variable = 'a'; // Start with 'a' for variable names

            for (int j = 0; j < binary.length(); j++) {
                char bit = binary.charAt(j);
                if (bit == '1') {
                    expression.append(variable); // Append the variable for '1'
                } else if (bit == '0') {
                    expression.append(variable).append("'"); // Append negated variable for '0'
                }
                // Skip '-' (no addition to expression)
                variable++; // Move to the next variable
            }

            // Add the current product term to the final expression
            finalExpression.append(expression);

            // Append '+' if it's not the last term
            if (i < binaries.size() - 1) {
                finalExpression.append(" + ");
            }
        }

        return finalExpression.toString();
    }


    // Method to write the PLA to a file
    public static void writePlaFile(String filePath) throws IOException {
        // Determine the width of the inputs based on the binary string length in finalExpression
        int numInputs = finalExpression.isEmpty() ? 0 : finalExpression.get(0).length();  // Assuming all terms are of equal length

        // Remove duplicates from the ArrayList
        ArrayList<String> expression = new ArrayList<>(new HashSet<>(finalExpression));


        // Open the file for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the number of inputs
            writer.write(".i " + numInputs);
            writer.newLine();

            // Write the number of outputs
            writer.write(".o 1");  // 1 output
            writer.newLine();

            // Write the number of minterms
            writer.write(".m " + expression.size());
            writer.newLine();

            // Write the number of don't-cares
            writer.write(".d " + dontcares.size());
            writer.newLine();

            // Write the minterms
            for (String term : expression) {
                writer.write(term + " 1");  // Each term corresponds to an output of 1
                writer.newLine();
            }

            // Write the don't-care terms
            for (String term : dontcares) {
                writer.write(term + " -");  // Don't-care terms are marked with '-'
                writer.newLine();
            }

            // Close the writer (implicitly done with the try-with-resources)
        }
    }


}
