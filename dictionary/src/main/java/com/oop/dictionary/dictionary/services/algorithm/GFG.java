package com.oop.dictionary.dictionary.services.algorithm;
// hamming distance

public class GFG {
    // function to calculate Hamming distance
    public static int hammingDist(String str1, String str2)
    {
        if(str2 == null || str2.isEmpty()){
            return 100;
        }

        int i = 0, count = 0;
        int lengthI = str1.length() < str2.length() ? str1.length() : str2.length();
        int subLength = Math.abs(str1.length() - str2.length());
        while (i < lengthI)
        {
            if (str1.charAt(i) != str2.charAt(i))
                count++;
            i++;
        }
        count = count + subLength;
        return count;
    }

}
