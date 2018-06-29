package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("henna")
public class Henna  {

    @Id
    @Column("symbol_id")
    private int symbolId;
    @Column("symbol_name")
    private String symbolName;
    @Column("dye_id")
    private int dyeId;
    @Column("dye_amount")
    private int dyeAmount;
    private int price;
    private int stat_INT;
    private int stat_STR;
    private int stat_CON;
    private int stat_MEM;
    private int stat_DEX;
    private int stat_WIT;

    public int getSymbolId() {
        return symbolId;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public int getDyeId() {
        return dyeId;
    }

    public int getDyeAmount() {
        return dyeAmount;
    }

    public int getPrice() {
        return price;
    }

    public int getStat_INT() {
        return stat_INT;
    }

    public int getStat_STR() {
        return stat_STR;
    }

    public int getStat_CON() {
        return stat_CON;
    }

    public int getStat_MEM() {
        return stat_MEM;
    }

    public int getStat_DEX() {
        return stat_DEX;
    }

    public int getStat_WIT() {
        return stat_WIT;
    }
}
