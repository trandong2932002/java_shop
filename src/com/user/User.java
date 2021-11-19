package com.user;

public class User {
    private String first_name;
    private String last_name;
    private String username;
    private UserType rank;
    private int reward_point;
    
    public User(String first_name, String last_name, String username, int rank, int reward_point) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.rank = UserType.valueOf(rank);
        this.reward_point = reward_point;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRank() {
        return rank.getValue();
    }

    public void setRank(int rank) {
        this.rank = UserType.valueOf(rank);
    }

    public int getReward_point() {
        return reward_point;
    }

    public void setReward_point(int reward_point) {
        this.reward_point = reward_point;
    }
}
