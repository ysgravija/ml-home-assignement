package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "features", uniqueConstraints = @UniqueConstraint( columnNames = {"feature_name", "email"} ))
public class FeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Long id;

    @Column(name = "feature_name")
    private String featureName;

    @Column(name = "email")
    private String email;

    @Column(name = "enable")
    private Boolean isEnable;
}
