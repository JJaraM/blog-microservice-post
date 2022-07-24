package com.jjara.microservice.ws.post.handler;

import com.jjara.microservice.ws.post.pojos.AuthenticatedUser;
import io.r2dbc.pool.ConnectionPool;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static com.jjara.microservice.api.ResponseHandler.ok;

@Component
public class UserHandler {

    private final ConnectionPool connectionPool;

    public UserHandler(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Mono<ServerResponse> findById() {
        return ok(connectionPool.create()
                .flatMapMany(connection -> connection.createStatement("SELECT * FROM USER_DETAILS").execute())
                .flatMap(result -> result.map((row, rowMetadata) -> new AuthenticatedUser(row)))
                .next()
        );
    }
}
