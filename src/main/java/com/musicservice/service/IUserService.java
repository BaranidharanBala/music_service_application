package com.musicservice.service;

import com.musicservice.model.User;

public interface IUserService {

    void setArtistService(IArtistService artistService);

    void setLibraryService(ILibraryService libraryService);

    boolean login(String email, String password);

    boolean addUser(User user);

    boolean removeUser(int id);

    boolean updateUser(User user);

    User findUserById(int id);

    User findUserByEmail(String email);

}
