package com.musicservice.repository;

import com.musicservice.model.Library;

public interface ILibraryRepository {

    void save(Library library);

    void update(Library library);

    void delete(int userId);

    Library findByUserId(int userId);

}