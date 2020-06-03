package com.panels.SLcrud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "OPERATION_TYPE", discriminatorType = DiscriminatorType.STRING, length = 2)
public class Operation implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;
    private double amount;
    private String op;
    private Long oid;
    private Long did;
    @ManyToOne
//    @JoinColumn(name = "id")
    private User user;

    public Operation() {
        super();
    }

    public Operation(Date operationDate, double amount, User user,
                     String op, Long oid, Long did) {
        super();
        this.operationDate = operationDate;
        this.amount = amount;
        this.user=user;
        this.op=op;
        this.oid = oid;
        this.did=did;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getOperationId() {
        return id;
    }

    public void setOperationId(Long operationId) {
        this.id = operationId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}



