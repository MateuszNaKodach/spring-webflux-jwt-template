package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.application;

import java.util.*;
import java.util.stream.Collectors;

public class CommandHandlersRegistry {

    private final List<CommandHandler<?>> handlers;

    private CommandHandlersRegistry(List<CommandHandler<?>> handlers) {
        checkIfEveryCommandHasOnlyOneHandler(handlers);
        this.handlers = handlers;
    }

    public static CommandHandlersRegistry register(CommandHandler<?>... commandHandler) {
        return new CommandHandlersRegistry(List.of(commandHandler));
    }


    public static CommandHandlersRegistry registerAll(List<CommandHandler<?>> commandHandlers) {
        return new CommandHandlersRegistry(commandHandlers);
    }

    CommandHandlersRegistry register(CommandHandler<?> commandHandler) {
        final List<CommandHandler<?>> newHandlers = new ArrayList<>();
        Collections.copy(newHandlers, this.handlers);
        newHandlers.add(commandHandler);
        return new CommandHandlersRegistry(newHandlers);
    }

    public Optional<CommandHandler<?>> handlerFor(Command command) {
        return this.handlers.stream()
                .filter(handler -> handler.isSupported(command))
                .findFirst();
    }

    void checkIfEveryCommandHasOnlyOneHandler(List<CommandHandler<?>> handlers) {
        final Map<? extends Class<?>, Long> handlersCountByCommandType = handlers.stream()
                .collect(Collectors.groupingBy(CommandHandler::supportedCommandType, Collectors.counting()));
        final Set<String> commandsClassesHandledMoreThanOnce = handlersCountByCommandType.entrySet().stream()
                .filter(it -> it.getValue() > 1L)
                .map(Map.Entry::getKey)
                .map(Class::getSimpleName)
                .collect(Collectors.toSet());
        if (!commandsClassesHandledMoreThanOnce.isEmpty()) {
            throw new IllegalStateException("Every command can have only one handler! Following commands has more than one handler: " + commandsClassesHandledMoreThanOnce);
        }
    }

}
