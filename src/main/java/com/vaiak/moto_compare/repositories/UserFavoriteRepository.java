package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.UserFavorite;
import com.vaiak.moto_compare.models.UserFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, UserFavoriteId> {

    @Query("SELECT uf.motorcycle FROM UserFavorite uf WHERE uf.id.userId = :userId")
    List<Motorcycle> findFavoriteMotorcyclesByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM UserFavorite uf WHERE uf.id.userId = :userId AND uf.id.motorcycleId = :motorcycleId")
    void deleteFavoriteMotorcycleForUser(@Param("userId") UUID userId,
                                         @Param("motorcycleId") Long motorcycleId);

    @Query("""
    SELECT uf.motorcycle.id, COUNT(uf.motorcycle.id)
    FROM UserFavorite uf
    GROUP BY uf.motorcycle.id
    ORDER BY COUNT(uf.motorcycle.id) DESC
    """)
    List<Object[]> getMotorcycleFavoriteCounts();

}
