package com.company;

import java.util.EmptyStackException;
import java.util.Stack;

public class Stackk {
        public static Object evaluate(String expression) {
            char[] curValue = expression.toCharArray();
            boolean raisedExcept = false;
            Stack<Integer> val = new Stack<>();
            Stack<Character> operators = new Stack<>();

            for (int i = 0; i < curValue.length; i++)
            {
                if (curValue[i] == ' ')
                    continue;
                if (curValue[i] >= '0' && curValue[i] <= '9')
                {
                    StringBuffer sbuf = new StringBuffer();
                    while (i < curValue.length && curValue[i] >= '0' && curValue[i] <= '9')
                        sbuf.append(curValue[i++]);
                    val.push(Integer.parseInt(sbuf.toString()));
                }

                else if (curValue[i] == '(')
                    operators.push(curValue[i]);

                else if (curValue[i] == ')')
                {
                    try{
                        while (operators.peek() != '(')
                            val.push(applyOp(operators.pop(), val.pop(), val.pop()));
                        operators.pop();
                    } catch (EmptyStackException ignored){
                        raisedExcept = true;
                        break;
                    }
                }

                else if (curValue[i] == '+' || curValue[i] == '-' ||
                        curValue[i] == '*' || curValue[i] == '/')
                {

                    while (!operators.empty() && hasPrecedence(curValue[i], operators.peek()))
                        val.push(applyOp(operators.pop(), val.pop(), val.pop()));

                    operators.push(curValue[i]);
                }
            }

            while (!operators.empty()){
                try{
                    val.push(applyOp(operators.pop(), val.pop(), val.pop()));
                } catch (EmptyStackException ignored){
                    raisedExcept = true;
                    break;
                }
            }

            if (val.isEmpty()){
                return "Invalid expression";
            } else if (raisedExcept){
                return "Invalid expression";
            }
            else {
                return val.pop();
            }
        }

        public static boolean hasPrecedence(char op1, char op2)
        {
            if (op2 == '(' || op2 == ')')
                return false;
            if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
                return false;
            else
                return true;
        }

        public static int applyOp(char op, int b, int a)
        {
            switch (op)
            {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                    if (b == 0)
                        throw new
                                UnsupportedOperationException("Cannot divide by zero");
                    return a / b;
            }
            return 0;
        }
    }
