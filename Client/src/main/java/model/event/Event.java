package model.event;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Event<T> {
    ArrayList<Consumer<T>> listeners = new ArrayList<>();

    public void invoke(T arg) {
        for (Consumer<T> listener : listeners) {
            listener.accept(arg);
        }
    }

    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }
}
