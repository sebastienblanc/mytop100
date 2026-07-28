package io.github.sebastienblanc.mytop100.service;

import io.github.sebastienblanc.mytop100.model.SongEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TopSongsServiceTest {

    private TopSongsService service;

    @BeforeEach
    void setUp() {
        service = new TopSongsService();
        service.createUser("alice");
        service.createUser("bob");
        service.createUser("charlie");
    }

    @Test
    void shouldComputeOverlapAndSuggestionsFromSimilarProfiles() {
        SongEntry oneMoreTime = new SongEntry("One More Time", "Daft Punk");
        SongEntry aroundTheWorld = new SongEntry("Around The World", "Daft Punk");
        SongEntry harderBetter = new SongEntry("Harder Better Faster Stronger", "Daft Punk");

        service.addSong("alice", oneMoreTime);
        service.addSong("alice", aroundTheWorld);

        service.addSong("bob", oneMoreTime);
        service.addSong("bob", harderBetter);

        service.addSong("charlie", oneMoreTime);
        service.addSong("charlie", harderBetter);

        assertEquals(1, service.overlap("alice", "bob").size());
        assertEquals(harderBetter, service.suggestions("alice", 5).get(0));
    }

    @Test
    void shouldEnforceOneTrackToOneArtistForEachUser() {
        service.addSong("alice", new SongEntry("Heroes", "David Bowie"));

        assertThrows(IllegalArgumentException.class,
                () -> service.addSong("alice", new SongEntry("Heroes", "Peter Gabriel")));
    }

    @Test
    void shouldLimitTopSongsTo100PerUser() {
        for (int i = 1; i <= 100; i++) {
            service.addSong("alice", new SongEntry("Track " + i, "Artist " + i));
        }

        assertThrows(IllegalStateException.class,
                () -> service.addSong("alice", new SongEntry("Track 101", "Artist 101")));
    }
}
