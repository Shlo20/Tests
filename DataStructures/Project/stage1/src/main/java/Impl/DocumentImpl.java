package Impl;

import edu.yu.cs.com1320.project.stage1.Document;

import java.net.URI;
import java.util.HashMap;

public class DocumentImpl implements Document {
    private final URI uri;
    private final String text;
    private final byte[] binaryData;
    private final HashMap<String, String> metadata;

    public DocumentImpl(URI uri, String txt) {
        if (uri == null || txt == null || txt.isBlank()) {
            throw new IllegalArgumentException("URI and text must not be null or empty");
        }
        this.uri = uri;
        this.text = txt;
        this.binaryData = null;
        this.metadata = new HashMap<>();
    }

    DocumentImpl(URI uri, byte[] binaryData) {
        if (uri == null || binaryData == null) {
            throw new IllegalArgumentException("URI and binary data must not be null");
        }
        this.uri = uri;
        this.text = null;
        this.binaryData = binaryData.clone();
        this.metadata = new HashMap<>();
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
    public HashMap<String, String> getMetadata() {
        return new HashMap<>(this.metadata);
    }

    @Override
    public String getDocumentTxt() {
        return this.text;
    }

    @Override
    public byte[] getDocumentBinaryData() {
        return this.binaryData.clone();
    }

    @Override
    public URI getKey() {
        return this.uri;
    }
}
