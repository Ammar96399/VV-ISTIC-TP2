# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

You can find our xml file [here](../code/Exercise3/exo3.xml).
The results we got running our rule on the projects common-collections are the following:
```
../../../pmd-bin-6.55.0/bin/run.sh pmd -d /home/ammar/Documents/gitRepos/commons-collections -R ./exo3.xml 
Mar 03, 2023 11:19:27 AM net.sourceforge.pmd.PMD encourageToUseIncrementalAnalysis
WARNING: This analysis could be faster, please consider using Incremental Analysis: https://pmd.github.io/pmd-6.55.0/pmd_userdocs_incremental_analysis.html
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1503:     AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1505:     AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/CollectionUtils.java:1507:     AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/MapUtils.java:226:     AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/MapUtils.java:926:     AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/MapUtils.java:1667:    AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:506:  AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:915:  AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/bidimap/TreeBidiMap.java:988:  AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/map/Flat3Map.java:138: AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/map/Flat3Map.java:211: AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/map/Flat3Map.java:306: AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/map/Flat3Map.java:457: AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/map/LRUMap.java:319:   AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/main/java/org/apache/commons/collections4/set/CompositeSet.java:372:     AvoidComplexCode:       Avoid using three or more nested if statements
/home/ammar/Documents/gitRepos/commons-collections/src/test/java/org/apache/commons/collections4/list/AbstractListTest.java:1175:       AvoidComplexCode:       Avoid using three or more nested if statements
```
