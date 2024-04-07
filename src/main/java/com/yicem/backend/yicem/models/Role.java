package com.yicem.backend.yicem.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
  @Id
  private String id;

  private ERole name;

  public Role(ERole name) {
    this.name = name;
  }
}
