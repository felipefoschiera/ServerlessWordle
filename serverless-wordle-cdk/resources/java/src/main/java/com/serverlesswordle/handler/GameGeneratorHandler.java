package com.serverlesswordle.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverlesswordle.config.component.DaggerHandlerComponent;
import com.serverlesswordle.config.component.HandlerComponent;
import com.serverlesswordle.model.dto.GameDTO;
import com.serverlesswordle.service.GameGeneratorService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;

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

        try {
            GameDTO game = gameGeneratorService.generateGame();
            return getSuccessMessage(game);
        } catch (Exception ex) {
            log.error(ex);
            return getErrorMessage(ex.getMessage());
        }
    }

    private String getSuccessMessage(GameDTO game) {
        return new JSONObject()
                .put("statusCode", 200)
                .put("game", game)
                .toString();
    }

    private String getErrorMessage(String message) {
        return new JSONObject()
                .put("statusCode", 500)
                .put("exception", message)
                .toString();
    }

}
