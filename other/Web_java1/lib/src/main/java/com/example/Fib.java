package com.example;

import java.util.*;

public class Fib{
    public static void main(String args[]){
        long fib0=0,fib1=1,fib2=1;
        ArrayList<Long>fibs=new ArrayList<>();
        fibs.add(fib0);
        fibs.add(fib1);
        long tmp=fib2;
        while(tmp<Long.MAX_VALUE&&tmp >= 0){ //计算Fib
            fibs.add(tmp);
            tmp=fibs.get(fibs.size()-1)+fibs.get(fibs.size()-2);
        }
        System.out.println("max fib(long):"+fibs.get(fibs.size()-1)+ " count:"+fibs.size());

        double ratios[]=new double[fibs.size()-1];
        Iterator iterator = fibs.iterator();
        long pre=(long)iterator.next();
        int i=0;
        while(iterator.hasNext()){  //求出比值
            long now=(long)iterator.next();
            ratios[i++]=(double) pre/now;
            pre=now;
        }
        int count=0;
        for (double r:ratios) {
            if((count++)%5==4)
                System.out.println(r);
            else System.out.print(r+"\t");
        }
    }
}

