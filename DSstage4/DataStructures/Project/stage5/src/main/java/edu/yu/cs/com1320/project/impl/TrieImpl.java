package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.undo.GenericCommand;
import edu.yu.cs.com1320.project.undo.Undoable;

import java.util.*;

public class TrieImpl<Value> implements Trie<Value> {
    private static final int ALPHABET_SIZE = 256;
    private Node<Value> root;
    private final StackImpl<Undoable> commandStack;

    public TrieImpl() {
        this.root = new Node<>();
        this.commandStack = new StackImpl<>();
    }
    @SuppressWarnings("unchecked")//risky keep track of types
    private static class Node<Value> {
        private Value val;
        private final Node<Value>[] links;

        Node() {
            this.links = new Node[ALPHABET_SIZE];
        }
    }


    @Override
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        // Create a GenericCommand to encapsulate the put operation
        GenericCommand<Value> command = new GenericCommand<>(val, (v) -> {
            // Undo logic: Remove the key-value pair from Trie
            delete(key, val);
        });
        this.commandStack.push(command); // Push the command onto the undo stack

        // Continue with the put operation
        this.root = put(this.root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d){
        if (x == null) {
            x = new Node<>();
        }
        if (d == key.length()) {
            x.val= val;
            return x;
        }
        char c = key.charAt(d);
        x.links[c] = put(x.links[c], key, val, d + 1);
        return x;
    }



    @Override
    public List<Value> getSorted(String key, Comparator<Value> comparator) {
        if (key == null || comparator == null) {
            throw new IllegalArgumentException("Key and comparator cannot be null");
        }

        System.out.println("Searching for keyword: " + key);

        Node<Value> x = get(root, key, 0);
        if (x == null || x.val == null) {
            System.out.println("No documents found for keyword: " + key);
            return List.of(); // if no matches, return empty list
        }

        List<Value> matches = new ArrayList<>();
        collect(x, key, matches);

        System.out.println("Matches for keyword " + key + ":");
        for (Value match : matches) {
            System.out.println(match);
        }

        matches.sort(comparator.reversed()); // sorts in descending order

        System.out.println("Sorted matches for keyword " + key + ":");
        for (Value sortedMatch : matches) {
            System.out.println(sortedMatch);
        }

        return matches;
    }


    private void collect(Node<Value> x, String prefix, List<Value> matches) {
        if (x == null) {
            return;
        }
        if(x.val != null){
            matches.add(x.val);
        }
        for(char c = 0; c < ALPHABET_SIZE; c++) {
            collect(x.links[c], prefix + c, matches);
        }
    }

    private Node<Value> get(Node<Value> x, String key, int d){
        if(x == null) {
            return null;
        }

        if(d == key.length()){
            return x;
        }

        char c = key.charAt(d);
        return  get(x.links[c], key, d + 1);

    }

    @Override
    public Set<Value> get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        Node<Value> x = get(root, key, 0);
        if (x == null || x.val == null) {
            return Set.of(); // Return an empty set if no matches
        }
        Set<Value> matches = new HashSet<>();
        collect(x, key, matches);
        return matches;
    }

    private void collect(Node<Value> x, String prefix, Set<Value> matches) {
        if (x == null) {
            return;
        }
        if (x.val != null) {
            matches.add(x.val);
        }
        for (char c = 0; c < ALPHABET_SIZE; c++) {
            collect(x.links[c], prefix + c, matches);
        }
    }

    @Override
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        if (prefix == null || comparator == null) {
            throw new IllegalArgumentException("Prefix and comparator cannot be null");
        }

        Node<Value> x = get(root, prefix, 0);
        List<Value> matches = new ArrayList<>();
        collect(x, prefix, matches);

        // Sort the matches using the provided comparator
        matches.sort(comparator); // Sort the matches using the provided comparator

        return matches;
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Set<Value> deleteAllWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }

        Set<Value> deletedValues = new HashSet<>();
        Node<Value> x = get(root, prefix, 0);
        if (x != null) {
            // Create a command to undo the deletion of values
            GenericCommand<Set<Value>> deleteCommand = new GenericCommand<>(deletedValues, set -> {
                // Undo
                for (Value value : set) {
                    put(prefix, value); // Restore
                }
            });

            // Traverse the trie
            collectAndDelete(x, prefix, deletedValues);

            commandStack.push(deleteCommand); // Push to undo stack
        }
        return deletedValues;
    }

    private void collectAndDelete(Node<Value> x, String prefix, Set<Value> deletedValues) {
        if (x == null) {
            return;
        }
        if (x.val != null) {
            deletedValues.add(x.val);
            delete(prefix, x.val); // Delete the value from the trie
        }
        for (char c = 0; c < ALPHABET_SIZE; c++) {
            collectAndDelete(x.links[c], prefix + c, deletedValues); // Recursively call child nodes
        }
    }



    @Override
    public Set<Value> deleteAll(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        Set<Value> deletedValues = new HashSet<>();
        System.out.println("Deleting all values for key: " + key);
        deleteAll(root, key, "", deletedValues);
        System.out.println("Deleted values: " + deletedValues);
        return deletedValues;
    }

    private void deleteAll(Node<Value> x, String key, String currentKey, Set<Value> deletedValues) {
        if (x == null || currentKey.length() > key.length()) {
            return;
        }
        if (currentKey.equals(key) && x.val != null) {
            deletedValues.add(x.val);
            System.out.println("Deleted value: " + x.val);
            x.val = null;
        }
        for (char c = 0; c < ALPHABET_SIZE; c++) {
            deleteAll(x.links[c], key, currentKey + c, deletedValues);
        }
    }












    @Override
    public Value delete(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        Node<Value> x = get(root, key, 0);
        if (x != null && x.val != null && x.val.equals(val)) {
            GenericCommand<Value> deleteCommand = new GenericCommand<>(val, v -> {
                // Undo
                put(key, v); // Restore
            });

            commandStack.push(deleteCommand); // Push to undo stack
            Value deletedValue = x.val; // Store the deleted value
            x.val = null;
            return deletedValue; // Return the deleted value
        }

        return null; // Key not found or value not matched
    }



}


