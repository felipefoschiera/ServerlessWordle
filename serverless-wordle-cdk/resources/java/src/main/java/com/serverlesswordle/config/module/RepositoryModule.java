package com.serverlesswordle.config.module;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.serverlesswordle.model.dto.GameDTO;
import com.serverlesswordle.model.dto.WordDTO;
import com.serverlesswordle.repository.GameRepository;
import com.serverlesswordle.repository.GameRepositoryImpl;
import com.serverlesswordle.repository.WordRepository;
import com.serverlesswordle.repository.WordRepositoryImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    static AmazonDynamoDB provideAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }

    @Provides
    @Singleton
    static IDynamoDBMapper provideDynamoDBMapper(AmazonDynamoDB dynamoDB) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withTableNameResolver(RepositoryModule::getTableNameResolver)
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.LAZY_LOADING)
                .build();
        return new DynamoDBMapper(dynamoDB, config);
    }

    @Provides
    @Singleton
    static GameRepository provideGameRepository(IDynamoDBMapper dynamoDBMapper) {
        return new GameRepositoryImpl(dynamoDBMapper);
    }

    @Provides
    @Singleton
    static WordRepository provideWordRepository(IDynamoDBMapper dynamoDBMapper) {
        return new WordRepositoryImpl(dynamoDBMapper);
    }

    private static String getTableNameResolver(Class<?> dtoClass, DynamoDBMapperConfig config) {
        if (dtoClass.equals(GameDTO.class)) {
            return System.getenv("GAME_TABLE_NAME");
        }else if(dtoClass.equals(WordDTO.class)) {
            return System.getenv("WORD_TABLE_NAME");
        }
        throw new UnsupportedOperationException(String.format("Unknown class %s", dtoClass));
    }
}
