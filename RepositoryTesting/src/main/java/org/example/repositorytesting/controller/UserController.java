package org.example.repositorytesting.controller;

import org.example.repositorytesting.model.User;
import org.example.repositorytesting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class UserController {

    @Autowired
    private UserService itemService;

    @GetMapping
    public List<User> getAll() {
        return itemService.getAll();
    }

    @PostMapping
    public ResponseEntity<User> createItem(@RequestBody User item) {
        User saved = itemService.save(item);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/search")
    public List<User> searchByName(@RequestParam String name) {
        return itemService.findByName(name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
