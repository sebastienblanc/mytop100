package io.github.sebastienblanc.mytop100.api;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;
import java.util.NoSuchElementException;

public class ErrorMappers {

    @Provider
    public static class BadRequestMapper implements ExceptionMapper<IllegalArgumentException> {
        @Override
        public Response toResponse(IllegalArgumentException exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", exception.getMessage()))
                    .build();
        }
    }

    @Provider
    public static class NotFoundMapper implements ExceptionMapper<NoSuchElementException> {
        @Override
        public Response toResponse(NoSuchElementException exception) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", exception.getMessage()))
                    .build();
        }
    }

    @Provider
    public static class ConflictMapper implements ExceptionMapper<IllegalStateException> {
        @Override
        public Response toResponse(IllegalStateException exception) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", exception.getMessage()))
                    .build();
        }
    }
}
