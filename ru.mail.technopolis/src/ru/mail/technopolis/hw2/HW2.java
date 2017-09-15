package ru.mail.technopolis.hw2;

import java.util.Scanner;

public class HW2 {

    interface Expression {
        double evaluate(double value);
    }

    private class Variable implements Expression {

        @Override
        public double evaluate(double value) {
            return value;
        }
    }

    private class Const implements Expression {
        private double value;

        Const(double value) {
            this.value = value;
        }

        @Override
        public double evaluate(double value) {
            return this.value;
        }
    }

    private abstract class BinaryOperator implements Expression {
        Expression first;
        Expression second;

        BinaryOperator(Expression first, Expression second) {
            this.first = first;
            this.second = second;
        }
    }

    private class Add extends BinaryOperator {

        Add(Expression first, Expression second) {
            super(first, second);
        }

        @Override
        public double evaluate(double value) {
            return first.evaluate(value) + second.evaluate(value);
        }
    }

    private class Minus extends BinaryOperator {

        Minus(Expression first, Expression second) {
            super(first, second);
        }

        @Override
        public double evaluate(double value) {
            return first.evaluate(value) - second.evaluate(value);
        }
    }

    private class Divide extends BinaryOperator {

        Divide(Expression first, Expression second) {
            super(first, second);
        }

        @Override
        public double evaluate(double value) throws DontDivideByZero {
            if (second.evaluate(value) == 0)
                throw new DontDivideByZero("Деление на ноль");
            return first.evaluate(value) / second.evaluate(value);
        }
    }

    private class Multiply extends BinaryOperator {

        Multiply(Expression first, Expression second) {
            super(first, second);
        }

        @Override
        public double evaluate(double value) {
            return first.evaluate(value) * second.evaluate(value);
        }
    }

    class ExpressionCalculator implements Expression {
        private final String[] OPERATIONS = {"-", "+", "*", "/"};
        String token = "";
        Scanner scanner;

        ExpressionCalculator(String string) {
            scanner = new Scanner(string);
        }

        @Override
        public double evaluate(double value) {
            double result = 0;
            try {
                next();
                result = expression().evaluate(value);
            } catch (IncorrectText incorrectText) {
                System.out.println("Некорректная запись");
            } catch (DontDivideByZero dontDivideByZero) {
                System.out.println("Деление на ноль");
            } finally {
                scanner.close();
            }
            return result;
        }

        private Expression expression() {
            Expression result = term();
            while (isAdd(token) || isMinus(token)) {
                if (isAdd(token)) {
                    next();
                    return new Add(result, expression());
                }
                if (isMinus(token)) {
                    next();
                    return new Minus(result, expression());
                }
            }
            return result;
        }

        private Expression term() {
            Expression result = factor();
            while (isDivide(token) || isMultiply(token)) {
                if (isDivide(token)) {
                    next();
                    return new Divide(result, term());
                }
                if (isMultiply(token)) {
                    next();
                    return new Multiply(result, term());
                }
            }
            return result;
        }

        private Expression factor() throws IncorrectText {
            if (isNumber(token)) {
                Expression result = new Const(Integer.parseInt(token));
                next();
                return result;
            } else if (isX(token)) {
                Expression result = new Variable();
                next();
                return result;
            } else if (isMinus(token)) {
                next();
                return new Minus(new Const(0), factor());
            } else if (isLBracket(token)) {
                next();
                Expression result = expression();
                if (!isRBracket(token))
                    throw new IncorrectText("Некорректная запись");
                next();
                return result;
            } else throw new IncorrectText("Некорректная запись");
        }

        private void next() throws IncorrectText {
            if (scanner.hasNext())
                token = scanner.next();
            if (!isCorrect(token)) {
                throw new IncorrectText("Некорректная запись");
            }
        }

        private boolean isX(String s) {
            return "x".equals(s);
        }

        private boolean isNumber(String s) {
            return s.matches("[-+]?\\d+");
        }

        private boolean isAdd(String s) {
            return "+".equals(s);
        }

        private boolean isMinus(String s) {
            return "-".equals(s);
        }

        private boolean isMultiply(String s) {
            return "*".equals(s);
        }

        private boolean isDivide(String s) {
            return "/".equals(s);
        }

        private boolean isOperation(String s) {
            for (String OPERATION : OPERATIONS) {
                if (s.equals(OPERATION))
                    return true;
            }
            return false;
        }

        private boolean isLBracket(String s) {
            return "(".equals(s);
        }

        private boolean isRBracket(String s) {
            return ")".equals(s);
        }

        private boolean isCorrect(String s) {
            return s.matches("[-+]?\\d+") || isOperation(s) || s.equals("(") || s.equals(")") || s.equals("x");
        }

    }

    private void run() {

        Expression test1 = new ExpressionCalculator("5 + x");
        System.out.println(test1.evaluate(4)); // Output: 9

        Expression test2 = new ExpressionCalculator("( 5 / 3 - x / 3 ) * 7");
        System.out.println(test2.evaluate(2)); // Output: 7

        Expression test3 = new ExpressionCalculator("- 5 + ( 4 * ( -3 ) ) ");
        System.out.println(test3.evaluate(1)); // Output: -17

        Expression test4 = new ExpressionCalculator("( x )");
        System.out.println(test4.evaluate(0)); // Output: 0

        Expression test5 = new ExpressionCalculator("- 5 + ) (");
        System.out.println(test5.evaluate(5)); // Output: -

        Expression test6 = new ExpressionCalculator("5 / x + 8 - ( 8 )");
        System.out.println(test6.evaluate(0)); // Output: -

        Expression test7 = new ExpressionCalculator("5 / x + 8 - ( 8 )");
        System.out.println(test7.evaluate(2)); // Output: 2,5
    }


    public static void main(String[] args) {
        new HW2().run();
    }

}
