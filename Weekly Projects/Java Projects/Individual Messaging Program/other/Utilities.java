package other;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tsamo
 */
public class Utilities {
    private static final HashMap<String, String> sqlTokens;
    private static Pattern sqlTokenPattern;

    static {
        String[][] search_regex_replacement = new String[][]
                {
                        //search string     search regex        sql replacement regex
                        {"\u0000", "\\x00", "\\\\0"},
                        {"'", "'", "\\\\'"},
                        {"\"", "\"", "\\\\\""},
                        {"\b", "\\x08", "\\\\b"},
                        {"\n", "\\n", "\\\\n"},
                        {"\r", "\\r", "\\\\r"},
                        {"\t", "\\t", "\\\\t"},
                        {"\u001A", "\\x1A", "\\\\Z"},
                        {"\\", "\\\\", "\\\\\\\\"}
                };

        sqlTokens = new HashMap<>();
        String patternStr = "";
        for (String[] srr : search_regex_replacement) {
            sqlTokens.put(srr[0], srr[2]);
            patternStr += (patternStr.isEmpty() ? "" : "|") + srr[1];
        }
        sqlTokenPattern = Pattern.compile('(' + patternStr + ')');
    }

    public static String checkUsernameOrPasswordLength(Scanner sc, String input, String type) {
        while (input.length() > 45 || input.length() < 5) {
            System.out.println("Error. " + type + "s must have at least 5 and at most 45 characters. Please try again.");
            System.out.print(type + " :> ");
            input = sc.nextLine();
        }
        return input;
    }

    public static String escapeSQL(String s) {
        Matcher matcher = sqlTokenPattern.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, sqlTokens.get(matcher.group(1)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        if (str.isEmpty()) {
            return false;
        }
        int length = str.length();
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static String formatMessageData(String data) {
        String[] words = data.split(" ");
        StringBuilder returnedData = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if ((i > 0) && (0 == (i % 12))) {
                returnedData.append('\n');
            }

            returnedData.append(words[i]);

            if (i != (words.length - 1)) {
                returnedData.append(' ');
            }
        }
        String returnedString = String.valueOf(returnedData);
        return returnedString;
    }

    public static String checkMessageDataLength(Scanner sc, String message) {
        while (message.length() > 250) {
            System.out.println("Error. Message could not be sent as it exceeds the limit of 250 characters.Please try again.");
            System.out.println("You message cannot exceed 250 characters.");
            System.out.print("Your message:> ");
            message = sc.nextLine();
        }
        return message;
    }
}
