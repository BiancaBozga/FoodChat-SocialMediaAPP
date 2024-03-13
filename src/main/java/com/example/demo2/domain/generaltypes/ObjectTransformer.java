package com.example.demo2.domain.generaltypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ObjectTransformer {
    public static <T> Collection<T> iterableToCollection(Iterable<T> iterable) {
        Collection<T> collection = new ArrayList<>();
        iterable.forEach(collection::add);
        return collection;
    }

    public static <T> Set<T> iterableToSet(Iterable<T> iterable) {
        Set<T> set = new HashSet<>();
        iterable.forEach(set::add);
        return set;
    }
}
