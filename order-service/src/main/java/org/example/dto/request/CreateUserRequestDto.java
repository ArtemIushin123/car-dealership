package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateUserRequestDto {
    private String name;
    private String role;
    private UUID externalId;
}
