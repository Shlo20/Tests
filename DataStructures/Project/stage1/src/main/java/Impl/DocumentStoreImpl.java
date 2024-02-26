package Impl;

import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.DocumentStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;

public class DocumentStoreImpl implements DocumentStore {
    private final HashMap<URI, Document> documentMap;

    public DocumentStoreImpl() {
        this.documentMap = new HashMap<>();
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
        return document.setMetadataValue(key, value);
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
        if (uri == null || uri.toString().isBlank() || format == null) {
            throw new IllegalArgumentException("URI and format must not be null or empty");
        }
        if (input == null) {
            Document previousDoc = this.documentMap.remove(uri);
            return previousDoc != null ? previousDoc.hashCode() : 0;
        }
        byte[] data = input.readAllBytes();
        Document document = (format == DocumentFormat.TXT) ?
                new DocumentImpl(uri, new String(data)) :
                new DocumentImpl(uri, data);
        Document previousDoc = this.documentMap.put(uri, document);
        return previousDoc != null ? previousDoc.hashCode() : 0;
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
        return this.documentMap.remove(uri) != null;
    }
}
