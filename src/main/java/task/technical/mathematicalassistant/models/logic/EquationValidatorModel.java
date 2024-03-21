package task.technical.mathematicalassistant.models.logic;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationValidatorModel {

    private final String ERROR_MESSAGE_PATTERN_INCORRECTLY_PLACED = "The '%s' cannot be before '%s'.";
    private final String ERROR_MESSAGE_PATTERN_START_WITH = "The %s can not start with %s.";
    private final String ERROR_MESSAGE_PATTERN_CANNOT_CONTAIN = "The equation can not contain %s.";

    private final String ERROR_MESSAGE_UNEQUAL_NUMBER_OF_PARENTHESES = "Unequal number of opening and closing parentheses.";
    private final String ERROR_MESSAGE_CONTAIN_VALID_CHARS = "The equation should contain only numbers and correct characters: \".=-+x/*()\".";

    @Getter
    private final String equation;

    public EquationValidatorModel(String equation) {
        this.equation = equation.trim().toLowerCase();
    }

    private char[] splitToChars(String text) {
        return text.replaceAll("\\s", "").toCharArray();
    }

    private boolean matcher(String regex, String checkingString) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(checkingString);
        return matcher.find();
    }

    public EquationValidatorModel checkContainsOnlyValidCharacters() throws IllegalArgumentException {
        String regexContainOnlyValidCharacters = "^[\\d.+\\-*/x()= ]*$";
        if (!equation.matches(regexContainOnlyValidCharacters)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_CONTAIN_VALID_CHARS);
        }
        return this;
    }

    public EquationValidatorModel checkParentheses() throws IllegalArgumentException {

        if (!isEqualNumberOfBracket()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_UNEQUAL_NUMBER_OF_PARENTHESES);
        }

        if (isCloseBracketGoesFirst()) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_PATTERN_START_WITH,"parentheses" ,')'));
        }

        char previousChar = ' ';
        for (char c : splitToChars(equation)) {
            if (c == '(') {
                if (previousChar == 'x' || previousChar == '.') {
                    throw new IllegalArgumentException(String.format(ERROR_MESSAGE_PATTERN_INCORRECTLY_PLACED, previousChar, c));
                }
            } else if (c == ')') {
                if (previousChar == '.' || previousChar == '=' ||
                        previousChar == '-' || previousChar == '+' ||
                        previousChar == '*' || previousChar == '(' ||
                        previousChar == '/') {
                    throw new IllegalArgumentException(String.format(ERROR_MESSAGE_PATTERN_INCORRECTLY_PLACED, previousChar, c));
                }
            }
            previousChar = c;
        }

        return this;
    }

    private boolean isCloseBracketGoesFirst() {

        int openBracket = equation.indexOf('(');
        int closeBracket = equation.indexOf(')');

        return openBracket >= 0 && openBracket > closeBracket;
    }

    private boolean isEqualNumberOfBracket() {

        int bracketCount = 0;
        for (char c : splitToChars(equation)) {
            if (c == '(') {
                bracketCount++;
            } else if (c == ')') {
                bracketCount--;
            }
        }

        return bracketCount == 0;
    }

    public EquationValidatorModel checkExpression() throws IllegalArgumentException {
        String regexStartWith = "^[\\.\\+\\*\\/=]";
        if (matcher(regexStartWith, equation)) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_PATTERN_START_WITH,"equation", "\".=+*/\""));
        }

        String regexSearchDoubleCharacters = "(\\.{2,})|(={2,})|(\\+{2,})|(x{2,})|(\\*{2,})";
        if (matcher(regexSearchDoubleCharacters, equation)) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_PATTERN_CANNOT_CONTAIN, "two or more characters in a row: \".=+x*/\""));
        }

        String regexSearchInvalidCombinationsOfOperandCharacters = "[-+*/][+*/=]";
        if (matcher(regexSearchInvalidCombinationsOfOperandCharacters, equation)) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_PATTERN_CANNOT_CONTAIN, "operand combination with such characters: \".=+x*\""));
        }
        return this;
    }
}