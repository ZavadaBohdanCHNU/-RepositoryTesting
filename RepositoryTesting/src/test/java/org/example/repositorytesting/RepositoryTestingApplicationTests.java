package org.example.repositorytesting;
/**
 *   @author Bohdan 
 *   @project repositorytesting
 *   @class RepositoryTestingApplicationTests.java
 *   @version 1.0
 *   @since 4/24/2025 13:56
 */


import org.example.repositorytesting.model.User;
import org.example.repositorytesting.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTestingApplicationTests {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.save(new User(null, "Ada Lovelace", "Pioneer", "###test - Computer Science"));
        underTest.save(new User(null, "Alan Turing", "Cryptanalyst", "###test - WWII Genius"));
        underTest.save(new User(null, "Grace Hopper", "Compiler", "###test - Navy legend"));
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll(underTest.findByDescriptionContaining("###test"));
    }

    @Test
    void shouldSaveItem() {
        // given
        User ada = new User(null, "Ada Lovelace", "Math", "###test - saved");
        
        // when
        User saved = underTest.save(ada);
        
        // then
        assertNotNull(saved.getId());
    }

    @Test
    void shouldFindByName() {
        // given - data prepared in setUp()
        
        // when
        List<User> result = underTest.findByName("Ada Lovelace");
        
        // then
        assertFalse(result.isEmpty());
        assertEquals("Ada Lovelace", result.get(0).getName());
    }

    @Test
    void shouldFindByCode() {
        // given - data prepared in setUp()
        
        // when
        List<User> result = underTest.findByCode("Cryptanalyst");
        
        // then
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldDeleteItem() {
        // given
        User item = new User(null, "ToDelete", "Temp", "###test");
        User saved = underTest.save(item);
        
        // when
        underTest.deleteById(saved.getId());
        boolean exists = underTest.findById(saved.getId()).isPresent();
        
        // then
        assertFalse(exists);
    }

    @Test
    void shouldFindAllItems() {
        // given - data prepared in setUp()
        
        // when
        List<User> all = underTest.findAll();
        
        // then
        assertTrue(all.size() >= 3);
    }

    @Test
    void shouldFindByDescriptionContaining() {
        // given - data prepared in setUp()
        
        // when
        List<User> found = underTest.findByDescriptionContaining("Navy");
        
        // then
        assertEquals(1, found.size());
    }

    @Test
    void shouldUpdateItem() {
        // given
        List<User> list = underTest.findByName("Alan Turing");
        assertFalse(list.isEmpty());
        User alan = list.get(0);
        
        // when
        alan.setCode("Mathematician");
        underTest.save(alan);
        User updated = underTest.findById(alan.getId())
            .orElseThrow(() -> new AssertionError("Updated item not found"));
        
        // then
        assertEquals("Mathematician", updated.getCode());
    }

    @Test
    void shouldNotFindNonExisting() {
        // given - non-existing name
        
        // when
        List<User> notFound = underTest.findByName("Non Existing");
        
        // then
        assertTrue(notFound.isEmpty());
    }

    @Test
    void shouldAssignIdAutomatically() {
        // given
        User newItem = new User(null, "New Scientist", "Physics", "###test - new");
        
        // when
        User saved = underTest.save(newItem);
        
        // then
        assertNotNull(saved.getId());
    }

    @Test
    void idsShouldBeUnique() {
        // given - data prepared in setUp()
        
        // when
        List<User> items = underTest.findByDescriptionContaining("###test");
        long uniqueCount = items.stream().map(User::getId).distinct().count();
        
        // then
        assertEquals(items.size(), uniqueCount);
    }

    @Test
    void shouldHandleNullValues() {
        // given
        User userWithNulls = new User(null, null, null, "###test");
        
        // when
        User saved = underTest.save(userWithNulls);
        
        // then
        assertNotNull(saved.getId());
        assertNull(saved.getName());
        assertNull(saved.getCode());
    }

    @Test
    void shouldHandleLongValues() {
        // given
        String longName = "a".repeat(1000);
        User userWithLongName = new User(null, longName, "Code", "###test");
        
        // when
        User saved = underTest.save(userWithLongName);
        
        // then
        assertEquals(longName, saved.getName());
    }

    @Test
    void shouldFindMultipleByDescription() {
        // given
        String commonDescription = "###test - Common";
        underTest.save(new User(null, "User1", "Code1", commonDescription));
        underTest.save(new User(null, "User2", "Code2", commonDescription));
        
        // when
        List<User> found = underTest.findByDescriptionContaining("Common");
        
        // then
        assertEquals(2, found.size());
    }

    @Test
    void shouldPerformBatchOperations() {
        // given
        List<User> users = List.of(
            new User(null, "Batch1", "Code1", "###test - batch"),
            new User(null, "Batch2", "Code2", "###test - batch"),
            new User(null, "Batch3", "Code3", "###test - batch")
        );
        
        // when
        List<User> savedUsers = underTest.saveAll(users);
        
        // then
        assertEquals(3, savedUsers.size());
        assertTrue(savedUsers.stream().allMatch(u -> u.getId() != null));
    }

    @Test
    void shouldHandleSpecialCharacters() {
        // given
        User userWithSpecialChars = new User(null, "Test@#$%", "Code!@#", "###test - спеціальні символи");
        
        // when
        User saved = underTest.save(userWithSpecialChars);
        
        // then
        assertEquals("Test@#$%", saved.getName());
    }
}
