package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.undo.Command;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

public class DocumentStoreImpl implements DocumentStore {
    private final HashTable<URI, Document> documentMap;
    private final Stack<Command> commandStack;
    private final TrieImpl<Document> trie; // TrieImpl
    private final MinHeapImpl<Document> minHeap;
    private int maxDocumentCount = Integer.MAX_VALUE;
    private int maxDocumentBytes = Integer.MAX_VALUE;
    private int currentDocumentBytes = 0;


    public DocumentStoreImpl() {
        this.documentMap = new HashTableImpl<>();
        this.commandStack = new Stack<>();
        this.trie = new TrieImpl<>(); // TrieImpl
        this.minHeap = new MinHeapImpl<>(); // Initialize minHeap here
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

        Document previousDocument = this.documentMap.get(uri);
        int previousDocumentSize = previousDocument != null ? getSizeInBytes(previousDocument) : 0;

        if (input == null) {
            delete(uri);
            return 0;
        }

        byte[] data = input.readAllBytes();
        Document newDocument = format == DocumentFormat.TXT ? new DocumentImpl(uri, new String(data)) : new DocumentImpl(uri, data);
        newDocument.setLastUseTime(System.nanoTime());

        // Indexing document content if it is text
        if (format == DocumentFormat.TXT) {
            String text = new String(data);
            String[] words = text.split("\\W+");
            for (String word : words) {
                this.trie.put(word.toLowerCase(), newDocument);  // Index each word in the trie
            }
        }

        // Indexing metadata
        if (newDocument.getMetadata() != null) {
            HashTable<String, String> metadata = newDocument.getMetadata();
            for (String key : metadata.keySet()) {
                String value = metadata.get(key);
                if (value != null) {
                    this.trie.put(key.toLowerCase() + ":" + value.toLowerCase(), newDocument);  // Index metadata in the trie
                }
            }
        }

        // Replacing the previous document if it exists
        if (previousDocument != null) {
            this.minHeap.remove(previousDocument); // Remove previous document from the heap if it exists
            this.currentDocumentBytes -= previousDocumentSize; // Adjust memory count
        }

        this.documentMap.put(uri, newDocument);  // Add new document to the document map
        this.minHeap.insert(newDocument);  // Insert new document into the minHeap
        int sizeChange = getSizeInBytes(newDocument) - previousDocumentSize;
        this.currentDocumentBytes += sizeChange;

        enforceMemoryLimits();  // Enforce memory limits after updating store

        // Creating and pushing the undo command
        Command command = new Command(uri, uri1 -> {
            this.documentMap.put(uri1, previousDocument);
            if (previousDocument != null) {
                previousDocument.setLastUseTime(System.nanoTime());
                this.minHeap.insert(previousDocument);
            }
            this.currentDocumentBytes -= sizeChange;
            enforceMemoryLimits();
        });
        this.commandStack.push(command);

        return previousDocument != null ? previousDocument.hashCode() : 0;
    }



    @Override
    public Document get(URI uri) {
        if (uri == null || uri.toString().isBlank()) {
            throw new IllegalArgumentException("URI must not be null or empty");
        }
        Document doc = this.documentMap.get(uri);
        if (doc != null) {
            doc.setLastUseTime(System.nanoTime());
            this.minHeap.reHeapify(doc);
        }
        return doc;
    }

    @Override
    public boolean delete(URI uri) {
        if (uri == null || uri.toString().isBlank()) {
            throw new IllegalArgumentException("URI must not be null or empty");
        }

        Document documentToDelete = this.documentMap.get(uri);
        if (documentToDelete == null) {
            return false;
        }

        // Remove the document from the documentMap and the minHeap
        this.documentMap.put(uri, null);
        this.minHeap.remove(documentToDelete); // Assuming 'remove' method exists in MinHeapImpl
        this.currentDocumentBytes -= getSizeInBytes(documentToDelete);


        Command undoCommand = new Command(uri, uri1 -> {
            this.documentMap.put(uri1, documentToDelete);
            this.minHeap.insert(documentToDelete);
            this.currentDocumentBytes += getSizeInBytes(documentToDelete);
             // Return true to satisfy the Function<URI, Boolean> requirement
        });

        this.commandStack.push(undoCommand);
        return true;
    }

    private int getSizeInBytes(Document doc) {
        return doc.getDocumentTxt() != null ? doc.getDocumentTxt().getBytes().length : doc.getDocumentBinaryData().length;
    }





