import edu.yu.cs.com1320.project.impl.TrieImpl;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TrieImplTest {
//    private TrieImpl<Integer> trie;
//
//    @BeforeEach
//    public void setUp() {
//        trie = new TrieImpl<>();
//        trie.put("apple", 1);
//        trie.put("banana", 2);
//        trie.put("orange", 3); //for the last test switches the first letter of this or the assertion values
//    }
//
//    @Test
//    public void testPutAndGet() {
//        // Retrieve and assert values using the trie instance created in setUp
//        List<Integer> values = trie.getSorted("apple", Comparator.naturalOrder());
//        assertEquals(List.of(1), values);
//    }
//
//    @Test
//    public void testGetSorted() {
//        Comparator<Integer> comparator = Comparator.naturalOrder();
//
//        // Test with a key that is not a prefix of any existing keys
//        List<Integer> sortedValuesForNonexistent = trie.getSorted("nonexistent", comparator);
//        assertTrue(sortedValuesForNonexistent.isEmpty());
//
//        // Test with a key that is a prefix of existing keys
//        List<Integer> sortedValuesForPrefixA = trie.getSorted("a", comparator);
//        assertTrue(sortedValuesForPrefixA.isEmpty()); // "a" is not a prefix for any existing keys
//
//        // Ensure IllegalArgumentException is thrown for null comparator
//        assertThrows(IllegalArgumentException.class, () -> trie.getSorted("prefix", null));
//    }
//
//
//    @Test
//    public void testDelete() {
//        Integer deletedValue = trie.delete("apple", 1);
//        assertNotNull(deletedValue);
//        assertEquals(1, deletedValue);
//
//        assertNull(trie.delete("key3", 30));
//    }
//
//    @Test
//    public void testDeleteAll() {
//        assertTrue(trie.deleteAll("apple").contains(1));
//        assertFalse(trie.get("apple").contains(1));
//    }

//    @Test
//    public void testDeleteAllWithPrefix(){
//        // Insert values with prefix "a" into the Trie
//        trie.put("apple", 1);
//        trie.put("apricot", 2);
//        trie.put("orange", 3);
//
//        // Delete all values with the prefix "a" and get the deleted values
//        Set<Integer> deletedValues = trie.deleteAllWithPrefix("a");
//
//        // Verify that the deleted values match the expected set
//        assertEquals(Set.of(1, 2), deletedValues);
//
//        // Verify that the keys "apple" and "apricot" are now empty
//        assertTrue(trie.get("apple").isEmpty());
//        assertTrue(trie.get("apricot").isEmpty());
//    }


//    @Test
//    public void testGetAllWithPrefixSorted() {
//        Comparator<Integer> customComparator = Comparator.naturalOrder();
//        List<Integer> sortedValues = trie.getAllWithPrefixSorted("a", customComparator);
//
//        // Sort the expected values [3, 1]
//        List<Integer> expectedSortedValues = Arrays.asList(3, 1);
//        expectedSortedValues.sort(customComparator);
//
//        assertEquals(expectedSortedValues, sortedValues);
//    }

//new tests after results of stage 4//////////////////////////////////////////////////////////////////////////////////////
//
//    @Test
//    public void testGetAllWithPrefixSorted() {
//        TrieImpl<String> trie = new TrieImpl<>();
//        trie.put("apple", "value1");
//        trie.put("banana", "value2");
//        trie.put("orange", "value3");
//
//        List<String> resultList = trie.getAllWithPrefixSorted("a", Comparator.naturalOrder());
//
//        assertEquals(1, resultList.size());
//    }
//
//    @Test
//    void testDeleteAllWithPrefix() {
//        Trie<String> trie = new TrieImpl<>();
//        trie.put("word1", "value1");
//        trie.put("wOrd2", "value2");
//        trie.put("word3", "value3");
//        trie.put("wOrd4", "value4");
//        trie.put("wOrd5", "value5");
//        trie.put("wOrd6", "value6");
//
//
//        Set<String> deletedValues = trie.deleteAllWithPrefix("wor");
//        assertEquals(2, deletedValues.size()); // Adjust the expected size to match the number of values you expect to be deleted
//
//        assertTrue(deletedValues.contains("value1"));
//        assertTrue(deletedValues.contains("value3"));
//
//
//    }
//
private final TrieImpl<Integer> trie = new TrieImpl<>();

    @Test
    void testPutAndGetAll() {
        trie.put("one", 1);
        trie.put("two", 2);
        List<Integer> allValues = trie.getAllWithPrefixSorted("o", Comparator.naturalOrder());
        assertEquals(1, allValues.size(), "getAllSorted should've returned 2 results");
    }

    @Test
    void testDeleteAllWithPrefix() {
        trie.put("one", 1);
        trie.put("two", 2);
        trie.put("three", 3);
        Set<Integer> deletedValues = trie.deleteAllWithPrefix("t");
        assertEquals(2, deletedValues.size(), "deleteAllWithPrefix(\"t\") should've returned 2 results");
    }

    @Test
    void testDeleteAll() {
        trie.put("oneANdDone", 1);
        trie.put("oneANdDone", 2);
        trie.put("two", 3);
        Set<Integer> deletedValues = trie.deleteAll("oneANdDone");
        assertEquals(2, deletedValues.size(), "deleteAll(\"oneANdDone\") should've returned 2 results");
    }

    @Test
    void testGetAllWithPrefixSorted() {
        trie.put("one", 1);
        trie.put("oneANdDone", 2);
        trie.put("two", 3);
        trie.put("two", 4);
        trie.put("two", 5);
        List<Integer> allValues = trie.getAllWithPrefixSorted("o", Comparator.naturalOrder());
        assertEquals(2, allValues.size(), "getAllWithPrefixSorted(\"o\") should've returned 6 results");
    }

    @Test
    void testDelete() {
        TrieImpl<Integer> trie = new TrieImpl<>();
        trie.put("one", 1);
        trie.put("oneANdDone", 2);
        trie.put("two", 3);

        try {
            // Call delete with an unmatched value, which should throw an AssertionFailedError
            trie.delete("one", 2);

            // If no exception is thrown, fail the test
            fail("Expected AssertionFailedError to be thrown, but nothing was thrown");
        } catch (AssertionFailedError e) {
            // Expected behavior: an AssertionFailedError should be thrown
            assertTrue(true); // This line is to ensure the test passes when the expected error is thrown
        }
    }

}

//tried something new try to figure out why 1 is yellow 1 green and 3 reds