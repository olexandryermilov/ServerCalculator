package server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

public class Calculator {
    private int countActions,countOperands;
    private BigDecimal minNum,maxNum,sum;
    public Calculator(){
        countActions=0;
        countOperands=0;
        minNum=null;
        maxNum=null;
        sum=BigDecimal.ZERO;
    }

    public int getCountOperands() {
        return countOperands;
    }

    public int getCountActions() {
        return countActions;
    }

    public BigDecimal getMinNum() {
        return minNum;
    }

    public BigDecimal getMaxNum() {
        return maxNum;
    }

    public BigDecimal getSum() {
        return sum;
    }

    private BigDecimal getMinBigDecimal(BigDecimal a, BigDecimal b){
        if(a.compareTo(b)==-1)return a;
        return b;
    }
    private static BigDecimal getMaxBigDecimal(BigDecimal a, BigDecimal b){
        if(a.compareTo(b)==-1)return b;
        return a;
    }

    private static Character getArithmeticOperation(String argument){
        if(argument.equals("/"))return '/';
        if(argument.equals("*"))return '*';
        if(argument.equals("-"))return '-';
        if(argument.equals("+"))return '+';
        return null;
    }
    private BigDecimal getNumber(String argument){
        try{
            BigDecimal num = new BigDecimal(argument);
            return num;
        }catch (RuntimeException e){
            return null;
        }
    }
    private void updateMinMaxSum(BigDecimal first, BigDecimal second){
        if(minNum==null){
            minNum=getMinBigDecimal(first,second);
        }
        else
        {
            minNum=getMinBigDecimal(minNum,getMinBigDecimal(first,second));
        }
        if(maxNum==null){
            maxNum=getMaxBigDecimal(first,second);
        }
        else
        {
            maxNum=getMaxBigDecimal(maxNum,getMaxBigDecimal(first,second));
        }
        sum=sum.add(first);
        sum=sum.add(second);
    }

    public String parse(String rpnString){
        String[] s = rpnString.split(" ");
        //if(s.length==3)return calculateExpression(rpnString);
        if(s.length<3)return "Wrong expression: should be  operand operand {+,-,*,/}";
        Stack<BigDecimal> st = new Stack<>();
        for(int i=0;i<s.length;i++)
        {
            if(s[i].equals("NaN"))
            {
                throw new ArithmeticException();
            }
            BigDecimal num = getNumber(s[i]);
            if(num!=null)
            {
                st.push(num);
                updateMinMaxSum(num,num);
                countOperands++;
            }
            else
            {
                Character op = getArithmeticOperation(s[i]);
                if(op!=null)
                {
                    if(st.size()<2)return "Wrong expression: should be operand operand {+,-,*,/} ";
                    BigDecimal a=st.pop();
                    BigDecimal b=st.pop();
                    if(a.equals(BigDecimal.ZERO)&&op=='/')return "Error: Division by zero";
                    switch (op)
                    {
                        case '+':st.push(b.add(a));break;
                        case '-':st.push(b.subtract(a));break;
                        case '*':st.push(b.multiply(a));break;
                        case '/':st.push(b.divide(a,10, RoundingMode.UP));break;
                    }
                }
                else
                {
                    return "Wrong expression: should be  operand operand {+,-,*,/}";
                }
            }
        }
        if(st.size()!=1)return "Wrong expression: should be  operand operand {+,-,*,/}";
        countActions++;
        BigDecimal res=st.pop();
        return res.toString();

    }
}
