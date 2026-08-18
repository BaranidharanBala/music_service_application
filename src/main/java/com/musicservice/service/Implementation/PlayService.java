package com.musicservice.service.Implementation;

import com.musicservice.enums.PlaybackStatus;
import com.musicservice.service.IPlayService;

import java.util.ArrayList;
import java.util.List;

public class PlayService implements IPlayService {

    private static PlayService instance;
    private PlaybackStatus status;
    private final List<Integer> queue;
    private int currentIndex;

    private PlayService() {
        this.status = PlaybackStatus.STOPPED;
        this.queue = new ArrayList<>();
        this.currentIndex = -1;
    }

    public static PlayService getInstance() {
        if (instance == null) {
            instance = new PlayService();
        }

        return instance;
    }

    @Override
    public boolean play(List<Integer> trackIds) {
        if (trackIds == null || trackIds.isEmpty()) {
            return false;
        }
        queue.clear();
        queue.addAll(trackIds);

        currentIndex = 0;
        status = PlaybackStatus.PLAYING;

        return true;
    }

    @Override
    public boolean pause() {
        if (status != PlaybackStatus.PLAYING) {
            return false;
        }

        status = PlaybackStatus.PAUSED;

        return true;
    }

    @Override
    public boolean resume() {
        if (status != PlaybackStatus.PAUSED) {
            return false;
        }
        status = PlaybackStatus.PLAYING;
        return true;
    }

    @Override
    public boolean stop() {
        if (status == PlaybackStatus.STOPPED) {
            return false;
        }
        status = PlaybackStatus.STOPPED;
        queue.clear();
        currentIndex = -1;
        return true;
    }

    @Override
    public boolean next() {
        if (status == PlaybackStatus.STOPPED) {
            return false;
        }
        if (queue.isEmpty()) {
            return false;
        }
        if (currentIndex + 1 >= queue.size()) {
            return false;
        }
        currentIndex++;
        status = PlaybackStatus.PLAYING;
        return true;
    }

    @Override
    public boolean previous() {
        if (status == PlaybackStatus.STOPPED) {
            return false;
        }
        if (queue.isEmpty()) {
            return false;
        }
        if (currentIndex <= 0) {
            return false;
        }
        currentIndex--;
        status = PlaybackStatus.PLAYING;
        return true;
    }

    @Override
    public PlaybackStatus getStatus() {
        return status;
    }

    @Override
    public int getCurrentTrackId() {
        if (currentIndex < 0) {
            return -1;
        }
        if (queue.isEmpty()) {
            return -1;
        }
        return queue.get(currentIndex);
    }

}
