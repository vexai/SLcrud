package com.panels.SLcrud.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue
    private Long messageid;
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageDate;
    private String messageType;
    private String text;
    private Long did;
    private Long oid;
    @ManyToOne
    private User user;

    public Message(){
        super();
    }

    public Message(Date messageDate, String messageType, String text, Long oid, Long did, User user) {
        super();
        this.messageDate=messageDate;
        this.messageType=messageType;
        this.text=text;
        this.oid=oid;
        this.did=did;
        this.user=user;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Long getMessageid() {
        return messageid;
    }

    public void setMessageid(Long messageid) {
        this.messageid = messageid;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
