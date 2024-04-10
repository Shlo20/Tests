package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.undo.Command;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

public class DocumentStoreImpl implements DocumentStore {
    private final HashTable<URI, Document> documentMap;
    private final Stack<Command> commandStack;
    private final TrieImpl<Document> trie; // TrieImpl

    public DocumentStoreImpl() {
        this.documentMap = new HashTableImpl<>();
        this.commandStack = new Stack<>();
        this.trie = new TrieImpl<>(); // TrieImpl
    }

    @Override
    public String setMetadata(URI uri, String key, String value) {
        if (uri == null || uri.toString().isBlank() || key == null || key.isBlank()) {
            throw new IllegalArgumentException("URI and metadata key must not be null or empty");
        }
        Document document = this.documentMap.get(uri);
        if (document == null) {
            throw new IllegalArgumentException("No document stored at URI: " + uri);
        }
        String oldValue = document.setMetadataValue(key, value);
        Command command = new Command(uri, uri1 -> document.setMetadataValue(key, oldValue));
        this.commandStack.push(command);
        return oldValue;
    }

    @Override
    public String getMetadata(URI uri, String key) {
        if (uri == null || uri.toString().isBlank() || key == null || key.isBlank()) {
            throw new IllegalArgumentException("URI and metadata key must not be null or empty");
        }
        Document document = this.documentMap.get(uri);
        if (document == null) {
            throw new IllegalArgumentException("No document stored at URI: " + uri);
        }
        return document.getMetadataValue(key);
    }

    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if (uri == null || format == null) {
            throw new IllegalArgumentException("URI and format must not be null");
        }
        if (input == null) {
            delete(uri);
            return 0;
        }
        byte[] data = input.readAllBytes();
        Document document;
        if (format == DocumentFormat.TXT) {
            String text = new String(data);
            document = new DocumentImpl(uri, text);
        } else {
            document = new DocumentImpl(uri, data);
        }
        Document previousDocument = documentMap.put(uri, document);
        Command command = new Command(uri, uri1 -> documentMap.put(uri1, previousDocument));
        this.commandStack.push(command);
        return previousDocument != null ? previousDocument.hashCode() : 0;
    }

    @Override
    public Document get(URI uri) {
        if (uri == null || uri.toString().isBlank()) {
            throw new IllegalArgumentException("URI must not be null or empty");
        }
        return this.documentMap.get(uri);
    }

    @Override
    public boolean delete(URI uri) {
        if (uri == null || uri.toString().isBlank()) {
            throw new IllegalArgumentException("URI must not be null or empty");
        }
        Document previousDocument = documentMap.put(uri, null);
        Command command = new Command(uri, uri1 -> documentMap.put(uri1, previousDocument));
        this.commandStack.push(command);
        return previousDocument != null;
    }

    @Override
    public void undo() throws IllegalStateException {
        if (this.commandStack.isEmpty()) {
            throw new IllegalStateException("There are no actions to be undone");
        }
        Command command = this.commandStack.pop();
        command.undo();
    }

    @Override
    public void undo(URI uri) throws IllegalStateException {
        if (this.commandStack.isEmpty()) {
            throw new IllegalStateException("There are no actions to be undone");
        }
        Stack<Command> tempStack = new Stack<>();
        boolean found = false;
        while (!this.commandStack.isEmpty() && !found) {
            Command command = this.commandStack.pop();
            if (command.getUri().equals(uri)) {
                command.undo();
                found = true;
            } else {
                tempStack.push(command);
            }
        }
        while (!tempStack.isEmpty()) {
            this.commandStack.push(tempStack.pop());
        }
        if (!found) {
            throw new IllegalStateException("There are no actions on the command stack for the given URI");
        }
    }
