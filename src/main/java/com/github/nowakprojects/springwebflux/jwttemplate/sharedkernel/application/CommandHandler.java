package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.application;

import reactor.core.publisher.Mono;

public abstract class CommandHandler<CommandType extends Command> {

    public abstract Mono<Void> execute(CommandType command);

    public final Mono<Void> handle(Command command) {
        return supportedCommandType().isInstance(command)
                ? execute(supportedCommandType().cast(command))
                : Mono.empty();
    }

    public boolean isSupported(Command command) {
        return supportedCommandType().isInstance(command);
    }

    public abstract Class<CommandType> supportedCommandType();
}
