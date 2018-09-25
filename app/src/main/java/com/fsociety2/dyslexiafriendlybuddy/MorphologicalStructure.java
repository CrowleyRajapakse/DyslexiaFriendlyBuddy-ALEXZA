package com.fsociety2.dyslexiafriendlybuddy;
import java.util.List;
import java.util.ArrayList;

public class MorphologicalStructure
{
    public static String Morphlogical(String text) {


        String word = text.trim();
        System.out.println("1 " + word);
        int wordSize = word.length();
        char array[] = word.toCharArray();
        String blop = "";
        List<String> a = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        int n = 0;

        for (int i = 0; i < array.length; i++) {
            if ((array[i] == 'a') | (array[i] == 'e') | (array[i] == 'i') | (array[i] == 'o') | (array[i] == 'u')) {
                // int index=i;
                indexes.add(i);
                n = n + 1;

                System.out.println("2: " + i);
                System.out.println(array[i]);


            }
        }
            if (indexes.size() != 0) {
                int[] inarray = new int[indexes.size()];
                int start = 0;
                for (int i = 0; i < indexes.size(); i++) {

                    inarray[i] = indexes.get(i);
                    String sub = word.substring(start, inarray[i] + 1);
                    a.add(sub + "-");
                    start = inarray[i] + 1;
                }

                String[] x = a.toArray(new String[a.size()]);
                String output = "";
                for (String str : x)
                    output = output + str;
                int num = output.length();
                System.out.println(num);

                if (output.charAt(num - 1) == '-') ;
                String newout = output.substring(0, num - 1);


                if (word.charAt(wordSize - 1) != newout.charAt(newout.length() - 1)) {

                    if (word.charAt(wordSize - 2) == newout.charAt(newout.length() - 1)) {
                        blop = newout + "-" + word.charAt(wordSize - 1);
                    } else if (word.charAt(wordSize - 3) == newout.charAt(newout.length() - 1)) {
                        blop = newout + "-" + word.charAt(wordSize - 2) + "-" + word.charAt(wordSize - 1);
                    } else if (word.charAt(wordSize - 4) == newout.charAt(newout.length() - 1)) {
                        blop = newout + "-" + word.charAt(wordSize - 3) + word.charAt(wordSize - 2) + word.charAt(wordSize - 1);
                    }
                } else
                    blop = newout;
                return blop;
            } else {
                blop = word;
                return blop;
            }

        }
    }


