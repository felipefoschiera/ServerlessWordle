package com.serverlesswordle.service;

import com.serverlesswordle.repository.GameRepository;
import com.serverlesswordle.repository.WordRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public final class GameGeneratorServiceImpl implements GameGeneratorService {

    @NonNull
    private GameRepository gameRepository;

    @NonNull
    private WordRepository wordRepository;

    @Override
    public void generateGame() {
        log.info("Started GameGenerator");

        String randomWord = getRandomWord();
    }

    private String getRandomWord() {
        return "";
    }

}
