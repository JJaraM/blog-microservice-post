package com.jjara.microservice.ws.post.routers;

import com.jjara.microservice.ws.post.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterFunction {

    @Bean
    public RouterFunction<ServerResponse> userRouter(UserHandler userHandler) {
        return route(GET("/user"), userHandler::findById);
    }
}
