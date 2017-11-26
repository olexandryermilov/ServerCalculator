package servercalculatortest.servertest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import server.Calculator;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class CalculatorTest {
    private static Calculator calculator;
    @BeforeClass
    public static void initCalculator(){
        calculator=new Calculator();
    }
    private boolean equalBigDecimal(BigDecimal a, BigDecimal b){
        BigDecimal sub = a.subtract(b);
        if(sub.compareTo(BigDecimal.ZERO)<0){
            sub=sub.multiply(new BigDecimal(-1));
        }
        final BigDecimal EPS = new BigDecimal(0.001);
        return sub.compareTo(EPS)<0;
    }
    @Test
    public void calculateRPNString_calculatesSimpleExpression(){
        final String exp = "3 5 +";
        final BigDecimal rightAnswer = new BigDecimal(8.0);
        BigDecimal answer =new BigDecimal(calculator.calculateRPNString(exp.split(" ")));
        assertTrue(equalBigDecimal(rightAnswer,answer));
    }
    @Test
    public void calculate_calculatesSimpleExpression(){
        final String exp = "3 + 5";
        final BigDecimal rightAnswer = new BigDecimal(8.0);
        BigDecimal answer =new BigDecimal(calculator.calculate(exp));
        assertTrue(equalBigDecimal(rightAnswer,answer));
    }
    @Test
    public void calculateRPNString_calculatesMinusExpression(){
        final String exp = "-3 5 -";
        final BigDecimal rightAnswer = new BigDecimal(-8.0);
        BigDecimal answer =new BigDecimal(calculator.calculateRPNString(exp.split(" ")));
        assertTrue(equalBigDecimal(rightAnswer,answer));
    }
    @Test
    public void calculate_calculatesMinusExpression(){
        final String exp = "-3 - 5";
        final BigDecimal rightAnswer = new BigDecimal(-8.0);
        BigDecimal answer =new BigDecimal(calculator.calculate(exp));
        assertTrue(equalBigDecimal(rightAnswer,answer));
    }

    @Test
    public void calculateRPNString_calculatesBigExpression(){
        final String exp = "15 7 1 1 + - / 3 * 2 1 1 + + -";
        final BigDecimal rightAnswer = new BigDecimal(5.0);
        BigDecimal answer =new BigDecimal(calculator.calculateRPNString(exp.split(" ")));
        assertTrue(equalBigDecimal(rightAnswer,answer));
    }
    @Test
    public void calculate_calculatesBigExpression(){
        final String exp = "( ( 15 / ( 7 - ( 1 + 1 ) ) ) * 3 ) - ( 2 + ( 1 + 1 ) )";
        final BigDecimal rightAnswer = new BigDecimal(5.0);
        BigDecimal answer =new BigDecimal(calculator.calculate(exp));
        assertTrue(equalBigDecimal(rightAnswer,answer));
    }

    @Test
    public void calculateRPNString_givesError(){
        final String exp = "15 7 1 1 + - / 3 * 2 1 1 + + -";
        final BigDecimal rightAnswer = new BigDecimal(5.0);
        BigDecimal answer =new BigDecimal(calculator.calculateRPNString(exp.split(" ")));
        assertTrue(equalBigDecimal(rightAnswer,answer));
    }
    @Test
    public void calculate_givesError(){
        final String exp = "( ( 15 / ( 7 - ( 1 + 1 ) ) ) * 3 ) - ( 2 + ( 1 + 1 ) )";
        final BigDecimal rightAnswer = new BigDecimal(5.0);
        BigDecimal answer =new BigDecimal(calculator.calculate(exp));
        assertTrue(equalBigDecimal(rightAnswer,answer));
    }
}
