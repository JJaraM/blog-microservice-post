package com.jjara.microservice.post.pojos;

import io.r2dbc.spi.Row;
import lombok.Data;

@Data
public class AuthenticatedUser {
    private String username;
    private String email;

    public AuthenticatedUser(Row row) {
        username = row.get("username", String.class);
        email = row.get("email", String.class);
    }
}
