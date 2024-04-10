import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentImplTest {

    @Test
    public void testDocumentTxt() {
        String text = "This is a test document.";
        Document document = new DocumentImpl(URI.create("https://example.com/document"), text);

        assertEquals(text, document.getDocumentTxt());
    }

    @Test
    public void testDocumentBinaryData() {
        byte[] binaryData = {0x00, 0x01, 0x02, 0x03};
        Document document = new DocumentImpl(URI.create("https://example.com/document"), binaryData);

        assertArrayEquals(binaryData, document.getDocumentBinaryData());
    }

    @Test
    public void testGetMetadataValue() {
        Document document = new DocumentImpl(URI.create("https://example.com/document"), "This is a test document.".getBytes());
        document.setMetadataValue("author", "John Doe");
        document.setMetadataValue("date", "2022-10-15");

        assertEquals("John Doe", document.getMetadataValue("author"));
        assertEquals("2022-10-15", document.getMetadataValue("date"));

    }


    @Test
    public void testGetDocumentBinaryData_NullBinaryData() {
        // Arrange
        URI uri = URI.create("http://example.com/document");
        DocumentImpl document = new DocumentImpl(uri, (byte[]) null);

        // Act
        byte[] binaryData = document.getDocumentBinaryData();

        // Assert
        assertNull(binaryData, "Binary data should be null when binaryData is null");
    }


    //new test methods
//test for the first error1
    @Test
    public void testTextDocumentHashCode() {
        URI uri = URI.create("http://www.example.com/document1");
        String text = "This is a test document.";
        DocumentImpl document = new DocumentImpl(uri, text);

        // Calculate the expected hash code manually based on the text content
        int expectedHashCode = text.hashCode();

        // Get the actual hash code calculated by the DocumentImpl class
        int actualHashCode = document.hashCode();

        // Assert that the actual hash code matches the expected hash code
        assertEquals(expectedHashCode, actualHashCode, "Document hashCode calculation is incorrect for text documents");
    }

    //test for the second error2
    @Test
    public void testBinaryDocumentHashCode() {
        URI uri = URI.create("http://example.com/document");
        byte[] binaryData = {0, 1, 2, 3}; // Sample binary data
        Document document = new DocumentImpl(uri, binaryData);

        // Calculate the expected hash code manually
        int expectedHashCode = binaryData.hashCode();

        // Get the actual hash code from the document
        int actualHashCode = document.getDocumentBinaryData() != null ? document.getDocumentBinaryData().hashCode() : 0;

        // Assert that the actual hash code matches the expected hash code
        assertEquals(expectedHashCode, actualHashCode, "Hash code should match for binary document");
    }
//test for stage 4//////////////////////////////////////////////////////////////////////////////////////////////////////
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
        assertEquals(9, words.size()); // Unique words count

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
        int wordCount = document.wordCount("brown");

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
}







