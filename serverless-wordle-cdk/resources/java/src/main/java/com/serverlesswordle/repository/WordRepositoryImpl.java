package com.serverlesswordle.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class WordRepositoryImpl implements WordRepository {

    @NonNull
    private IDynamoDBMapper mapper;


}
