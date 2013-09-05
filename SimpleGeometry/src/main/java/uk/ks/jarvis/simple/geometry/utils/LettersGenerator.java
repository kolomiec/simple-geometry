package uk.ks.jarvis.simple.geometry.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksk
 * Date: 17.03.13
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class LettersGenerator {
    private static LettersGenerator instance = new LettersGenerator();
    private List<String> englishLowCaseLetters = new ArrayList<String>();
    private List<String> englishUpperCaseLetters = new ArrayList<String>();
    private int upperCaseDigit = 0;
    private int lowCaseDigit = 0;
    private int nextIndexUpperCaseLetter = 0;
    private int nextIndexLowCaseLetter = 0;

    private LettersGenerator() {
        fillLowCaseEnglishLettersList();
        fillUpperCaseEnglishLettersList();
    }

    public synchronized static LettersGenerator getInstance() {
        return instance;
    }

    public String getNextUpperCaseName() {
        String nextName = getNextUpperCaseLetter();
        if (upperCaseDigit > 0) {
            return nextName.concat(Integer.toString(upperCaseDigit));
        }
        return nextName;
    }

    public String getNextLowCaseName() {
        String nextName = getNextLowCaseLetter();
        if (lowCaseDigit > 0) {
            return nextName.concat(Integer.toString(lowCaseDigit));
        }
        return nextName;
    }

    private void fillLowCaseEnglishLettersList() {
        for (int i = 97; i <= 122; i++) {
            englishLowCaseLetters.add(new String(Character.toChars(i)));
        }
    }

    private void fillUpperCaseEnglishLettersList() {
        for (int i = 65; i <= 90; i++) {//97-122
            englishUpperCaseLetters.add(new String(Character.toChars(i)));
        }
    }

    private String getNextUpperCaseLetter() {
        if (nextIndexUpperCaseLetter < englishUpperCaseLetters.size()) {
            return englishUpperCaseLetters.get(nextIndexUpperCaseLetter++);
        } else {
            nextIndexUpperCaseLetter = 0;
            upperCaseDigit++;
            return englishUpperCaseLetters.get(nextIndexUpperCaseLetter++);
        }
    }

    private String getNextLowCaseLetter() {
        if (nextIndexLowCaseLetter < englishLowCaseLetters.size()) {
            return englishLowCaseLetters.get(nextIndexLowCaseLetter++);
        } else {
            nextIndexLowCaseLetter = 0;
            lowCaseDigit++;
            return englishLowCaseLetters.get(nextIndexLowCaseLetter++);
        }
    }
}