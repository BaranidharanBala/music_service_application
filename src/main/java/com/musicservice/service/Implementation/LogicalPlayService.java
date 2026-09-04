package com.musicservice.service.Implementation;

import com.musicservice.enums.PlaybackStatus;
import com.musicservice.model.Track;

import com.musicservice.service.IPlayService;
import com.musicservice.service.ITrackService;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.util.ArrayList;
import java.util.List;

public class LogicalPlayService implements IPlayService {

    private static LogicalPlayService instance;
    private final ITrackService trackService;

    private final MediaPlayerFactory factory;
    private final MediaPlayer player;

    private final List<Integer> queue;
    private int currentIndex;

    private LogicalPlayService(ITrackService trackService) {
        this.trackService = trackService;
        this.factory = new MediaPlayerFactory();
        this.player = factory.mediaPlayers().newMediaPlayer();
        this.queue = new ArrayList<>();
        this.currentIndex = -1;

        registerPlayerListener();
    }

    public static synchronized LogicalPlayService getInstance(ITrackService trackService) {
        if (instance == null) {
            instance = new LogicalPlayService(trackService);
        }
        return instance;
    }

    private void registerPlayerListener() {
        player.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                mediaPlayer.submit(() -> {
                    int trackId = getCurrentTrackId();
                    if (trackId != -1) {
                        trackService.updatePlaybackStatus(trackId, PlaybackStatus.STOPPED);
                    }
                    if (!next()) {
                        currentIndex = -1;
                    }
                });
            }
        });
    }

    @Override
    public boolean play(List<Integer> trackIds) {
        if (trackIds == null || trackIds.isEmpty()) {
            return false;
        }
        player.controls().stop();

        stopCurrentTrackStatus();

        queue.clear();
        queue.addAll(trackIds);

        currentIndex = 0;

        return playCurrentTrack();
    }

    @Override
    public boolean pause() {
        int trackId = getCurrentTrackId();
        if (trackId == -1) {
            return false;
        }

        Track track = trackService.getTrackById(trackId);
        if (track == null) {
            return false;
        }

        if (track.getPlaybackStatus() != PlaybackStatus.PLAYING) {
            return false;
        }
        player.controls().pause();

        trackService.updatePlaybackStatus(trackId, PlaybackStatus.PAUSED);

        return true;
    }

    @Override
    public boolean resume() {
        int trackId = getCurrentTrackId();
        if (trackId == -1) {
            return false;
        }

        Track track = trackService.getTrackById(trackId);
        if (track == null) {
            return false;
        }

        if (track.getPlaybackStatus() != PlaybackStatus.PAUSED) {
            return false;
        }
        player.controls().play();

        trackService.updatePlaybackStatus(trackId, PlaybackStatus.PLAYING);

        return true;
    }

    @Override
    public boolean stop() {
        int trackId = getCurrentTrackId();
        if (trackId == -1) {
            return false;
        }
        player.controls().stop();

        trackService.updatePlaybackStatus(trackId, PlaybackStatus.STOPPED);

        queue.clear();
        currentIndex = -1;

        return true;
    }

    @Override
    public boolean next() {
        if (queue.isEmpty()) {
            return false;
        }

        if (currentIndex + 1 >= queue.size()) {
            return false;
        }
        stopCurrentTrackStatus();

        currentIndex++;

        return playCurrentTrack();
    }

    @Override
    public boolean previous() {
        if (queue.isEmpty()) {
            return false;
        }

        if (currentIndex <= 0) {
            return false;
        }
        stopCurrentTrackStatus();

        currentIndex--;

        return playCurrentTrack();
    }

    @Override
    public PlaybackStatus getStatus() {
        int trackId = getCurrentTrackId();
        if (trackId == -1) {
            return PlaybackStatus.STOPPED;
        }

        Track track = trackService.getTrackById(trackId);
        if (track == null) {
            return PlaybackStatus.STOPPED;
        }

        return track.getPlaybackStatus();
    }

    @Override
    public int getCurrentTrackId() {
        if (queue.isEmpty()) {
            return -1;
        }

        if (currentIndex < 0 || currentIndex >= queue.size()) {
            return -1;
        }

        return queue.get(currentIndex);
    }

    private boolean playCurrentTrack() {
        int trackId = getCurrentTrackId();
        if (trackId == -1) {
            return false;
        }

        Track track = trackService.getTrackById(trackId);
        if (track == null) {
            return false;
        }

        String path = track.getPath();
        if (path == null || path.isBlank()) {
            return false;
        }

        boolean started = player.media().play(path);
        if (!started) {
            return false;
        }
        trackService.updatePlaybackStatus(trackId, PlaybackStatus.PLAYING);

        return true;
    }

    private void stopCurrentTrackStatus() {
        int trackId = getCurrentTrackId();
        if (trackId == -1) {
            return;
        }
        trackService.updatePlaybackStatus(trackId, PlaybackStatus.STOPPED);
    }

    public void release() {
        player.release();
        factory.release();
    }
}