package com.serverlesswordle.repository;

import com.serverlesswordle.model.dto.WordDTO;

import java.util.List;
import java.util.Optional;

public interface WordRepository {

    List<WordDTO> scan();

    Optional<WordDTO> getWord(Long id);

}
