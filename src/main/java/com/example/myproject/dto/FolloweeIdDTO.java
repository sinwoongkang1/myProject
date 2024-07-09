package com.example.myproject.dto;

public class FolloweeIdDTO {
    private Long followeeId;

    public FolloweeIdDTO(Long followeeId) {
        this.followeeId = followeeId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(Long followeeId) {
        this.followeeId = followeeId;
    }
}
