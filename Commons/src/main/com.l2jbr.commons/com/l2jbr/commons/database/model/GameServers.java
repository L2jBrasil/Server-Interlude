package com.l2jbr.commons.database.model;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("gameservers")
public class GameServers extends Entity<Integer> {

    @Id
    @Column("server_id")
    private Integer serverId;
    private String hexid;
    private String host;

    public GameServers() { }

    public GameServers(int id, String hexId, String host) {
        this.serverId = id;
        this.hexid = hexId;
        this.host = host;
    }

    @Override
    public Integer getId() {
        return serverId;
    }

    public String getHexid() {
        return hexid;
    }

    public String getHost() {
        return host;
    }
}
