package ru.job4j.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    private final ExecutorService service = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void emailTo(User user) {
        service.submit(() -> {
            String subject = String.format("Notification {%s} to email {%s}.",
                    user.getUsername(), user.getEmail());
            String body = String.format("Add a new event to {username}.",
                    user.getUsername());
            user.send(subject, body, user.getEmail());
        });
    }

    public void close() {
        service.shutdown();
    }

    public static void main(String[] args) {
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.emailTo(new User("Rustam", "test@gmail.com"));
        emailNotification.emailTo(new User("Oleg", "test@gmail.com"));
        emailNotification.emailTo(new User("Marina", "test@gmail.com"));
        emailNotification.emailTo(new User("Andrey", "test@gmail.com"));
        emailNotification.emailTo(new User("Elena", "test@gmail.com"));
        emailNotification.close();
    }
}
