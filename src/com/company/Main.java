package com.company;

import java.util.Arrays;

public class Main {



    public static void main(String[] args) {

        //Majority.runTest();
        //KMP.runTest();
        //JumpGames.runTestWays();
        //JumpGames.runTestPrice(new int[] {0,1,4,6,2,5,3,9,6,2,2,3,5,4,6,5,4,9});
        //JumpGames.runTestPrice(new int[] {0, 7, 7, 30, 5 ,18, 19, 10});// isMoreThanOneWayExists = true
        LongestCommonSubsequence.runTest();

    }

    /* Dynamic Programming Java implementation of LCS problem */
    public static class LongestCommonSubsequence
    {
        private int matrix[][];
        private static char[] X;
        private static char[] Y;

        /* Returns length of LCS for X[0..m-1], Y[0..n-1] */
        int lcs( char[] X, char[] Y, int m, int n )
        {
            matrix = new int[m+1][n+1];

	/* Following steps build matrix[m+1][n+1] in bottom up fashion. Note
		that matrix[i][j] contains length of LCS of X[0..i-1] and Y[0..j-1] */
            for (int i=0; i<=m; i++)
            {
                for (int j=0; j<=n; j++)
                {
                    if (i == 0 || j == 0)
                        matrix[i][j] = 0;
                    else if (X[i-1] == Y[j-1])
                        matrix[i][j] = matrix[i-1][j-1] + 1;
                    else
                        matrix[i][j] = max(matrix[i-1][j], matrix[i][j-1]);
                }
            }
            return matrix[m][n];
        }

        /* Utility function to get max of 2 integers */
        int max(int a, int b)
        {
            return (a > b)? a : b;
        }

        public static void runTest()
        {
            LongestCommonSubsequence lcs = new LongestCommonSubsequence();
            String x = "AGGTAB";
            String y = "GXTXAYB";

            X = x.toCharArray();
            Y = y.toCharArray();
            int m = X.length;
            int n = Y.length;

            System.out.println("Length of LCS  for '" + x +"' '" + y + "' is " + lcs.lcs( X, Y, m, n ));
            System.out.println();
            lcs.printMatrix();
            System.out.println();
            lcs.printLCS();
        }

        public void printMatrix() {
            if (matrix == null)
                return;
            System.out.print(" ");
            for (char aY : Y)
                System.out.print(" " + aY);
            System.out.println();
            for (int row = 0; row < matrix.length; row++) { // column length

                for (int column = 0; column < matrix[row].length; column++) { //row length
                    if (column == 0)
                        if (row < X.length)
                            System.out.print(X[row]+" ");
                        else if (row == X.length)
                            System.out.print("  ");
                    System.out.print(matrix[row][column] + " ");
                }
                System.out.println();
            }
        }

        public void printLCS() {
            int row=X.length;
            int column=Y.length;
            int lcsLength = matrix[row][column];
            char[] out = new char[lcsLength];

            while (matrix[row][column] > 0) {
                if (matrix[row][column] != matrix[row-1][column-1]) {
                    out[lcsLength-1] = X[row-1];
                    lcsLength--;
                    row--;
                    column--;
                } else if (matrix[row-1][column] == matrix[row][column])
                    row--; // go up
                else
                    column--; // go left
            }
            System.out.print("LCS string is: ");
            for (char c: out)
                System.out.print(c);
        }
    }

    static class JumpGames {

        static class PriceForPath {
            int[] summedPricePerEachLevelArr;
            boolean isMoreThanOneWayExists = false;

            @Override
            public String toString() {
                if (summedPricePerEachLevelArr != null) {
                    return "summedPricePerEachLevelArr: " + Arrays.toString(summedPricePerEachLevelArr) + "\n" +
                            "isMoreThanOneWayExists: " + isMoreThanOneWayExists;
                } else
                    return super.toString();
            }
        }

        static void runTestPrice(int[] pricesPerLevel) {
            System.out.println("calculatePricePerEachLevel: \n" +
                    "input - pricesPerLevel: "+ Arrays.toString(pricesPerLevel)+ "\n" +
                    calculatePricePerEachLevel(pricesPerLevel,2));
        }

        static PriceForPath calculatePricePerEachLevel(int[] pricesPerLevel, int singleJumpMax) {
            int lowestLevelPrice;
            int currentLevelPrice;
            int candidatePriceForLevel;
            int totalLevels = pricesPerLevel.length-1;
            PriceForPath pfp = new PriceForPath();

            pfp.summedPricePerEachLevelArr = new int[totalLevels+1];
            pfp.summedPricePerEachLevelArr[0] = pricesPerLevel[0];
            pfp.summedPricePerEachLevelArr[1] = pricesPerLevel[1];

            for (int level = 2; level <= totalLevels; level++) {
                currentLevelPrice = pricesPerLevel[level];
                lowestLevelPrice = Integer.MAX_VALUE;
                for (int j=1; j<=level && j<=singleJumpMax; j++) {
                    candidatePriceForLevel = currentLevelPrice + pfp.summedPricePerEachLevelArr[level-j];
                    if (candidatePriceForLevel < lowestLevelPrice)
                        lowestLevelPrice = candidatePriceForLevel;
                    else if (candidatePriceForLevel == lowestLevelPrice)
                        pfp.isMoreThanOneWayExists = true;
                }
                pfp.summedPricePerEachLevelArr[level] = lowestLevelPrice;
            }
            return pfp;
        }

        static void runTestWays() {
            System.out.println("countWaysToReachEachLevel level 4, with maximum jump of 2 is:   "
                    + countWaysToReachEachLevel(4,2));
        }

