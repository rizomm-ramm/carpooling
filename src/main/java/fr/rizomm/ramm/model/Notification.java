package fr.rizomm.ramm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "notifications")
public class Notification {

    public enum Status {
        READ,
        UNREAD
    }

    public enum Type {
        DEFAULT,
        SUCCESS,
        INFO,
        WARNING,
        DANGER
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.UNREAD;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type = Type.DEFAULT;

    @Column(name = "message", nullable = false)
    private String message;
}
