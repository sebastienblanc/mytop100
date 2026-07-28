package io.github.sebastienblanc.mytop100.service;

import io.github.sebastienblanc.mytop100.model.SongEntry;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class TopSongsService {

    private static final int MAX_TOP_SONGS = 100;

    private final Map<String, LinkedHashMap<String, SongEntry>> userTopSongs = new ConcurrentHashMap<>();

    public void createUser(String userId) {
        validateUserId(userId);
        userTopSongs.putIfAbsent(userId.trim(), new LinkedHashMap<>());
    }

    public List<SongEntry> topSongs(String userId) {
        return new ArrayList<>(getSongsForUser(userId).values());
    }

    public SongEntry addSong(String userId, SongEntry songEntry) {
        LinkedHashMap<String, SongEntry> songs = getSongsForUser(userId);

        synchronized (songs) {
            SongEntry existingTrack = songs.get(songEntry.normalizedTrack());
            if (existingTrack != null && !existingTrack.normalizedArtist().equals(songEntry.normalizedArtist())) {
                throw new IllegalArgumentException("one track can only map to one artist");
            }
            if (existingTrack == null && songs.size() >= MAX_TOP_SONGS) {
                throw new IllegalStateException("top songs list cannot exceed 100 songs");
            }
            songs.put(songEntry.normalizedTrack(), songEntry);
            return songEntry;
        }
    }

    public List<SongEntry> overlap(String userId, String friendId) {
        List<SongEntry> userSongs = topSongs(userId);
        Map<String, SongEntry> friendSongsByKey = topSongs(friendId).stream()
                .collect(Collectors.toMap(SongEntry::normalizedKey, song -> song, (left, right) -> left));

        return userSongs.stream()
                .filter(song -> friendSongsByKey.containsKey(song.normalizedKey()))
                .toList();
    }

    public List<SongEntry> suggestions(String userId, int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be greater than 0");
        }

        List<SongEntry> userSongs = topSongs(userId);
        Map<String, SongEntry> ownSongs = userSongs.stream()
                .collect(Collectors.toMap(SongEntry::normalizedKey, song -> song, (left, right) -> left));

        Map<String, Integer> scores = new HashMap<>();
        Map<String, SongEntry> byKey = new HashMap<>();

        userTopSongs.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(userId))
                .forEach(entry -> {
                    List<SongEntry> otherSongs = new ArrayList<>(entry.getValue().values());
                    long overlapCount = otherSongs.stream()
                            .filter(song -> ownSongs.containsKey(song.normalizedKey()))
                            .count();

                    if (overlapCount == 0) {
                        return;
                    }

                    otherSongs.stream()
                            .filter(song -> !ownSongs.containsKey(song.normalizedKey()))
                            .forEach(song -> {
                                scores.merge(song.normalizedKey(), (int) overlapCount, Integer::sum);
                                byKey.putIfAbsent(song.normalizedKey(), song);
                            });
                });

        return scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey))
                .limit(limit)
                .map(entry -> byKey.get(entry.getKey()))
                .toList();
    }

    private LinkedHashMap<String, SongEntry> getSongsForUser(String userId) {
        validateUserId(userId);
        LinkedHashMap<String, SongEntry> songs = userTopSongs.get(userId.trim());
        if (songs == null) {
            throw new NoSuchElementException("user not found: " + userId);
        }
        return songs;
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
    }
}
