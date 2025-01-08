# Kala Common Tutorial

Table of contents:

* [Use Kala Collections](#use-kala-collections) (WIP)
* ... (WIP)

## Use Kala Collections

### Overview

Kala Collections provides the following common collection interfaces:

* `Collection`: Common interface implemented by collections.
  * `Seq`: An ordered collection of values that can be accessed by integer index.
  * `Set`: A collection that contains no duplicate elements.
    * `SortedSet`: A `Set` that further provides a total ordering on its elements.
* `Map`: An object that maps keys to values.

These collections have immutable and mutable variants.
For example, for `Seq`, it has the following important subinterfaces:

* `Seq`
  * `ImmutableSeq`: An immutable seq. The user cannot modify it, but can generate another seq from an existing seq.
  * `MutableSeq`: A mutable seq. It is similar to an array in that the user can modify this seq, but it does not provide methods that would change the size of the seq.
    * `MutableList`: It is a better alternative to `java.util.List` and provides more useful methods.

This is a diagram of the basic collection types:

Basic:
```mermaid
graph TD;
    Collection --> Seq;
    Collection --> Set;
    Set --> SortedSet;

    Seq ---> ArraySeq;

    Map --> SortedMap;

    classDef impl fill: #4a5659, stroke: #fff, stroke-width: 4px;
    class ArraySeq impl;
```

Immutable Collections:
```mermaid
graph TD;
    Seq --> ImmutableSeq;
    ImmutableCollection --> ImmutableSeq;
    Set --> ImmutableSet;
    ImmutableCollection --> ImmutableSet;
    ImmutableSet --> ImmutableSortedSet;
    
    ImmutableSeq --> ImmutableArray;
    ImmutableSeq --> ImmutableVector;
    ImmutableSeq --> ImmutableLinkedSeq;
    ImmutableSeq --> ImmutableTreeSeq;
    ImmutableSortedSet ---> ImmutableSortedArraySet;
    ImmutableSet ----> ImmutableHashSet;
    ArraySeq --> ImmutableArray;

    classDef impl fill: #4a5659, stroke: #fff, stroke-width: 4px;
    class ArraySeq,ImmutableArray,ImmutableVector,ImmutableHashSet,ImmutableSortedArraySet,ImmutableLinkedSeq,ImmutableTreeSeq impl;
```

Mutable Collections:

```mermaid
graph TD;
  Seq --> MutableSeq;
  MutableCollection --> MutableSeq;
  MutableSeq --> MutableList;
  Set --> MutableSet;
  MutableCollection --> MutableSet;
  MutableSet --> MutableSortedSet;
  
  MutableSeq ---> MutableArray;
  MutableList --> MutableArrayList;
  MutableList --> MutableArrayDeque;
  MutableList --> MutableSmartArrayList;
  MutableList --> MutableLinkedList;
  MutableList --> MutableSinglyLinkedList;
  ArraySeq --> MutableArray;
  MutableSet ---> MutableEnumSet;
  MutableSet ---> MutableHashSet;
  MutableSortedSet --> MutableTreeSet;
  
  
  classDef impl fill: #4a5659, stroke: #fff, stroke-width: 4px;
  class ArraySeq,MutableArray,MutableArrayList,MutableArrayDeque,MutableSmartArrayList,MutableLinkedList,MutableSinglyLinkedList,MutableEnumSet,MutableHashSet,MutableTreeSet impl;
```