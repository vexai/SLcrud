package com.panels.SLcrud.service;

public interface UserService {
    public void payToAccount(Long id, Long did, Long oid, double budget, String op);

    public void removeFromAccount(Long id, Long oid, Long did, double budget, String op);

    public void transfer( Long userIdOrigin, Long userIdDestination, double budget, String op, Long did, Long oid);
}
