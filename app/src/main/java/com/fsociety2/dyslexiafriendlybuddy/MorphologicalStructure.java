package com.fsociety2.dyslexiafriendlybuddy;

import java.util.List;
import java.util.ArrayList;

public class MorphologicalStructure {
    public static String Morphlogical(String text) {

        String word = text.trim();
        System.out.println("1 " + word);
        int wordSize = word.length();
        char splitArray[] = word.toCharArray();
        String finalOutput = "";
        List<String> middleArray = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        String newOutput = "";
        int incrementCount = 0;

        for (int i = 0; i < splitArray.length; i++) {
            if ((splitArray[i] == 'a') | (splitArray[i] == 'e') | (splitArray[i] == 'i') | (splitArray[i] == 'o') | (splitArray[i] == 'u')) {

                indexes.add(i);
                incrementCount = incrementCount + 1;
                System.out.println("2: " + i);
                System.out.println(splitArray[i]);


            }
        }
        if (indexes.size() != 0) {
            int[] insideArray = new int[indexes.size()];
            int start = 0;
            for (int i = 0; i < indexes.size(); i++) {

                insideArray[i] = indexes.get(i);
                String sub = word.substring(start, insideArray[i] + 1);
                middleArray.add(sub + "-");
                start = insideArray[i] + 1;
            }

            String[] x = middleArray.toArray(new String[middleArray.size()]);
            String output = "";
            for (String str : x)
                output = output + str;
            int outLength = output.length();
            System.out.println(output);
            System.out.println(outLength);

            if (output.charAt(outLength - 1) == '-') {
                newOutput = output.substring(0, outLength - 1);
                System.out.println("new: " + newOutput);


                if (word.charAt(wordSize - 1) != newOutput.charAt(newOutput.length() - 1)) {

                    if (word.charAt(wordSize - 2) == newOutput.charAt(newOutput.length() - 1)) {
                        finalOutput = newOutput + "-" + word.charAt(wordSize - 1);

                    } else if (word.charAt(wordSize - 3) == newOutput.charAt(newOutput.length() - 1))

                    {
                        finalOutput = newOutput + "-" + word.charAt(wordSize - 2) + word.charAt(wordSize - 1);
                    } else if (word.charAt(wordSize - 4) == newOutput.charAt(newOutput.length() - 1)) {
                        finalOutput = newOutput + "-" + word.charAt(wordSize - 3) + word.charAt(wordSize - 2) + word.charAt(wordSize - 1);
                    } else if (word.charAt(wordSize - 5) == newOutput.charAt(newOutput.length() - 1)) {
                        finalOutput = newOutput + "-" + word.charAt(wordSize - 4) + word.charAt(wordSize - 3) + word.charAt(wordSize - 2) + word.charAt(wordSize - 1);
                    }

                    System.out.println("finalOutput: " + finalOutput);
                    return finalOutput;
                } else {
                    finalOutput = newOutput;
                    return finalOutput;

                }

            }

        } else {
            finalOutput = word;
            return finalOutput;

        }
        return finalOutput;
    }
}


