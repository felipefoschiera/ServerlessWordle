package com.serverlesswordle.model.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
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
    private Number timestamp;

    private String word;
}
