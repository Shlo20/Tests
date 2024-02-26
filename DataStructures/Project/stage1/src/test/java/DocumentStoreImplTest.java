import Impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;


public class DocumentStoreImplTest {

    @Test
    public void testPutAndGet() throws Exception {
        DocumentStoreImpl store = new DocumentStoreImpl();

        // Put a document
        InputStream inputStream = new ByteArrayInputStream("Test document".getBytes());
        URI uri = URI.create("https://example.com/doc1");
        assertEquals(0, store.put(inputStream, uri, DocumentStore.DocumentFormat.TXT));

        // Get the document
        Document document = store.get(uri);
        assertNotNull(document);
        assertEquals("Test document", document.getDocumentTxt());
    }

}

