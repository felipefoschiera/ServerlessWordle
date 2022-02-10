package com.serverlesswordle.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.serverlesswordle.service.GameGeneratorService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        verify(gameGeneratorService, times(1)).generateGame();
    }

}
