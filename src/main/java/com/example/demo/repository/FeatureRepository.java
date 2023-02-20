package com.example.demo.repository;

import com.example.demo.entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<FeatureEntity, Long> {
    @Query(value =
            "SELECT * FROM features WHERE "
            + "email = :email AND "
            + "feature_name = :featureName", nativeQuery = true)
    Optional<FeatureEntity> getFeatureInfo(@Param("email") String email, @Param("featureName") String featureName);

    @Query(value =
            "SELECT * FROM features WHERE "
                    + "email = :email AND "
                    + "feature_name = :featureName FOR UPDATE", nativeQuery = true)
    Optional<FeatureEntity> findFeatureForUpdate(@Param("email") String email, @Param("featureName") String featureName);
}
