package com.anush.whatsapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Chatroom {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "UUID")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @Column
    private String name;

    @Column
    private String profileImageUrl;

    @Column
    private UUID adminId;

    @Column
    private OffsetDateTime lastMessageTime;

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(
            name = "UserChatroom",
            joinColumns = @JoinColumn(name = "chatroomId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    @JsonIgnore
    private Set<User> users;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Message> messages;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
