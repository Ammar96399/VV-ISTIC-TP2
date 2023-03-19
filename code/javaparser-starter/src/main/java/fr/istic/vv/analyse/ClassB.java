package fr.istic.vv.analyse;

public class ClassB {
    private int field3;
    private int field4;
    private ClassA field5;

    public void method3() {
        System.out.println(field3);
    }

    public void method4() {
        System.out.println(field4);
    }

    public void method5() {
        field5.method1();
    }

    public void method6() {
        field5.method2();
    }
}