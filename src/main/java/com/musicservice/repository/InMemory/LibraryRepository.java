package com.musicservice.repository.InMemory;

import com.musicservice.model.Library;
import com.musicservice.repository.ILibraryRepository;

import java.util.HashMap;
import java.util.Map;

public class LibraryRepository implements ILibraryRepository {

    private static LibraryRepository instance;
    private final Map<Integer, Library> LIBRARIES;

    private LibraryRepository() {
        LIBRARIES = new HashMap<>();
    }

    public static LibraryRepository getInstance() {
        if (instance == null) {
            instance = new LibraryRepository();
        }
        return instance;
    }

    @Override
    public void save(Library library) {
        LIBRARIES.put(library.getUserId(), library);
    }

    @Override
    public void update(Library library) {
        LIBRARIES.put(library.getUserId(), library);
    }

    @Override
    public void delete(int userId) {
        LIBRARIES.remove(userId);
    }

    @Override
    public Library findByUserId(int userId) {
        return LIBRARIES.get(userId);
    }

}