package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import org.springframework.data.annotation.Id;

@Table("char_templates")
public class PlayerTemplate extends CharTemplate {

    @Id
    private Integer id;
    @Column("class_name")
    private String className;
    @Column("class_level")
    private byte classLevel;
    @Column("race_id")
    private byte raceId;
    @Column("parent_id")
    private byte parentId;
    private short accuracy;
    private short evasion;
    @Column("_load")
    private short load;
    @Column("hp_add")
    private Float hpAdd;
    @Column("hp_mod")
    private Float hpMod;
    private Float cp;
    @Column("cp_add")
    private Float cpAdd;
    @Column("cp_mod")
    private Float cpMod;
    @Column("mp_add")
    private Float mpAdd;
    @Column("mp_mod")
    private Float mpMod;
    private Integer x;
    private Integer y;
    private Integer z;
    @Column("can_craft")
    private boolean canCraft;
    private Float M_UNK1;
    private Float M_UNK2;
    private Float F_UNK1;
    private Float F_UNK2;
    @Column("f_collision_radius")
    private Float fCollisionRadius;
    @Column("f_collision_height")
    private Float fCollisionHeight;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;

    @Override
    public Integer getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public int getRaceId() {
        return raceId;
    }

    public byte getClassLevel() {
        return classLevel;
    }

    public byte getParentId() {
        return parentId;
    }

    public short getAccuracy() {
        return accuracy;
    }

    public short getEvasion() {
        return evasion;
    }

    public short getLoad() {
        return load;
    }

    public float getHpAdd() {
        return hpAdd;
    }

    public float getHpMod() {
        return hpMod;
    }

    public float getCp() {
        return cp;
    }

    public float getCpAdd() {
        return cpAdd;
    }

    public float getCpMod() {
        return cpMod;
    }

    public float getMpAdd() {
        return mpAdd;
    }

    public float getMpMod() {
        return mpMod;
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

    public boolean canCraft() {
        return canCraft;
    }

    public float getM_UNK1() {
        return M_UNK1;
    }

    public float getM_UNK2() {
        return M_UNK2;
    }

    public float getF_UNK1() {
        return F_UNK1;
    }

    public float getF_UNK2() {
        return F_UNK2;
    }

    public float getFCollisionRadius() {
        return fCollisionRadius;
    }

    public float getFCollisionHeight() {
        return fCollisionHeight;
    }

    public Integer getItem1() {
        return item1;
    }

    public Integer getItem2() {
        return item2;
    }

    public Integer getItem3() {
        return item3;
    }

    public Integer getItem4() {
        return item4;
    }

    public Integer getItem5() {
        return item5;
    }
}
