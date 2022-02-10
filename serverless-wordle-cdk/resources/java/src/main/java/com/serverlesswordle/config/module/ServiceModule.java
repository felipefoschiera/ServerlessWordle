package com.serverlesswordle.config.module;

import com.serverlesswordle.repository.GameRepository;
import com.serverlesswordle.repository.WordRepository;
import com.serverlesswordle.service.GameGeneratorService;
import com.serverlesswordle.service.GameGeneratorServiceImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;


@Module
public class ServiceModule {

    @Provides
    @Singleton
    static GameGeneratorService provideGameGeneratorService(GameRepository gameRepository, WordRepository wordRepository) {
        return new GameGeneratorServiceImpl(gameRepository, wordRepository);
    }
}
