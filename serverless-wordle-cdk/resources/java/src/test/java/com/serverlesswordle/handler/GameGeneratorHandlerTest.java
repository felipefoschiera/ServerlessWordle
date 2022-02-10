package com.serverlesswordle.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverlesswordle.service.GameGeneratorService;
import lombok.extern.log4j.Log4j2;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Log4j2
public class GameGeneratorHandlerTest {

    @Mock
    private Context context;
    @Mock
    private GameGeneratorService gameGeneratorService;

    private GameGeneratorHandler handler;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new GameGeneratorHandler(gameGeneratorService);
    }

    @Test
    public void testHandleRequest() {
        handler.handleRequest(new Object(), context);
        log.info("Started test!");
        verify(gameGeneratorService, times(1)).generateGame();
    }

}
