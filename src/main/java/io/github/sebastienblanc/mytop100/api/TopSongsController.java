package io.github.sebastienblanc.mytop100.api;

import io.github.sebastienblanc.mytop100.model.SongEntry;
import io.github.sebastienblanc.mytop100.service.TopSongsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TopSongsController {

    @Inject
    TopSongsService topSongsService;

    @POST
    @Path("/users")
    public Response createUser(CreateUserRequest request) {
        topSongsService.createUser(request.userId());
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/users/{userId}/top-songs")
    public Response addSong(@PathParam("userId") String userId, SongRequest request) {
        SongEntry song = topSongsService.addSong(userId, new SongEntry(request.track(), request.artist()));
        return Response.status(Response.Status.CREATED).entity(song).build();
    }

    @GET
    @Path("/users/{userId}/top-songs")
    public List<SongEntry> topSongs(@PathParam("userId") String userId) {
        return topSongsService.topSongs(userId);
    }

    @GET
    @Path("/users/{userId}/overlap/{friendId}")
    public OverlapResponse overlap(@PathParam("userId") String userId, @PathParam("friendId") String friendId) {
        List<SongEntry> songs = topSongsService.overlap(userId, friendId);
        return new OverlapResponse(songs.size(), songs);
    }

    @GET
    @Path("/users/{userId}/suggestions")
    public List<SongEntry> suggestions(@PathParam("userId") String userId,
                                       @QueryParam("limit") @DefaultValue("10") int limit) {
        return topSongsService.suggestions(userId, limit);
    }

    public record CreateUserRequest(String userId) {
    }

    public record SongRequest(String track, String artist) {
    }

    public record OverlapResponse(int count, List<SongEntry> songs) {
    }
}
