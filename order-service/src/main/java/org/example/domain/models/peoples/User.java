package org.example.domain.models.peoples;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public abstract class User {
    private final UUID id;
    private final String name;

    public User(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        if  (this == o) return true;
        if (!(o instanceof User other)) return false;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
