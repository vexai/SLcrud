package com.panels.SLcrud.service;

public interface UserService {
    public void payToAccount(Long id, double budget);

    public void removeFromAccount(Long id, double budget);

    public void transfer(Long id, Long userIdDestination, double budget);
}
