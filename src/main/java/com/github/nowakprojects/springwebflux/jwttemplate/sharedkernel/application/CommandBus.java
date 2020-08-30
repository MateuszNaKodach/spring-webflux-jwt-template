package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.application;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class CommandBus {

    private final CommandHandlersRegistry commandHandlersRegistry;

    private CommandBus(CommandHandlersRegistry commandHandlersRegistry) {
        this.commandHandlersRegistry = commandHandlersRegistry;
    }

    public static CommandBus withRegistry(CommandHandlersRegistry commandHandlersRegistry) {
        return new CommandBus(commandHandlersRegistry);
    }

    public Mono<Void> dispatch(Command command) {
        return commandHandlersRegistry.handlerFor(command)
                .map(it -> it.handle(command).doOnError(err -> log.error("Error while processing command: " + command.toString(), err)))
                .orElseGet(() -> Mono.error(new IllegalStateException("Not found handler for command: " + command.getClass().getSimpleName())));
    }
}
