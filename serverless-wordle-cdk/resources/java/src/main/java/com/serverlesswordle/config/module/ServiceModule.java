package com.serverlesswordle.config.module;

import com.serverlesswordle.service.GameGeneratorService;
import com.serverlesswordle.service.GameGeneratorServiceImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;


@Module
public class ServiceModule {

    @Provides
    @Singleton
    static GameGeneratorService provideGameGeneratorService() {
        return new GameGeneratorServiceImpl();
    }
}
