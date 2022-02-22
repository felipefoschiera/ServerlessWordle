package com.serverlesswordle.service;

import com.serverlesswordle.model.dto.GameDTO;
import com.serverlesswordle.model.dto.WordDTO;
import com.serverlesswordle.repository.GameRepository;
import com.serverlesswordle.repository.WordRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Log4j2
@AllArgsConstructor
public final class GameGeneratorServiceImpl implements GameGeneratorService {

    @NonNull
    private GameRepository gameRepository;

    @NonNull
    private WordRepository wordRepository;

    @Override
    public GameDTO generateGame() throws Exception {
        log.info("Started GameGenerator");

        String randomWord = getRandomWord();
        log.info("Picked random word: {}", randomWord);

        GameDTO newGame = createGame(randomWord);

        log.info("Generated game: {}", newGame);

        return newGame;

    }

    private String getRandomWord() throws Exception {
        List<WordDTO> words = wordRepository.scan();

        if (words.size() == 0) {
            throw new Exception("Word table count is zero.");
        }

        int randomIndex = RandomUtils.nextInt(0, words.size());

        WordDTO randomWord = words.get(randomIndex);
        return randomWord.getWord();
    }

    private GameDTO createGame(String word) {
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        GameDTO game = GameDTO.builder()
                .word(word)
                .timestamp(Instant.now().toEpochMilli())
                .date(formattedDate)
                .build();

        gameRepository.save(game);
        return game;
    }

}
