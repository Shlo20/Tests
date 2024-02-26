import Impl.DocumentImpl;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.testng.AssertJUnit.assertEquals;

public class DocumentImplTest {

    @Test
    public void testSetAndGetMetadata() {
        // Create a DocumentImpl instance
        DocumentImpl document = new DocumentImpl(URI.create("https://example.com/doc1"), "Test document");

        // Set metadata
        assertNull(document.setMetadataValue("key1", "value1"));

        // Get metadata
        assertEquals("value1", document.getMetadataValue("key1"));
    }

    //version w one test
}
