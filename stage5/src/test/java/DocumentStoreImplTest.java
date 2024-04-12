import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
//new tests after stage 4 results/ / / / ///////////////////////////////////////////////////////////////////////////////







    @Test
    public void testPutNewVersionOfDocumentTxt() {
        DocumentStoreImpl documentStore = new DocumentStoreImpl();
        URI uri = URI.create("http://example.com/document1");
        try {
            // Attempt IO operation that can throw IOException
            documentStore.put(new ByteArrayInputStream("Test document".getBytes()), uri, DocumentStore.DocumentFormat.TXT);

            // Test putting a new version of a text document
            int oldHashCode = documentStore.put(new ByteArrayInputStream("Updated document".getBytes()), uri, DocumentStore.DocumentFormat.TXT);
            assertEquals(true, oldHashCode != 0);
        } catch (IOException e) {
            // Handle IOException
            fail("IOException occurred: " + e.getMessage());
        }

    }

    @Test
    public void testGetBinaryDocAsTxt() throws IOException {
        // Create a URI and binary data
        URI uri = URI.create("http://www.example.com/doc1");
        byte[] binaryData = "Binary data".getBytes();

        // Create a binary document
        Document document = new DocumentImpl(uri, binaryData);

        // Ensure that the document is binary
        assertNotNull(document.getDocumentBinaryData());
        assertNull(document.getDocumentTxt());

        // Test that trying to get the text of a binary doc returns null
        assertNull(document.getDocumentTxt());
    }

    @Test
    public void stage4SearchTest() {
        // Create a DocumentStoreImpl instance
        DocumentStoreImpl documentStore = new DocumentStoreImpl();

        // Add documents to the document store
        URI uri1 = URI.create("document1");
        InputStream inputStream1 = new ByteArrayInputStream("This is document one".getBytes());
        try {
            documentStore.put(inputStream1, uri1, DocumentStore.DocumentFormat.TXT);
        } catch (IOException e) {
            fail("IOException while putting document1");
        }

        URI uri2 = URI.create("document2");
        InputStream inputStream2 = new ByteArrayInputStream("This is document two".getBytes());
        try {
            documentStore.put(inputStream2, uri2, DocumentStore.DocumentFormat.TXT);
        } catch (IOException e) {
            fail("IOException while putting document2");
        }

        // Perform search for keyword "document"
        List<Document> searchResults = documentStore.search("document");

        // Debugging output
        System.out.println("Search Results for keyword 'document':");
        for (Document doc : searchResults) {
            System.out.println(doc.getKey());
        }

        // Assert the number of search results
        assertEquals(2, searchResults.size(), "expected 2 matches, received " + searchResults.size());

        // Additional test for searching with a specific keyword "document one"
        List<Document> specificSearchResults = documentStore.search("document one");

        // Debugging output
        System.out.println("Search Results for keyword 'document one':");
        for (Document doc : specificSearchResults) {
            System.out.println(doc.getKey());
        }

    }

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

        // Print to verify documents are indexed correctly
        System.out.println("Document 1 metadata: " + documentStore.getMetadata(uri1, "type"));
        System.out.println("Document 2 metadata: " + documentStore.getMetadata(uri2, "type"));

        // Search by prefix and metadata
        Map<String, String> metadataMap = new HashMap<>();
        metadataMap.put("type", "text");
        List<Document> documents = documentStore.searchByPrefixAndMetadata("This", metadataMap);

        // Debug output
        System.out.println("Documents found: " + documents.size());
        for (Document doc : documents) {
            System.out.println("Doc URI: " + doc.getKey() + ", Type: " + doc.getMetadataValue("type"));
        }

        // Verify the results
        assertEquals(2, documents.size());
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

    @Test
    public void stage4SearchBinary() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri1 = URI.create("http://doc1");
        URI uri2 = URI.create("http://doc2");
        byte[] data1 = {1, 2, 3, 4};
        byte[] data2 = {5, 6, 7, 8};
        store.put(new ByteArrayInputStream(data1), uri1, DocumentStore.DocumentFormat.BINARY);
        store.put(new ByteArrayInputStream(data2), uri2, DocumentStore.DocumentFormat.BINARY);

        List<Document> results = store.search("someText");
        assertEquals(0, results.size(), "expected 2 matches, received " + results.size());
    }

    @Test
    public void stage4SearchBinaryByPrefix() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();

        // Adding binary documents
        URI binaryUri1 = URI.create("http://binary1");
        URI binaryUri2 = URI.create("http://binary2");
        byte[] binaryData1 = {0, 1, 2, 3};
        byte[] binaryData2 = {4, 5, 6, 7};
        store.put(new ByteArrayInputStream(binaryData1), binaryUri1, DocumentStore.DocumentFormat.BINARY);
        store.put(new ByteArrayInputStream(binaryData2), binaryUri2, DocumentStore.DocumentFormat.BINARY);

        // Adding text documents that should match the prefix search
        URI textUri1 = URI.create("http://text1");
        URI textUri2 = URI.create("http://text2");
        String text1 = "apple banana carrot";
        String text2 = "apple berry cherry";
        store.put(new ByteArrayInputStream(text1.getBytes()), textUri1, DocumentStore.DocumentFormat.TXT);
        store.put(new ByteArrayInputStream(text2.getBytes()), textUri2, DocumentStore.DocumentFormat.TXT);

        // Search by prefix that matches the text in the text documents
        List<Document> results = store.searchByPrefix("app");
        assertEquals(2, results.size(), "expected 2 matches, received " + results.size());

        // Verify that the returned documents are indeed the text documents
        assertTrue(results.stream().anyMatch(doc -> textUri1.equals(doc.getKey())));
        assertTrue(results.stream().anyMatch(doc -> textUri2.equals(doc.getKey())));
    }

    @Test
    public void stage4SearchByKeywordAndMetadata() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();

        // Adding documents with metadata and content that should match the search criteria
        URI uri1 = URI.create("http://doc1");
        URI uri2 = URI.create("http://doc2");
        String text1 = "apple banana carrot";
        String text2 = "apple berry cherry";
        Document doc1 = new DocumentImpl(uri1, text1);
        Document doc2 = new DocumentImpl(uri2, text2);
        doc1.setMetadataValue("author", "John Doe");
        doc2.setMetadataValue("author", "Jane Doe");

        store.put(new ByteArrayInputStream(text1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        store.put(new ByteArrayInputStream(text2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        store.setMetadata(uri1, "author", "John Doe");
        store.setMetadata(uri2, "author", "Jane Doe");

        // Search criteria: keyword 'apple' and metadata with author 'John Doe'
        Map<String, String> metadataCriteria = new HashMap<>();
        metadataCriteria.put("author", "John Doe");
        List<Document> results = store.searchByKeywordAndMetadata("apple", metadataCriteria);

        assertEquals(1, results.size(), "searchByKeywordAndMetadata should've returned 1 matching document, instead returned " + results.size());
        assertEquals(uri1, results.get(0).getKey(), "The returned document does not match the expected URI");
    }

    @Test
    public void stage4SearchTxtByPrefix() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();

        // Adding text documents that should match the prefix search
        URI uri1 = URI.create("http://doc1");
        URI uri2 = URI.create("http://doc2");
        String text1 = "apple banana";
        String text2 = "application orange";
        store.put(new ByteArrayInputStream(text1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        store.put(new ByteArrayInputStream(text2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);

        // Adding a text document that should not match the prefix search
        URI uri3 = URI.create("http://doc3");
        String text3 = "berry banana";
        store.put(new ByteArrayInputStream(text3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT);

        // Perform a prefix search for 'app'
        List<Document> results = store.searchByPrefix("app");

        // Assert that the correct number of documents (2 in this case) is returned
        assertEquals(2, results.size(), "expected 2 matches, received " + results.size());

        // Verify that the returned documents are the correct ones
        assertTrue(results.stream().anyMatch(doc -> uri1.equals(doc.getKey())), "Document with URI1 should be in the results");
        assertTrue(results.stream().anyMatch(doc -> uri2.equals(doc.getKey())), "Document with URI2 should be in the results");
    }

    @Test
    public void stage4DeleteAllWithPrefixAndMetadata() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();

        // Adding documents that match the prefix and metadata criteria
        URI uri1 = URI.create("http://doc1");
        URI uri2 = URI.create("http://doc2");
        String text1 = "apple banana carrot";
        String text2 = "apple berry cherry";
        store.put(new ByteArrayInputStream(text1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        store.put(new ByteArrayInputStream(text2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        store.setMetadata(uri1, "author", "John Doe");
        store.setMetadata(uri2, "author", "John Doe");

        // Adding a document that does not match the prefix and metadata criteria
        URI uri3 = URI.create("http://doc3");
        String text3 = "application orange";
        store.put(new ByteArrayInputStream(text3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT);
        store.setMetadata(uri3, "author", "Jane Doe");

        // Delete documents with prefix 'app' and metadata author 'John Doe'
        Map<String, String> metadataCriteria = new HashMap<>();
        metadataCriteria.put("author", "John Doe");
        Set<URI> deletedURIs = store.deleteAllWithPrefixAndMetadata("app", metadataCriteria);

        // Assert that 2 URIs are returned from the delete operation
        assertEquals(2, deletedURIs.size(), "deleteAllWithPrefixAndMetadata should've returned 2 deleted URIs, instead returned " + deletedURIs.size());

        // Verify that the correct URIs were deleted
        assertTrue(deletedURIs.contains(uri1), "URI1 should be deleted");
        assertTrue(deletedURIs.contains(uri2), "URI2 should be deleted");
    }

//undo tests stage 4////////////////////////////////////////////////////////////////////////////////////////////////////
@Test
public void stage4PlainUndoThatImpactsMultiple() throws IOException {
    DocumentStoreImpl store = new DocumentStoreImpl();
    URI uri1 = URI.create("http://edu.yu.cs/com1320/project/doc1");
    URI uri2 = URI.create("http://edu.yu.cs/com1320/project/doc2");
    store.put(new ByteArrayInputStream("Document 1".getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
    store.put(new ByteArrayInputStream("Document 2".getBytes()), uri2, DocumentStore.DocumentFormat.TXT);

    store.deleteAll("Document");

    store.undo();  // This should restore both documents

    Document doc1 = store.get(uri1);
    Document doc2 = store.get(uri2);
    assertNotNull(doc1, "document with URI " + uri1 + " should've been restored");
    assertNotNull(doc2, "document with URI " + uri2 + " should've been restored");
    assertEquals("Document 1", doc1.getDocumentTxt());
    assertEquals("Document 2", doc2.getDocumentTxt());
}


    @Test
    public void stage4UndoMetadataOneDocumentThenSearch() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        // Add a document with metadata
        URI uri = URI.create("http://edu.yu.cs/com1320/project/doc1");
        store.put(new ByteArrayInputStream("Document 1".getBytes()), uri, DocumentStore.DocumentFormat.TXT);
        store.setMetadata(uri, "author", "John Doe");

        // Delete by metadata
        store.deleteAllWithMetadata(Collections.singletonMap("author", "John Doe"));

        // Undo the delete
        store.undo();

        // Search by metadata should return the restored document
        List<Document> searchResults = store.searchByMetadata(Collections.singletonMap("author", "John Doe"));
        assertFalse(searchResults.isEmpty(), "Search should return at least one document");
        assertEquals(uri, searchResults.get(0).getKey(), "The restored document should be searchable by its metadata");
    }

    @Test
    public void stage4UndoByURIThatImpactsEarlierThanLast() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        // Add documents
        URI uri1 = URI.create("http://edu.yu.cs/com1320/project/doc1");
        URI uri2 = URI.create("http://edu.yu.cs/com1320/project/doc2");
        store.put(new ByteArrayInputStream("Document 1".getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        store.put(new ByteArrayInputStream("Document 2".getBytes()), uri2, DocumentStore.DocumentFormat.TXT);

        // Delete documents individually
        store.delete(uri1);
        store.delete(uri2);

        // Undo the delete operation for the first document
        store.undo(uri1);

        // The first document should be restored
        assertNotNull(store.get(uri1), "document with URI " + uri1 + " should've been restored");
        assertNull(store.get(uri2), "document with URI " + uri2 + " should still be deleted");
    }

    @Test
    public void stage4UndoByURIThatImpactsOne() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        // Add documents
        URI uri1 = URI.create("http://edu.yu.cs/com1320/project/doc1");
        URI uri2 = URI.create("http://edu.yu.cs/com1320/project/doc2");
        store.put(new ByteArrayInputStream("Document 1".getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        store.put(new ByteArrayInputStream("Document 2".getBytes()), uri2, DocumentStore.DocumentFormat.TXT);

        // Delete all documents with a common keyword
        store.deleteAll("Document");

        // Undo the delete operation for the first document only
        store.undo(uri1);

        // Check if the first document is restored and the second is still deleted
        assertNotNull(store.get(uri1), "document with URI " + uri1 + " should've been restored");
        assertNull(store.get(uri2), "document with URI " + uri2 + " should still be deleted");
    }



//stage 5 tests ////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Test
    public void testMemoryManagement() throws IOException {
        // Create a DocumentStore
        DocumentStore documentStore = new DocumentStoreImpl();

        // Set max document bytes limit
        documentStore.setMaxDocumentBytes(15); // Limit is set to 15 bytes

        // Add documents until memory limit is reached
        URI uri1 = URI.create("uri1");
        InputStream inputStream1 = new ByteArrayInputStream("Document 1".getBytes());
        documentStore.put(inputStream1, uri1, DocumentStore.DocumentFormat.TXT);

        URI uri2 = URI.create("uri2");
        InputStream inputStream2 = new ByteArrayInputStream("Document 2".getBytes());
        documentStore.put(inputStream2, uri2, DocumentStore.DocumentFormat.TXT);

        URI uri3 = URI.create("uri3");
        InputStream inputStream3 = new ByteArrayInputStream("Document 3".getBytes());
        documentStore.put(inputStream3, uri3, DocumentStore.DocumentFormat.TXT);

        // Check if memory limit is enforced by adding another document
        URI uri4 = URI.create("uri4");
        InputStream inputStream4 = new ByteArrayInputStream("Document 4".getBytes());
        int hashCode = documentStore.put(inputStream4, uri4, DocumentStore.DocumentFormat.TXT);

        // Ensure that the least recently used document (Document 1) is deleted to comply with the memory limit
        assertNull(documentStore.get(uri1));
    }

//    @Test
//    public void testSetMetadata() throws IOException {
//        // Create a DocumentStore
//        DocumentStore documentStore = new DocumentStoreImpl();
//
//        // Add a document
//        URI uri = URI.create("uri1");
//        InputStream inputStream = new ByteArrayInputStream("Document 1".getBytes());
//        documentStore.put(inputStream, uri, DocumentStore.DocumentFormat.TXT);
//
//        // Set metadata
//        String key = "author";
//        String value = "John Doe";
//        documentStore.setMetadata(uri, key, value);
//
//        // Retrieve the document and check if metadata is set correctly
//        Document document = documentStore.get(uri);
//        assertNotNull(document);
//        assertEquals(value, document.getMetadataValue(key));
//    }

    @Test
    public void testSearchByMetadata() throws IOException {
        // Create a DocumentStore
        DocumentStore documentStore = new DocumentStoreImpl();

        // Add documents with metadata
        URI uri1 = URI.create("uri1");
        InputStream inputStream1 = new ByteArrayInputStream("Document 1".getBytes());
        documentStore.put(inputStream1, uri1, DocumentStore.DocumentFormat.TXT);
        documentStore.setMetadata(uri1, "author", "John Doe");

        URI uri2 = URI.create("uri2");
        InputStream inputStream2 = new ByteArrayInputStream("Document 2".getBytes());
        documentStore.put(inputStream2, uri2, DocumentStore.DocumentFormat.TXT);
        documentStore.setMetadata(uri2, "author", "Jane Smith");

        // Search by metadata
        Map<String, String> metadataSearch = new HashMap<>();
        metadataSearch.put("author", "John Doe");
        assertEquals(1, documentStore.searchByMetadata(metadataSearch).size());
    }

    @Test
    public void testPutAndGetDocument() throws Exception {
        DocumentStoreImpl store = new DocumentStoreImpl();
        URI uri = new URI("http://test.com");
        String content = "Hello World";
        store.put(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), uri, DocumentStore.DocumentFormat.TXT);

        Document retrieved = store.get(uri);
        assertNotNull(retrieved);
        assertEquals("Hello World", retrieved.getDocumentTxt());
        assertEquals(uri, retrieved.getKey());
    }
//stage 5 tests/////////////////////////////////////////////////////////////////////////////////////////////////////////
    private DocumentStoreImpl store;
    private URI uri1, uri2;

    @BeforeEach
    public void setUp() throws Exception {
        store = new DocumentStoreImpl();
        uri1 = new URI("http://doc1.com");
        uri2 = new URI("http://doc2.com");
    }

    @Test
    public void testSetMetadata() throws Exception {
        String content = "Hello World";
        store.put(new ByteArrayInputStream(content.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        assertNull(store.setMetadata(uri1, "author", "John Doe"));
        assertEquals("John Doe", store.getMetadata(uri1, "author"));
    }

    @Test
    public void testSearch() throws Exception {
        store.put(new ByteArrayInputStream("Hello World".getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        store.put(new ByteArrayInputStream("Hello Universe".getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        List<Document> results = store.search("Hello");
        assertEquals(2, results.size());
    }

    @Test
    public void testUndoSpecificURI() throws Exception {
        store.put(new ByteArrayInputStream("Hello World".getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        store.delete(uri1);
        store.undo(uri1);
        assertNotNull(store.get(uri1));
    }

    @Test
    public void testMemoryLimit() throws Exception {
        store.setMaxDocumentCount(1);
        store.put(new ByteArrayInputStream("Hello World".getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        store.put(new ByteArrayInputStream("Hello Universe".getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        assertNull(store.get(uri1));
        assertNotNull(store.get(uri2));
    }

}