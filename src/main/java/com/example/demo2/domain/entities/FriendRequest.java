package com.example.demo2.domain.entities;

public class FriendRequest extends Entity<Long>{
    Long from_id;
    Long to_id;
    String status="pending";

    public Long getFrom_id() {
        return from_id;
    }

    public Long getTo_id() {
        return to_id;
    }

    public String getStatus() {
        return status;
    }

    public FriendRequest(Long aLong, Long from_id, Long to_id) {
        super(aLong);
        this.from_id = from_id;
        this.to_id=to_id;
    }

    public FriendRequest(Long from_id,Long to_id) {
        this.from_id = from_id;
        this.to_id=to_id;
    }
    public FriendRequest(Long from_id,Long to_id,String status) {
        this.from_id = from_id;
        this.to_id=to_id;
        this.status=status;
    }
}
