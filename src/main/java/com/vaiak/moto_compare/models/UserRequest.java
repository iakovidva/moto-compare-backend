package com.vaiak.moto_compare.models;

import com.vaiak.moto_compare.enums.Manufacturer;
import com.vaiak.moto_compare.enums.Status;
import com.vaiak.moto_compare.security.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;
}
