package com.musicservice.service.Implementation;

import com.musicservice.enums.Role;
import com.musicservice.model.Library;
import com.musicservice.model.User;
import com.musicservice.repository.IUserRepository;
import com.musicservice.service.IArtistService;
import com.musicservice.service.ILibraryService;
import com.musicservice.service.IUserService;
import com.musicservice.util.PasswordUtil;

public class UserService implements IUserService {

    private static UserService instance;
    private final IUserRepository userRepository;
    IArtistService artistService;
    ILibraryService libraryService;

    private UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static UserService getInstance(IUserRepository userRepository) {
        if (instance == null) {
            instance = new UserService(userRepository);
        }
        return instance;
    }

    @Override
    public void setArtistService(IArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public void setLibraryService(ILibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return PasswordUtil.verify(password, user.getPassword());
    }

    @Override
    public boolean addUser(User user) {
        if(user == null) {
            return false;
        }

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return false;
        }

        String hashedPassword = PasswordUtil.hash(user.getPassword());

        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        userRepository.save(user);

        Library library = new Library(user.getId());
        libraryService.addLibrary(library);

        return true;
    }

    @Override
    public boolean removeUser(int id) {
        if (id <= 0) {
            return false;
        }

        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return false;
        }

        //libraryService.removeLibrary(id);

        userRepository.delete(existingUser.getId());

        return true;
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null) {
            return false;
        }

        User existingUser = userRepository.findById(user.getId());
        if (existingUser == null) {
            return false;
        }

        userRepository.update(user);

        return true;
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
