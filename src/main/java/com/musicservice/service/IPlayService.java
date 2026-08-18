package com.musicservice.service;

import com.musicservice.enums.PlaybackStatus;

import java.util.List;

public interface IPlayService {

    boolean play(List<Integer> trackIds);

    boolean pause();

    boolean resume();

    boolean stop();

    boolean next();

    boolean previous();

    PlaybackStatus getStatus();

    int getCurrentTrackId();

}