    @Override
    public void undo() throws IllegalStateException {
        if (this.commandStack.isEmpty()) {
            throw new IllegalStateException("There are no actions to be undone");
        }
        Command command = this.commandStack.pop();
        command.undo();
        enforceMemoryLimits();
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
        enforceMemoryLimits();
    }
//new methods for p4/////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public List<Document> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword must not be null or empty");
        }

        // Normalize the keyword (if your indexing is case-insensitive)
        keyword = keyword.toLowerCase();
        System.out.println("Searching for normalized keyword: " + keyword);

        final String finalKeyword = keyword.toLowerCase();
        List<Document> searchResults = trie.getSorted(keyword, Comparator.comparingInt(doc -> doc.wordCount(finalKeyword)));

        System.out.println("Matches found: " + searchResults.size());
        if (searchResults.isEmpty()) {
            System.out.println("No documents found for keyword: " + keyword);
        } else {
            System.out.println("Search results for keyword " + keyword + ":");
            for (Document doc : searchResults) {
                System.out.println(doc.getKey() + " - Count: " + doc.wordCount(keyword));
                // Update last use time and reheapify for each accessed document
                doc.setLastUseTime(System.nanoTime());
                this.minHeap.reHeapify(doc);
            }
        }

        return searchResults;
    }


    @Override
    public List<Document> searchByPrefix(String keywordPrefix) {
        if (keywordPrefix == null || keywordPrefix.isBlank()) {
            throw new IllegalArgumentException("Keyword prefix must not be null or empty");
        }

        System.out.println("Searching for prefix: " + keywordPrefix);  // Debug stuff
        List<Document> searchResults = trie.getAllWithPrefixSorted(keywordPrefix, Comparator.comparingInt(doc -> doc.wordCount(keywordPrefix)));
        System.out.println("Documents found: " + searchResults.size());  // Debug stuff

        for (Document doc : searchResults) {
            doc.setLastUseTime(System.nanoTime());
            this.minHeap.reHeapify(doc);
        }

        return searchResults;
    }




    @Override
    public Set<URI> deleteAll(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword must not be null or blank");
        }
        List<Document> documentsToDelete = new ArrayList<>(search(keyword));  // Collect all documents first
        Set<URI> deletedURIs = new HashSet<>();

        for (Document doc : documentsToDelete) {
            URI uri = doc.getKey();
            if (delete(uri)) {  // Assuming delete returns a boolean indicating success
                deletedURIs.add(uri);
            }
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
        for (Document doc : documentMap.values()) {
            boolean match = true;
            for (Map.Entry<String, String> entry : keysValues.entrySet()) {
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

        // Debug: Ensure the keysValues map contains expected keys and values
        System.out.println("Searching for Metadata: " + keysValues);

        // Adjust to handle case sensitivity or other formatting issues
        keywordPrefix = keywordPrefix.toLowerCase(); // Assuming you index in lower case
        Map<String, String> formattedKeysValues = new HashMap<>();
        keysValues.forEach((k, v) -> formattedKeysValues.put(k.toLowerCase(), v.toLowerCase())); // Format metadata keys and values

        List<Document> documents = this.searchByPrefix(keywordPrefix);
        List<Document> filteredDocuments = new ArrayList<>();

        for (Document doc : documents) {
            if (matchesAllMetadata(doc, formattedKeysValues)) {
                filteredDocuments.add(doc);
            }
        }

        System.out.println("Total documents found: " + documents.size() + ", filtered by metadata: " + filteredDocuments.size()); // Debug output
        return filteredDocuments;
    }

    private boolean matchesAllMetadata(Document doc, Map<String, String> keysValues) {
        for (Map.Entry<String, String> entry : keysValues.entrySet()) {
            String metadataKey = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = doc.getMetadataValue(metadataKey);
            if (actualValue == null || !actualValue.toLowerCase().equals(expectedValue)) {
                return false; // If any metadata does not match, exclude this document
            }
        }
        return true;
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
        this.maxDocumentCount = limit;
        enforceMemoryLimits();
    }

    @Override
    public void setMaxDocumentBytes(int limit) {
        this.maxDocumentBytes = limit;
        enforceMemoryLimits();
    }

    private void enforceMemoryLimits() {
        while (this.minHeap.size() > this.maxDocumentCount || this.currentDocumentBytes > this.maxDocumentBytes) {
            Document leastUsed = this.minHeap.remove();
            URI uriToRemove = leastUsed.getKey();
            this.documentMap.put(uriToRemove, null);

            // Assuming the Document has a method to get all its words or keywords
            Set<String> words = leastUsed.getWords();
            for (String word : words) {
                this.trie.delete(word, leastUsed);
            }

            this.currentDocumentBytes -= getSizeInBytes(leastUsed);
            // You might need to remove the document from the command stack or other structures if necessary
        }
    }


}


