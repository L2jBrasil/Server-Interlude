package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Id;

@Table("char_templates")
public class CharTemplate extends Entity<Integer> {

    @Id
    private Integer classId;
    private String className;
    private Integer classLevel;
    private Integer RaceId;
    @Column("parent_id")
    private Integer parentId;
    private Integer STR;
    private Integer CON;
    private Integer DEX;
    private Integer _INT;
    private Integer WIT;
    private Integer MEN;
    @Column("p_atk")
    private Integer pAtk;
    @Column("p_def")
    private Integer pDef;
    @Column("m_atk")
    private Integer mAtk;
    @Column("M_DEF")
    private Integer mDef;
    @Column("P_SPD")
    private Integer pSpd;
    @Column("M_SPD")
    private Integer mSpd;
    private Integer acc;
    private Integer critical;
    private Integer evasion;
    @Column("move_spd")
    private Integer moveSpeed;
    private Integer _load;
    private Float defaultHpBase;
    private Float defaultHpAdd;
    private Float defaultHpMod;
    private Float defaultCpBase;
    private Float defaultCpAdd;
    private Float defaultCpMod;
    private Float defaultMpBase;
    private Float defaultMpAdd;
    private Float defaultMpMod;
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer canCraft;
    private Float M_UNK1;
    private Float M_UNK2;
    private Float M_COL_R;
    private Float M_COL_H;
    private Float F_UNK1;
    private Float F_UNK2;
    private Float F_COL_R;
    private Float F_COL_H;
    private Integer items1;
    private Integer items2;
    private Integer items3;
    private Integer items4;
    private Integer items5;

    @Override
    public Integer getId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public int getRaceId() {
        return RaceId;
    }

    public int getSTR() {
        return STR;
    }

    public Integer getClassLevel() {
        return classLevel;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getCON() {
        return CON;
    }

    public Integer getDEX() {
        return DEX;
    }

    public Integer getINT() {
        return _INT;
    }

    public Integer getWIT() {
        return WIT;
    }

    public Integer getMEN() {
        return MEN;
    }

    public Integer getpAtk() {
        return pAtk;
    }

    public Integer getpDef() {
        return pDef;
    }

    public Integer getmAtk() {
        return mAtk;
    }

    public Integer getmDef() {
        return mDef;
    }

    public Integer getpSpd() {
        return pSpd;
    }

    public Integer getmSpd() {
        return mSpd;
    }

    public Integer getAcc() {
        return acc;
    }

    public Integer getCritical() {
        return critical;
    }

    public Integer getEvasion() {
        return evasion;
    }

    public Integer getMoveSpeed() {
        return moveSpeed;
    }

    public Integer get_load() {
        return _load;
    }

    public float getDefaultHpBase() {
        return defaultHpBase;
    }

    public float getDefaultHpAdd() {
        return defaultHpAdd;
    }

    public float getDefaultHpMod() {
        return defaultHpMod;
    }

    public float getDefaultCpBase() {
        return defaultCpBase;
    }

    public float getDefaultCpAdd() {
        return defaultCpAdd;
    }

    public float getDefaultCpMod() {
        return defaultCpMod;
    }

    public float getDefaultMpBase() {
        return defaultMpBase;
    }

    public float getDefaultMpAdd() {
        return defaultMpAdd;
    }

    public float getDefaultMpMod() {
        return defaultMpMod;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Integer getCanCraft() {
        return canCraft;
    }

    public float getM_UNK1() {
        return M_UNK1;
    }

    public float getM_UNK2() {
        return M_UNK2;
    }

    public float getM_COL_R() {
        return M_COL_R;
    }

    public float getM_COL_H() {
        return M_COL_H;
    }

    public float getF_UNK1() {
        return F_UNK1;
    }

    public float getF_UNK2() {
        return F_UNK2;
    }

    public float getF_COL_R() {
        return F_COL_R;
    }

    public float getF_COL_H() {
        return F_COL_H;
    }

    public Integer getItems1() {
        return items1;
    }

    public Integer getItems2() {
        return items2;
    }

    public Integer getItems3() {
        return items3;
    }

    public Integer getItems4() {
        return items4;
    }

    public Integer getItems5() {
        return items5;
    }
}
