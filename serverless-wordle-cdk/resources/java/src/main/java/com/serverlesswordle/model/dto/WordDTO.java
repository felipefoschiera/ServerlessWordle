package com.serverlesswordle.model.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = WordDTO.TABLE_NAME)
public class WordDTO {

    public static final String TABLE_NAME = "Word";
    public static final String GSI_QUERY_BY_WORD_INDEX = "QueryByWord";

    @DynamoDBHashKey
    private String id;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = GSI_QUERY_BY_WORD_INDEX)
    private String word;
}
