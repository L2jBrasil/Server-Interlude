package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import com.l2jbr.gameserver.idfactory.IdFactory;
import com.l2jbr.gameserver.model.entity.database.repository.ModsWeddingRepository;
import org.springframework.data.annotation.Id;

import static com.l2jbr.commons.database.DatabaseAccess.getRepository;

@Table("mods_wedding")
public class Wedding extends Entity<Integer> {

    @Id
    private int id;
    private int player1Id;
    private int player2Id;
    private boolean married;
    private long affianceDate;
    private long weddingDate;

    public Wedding() {}

    public Wedding(int id, int player1Id, int player2Id, boolean married, long affianceDate, long weddingDate) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.married = married;
        this.affianceDate = affianceDate;
        this.weddingDate = weddingDate;
    }

    public Wedding(int player1Id, int player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        id = IdFactory.getInstance().getNextId();
        affianceDate = System.currentTimeMillis();
        weddingDate = System.currentTimeMillis();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public boolean isMarried() {
        return married;
    }

    public long getAffianceDate() {
        return affianceDate;
    }

    public long getWeddingDate() {
        return weddingDate;
    }

    public void divorce() {
        getRepository(ModsWeddingRepository.class).deleteById(id);
    }

    public void marry() {
        weddingDate = System.currentTimeMillis();
        married = true;
        ModsWeddingRepository repository = getRepository(ModsWeddingRepository.class);
        repository.updateMarried(id, married, weddingDate);

    }
}
