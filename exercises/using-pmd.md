# Using PMD

Pick a Java project from GitHub (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer
One of the issues found by PMD when we ran it on apache commons collections is the following:
```
src/main/java/org/apache/commons/collections4/map/Flat3Map.java:139:    SwitchStmtsShouldHaveDefault:   Switch statements should be exhaustive, add a default case (or missing enum branches)
```
This issue is a true positive because the switch statement is not exhaustive. The default case should be added to the switch statement. The changes that should be added to the source code are the following:
```java
public class Flat3Map<K, V> implements IterableMap<K, V>, Serializable, Cloneable {
    // ....
    @Override
    public V get(final Object key) {
        if (delegateMap != null) {
            return delegateMap.get(key);
        }
        if (key == null) {
            switch (size) {
                // drop through
                case 3:
                    if (key3 == null) {
                        return value3;
                    }
                case 2:
                    if (key2 == null) {
                        return value2;
                    }
                case 1:
                    if (key1 == null) {
                        return value1;
                    }
                case default:
                    return null;
            }
        }
        // ....
    }
    // ....
}
```