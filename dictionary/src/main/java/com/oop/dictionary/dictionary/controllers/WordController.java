package com.oop.dictionary.dictionary.controllers;

import com.oop.dictionary.dictionary.models.ResponseObject;
import com.oop.dictionary.dictionary.models.Word;
import com.oop.dictionary.dictionary.repositories.WordRepository;
import com.oop.dictionary.dictionary.services.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080/")
@RestController
@RequestMapping(path = "api/v1/Words")
public class WordController {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private WordService wordService;

    @CrossOrigin(origins = "http://localhost:8080/")
    @RequestMapping(method = RequestMethod.GET, path = "/{character}")
    public ResponseEntity<ResponseObject> fildAllWords(@PathVariable String character) {
        return ResponseEntity.status(HttpStatus.OK).body(wordService.findWords(character));

    }
    @CrossOrigin(origins = "http://localhost:8080/")
    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertWord(@RequestBody Word word) {
        return ResponseEntity.status(HttpStatus.OK).body(wordService.insertWord(word));
    }

    @CrossOrigin(origins = "http://localhost:8080/")
    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateWord(@RequestBody Word newWord) {
        return ResponseEntity.status(HttpStatus.OK).body(wordService.updateWord(newWord));
    }

    @CrossOrigin(origins = "http://localhost:8080/")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteWord(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(wordService.deleteWord(id));
    }


}
