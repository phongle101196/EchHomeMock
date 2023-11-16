package com.mock.entity;

import com.mock.payload.request.CommentDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Setter
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    @Column(name = "post_title", length = 100, nullable = false)
    private String title;
    @Column(name = "post_content", length = 5000, nullable = false)
    private String content;
    private String roomImage;
    private String roomAddress;
    private double roomPrice;
    private double roomSize;
    @Column(name = "room_description", length = 10000, nullable = false)
    private String roomDescription;
    private String roomDistrict;
    private Date addDate;
    @Column(name = "roomStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;
    @PrePersist
    public void prePersist() {
        if(roomStatus == null){
            roomStatus = RoomStatus.AVAILABLE;
        }
    }
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SavePost> savedByUsers = new HashSet<>();

}
