package com.l2jbr.gameserver.model.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.commons.util.Util;
import com.l2jbr.gameserver.datatables.ItemTable;
import com.l2jbr.gameserver.model.base.ClassId;
import com.l2jbr.gameserver.model.base.Race;
import com.l2jbr.gameserver.templates.L2Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.LinkedList;
import java.util.List;

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

    @Transient
    private ClassId classId;
    @Transient
    private Race race;
    @Transient
    private List<L2Item> items;

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

    public ClassId getClassId() {
        return classId;
    }

    public Race getRace() {
        return race;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setHpRegen(1.5f);
        setMpRegen(0.9f);

        classId = ClassId.values()[id];
        race = Race.values()[raceId];
        loadItems();
    }

    private void loadItems() {
        items = new LinkedList<>();
        addItem(item1);
        addItem(item2);
        addItem(item3);
        addItem(item4);
        addItem(item5);
    }

    private void addItem(int itemId) {
        L2Item item = ItemTable.getInstance().getTemplate(itemId);
        if(Util.isNotNull(item)) {
            items.add(item);
        }
    }

    public List<L2Item> getItems() {
        return items;
    }
}
