package itu.framework.backoffice.entities;

import legacy.annotations.Column;
import legacy.annotations.Entity;
import legacy.annotations.Generated;
import legacy.annotations.Id;
import legacy.strategy.GeneratedAfterPersistence;

import java.time.LocalDateTime;

@Entity(tableName = "token_client")
public class TokenClient {
    @Id
    @Column
    @Generated(strategy = GeneratedAfterPersistence.class)
    Integer id;

    @Column(name = "token")
    String token;

    @Column(name = "date_expiration")
    LocalDateTime dateExpiration;
}
