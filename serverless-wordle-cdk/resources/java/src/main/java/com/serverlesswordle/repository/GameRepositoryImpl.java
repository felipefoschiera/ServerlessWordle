package com.serverlesswordle.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.serverlesswordle.model.dto.GameDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class GameRepositoryImpl implements GameRepository {

    @NonNull
    private IDynamoDBMapper mapper;


    @NonNull
    private String tableName;

    public void save(GameDTO game) {
        mapper.save(game);
    }


}
