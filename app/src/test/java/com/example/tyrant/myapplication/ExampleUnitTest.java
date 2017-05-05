package com.example.tyrant.myapplication;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);

        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_MONTH);//获取是本月的第几周
        int day = c.get(Calendar.DAY_OF_WEEK);
        day = c.get(Calendar.DAY_OF_MONTH);
        day = c.get(Calendar.YEAR);
        System.out.print(1/7);
    }
}