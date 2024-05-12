package br.com.fatec.techninja.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private List<Quiz> completedQuizes;

    public User() {
        this.completedQuizes = new ArrayList<>();
    }

    // getters e setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Quiz> getCompletedQuizes() {
        return completedQuizes;
    }

    public void setCompletedQuizes(List<Quiz> completedQuizes) {
        this.completedQuizes = completedQuizes;
    }
}