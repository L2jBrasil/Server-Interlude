package com.l2jbr.gameserver.model.database;

import org.springframework.data.annotation.Id;

public class Henna  {

    @Id
    private int symbol_id;
    private String symbol_name;
    private int dye_id;
    private int dye_amount;
    private int price;
    private int stat_INT;
    private int stat_STR;
    private int stat_CON;
    private int stat_MEM;
    private int stat_DEX;
    private int stat_WIT;

    public int getSymbolId() {
        return symbol_id;
    }

    public String getSymbol_name() {
        return symbol_name;
    }

    public int getDyeId() {
        return dye_id;
    }

    public int getDyeAmount() {
        return dye_amount;
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
