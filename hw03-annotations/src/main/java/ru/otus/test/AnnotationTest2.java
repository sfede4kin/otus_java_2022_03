package ru.otus.test;

import ru.otus.test.annotations.After;
import ru.otus.test.annotations.Before;
import ru.otus.test.annotations.Test;

public class AnnotationTest2 {

    public AnnotationTest2() {
    }

    @Before
    public void setUp(){
        System.out.println("@Before setUp()");
    }

    @Test
    public void test1(){
        System.out.println("@Test test1()");
    }

    @Test
    public void test2(){
        System.out.println("@Test test2()");
    }

    @After
    public void tearDown(){
        System.out.println("@After tearDown()");
    }

}
