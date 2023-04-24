package com.klima.matej.tennis_reservations_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.util.List;

/**
 * Entity class representing a reservation.
 * It is used to store information about the reservation.
 * It is also used to map the court to the database.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SQLDelete(sql = "UPDATE reservation SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@JsonIgnoreProperties({"deleted"})
@Entity
public class Reservation {
    /**
     * Unique identifier of the reservation used as PK in database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * Boolean value representing whether the reservation is for doubles game or not.
     */
    private boolean doubles;

    /**
     * Timestamp of the creation of the reservation.
     */
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    /**
     * Timestamp representing the beginning of the reservation.
     */
    private Timestamp startsAt;

    /**
     * Timestamp representing the end of the reservation.
     */
    private Timestamp endsAt;

    /**
     * Price of the reservation.
     */
    private double price;

    /**
     * Customer who made the reservation.
     */
    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Customer customer;

    /**
     * Court on which the reservation is made.
     */
    @ManyToOne
    @JoinColumn(name="court_id", nullable=false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Court court;

    /**
     * Boolean value representing whether the reservation is deleted or not.
     */
    @Setter(AccessLevel.NONE)
    private Boolean deleted = false;

    public Reservation(boolean isDoubles, Customer customer, Court court, Timestamp startsAt, Timestamp endsAt) {
        this.doubles = isDoubles;
        this.customer = customer;
        this.court = court;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.price = calculatePrice();
    }

    /**
     * Method used to calculate the price of the reservation.
     * @return price of the reservation
     */
    public double calculatePrice() {
        long duration = (this.endsAt.getTime() - this.startsAt.getTime()) / 60000;
        return duration * this.court.getSurface().getMinutePrice() * (this.doubles ? 1.5 : 1);
    }

    /**
     * Method used to check if possible to create new reservation that does not overlap with any existing reservation.
     * @param startsAt Timestamp representing the beginning of the new reservation
     * @param endsAt Timestamp representing the end of the new reservation
     * @param reservations List of reservations to check against
     * @return true if the reservation overlaps with any other reservation, false otherwise
     */
    public static boolean isOverlapping(Timestamp startsAt, Timestamp endsAt, List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            if (startsAt.before(reservation.getEndsAt()) && endsAt.after(reservation.getStartsAt())) {
                return true;
            }
        }
        return false;
    }
}
