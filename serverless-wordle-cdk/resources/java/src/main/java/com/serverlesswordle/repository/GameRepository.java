package com.serverlesswordle.repository;

import com.serverlesswordle.model.dto.GameDTO;

public interface GameRepository {

    void save(GameDTO game);

}
