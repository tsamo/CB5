/*
 *
 *  The MIT License
 *
 *  Copyright 2018 tsamo.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 * /
 */

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.*;

/**
 * @author tsamo
 */
public class Draw {
    private final static Set<Integer> normalNumbersResults = new LinkedHashSet<>();
    private int jokerResult;
    private static Map<Integer, Integer> numbersStats = new HashMap<>();

    static <K, V extends Comparable<? super V>>
    SortedSet<Map.Entry<K, V>> entriesSortedByValues(@NotNull Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public Set<Integer> getNormalNumbersResults() {
        return normalNumbersResults;
    }

    public int getJokerResult() {
        return jokerResult;
    }

    public void draw() {
        normalNumbersResults.clear();
        SecureRandom r = new SecureRandom();

        while (normalNumbersResults.size() < 5) {
            Integer next = r.nextInt(45) + 1;
            normalNumbersResults.add(next);
        }

        jokerResult = r.nextInt(20) + 1;
    }

    public static void twentyDrawsStats() {
        Draw joker = new Draw();

        for (int i = 1; i <= 45; i++) {
            numbersStats.put(i, 0);
        }
        for (int k = 0; k < 20; k++) {
            joker.draw();

            for (int l = 1; l <= 45; l++) {
                for (int i : joker.getNormalNumbersResults()) {
                    if (l == i) {
                        numbersStats.put(l, numbersStats.get(l) + 1);
                    }
                }
            }
        }

        int n = numbersStats.size();
        Map.Entry sorted[] = new Map.Entry[n];

        int j = 0;
        for (Map.Entry me : entriesSortedByValues(numbersStats)) {
            sorted[j++] = me;
        }

        Set<Integer> temp = new HashSet<>();
        System.out.println("\nAfter 20 draws, the most and least occurences of numbers, are as follows:");

        int count = count(0, "plus", sorted);
        display(count, "min", "", sorted, temp);
        int count2 = count(count, "plus", sorted);
        display(count2, "min", "Second", sorted, temp);
        int count3 = count(count2, "plus", sorted);
        display(count3, "min", "Third", sorted, temp);
        display(sorted.length - 1, "max", "", sorted, temp);
        count = count(sorted.length - 1, "minus", sorted);
        display(count, "max", "Second", sorted, temp);
        count2 = count(count, "minus", sorted);
        display(count2, "max", "Third", sorted, temp);
    }

    public static void display(int i, String s1, String s2, Map.Entry[] sorted, Set<Integer> temp) {
        for (Map.Entry me : sorted) {
            if (sorted[i].getValue() == me.getValue()) {
                temp.add((Integer) me.getKey());
            }
        }
        if (s1.equals("max")) {
            if (s2.equals("")) {
                if (temp.size() == 1) {
                    System.out.println("\n" + s2 + "Most frequent number, with " + sorted[i].getValue() + " occurences is " + temp + ".");
                } else {
                    System.out.println("\n" + s2 + "Most frequent numbers, with " + sorted[i].getValue() + " occurences are " + temp + ".");
                }
            } else {
                if (temp.size() == 1) {
                    System.out.println(s2 + " Most frequent number, with " + sorted[i].getValue() + " occurences is " + temp + ".");
                } else {
                    System.out.println(s2 + " Most frequent numbers, with " + sorted[i].getValue() + " occurences are " + temp + ".");
                }
            }
            temp.clear();
        } else if (s1.equals("min")) {
            if (s2.equals("")) {
                if (temp.size() == 1) {
                    System.out.println("\n" + s2 + "Least frequent number, with " + sorted[i].getValue() + " occurence is " + temp + ".");
                } else {
                    System.out.println("\n" + s2 + "Least frequent numbers, with " + sorted[i].getValue() + " occurence are " + temp + ".");
                }
            } else {
                if (temp.size() == 1) {
                    System.out.println(s2 + " Least frequent number, with " + sorted[i].getValue() + " occurences is " + temp + ".");
                } else {
                    System.out.println(s2 + " Least frequent numbers, with " + sorted[i].getValue() + " occurences are " + temp + ".");
                }
            }
            temp.clear();
        }
    }

    public static int count(int i, String s, Map.Entry[] sorted) {
        int count = i;
        if (s.equals("plus")) {
            while (sorted[i].getValue() == sorted[count].getValue()) {
                count++;
            }
            return count;
        } else
            while (sorted[i].getValue() == sorted[count].getValue()) {
                count--;
            }
        return count;

    }
}
