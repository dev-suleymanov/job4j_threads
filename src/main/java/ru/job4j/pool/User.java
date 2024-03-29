package ru.job4j.pool;

public class User {
    private final String username;
    private final String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void send(String subject, String body, String email) {
        System.out.println(subject + " " + body + " " + email);
    }
}
