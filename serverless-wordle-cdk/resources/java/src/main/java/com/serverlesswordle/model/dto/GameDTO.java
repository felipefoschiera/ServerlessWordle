package com.serverlesswordle.model.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = GameDTO.TABLE_NAME)
public class GameDTO {

    public static final String TABLE_NAME = "Game";

    @DynamoDBHashKey
    private String date;
    @DynamoDBRangeKey
    private long timestamp;

    private String word;
}
