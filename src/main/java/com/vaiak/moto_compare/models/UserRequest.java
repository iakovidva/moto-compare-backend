package com.vaiak.moto_compare.models;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "new_motorcycle_request", nullable = false)
    private boolean newMotorcycleRequest;

    @Column(name = "request_content", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String requestContent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
