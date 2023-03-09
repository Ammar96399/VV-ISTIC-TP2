# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

In general, TCC and LCC metrics produce the same value when there are no methods within a class or when all methods within a class share all attributes. In other words, if a class has only one method or if all methods within the class access all attributes, then the TCC and LCC metrics will be the same.
```java
public class test {
   private int num;
   
   public void setNum(int num) {
      this.num = num;
   }
   
   public int getNum() {
      return num;
   }
}
```
It is possible for LCC to be lower than TCC for a given class. This can happen when there are multiple methods within a class that access different sets of attributes. In such cases, the TCC metric will be high because there are many method pairs that share attributes, but the LCC metric will be low because there are many method pairs that do not share attributes.
```java
public class test {
   private int int1;
   private int i;
   
   public void setInt1(int int1) {
      this.int1 = int1;
   }
   
   public int getInt1() {
      return int1;
   }
   
   public void setInt2(int int2) {
      this.int2 = int2;
   }
   
   public int getInt2() {
      return int2;
   }
}
```