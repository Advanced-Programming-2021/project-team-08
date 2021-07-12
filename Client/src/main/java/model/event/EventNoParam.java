package model.event;

import java.util.ArrayList;

public class EventNoParam {
    ArrayList<func> listeners = new ArrayList<>();

    public void invoke() {
        for (func listener : listeners) {
            listener.call();
        }
    }

    public void addListener(func listener) {
        listeners.add(listener);
    }

    public void removeListener(func listener) {
        listeners.remove(listener);
    }

    @FunctionalInterface
    public interface func {
        void call();
    }
}
