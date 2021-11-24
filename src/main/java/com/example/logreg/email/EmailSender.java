package com.example.logreg.email;

public interface EmailSender {
    void send(String to, String email);
    public String buildEmail(String name, String link);
}
