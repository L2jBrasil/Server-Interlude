package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("clanhall")
public class ClanHallEntity extends Entity<Integer> {

    @Id
    private Integer id;
    private String name;
    private Integer ownerId;
    private Integer lease;
    private String description;
    private String location;
    private Long paidUntil;
    private Integer Grade;
    private boolean paid;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLease() {
        return lease;
    }

    public void setLease(int lease) {
        this.lease = lease;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Long getPaidUntil() {
        return paidUntil;
    }

    public void setPaidUntil(long paidUntil) {
        this.paidUntil = paidUntil;
    }

    public Integer getGrade() {
        return Grade;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
