package com.musicservice.exception;

public class UserNotFoundException extends MusicServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
