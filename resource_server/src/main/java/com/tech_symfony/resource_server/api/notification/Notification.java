package com.tech_symfony.resource_server.api.notification;

import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.system.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private String message;

    @Column(name = "is_read")
    @ColumnDefault("false")
    private boolean isRead = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}