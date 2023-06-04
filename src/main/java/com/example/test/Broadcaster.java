package com.example.test;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {

    private static Executor executor = Executors.newSingleThreadExecutor();
    private static LinkedList<Consumer<Boolean>> listeners = new LinkedList<>();

    public static synchronized void register(Consumer<Boolean> listener) {
        listeners.add(listener);
    }

    public static synchronized void unregister(Consumer<Boolean> listener) {
        listeners.remove(listener);
    }

    public static synchronized void broadcast(Boolean message) {
        for (Consumer<Boolean> listener : listeners) {
            executor.execute(() -> listener.accept(message));
        }
    }
}