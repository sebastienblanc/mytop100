package io.github.sebastienblanc.mytop100.model;

import java.util.Locale;

public record SongEntry(String track, String artist) {

    public SongEntry {
        if (track == null || track.isBlank()) {
            throw new IllegalArgumentException("track is required");
        }
        if (artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("artist is required");
        }
        track = track.trim();
        artist = artist.trim();
    }

    public String normalizedTrack() {
        return track.toLowerCase(Locale.ROOT);
    }

    public String normalizedArtist() {
        return artist.toLowerCase(Locale.ROOT);
    }

    public String normalizedKey() {
        return normalizedTrack() + "|" + normalizedArtist();
    }
}
