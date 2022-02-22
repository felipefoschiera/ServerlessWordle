package com.serverlesswordle.service;

import com.serverlesswordle.model.dto.GameDTO;

public interface GameGeneratorService {

    GameDTO generateGame() throws Exception;
}
