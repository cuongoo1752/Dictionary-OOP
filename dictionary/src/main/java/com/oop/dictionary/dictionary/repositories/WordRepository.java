package com.oop.dictionary.dictionary.repositories;

import com.oop.dictionary.dictionary.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Integer> {
    List<Word> findByWord(String word);
}
