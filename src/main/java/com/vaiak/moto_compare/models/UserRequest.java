package com.vaiak.moto_compare.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "user_requests")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "new_motorcycle_request", nullable = false)
    private boolean newMotorcycleRequest;

    @Column(name = "request_content", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String requestContent;
}
