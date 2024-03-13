package com.example.demo2.domain.entities;

import java.time.LocalDateTime;


public class Friendship extends Entity<Tuple<Long,Long>> {

    LocalDateTime friendsFrom;



    public Friendship(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public Friendship() {


        friendsFrom = LocalDateTime.now();
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return friendsFrom;
    }
}

