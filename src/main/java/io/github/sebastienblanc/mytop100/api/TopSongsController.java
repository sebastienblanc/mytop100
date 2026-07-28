package io.github.sebastienblanc.mytop100.api;

import io.github.sebastienblanc.mytop100.model.SongEntry;
import io.github.sebastienblanc.mytop100.service.TopSongsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class TopSongsController {

    private final TopSongsService topSongsService;

    public TopSongsController(TopSongsService topSongsService) {
        this.topSongsService = topSongsService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody CreateUserRequest request) {
        topSongsService.createUser(request.userId());
    }

    @PostMapping("/users/{userId}/top-songs")
    @ResponseStatus(HttpStatus.CREATED)
    public SongEntry addSong(@PathVariable String userId, @RequestBody SongRequest request) {
        return topSongsService.addSong(userId, new SongEntry(request.track(), request.artist()));
    }

    @GetMapping("/users/{userId}/top-songs")
    public List<SongEntry> topSongs(@PathVariable String userId) {
        return topSongsService.topSongs(userId);
    }

    @GetMapping("/users/{userId}/overlap/{friendId}")
    public OverlapResponse overlap(@PathVariable String userId, @PathVariable String friendId) {
        List<SongEntry> songs = topSongsService.overlap(userId, friendId);
        return new OverlapResponse(songs.size(), songs);
    }

    @GetMapping("/users/{userId}/suggestions")
    public List<SongEntry> suggestions(@PathVariable String userId,
                                       @RequestParam(defaultValue = "10") int limit) {
        return topSongsService.suggestions(userId, limit);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleConflict(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", exception.getMessage()));
    }

    public record CreateUserRequest(String userId) {
    }

    public record SongRequest(String track, String artist) {
    }

    public record OverlapResponse(int count, List<SongEntry> songs) {
    }
}
