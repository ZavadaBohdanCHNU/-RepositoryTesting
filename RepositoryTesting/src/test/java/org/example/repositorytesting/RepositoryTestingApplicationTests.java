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
        User ada = new User(null, "Ada Lovelace", "Math", "###test - saved");
        User saved = underTest.save(ada);
        assertNotNull(saved.getId());
    }

    @Test
    void shouldFindByName() {
        List<User> result = underTest.findByName("Ada Lovelace");
        assertFalse(result.isEmpty());
        assertEquals("Ada Lovelace", result.get(0).getName());
    }

    @Test
    void shouldFindByCode() {
        List<User> result = underTest.findByCode("Cryptanalyst");
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldDeleteItem() {
        User item = new User(null, "ToDelete", "Temp", "###test");
        User saved = underTest.save(item);
        underTest.deleteById(saved.getId());

        boolean exists = underTest.findById(saved.getId()).isPresent();
        assertFalse(exists);
    }


    @Test
    void shouldFindAllItems() {
        List<User> all = underTest.findAll();
        assertTrue(all.size() >= 3);
    }

    @Test
    void shouldFindByDescriptionContaining() {
        List<User> found = underTest.findByDescriptionContaining("Navy");
        assertEquals(1, found.size());
    }

    @Test
    void shouldUpdateItem() {
        List<User> list = underTest.findByName("Alan Turing");
        assertFalse(list.isEmpty());
        User alan = list.get(0);
        alan.setCode("Mathematician");
        underTest.save(alan);

        User updated = underTest.findById(alan.getId()).orElseThrow(() -> new AssertionError("Updated item not found"));
        assertEquals("Mathematician", updated.getCode());
    }


    @Test
    void shouldNotFindNonExisting() {
        List<User> notFound = underTest.findByName("Non Existing");
        assertTrue(notFound.isEmpty());
    }

    @Test
    void shouldAssignIdAutomatically() {
        User newItem = new User(null, "New Scientist", "Physics", "###test - new");
        User saved = underTest.save(newItem);
        assertNotNull(saved.getId());
    }


    @Test
    void idsShouldBeUnique() {
        List<User> items = underTest.findByDescriptionContaining("###test");
        long uniqueCount = items.stream().map(User::getId).distinct().count();
        assertEquals(items.size(), uniqueCount);
    }
}
