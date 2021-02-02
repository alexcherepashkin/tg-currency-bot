package ua.alexch.bot.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class TemporalStorage {
    private final Map<Integer, String> storage;

    public TemporalStorage() {
        this.storage = new HashMap<>();
    }

    public void saveCurrency(int id, String currency) {
        storage.put(id, currency);
    }

    public String getCurrency(int id) {
        return storage.get(id);
    }

//    public void remove(int id) {
//        storage.remove(id);
//    }
}
