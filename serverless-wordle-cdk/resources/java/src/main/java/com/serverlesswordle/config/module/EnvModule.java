package com.serverlesswordle.config.module;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class EnvModule {

    @Provides
    @Singleton
    @Named("GameTableName")
    static String provideGameTableName() {
        return System.getenv("GAME_TABLE_NAME");
    }

    @Provides
    @Singleton
    @Named("WordTableName")
    static String provideWordTableName() {
        return System.getenv("WORD_TABLE_NAME");
    }

}
