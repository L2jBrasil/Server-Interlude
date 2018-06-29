package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("mapregion")
public class MapRegion  {

    @Id
    private int region;
    private int sec0;
    private int sec1;
    private int sec2;
    private int sec3;
    private int sec4;
    private int sec5;
    private int sec6;
    private int sec7;
    private int sec8;
    private int sec9;

    public int getRegion() {
        return region;
    }

    public int getSec0() {
        return sec0;
    }

    public int getSec1() {
        return sec1;
    }

    public int getSec2() {
        return sec2;
    }

    public int getSec3() {
        return sec3;
    }

    public int getSec4() {
        return sec4;
    }

    public int getSec5() {
        return sec5;
    }

    public int getSec6() {
        return sec6;
    }

    public int getSec7() {
        return sec7;
    }

    public int getSec8() {
        return sec8;
    }

    public int getSec9() {
        return sec9;
    }

}
