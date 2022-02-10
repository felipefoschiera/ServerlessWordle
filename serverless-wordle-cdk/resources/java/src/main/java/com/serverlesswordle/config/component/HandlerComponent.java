package com.serverlesswordle.config.component;

import com.serverlesswordle.config.module.EnvModule;
import com.serverlesswordle.config.module.RepositoryModule;
import com.serverlesswordle.config.module.ServiceModule;
import com.serverlesswordle.service.GameGeneratorService;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {EnvModule.class, RepositoryModule.class, ServiceModule.class})
public interface HandlerComponent {

    /**
     * Provides a {@link GameGeneratorService} to handle generating a new game.
     *
     * @return an instance of {@link GameGeneratorService}.
     */
    GameGeneratorService gameGeneratorService();
}