        private static int countWaysToReachEachLevel(int totalLevels, int singleJumpMax) {
            int res[] = new int[totalLevels+1];
            res[0] = 1; res[1] = 1;
            for (int currentLevel=2; currentLevel<=totalLevels; currentLevel++)
            {
                res[currentLevel] = 0;
                for (int j=1; j<=singleJumpMax && j<=currentLevel; j++)
                    // j<=singleJumpMax - because the 2 previous cells (res[currentLevel-1], res[currentLevel-2]) do describe ALL of the ways to reach them.
                    // j<=currentLevel, consider when singleJumpMax is higher then currentLevel
                    res[currentLevel] += res[currentLevel-j];
            }
            return res[totalLevels];
        }
    }

    static class KMP {

        public static void runTest() {
            String[] contentsMtrx = {
                    "dgakbfcfdhfjchehkcggegjcckefakjbkicgbkhekjagbkbhhjfadgjgeichadfcfidbbgikajckjfebigddiiekadfbkadjfjkcegciegckiakjccjkebiahcfdfigahghaibfghhgcdhfjkfheafibdjbbifaiadihhdijdbfdgjdfbajjcgcffjcgjegbbbhjgcbkbcjeekdecahiedkedkjhdcfhkjgcicefhfbcfjfbigbfdkcgbiekdgbghcjgbejahakfadchckjdcbhkdkkgcbcihigefiafkeej"
            };
            String pattern1 = "ababaca";

            for(String s : contentsMtrx) {
                System.out.println("pattern: "+pattern1);
                System.out.println("content: "+s);
                hasMatches(pattern1.toCharArray(), s.toCharArray());
                System.out.println();
            }
        }

        /**
         * longest prefix which is also suffix.
         * @param pat pattern
         * @return lps array. <br>
         * each cell value at the array indicates the length of
         * : how much chars in the suffix are equal to the prefix, relative to this index.
         */
        static int[] getLPSFor(char[] pat) {

            /*
             * ji
             * ↓↓
             * abcdaaa
             */

            int j = 0;
            int i = 1;
            int[] lps = new int[pat.length];

            lps[0] = 0;
            while (i < pat.length) {
                if (pat[i] == pat[j]) {
                    j++;
                    lps[i] = j;
                    i++;
                } else {
                    // This is tricky. Consider the example.
                    // AAACAAAA and i = 7. The idea is similar
                    // to search step.
                    if (j != 0) {
                        j = lps[j - 1];
                        // Also, note that we do not increment i here
                    } else {
                        lps[i] = 0;
                        i++;
                    }
                }
            }
            return lps;
        }

        public static void hasMatches(char[] pattern, char[] content) {
            // create lps[] that will hold the longest
            // prefix suffix values for pattern
            int lps[] = getLPSFor(pattern);
            System.out.println("lps is: "+Arrays.toString(lps).replaceAll("]", "").replaceAll("\\[", "").replaceAll(",", "").replaceAll(" ",""));
            int patIndex = 0; // index for pattern[]
            int contentIndex = 0; // index for content[]
            while (contentIndex < content.length) {
                if (pattern[patIndex] == content[contentIndex]) {
                    patIndex++;
                    contentIndex++;
                }
                if (patIndex == pattern.length) {
                    System.out.println("Found pattern "
                            + "at index " + (contentIndex - patIndex));
                    patIndex = lps[patIndex - 1];
                }
                // mismatch after contentIndex matches
                else if (contentIndex < content.length && pattern[patIndex] != content[contentIndex]) {
                    // Do not match lps[0..lps[j-1]] characters,
                    // they will match anyway
                    if (patIndex != 0)
                        patIndex = lps[patIndex - 1];
                    else
                        contentIndex = contentIndex + 1;
                }
            }
        }
    }

    static class Majority {
        private static void runTest() {
            int[][] numsMatrix = {
                    {34,15,34,34,34,34,15,15,34,34,22,15,15,15,15,34,15,34,15,15,34,15,34,15,34,22,22,15,34,15,34,15,34,15,34,22,34,22,34,34,34,34,34,22,15,34,34,34,15,34,15,15,22,34,15,15,34,34,34,22,34,15,15,34,34,34,15,22,22,22,15,34,34,22,34,34,22,34,15,22,34,34,15,22,34,34,34,34,22,22,15,34,34,22,34,34,34,22,34,22}
            };
            for (int[] numsArr : numsMatrix)
                getMajorityInArray(numsArr);
        }

        private static int getMajorityInArray(int[] numsArr) {
            int candidateIndex = findCandidate(numsArr);
            int candidate = numsArr[candidateIndex];
            if (isMajority(candidateIndex, numsArr)) {
                System.out.println("Majority number is: " + candidate);
                return candidate;
            } else
                System.out.println("No majority was found");
            return -1;
        }

        private static boolean isMajority(int candidateIndex, int[] numsArr) {
            int amount = 0;
            int candidate = numsArr[candidateIndex];
            for (int number : numsArr) {
                if (number == candidate)
                    amount++;
            }
            return amount>(numsArr.length/2);
        }

        private static int findCandidate(int[] numsArr) {
            if (numsArr == null || numsArr.length < 1)
                return -1;
            if (numsArr.length == 1)
                return 0;

            int voted = 1;
            int candidateIndex = 0;

            for (int i =1; i<numsArr.length ; i++) {
                if (numsArr[candidateIndex] == numsArr[i])
                    voted++;
                else
                    voted--;

                if(voted == 0){
                    candidateIndex = i;
                    voted = 1;
                }
            }
            return candidateIndex;
        }
    }
}
