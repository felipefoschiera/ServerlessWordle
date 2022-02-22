package com.serverlesswordle.service;

import com.serverlesswordle.model.dto.GameDTO;
import com.serverlesswordle.model.dto.WordDTO;
import com.serverlesswordle.repository.GameRepository;
import com.serverlesswordle.repository.WordRepository;
import org.apache.commons.lang3.RandomUtils;


import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class GameGeneratorServiceImplTest {


    @Mock
    private GameRepository gameRepository;
    @Mock
    private WordRepository wordRepository;

    private GameGeneratorService service;

    private static final int WORD_INDEX = 0;

    private static final WordDTO WORD_DTO = WordDTO.builder()
            .id(String.valueOf(WORD_INDEX))
            .word("hello")
            .build();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new GameGeneratorServiceImpl(gameRepository, wordRepository);
    }

    @Test
    public void generateGameShouldGetRandomWordFromRepository() throws Exception {
        Mockito.doReturn(List.of(WORD_DTO)).when(wordRepository).scan();
        Mockito.doNothing().when(gameRepository).save(Mockito.any(GameDTO.class));

        MockedStatic<RandomUtils> randomUtils = Mockito.mockStatic(RandomUtils.class);
        randomUtils.when(() -> RandomUtils.nextInt(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(WORD_INDEX);

        GameDTO game = service.generateGame();

        assertEquals(game.getWord(), WORD_DTO.getWord());
    }

    @Test
    public void generateGameShouldThrowExceptionIfNoWordsInRepository() throws Exception {
        Mockito.doReturn(List.of()).when(wordRepository).scan();

        assertThrows(Exception.class, () -> service.generateGame());
    }


}
