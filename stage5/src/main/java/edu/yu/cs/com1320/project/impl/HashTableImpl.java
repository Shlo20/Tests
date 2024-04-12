package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

import java.util.*;

public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {
    private static final int TABLE_SIZE = 5; // Fixed size
    private final List<Entry<Key, Value>>[] table;

    @SuppressWarnings("unchecked")
    public HashTableImpl() {
        this.table = new LinkedList[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            this.table[i] = new LinkedList<>();
        }
    }

    @Override
    public Value get(Key k) {
        int index = hash(k);
        for (Entry<Key, Value> entry : table[index]) {
            if (entry.getKey().equals(k)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Value put(Key k, Value v) {
        int index = hash(k);
        for (Entry<Key, Value> entry : table[index]) {
            if (entry.getKey().equals(k)) {
                Value oldValue = entry.getValue();
                entry.setValue(v);
                return oldValue;
            }
        }
        table[index].add(new Entry<>(k, v));
        return null;
    }

    @Override
    public boolean containsKey(Key key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        int count = 0;
        for (List<Entry<Key, Value>> bucket : table) {
            count += bucket.size();
        }
        return count;
    }

    @Override
    public Set<Key> keySet() {
        Set<Key> keys = new HashSet<>();
        for (List<Entry<Key, Value>> bucket : table) {
            for (Entry<Key, Value> entry : bucket) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    @Override
    public Collection<Value> values() {
        List<Value> values = new ArrayList<>();
        for (List<Entry<Key, Value>> bucket : table) {
            for (Entry<Key, Value> entry : bucket) {
                values.add(entry.getValue());
            }
        }
        return values;
    }

    private int hash(Key key) {
        return Math.abs(key.hashCode()) % TABLE_SIZE;
    }

    private static class Entry<Key, Value> {
        private final Key key;
        private Value value;

        Entry(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        public Key getKey() {
            return key;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }
    }
}