//new methods for p4/////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public List<Document> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword must not be null or empty");
        }

        System.out.println("Searching for keyword: " + keyword);

        // Add this line to debug the documents in the trie
        trie.getAllWithPrefixSorted(keyword, Comparator.comparingInt(doc -> doc.wordCount(keyword)));

        List<Document> searchResults = trie.getSorted(keyword, Comparator.comparingInt(doc -> doc.wordCount(keyword)));

        if (searchResults.isEmpty()) {
            System.out.println("No documents found for keyword: " + keyword);
        } else {
            System.out.println("Search results for keyword " + keyword + ":");
            for (Document doc : searchResults) {
                System.out.println(doc.getKey());
            }
        }

        return searchResults;
    }





    @Override
    public List<Document> searchByPrefix(String keywordPrefix) {
        if (keywordPrefix == null || keywordPrefix.isBlank()) {
            throw new IllegalArgumentException("keyword prefix must not be null");
        }
        return trie.getAllWithPrefixSorted(keywordPrefix,Comparator.comparingInt(doc -> doc.wordCount(keywordPrefix)));
    }



    @Override
    public Set<URI> deleteAll(String keyword) {
        Set<URI> deletedURIs = new HashSet<>();
        for (Document doc : search(keyword)) {
            delete(doc.getKey());
            deletedURIs.add(doc.getKey());
        }
        return deletedURIs;
    }

    @Override
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
        Set<URI> deletedURIs = new HashSet<>();
        for (Document doc : searchByPrefix(keywordPrefix)) {
            delete(doc.getKey());
            deletedURIs.add(doc.getKey());
        }
        return deletedURIs;
    }


    @Override
    public List<Document> searchByMetadata(Map<String, String> keysValues) {
        //use TrieImpl to implement the search by metadata
        List<Document> result = new ArrayList<>();
        for (Document doc : documentMap.values()){
            boolean match = true;
            for (Map.Entry<String, String> entry : keysValues.entrySet()){
                String metadataKey = entry.getKey();
                String expectedValue = entry.getValue();

                // Check if metadata value is null
                String actualValue = doc.getMetadataValue(metadataKey);
                if (actualValue == null || !actualValue.equals(expectedValue)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                result.add(doc);
            }
        }
        return result;
    }


    @Override
    public List<Document> searchByKeywordAndMetadata(String keyword, Map<String, String> keysValues) {
        if (keyword == null || keysValues == null || keysValues.isEmpty()) {
            throw new IllegalArgumentException("Keyword and keysValues map must not be null or empty.");
        }

        List<Document> result = new ArrayList<>();
        for (Document doc : search(keyword)) {
            boolean match = true;
            for (Map.Entry<String, String> entry : keysValues.entrySet()) {
                String metadataKey = entry.getKey();
                String expectedValue = entry.getValue();

                // Null check for metadata key
                if (metadataKey == null) {
                    throw new IllegalArgumentException("Metadata key must not be null.");
                }

                // Get metadata value
                String actualValue = doc.getMetadataValue(metadataKey);
                if (actualValue == null || !actualValue.equalsIgnoreCase(expectedValue)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                result.add(doc);
            }
        }
        return result;
    }


    @Override
    public List<Document> searchByPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) {
        if (keywordPrefix == null || keysValues == null || keysValues.isEmpty()) {
            throw new IllegalArgumentException("Keyword prefix and keysValues map must not be null or empty.");
        }

        List<Document> result = new ArrayList<>();
        for (Document doc : searchByPrefix(keywordPrefix)) {
            boolean match = true;
            for (Map.Entry<String, String> entry : keysValues.entrySet()) {
                String metadataKey = entry.getKey();
                String expectedValue = entry.getValue();

                // Null check for metadata key and value
                if (metadataKey == null || expectedValue == null) {
                    throw new IllegalArgumentException("Metadata key and value must not be null.");
                }

                // Get metadata value
                String actualValue = doc.getMetadataValue(metadataKey);
                if (actualValue == null || !actualValue.equalsIgnoreCase(expectedValue)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                result.add(doc);
            }
        }
        return result;
    }






    @Override
    public Set<URI> deleteAllWithMetadata(Map<String, String> keysValues) {
        Set<URI> deletedURIs = new HashSet<>();
        for (Document doc : searchByMetadata(keysValues)) {
            delete(doc.getKey());
            deletedURIs.add(doc.getKey());
        }
        return deletedURIs;
    }


    @Override
    public Set<URI> deleteAllWithKeywordAndMetadata(String keyword, Map<String, String> keysValues) {
        Set<URI> deletedURIs = new HashSet<>();
        for (Document doc : searchByKeywordAndMetadata(keyword, keysValues)) {
            delete(doc.getKey());
            deletedURIs.add(doc.getKey());
        }
        return deletedURIs;
    }

    @Override
    public Set<URI> deleteAllWithPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) {
        Set<URI> deletedURIs = new HashSet<>();
        for (Document doc : searchByPrefixAndMetadata(keywordPrefix, keysValues)) {
            delete(doc.getKey());
            deletedURIs.add(doc.getKey());
        }
        return deletedURIs;
    }
//stage 5 methods///////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void setMaxDocumentCount(int limit) {

    }

    @Override
    public void setMaxDocumentBytes(int limit) {

    }
}

