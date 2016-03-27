package com.dianping.pelican.pagelet;

import com.dianping.pelican.model.Pagelet;

/**
 * Created by liming_liu on 15/10/13.
 */
public final class FooImpl implements Foo {
    public void printFoo() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Pagelet pagelet = new HomePage();
        Pagelet pRef = pagelet;
        pagelet = new HomeFirst();
        System.out.println(pRef);
    }
}
