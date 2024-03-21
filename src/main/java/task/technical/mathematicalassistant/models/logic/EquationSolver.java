package task.technical.mathematicalassistant.models.logic;

import org.mariuszgromada.math.mxparser.Expression;

public class EquationSolver {
    public boolean isRootValidForEquation(String equation, String root) {
        Expression expression = new Expression(equation.replaceAll("x", root));
        double result = expression.calculate();
        return result != 0;
    }
}

