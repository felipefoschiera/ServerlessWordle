package com.serverlesswordle.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.serverlesswordle.model.dto.WordDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
public class WordRepositoryImpl implements WordRepository {

    @NonNull
    private IDynamoDBMapper mapper;

    @NonNull
    private String tableName;

    @Override
    public List<WordDTO> scan() {
        return mapper.scan(WordDTO.class, new DynamoDBScanExpression());
    }

    @Override
    public Optional<WordDTO> getWord(Long id) {
        WordDTO key = buildWordKey(id);
        return Optional.ofNullable(mapper.load(key));
    }


    private WordDTO buildWordKey(Long id) {
        return WordDTO.builder()
                .id(id.toString())
                .build();
    }


}
