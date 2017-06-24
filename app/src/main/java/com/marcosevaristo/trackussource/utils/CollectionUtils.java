package com.marcosevaristo.trackussource.utils;


import java.util.Collection;

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> coll) {
         return coll == null || coll.size() == 0;
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

}
