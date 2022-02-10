package com.serverlesswordle.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverlesswordle.config.component.DaggerHandlerComponent;
import com.serverlesswordle.config.component.HandlerComponent;
import com.serverlesswordle.service.GameGeneratorService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class GameGeneratorHandler implements RequestHandler<Object, String> {


    @NonNull
    private final GameGeneratorService gameGeneratorService;

    public GameGeneratorHandler() {
        HandlerComponent component = DaggerHandlerComponent.create();
        this.gameGeneratorService = component.gameGeneratorService();
    }

    @Override
    public String handleRequest(Object input, Context context) {
        log.info("Started request handler!");
        gameGeneratorService.generateGame();
        return "Hello World!";
    }

}
