import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentStoreImplTest {
//    private DocumentStoreImpl documentStore;
//
//    @BeforeEach
//    public void setUp() {
//        documentStore = new DocumentStoreImpl();
//    }
//
//    @Test
//    public void testPutAndGetMetadata() throws IOException {
//        URI uri = URI.create("test://document");
//        String key = "key";
//        String value = "value";
//
//        // Put a document
//        InputStream input = new ByteArrayInputStream("Hello, world!".getBytes());
//        documentStore.put(input, uri, DocumentStore.DocumentFormat.TXT);
//
//        // Set metadata
//        documentStore.setMetadata(uri, key, value);
//
//        // Get metadata and assert
//        String retrievedValue = documentStore.getMetadata(uri, key);
//        assertEquals(value, retrievedValue);
//    }
//
//    @Test
//    public void testPutAndGetMetadataMultipleValues() throws IOException {
//        URI uri = URI.create("test://document");
//        String key1 = "key1";
//        String value1 = "value1";
//        String key2 = "key2";
//        String value2 = "value2";
//
//        // Put a document
//        InputStream input = new ByteArrayInputStream("Hello, world!".getBytes());
//        documentStore.put(input, uri, DocumentStore.DocumentFormat.TXT);
//
//        // Set metadata
//        documentStore.setMetadata(uri, key1, value1);
//        documentStore.setMetadata(uri, key2, value2);
//
//        // Get metadata and assert
//        String retrievedValue1 = documentStore.getMetadata(uri, key1);
//        assertEquals(value1, retrievedValue1);
//        String retrievedValue2 = documentStore.getMetadata(uri, key2);
//        assertEquals(value2, retrievedValue2);
//    }
//
//    @Test
//    public void testPutAndGetMetadataDifferentURIs() throws IOException { //for this test fix docstoreimpl method put and use the correct imp
//        URI uri1 = URI.create("test://document1");
//        URI uri2 = URI.create("test://document2");
//        String key = "key";
//        String value = "value";
//
//        // Put documents
//        InputStream input1 = new ByteArrayInputStream("Document 1".getBytes());
//        InputStream input2 = new ByteArrayInputStream("Document 2".getBytes());
//        documentStore.put(input1, uri1, DocumentStore.DocumentFormat.TXT);
//        documentStore.put(input2, uri2, DocumentStore.DocumentFormat.TXT);
//
//        // Set metadata for different URIs
//        documentStore.setMetadata(uri1, key, value);
//        documentStore.setMetadata(uri2, key, value);
//
//        // Get metadata and assert
//        String retrievedValue1 = documentStore.getMetadata(uri1, key);
//        assertEquals(value, retrievedValue1);
//        String retrievedValue2 = documentStore.getMetadata(uri2, key);
//        assertEquals(value, retrievedValue2);
//    }
//
//    @Test
//    public void testPutAndGetMetadataNonExistingDocument() {
//        URI uri = URI.create("test://non-existing-document");
//        String key = "key";
//        String value = "value";
//
//        // Attempt to set metadata for non-existing document
//        assertThrows(IllegalArgumentException.class, () -> documentStore.setMetadata(uri, key, value));
//    }
//
//    @Test
//    public void testPutNullInput() throws IOException {
//        URI uri = URI.create("test://null-input-document");
//
//        // Put a null input (simulate delete)
//        documentStore.put(null, uri, DocumentStore.DocumentFormat.TXT);
//
//        // Attempt to get metadata for non-existing document
//        assertThrows(IllegalArgumentException.class, () -> documentStore.getMetadata(uri, "key"));
//    }
////new tests after stage 4 results/ / / / ///////////////////////////////////////////////////////////////////////////////
//
//    @Test
//    public void testPutNewVersionOfDocumentTxt() {
//        DocumentStoreImpl documentStore = new DocumentStoreImpl();
//        URI uri = URI.create("http://example.com/document1");
//        try {
//            // Attempt IO operation that can throw IOException
//            documentStore.put(new ByteArrayInputStream("Test document".getBytes()), uri, DocumentStore.DocumentFormat.TXT);
//
//            // Test putting a new version of a text document
//            int oldHashCode = documentStore.put(new ByteArrayInputStream("Updated document".getBytes()), uri, DocumentStore.DocumentFormat.TXT);
//            assertEquals(true, oldHashCode != 0);
//        } catch (IOException e) {
//            // Handle IOException
//            fail("IOException occurred: " + e.getMessage());
//        }
//
//    }
//
//    @Test
//    public void testGetBinaryDocAsTxt() throws IOException {
//        // Create a URI and binary data
//        URI uri = URI.create("http://www.example.com/doc1");
//        byte[] binaryData = "Binary data".getBytes();
//
//        // Create a binary document
//        Document document = new DocumentImpl(uri, binaryData);
//
//        // Ensure that the document is binary
//        assertNotNull(document.getDocumentBinaryData());
//        assertNull(document.getDocumentTxt());
//
//        // Test that trying to get the text of a binary doc returns null
//        assertNull(document.getDocumentTxt());
//    }
//
//    @Test
//    public void stage4SearchTest() {
//        // Create a DocumentStoreImpl instance
//        DocumentStoreImpl documentStore = new DocumentStoreImpl();
//
//        // Add documents to the document store
//        URI uri1 = URI.create("document1");
//        InputStream inputStream1 = new ByteArrayInputStream("This is document one".getBytes());
//        try {
//            documentStore.put(inputStream1, uri1, DocumentStore.DocumentFormat.TXT);
//        } catch (IOException e) {
//            fail("IOException while putting document1");
//        }
//
//        URI uri2 = URI.create("document2");
//        InputStream inputStream2 = new ByteArrayInputStream("This is document two".getBytes());
//        try {
//            documentStore.put(inputStream2, uri2, DocumentStore.DocumentFormat.TXT);
//        } catch (IOException e) {
//            fail("IOException while putting document2");
//        }
//
//        // Perform search for keyword "document"
//        List<Document> searchResults = documentStore.search("document");
//
//        // Debugging output
//        System.out.println("Search Results for keyword 'document':");
//        for (Document doc : searchResults) {
//            System.out.println(doc.getKey());
//        }
//
//        // Assert the number of search results
//        assertEquals(2, searchResults.size(), "expected 2 matches, received " + searchResults.size());
//
//        // Additional test for searching with a specific keyword "document one"
//        List<Document> specificSearchResults = documentStore.search("document one");
//
//        // Debugging output
//        System.out.println("Search Results for keyword 'document one':");
//        for (Document doc : specificSearchResults) {
//            System.out.println(doc.getKey());
//        }
//
//        // Assert the number of specific search results
//        assertEquals(1, specificSearchResults.size(), "expected 1 match for 'document one', received " + specificSearchResults.size());
//    }

    private DocumentStoreImpl documentStore;

    @BeforeEach
    public void setup() {
        documentStore = new DocumentStoreImpl();
    }

    @Test
    public void stage4DeleteAllWithMetadata() throws IOException {
        URI uri1 = URI.create("http://example.com/doc1");
        URI uri2 = URI.create("http://example.com/doc2");
        String text1 = "Sample text for document 1";
        String text2 = "Sample text for document 2";

        InputStream stream1 = new ByteArrayInputStream(text1.getBytes());
        InputStream stream2 = new ByteArrayInputStream(text2.getBytes());

        // Add documents with metadata
        documentStore.put(stream1, uri1, DocumentStore.DocumentFormat.TXT);
        documentStore.put(stream2, uri2, DocumentStore.DocumentFormat.TXT);

        // Set metadata for the documents
        documentStore.setMetadata(uri1, "key1", "value1");
        documentStore.setMetadata(uri2, "key2", "value2");

        // Create metadata map for deletion
        Map<String, String> metadataToDelete = new HashMap<>();
        metadataToDelete.put("key1", "value1");

        // Delete documents with matching metadata
        Set<URI> deletedURIs = documentStore.deleteAllWithMetadata(metadataToDelete);

        // Verify that the correct URIs are returned
        assertEquals(1, deletedURIs.size()); // Expecting one URI to be deleted
        assertTrue(deletedURIs.contains(uri1)); // URI 1 should be deleted

        // Verify that the deleted documents are no longer in the document store
        assertNull(documentStore.get(uri1)); // Document 1 should not be found
        assertNotNull(documentStore.get(uri2)); // Document 2 should still exist
    }

    @Test
    public void stage4SearchByPrefixAndMetadata() throws IOException {
        DocumentStoreImpl documentStore = new DocumentStoreImpl();

        // Add documents with metadata
        URI uri1 = URI.create("http://example.com/document1");
        String text1 = "This is document 1";
        InputStream inputStream1 = new ByteArrayInputStream(text1.getBytes());
        documentStore.put(inputStream1, uri1, DocumentStore.DocumentFormat.TXT);
        documentStore.setMetadata(uri1, "type", "text");

        URI uri2 = URI.create("http://example.com/document2");
        String text2 = "This is document 2";
        InputStream inputStream2 = new ByteArrayInputStream(text2.getBytes());
        documentStore.put(inputStream2, uri2, DocumentStore.DocumentFormat.TXT);
        documentStore.setMetadata(uri2, "type", "text");

        // Search by prefix and metadata
        Map<String, String> metadataMap = new HashMap<>();
        metadataMap.put("type", "text");
        List<Document> documents = documentStore.searchByPrefixAndMetadata("doc", metadataMap);

        // Verify the results
        assertEquals(2, documents.size(), "searchByPrefixAndMetadata should return 2 matching documents");
        assertTrue(documents.stream().anyMatch(doc -> doc.getKey().equals(uri1)), "Document 1 should be in the search results");
        assertTrue(documents.stream().anyMatch(doc -> doc.getKey().equals(uri2)), "Document 2 should be in the search results");
    }

    @Test
    public void stage4SearchByMetadata() {
        DocumentStoreImpl documentStore = new DocumentStoreImpl();

        // Add a document without metadata
        URI uri = URI.create("http://example.com/document1");
        String text = "This is document 1";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        try {
            documentStore.put(inputStream, uri, DocumentStore.DocumentFormat.TXT);
        } catch (IOException e) {
            fail("IOException should not occur");
        }

        // Search by metadata that doesn't exist
        Map<String, String> metadataMap = new HashMap<>();
        metadataMap.put("type", "text");
        List<Document> documents = documentStore.searchByMetadata(metadataMap);

        // Verify the results
        assertNotNull(documents, "Search result should not be null");
        assertTrue(documents.isEmpty(), "Search result should be empty");
    }
}


