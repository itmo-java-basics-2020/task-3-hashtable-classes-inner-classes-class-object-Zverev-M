package ru.itmo.java;

import java.util.Map;

public class HashTable {

    public Entry[] array;
    private int sizeOf;
    private int places;
    private final float loadFactor;
    private int threshold;

    HashTable(int initialCapacity) {
        array = new Entry[initialCapacity];
        loadFactor = 0.5f;
        sizeOf = 0;
        places = initialCapacity;
        threshold = (int)(initialCapacity * loadFactor);
    }

    HashTable(int initialCapacity, float initialLoadFactor) {
        array = new Entry[initialCapacity];
        loadFactor = initialLoadFactor;
        sizeOf = 0;
        places = initialCapacity;
        threshold = (int)(initialCapacity * loadFactor);
    }

    int getHash(Object key) {
        return Math.abs(key.hashCode());
    }

    void setThreshold(int length) {
        threshold = (int)(length * loadFactor);
    }

    Entry[] arrayResize() {
        Entry[] bigArray = new Entry[array.length * 2];
        places = bigArray.length;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null || array[i].deleted) {
                continue;
            }
            int hash = Math.abs(array[i].key.hashCode()) % bigArray.length;

            while (bigArray[hash] != null && !(bigArray[hash].deleted && bigArray[hash].key.equals(array[i].key)) && !bigArray[hash].key.equals(array[i].key)) {
                hash = (hash + 1) % bigArray.length;
            }

            bigArray[hash] = new Entry(array[i].key, array[i].value);
            places--;
        }


        return bigArray;
    }

    Object put(Object key, Object value) {
        int hash = getHash(key) % array.length;

        while (array[hash] != null && !(array[hash].deleted && array[hash].key.equals(key)) && !array[hash].key.equals(key)) {
            hash = (hash + 1) % array.length;
        }

        if (array[hash] == null || (array[hash].deleted)) {
            sizeOf++;
            if (array[hash] == null) {
                places--;
            }
            array[hash] = new Entry(key, value);

            if (threshold <= sizeOf || places < (int) Math.sqrt(array.length)) {
                array = arrayResize();
                setThreshold(array.length);
            }

            return null;
        }

        Object val = array[hash].value;
        array[hash].value = value;
        return val;
    }

    Object get(Object key) {
        int hash = getHash(key) % array.length;

        while (array[hash] != null && !array[hash].key.equals(key)) {
            hash = (hash + 1) % array.length;
        }

        if (array[hash] == null || (array[hash].key.equals(key) && array[hash].deleted)) {
            return null;
        }

        return array[hash].value;
    }

    Object remove(Object key) {
        int hash = getHash(key) % array.length;

        while (array[hash] != null && !array[hash].key.equals(key)) {
            hash = (hash + 1) % array.length;
        }

        if (array[hash] == null || (array[hash].key.equals(key) && array[hash].deleted)) {
            return null;
        }

        sizeOf--;
        array[hash].deleted = true;
        return array[hash].value;
    }

    int size() {
        return sizeOf;
    }

    static class Entry {

        Object key;
        Object value;
        Boolean deleted;

        Entry(Object key_, Object value_) {
            key = key_;
            value = value_;
            deleted = false;
        }

        Entry(Object key_, Object value_, Boolean deleted_) {
            key = key_;
            value = value_;
            deleted = deleted_;
        }
    }
}
