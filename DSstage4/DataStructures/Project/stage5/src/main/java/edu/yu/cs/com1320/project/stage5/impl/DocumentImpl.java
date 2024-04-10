package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage5.Document;

import java.net.URI;
import java.util.*;


public class DocumentImpl implements Document {
    private final URI uri;
    private final String text;
    private final byte[] binaryData;
    private final HashTable<String, String> metadata;

    public DocumentImpl(URI uri, String txt) {
        if (uri == null || txt == null || txt.isBlank()) {
            throw new IllegalArgumentException("URI and text must not be null or empty");
        }
        this.uri = uri;
        this.text = txt;
        this.binaryData = null;
        this.metadata = new HashTableImpl<>();
    }

    public DocumentImpl(URI uri, byte[] binaryData) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }
        this.uri = uri;
        this.text = "";
        this.binaryData = binaryData != null ? binaryData.clone() : null;
        this.metadata = new HashTableImpl<>();
    }


    @Override
    public String setMetadataValue(String key, String value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Metadata key must not be null or empty");
        }
        return this.metadata.put(key, value);
    }

    @Override
    public String getMetadataValue(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Metadata key must not be null or empty");
        }
        return this.metadata.get(key);
    }

    @Override
    public HashTable<String, String> getMetadata() {
        return this.metadata;
    }

    @Override
    public String getDocumentTxt() {
        if (this.binaryData != null) {
            return null; // Return null for binary documents
        }
        return this.text;
    }

    @Override
    public byte[] getDocumentBinaryData() {
        if (this.binaryData != null) {
            return this.binaryData.clone();
        }
        return null;
    }

    @Override
    public URI getKey() {
        return this.uri;
    }


    //new methods for errors in 3///////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        if (this.binaryData != null) {
            return Arrays.hashCode(this.binaryData);
        } else if (this.text != null) {
            return this.text.hashCode();
        } else {
            return 0;
        }
    }

    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DocumentImpl document = (DocumentImpl) obj;
        return uri.equals(document.uri) && text.equals(document.text);
    }


//new methods for 4////////////////////////////////////////////////////////////////////////


    @Override
    public int wordCount(String word) {
        if (word == null || word.isBlank()) {
            throw new IllegalArgumentException("Word must not be null or empty");
        }

        if (this.binaryData != null) {
            return 0; // Binary document, return 0
        }

        // Word count
        int count = 0;
        String[] words = this.text.split("\\s+"); // Split text into words
        for (String w : words) {
            if (w.equalsIgnoreCase(word)) { // Check for case-insensitive match
                count++;
            }
        }

        // Debug output
        System.out.println("Word count for '" + word + "' in document " + getKey() + ": " + count);

        return count;
    }


    @Override
    public Set<String> getWords() {
        if (this.binaryData != null) {
            return Collections.emptySet();//bin doc return empty set
        }
        String[] words = this.text.split("\\s+");// Split text into words
        return new HashSet<>(Arrays.asList(words));
    }

//methods for stage 5///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public long getLastUseTime() {
        return 0;
    }

    @Override
    public void setLastUseTime(long timeInNanoseconds) {

    }

    @Override
    public int compareTo(Document o) {
        return 0;
    }
}



