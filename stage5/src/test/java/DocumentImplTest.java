import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static java.lang.reflect.Array.get;
import static org.junit.jupiter.api.Assertions.*;

public class DocumentImplTest {
    private DocumentImpl textDocument;
    private DocumentImpl binaryDocument;

    public void setUp() {
        // Create a text document
        URI textUri = URI.create("text://document1");
        String textContent = "the quick brown fox jumps over the lazy dog";
        textDocument = new DocumentImpl(textUri, textContent);

        // Create a binary document
        URI binaryUri = URI.create("binary://document2");
        byte[] binaryData = { 0x12, 0x34, 0x56, 0x78 };
        binaryDocument = new DocumentImpl(binaryUri, binaryData);
    }

    @Test
    public void testWordCount() {
        setUp(); // Initialize test setup
        // Test word count in text document
        assertEquals(2, textDocument.wordCount("the"));
        assertEquals(1, textDocument.wordCount("quick"));
        assertEquals(0, textDocument.wordCount("apple")); // Word not present

        // Test word count in binary document
        assertEquals(0, binaryDocument.wordCount("any")); // Binary document, return 0
    }
    /////tests after stage 4 results/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testGetWords() {
        setUp(); // Initialize test setup
        // Test getting words from text document
        Set<String> words = textDocument.getWords();
        assertTrue(words.contains("quick"));
        assertTrue(words.contains("brown"));
        assertTrue(words.contains("dog"));
        assertEquals(8, words.size()); // Unique words count

        // Test getting words from binary document (empty set expected)
        Set<String> binaryWords = binaryDocument.getWords();
        assertTrue(binaryWords.isEmpty());
    }

    @Test
    public void testWordCountCaseSensitive() {
        // Create a document with text "This is a Test test tEsT"
        DocumentImpl document = new DocumentImpl(URI.create("http://example.com/document1"), "This is a Test test tEsT");

        // Test case-sensitive word count
        assertEquals(1, document.wordCount("Test"));
        assertEquals(1, document.wordCount("test"));
        assertEquals(1, document.wordCount("tEsT"));
        assertEquals(0, document.wordCount("TEST")); // Not case-sensitive
        // assertEquals(0, document.wordCount(null)); // Empty word should return 0
        // assertThrows(IllegalArgumentException.class, () -> document.wordCount(null)); // Null word should throw exception
    }

    @Test
    public void stage4WordCount() {
        // Create a DocumentImpl instance with text
        DocumentImpl document = new DocumentImpl(URI.create("test-uri"), "The quick brown fox jumps over the lazy dog");

        // Test word count for a specific word
        int wordCount = document.wordCount("fox");

        // Assert that the word count matches the expected value
        assertEquals(1, wordCount);
    }


    @Test
    public void testGetTextDocumentTextHashCode() {
        // Create a text document
        URI uri = URI.create("http://example.com/document1");
        String text = "This is a test document.";
        DocumentImpl document = new DocumentImpl(uri, text);

        // Calculate the expected hash code manually
        int expectedHashCode = text.hashCode();

        // Get the actual hash code from the document
        int actualHashCode = document.hashCode();

        // Print out the actual hash code for debugging
        System.out.println("Actual Hash Code: " + actualHashCode);

        // Check if the actual hash code matches the expected hash code
        assertEquals(expectedHashCode, actualHashCode, "Document hashCode is incorrect for text document");
    }

    @Test
    public void testHashCodeForBinaryDocument() {
        // Create a URI for the document
        String uriString = "http://example.com/document";
        URI uri = URI.create(uriString);

        // Create binary data for the document
        byte[] binaryData = {1, 2, 3, 4, 5};

        // Create a binary document
        DocumentImpl document = new DocumentImpl(uri, binaryData);

        // Calculate the expected hash code for the binary document
        int expectedHashCode = Arrays.hashCode(binaryData);

        // Check if the actual hash code matches the expected hash code
        assertEquals(expectedHashCode, document.hashCode(), "Document hashCode is incorrect for binary document");
    }
//stage 5///////////////////////////////////////////////////////////////////////////////////////////////////////////////

//



//    private DocumentStoreImpl store;
//
//    @BeforeEach
//    void setUp() {
//        store = new DocumentStoreImpl();
//        // Adding documents with known content and metadata
//        try {
//            URI uri1 = new URI("http://example.com/doc1");
//            URI uri2 = new URI("http://example.com/doc2");
//            String text1 = "Example text for testing metadata filtering";
//            String text2 = "Test text that also includes metadata";
//
//            ByteArrayInputStream input1 = new ByteArrayInputStream(text1.getBytes());
//            ByteArrayInputStream input2 = new ByteArrayInputStream(text2.getBytes());
//
//            store.put(input1, uri1, DocumentStore.DocumentFormat.TXT);
//            store.put(input2, uri2, DocumentStore.DocumentFormat.TXT);
//
//            store.setMetadata(uri1, "author", "John Doe");
//            store.setMetadata(uri2, "author", "Jane Doe");
//            store.setMetadata(uri1, "topic", "Test");
//            store.setMetadata(uri2, "topic", "Example");
//        } catch (Exception e) {
//            fail("Setup failed due to exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void testSearchByPrefixAndMetadata() throws IOException {
//        URI uri1 = URI.create("http://example.com/doc1");
//        URI uri2 = URI.create("http://example.com/doc2");
//        String content1 = "Example content one";
//        String content2 = "Example content two";
//        store.put(new ByteArrayInputStream(content1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
//        store.put(new ByteArrayInputStream(content2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
//
//        store.setMetadata(uri1, "author", "John Doe");
//        store.setMetadata(uri2, "author", "Jane Doe");
//
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("author", "John Doe");
//        List<Document> results = store.searchByPrefixAndMetadata("Examp", metadata);
//
//        assertEquals(1, results.size(), "Should return exactly one document matching both prefix and metadata");
//        assertEquals(uri1, results.get(0).getKey(), "The returned document should have the URI of the first document");
//    }
//
//    @Test
//    void testSearchByKeywordAndMetadata() {
//        Map<String, String> metadataCriteria = new HashMap<>();
//        metadataCriteria.put("author", "John Doe");
//
//        List<Document> results = store.searchByKeywordAndMetadata("Example", metadataCriteria);
//        assertEquals(1, results.size(), "Should return exactly one document matching keyword and metadata.");
//        assertTrue(results.get(0).getKey().toString().contains("doc1"), "The URI should correspond to the first document.");
//    }


}



















