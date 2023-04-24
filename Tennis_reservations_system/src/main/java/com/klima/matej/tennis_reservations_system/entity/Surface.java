package com.klima.matej.tennis_reservations_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a surface.
 * It is used to store information about the surface.
 * It is also used to map the surface to the database.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql = "UPDATE surface SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@JsonIgnoreProperties({"courts", "deleted"} )
@Entity
public class Surface {
    /**
     * Unique identifier of the surface used as PK in database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * Name of the surface.
     * It is used as a unique identifier of the surface.
     */
    @Column(unique = true)
    @NotNull(message = "Name is required")
    private String name;

    /**
     * Price per minute of the surface.
     */
    @NotNull(message = "Minute price is required")
    private int minutePrice;

    /**
     * Set of courts with this surface.
     */
    @OneToMany(mappedBy="surface")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Court> courts = new HashSet<>();

    /**
     * Boolean value representing whether the surface is deleted.
     * Used for soft delete.
     */
    @Setter(AccessLevel.NONE)
    private boolean deleted = false;
}
