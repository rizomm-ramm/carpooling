package fr.rizomm.ramm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Maximilien on 11/01/2015.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"username"})
@ToString(of = {"username"})
@Builder
@Entity(name = "users")
public class User {

    @Id
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<StopOffReservation> stopOffReservations;

    @OneToMany(mappedBy = "user")
    private List<UserRole> roles;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Journey> journeys;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Notification> notifications;

    public List<StopOffReservation> getActiveReservations() {
        Date now = new Date();
        return stopOffReservations
                .stream()
                .filter(s -> s.getStopOff().getDeparturePoint().getDate().compareTo(now) != -1)
                .sorted((s1, s2) -> s1.getStopOff().getDeparturePoint().getDate().compareTo(s2.getStopOff().getDeparturePoint().getDate()))
                .collect(Collectors.toList());
    }

    public List<StopOffReservation> getArchiveReservations() {
        Date now = new Date();
        return stopOffReservations
                .stream()
                .filter(s -> s.getStopOff().getDeparturePoint().getDate().compareTo(now) == -1)
                .sorted((s1, s2) -> s1.getStopOff().getDeparturePoint().getDate().compareTo(s2.getStopOff().getDeparturePoint().getDate()))
                .collect(Collectors.toList());
    }
}
