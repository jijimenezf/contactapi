package com.fullstack.contactapi;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.fullstack.contactapi.Constant.PHOTO_DIRECTORY;

@RestController
@RequestMapping(value = "/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    /*public ResponseEntity<Page<Contact>> getContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
            return ResponseEntity.ok().body(contactService.getAllContacts(page, size));
    }*/

    @GetMapping
    public ResponseEntity<List<Contact>> getContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Contact> results = contactService.getAllContacts(page, size);
        return ResponseEntity.ok(results.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable String id) {
        // Implicit exception if the contact doesn't exist
        return ResponseEntity.ok().body(contactService.getContact(id));
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact, UriComponentsBuilder ucb) {
        Contact result = contactService.createContact(contact);
        URI locationOfNew = ucb
            .path("contacts/{id}")
            .buildAndExpand(result.getId())
            .toUri();
        return ResponseEntity.created(locationOfNew).build();
    }

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        contactService.deleteContact(contactService.getContact(id));
        return ResponseEntity.noContent().build();
    }
}
