package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.annotation.Table;
import com.l2jbr.gameserver.datatables.ItemTable;
import com.l2jbr.gameserver.model.base.PlayerClass;
import com.l2jbr.gameserver.model.base.Race;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Table("player_templates")
public class PlayerTemplate extends CharTemplate {

    @Id
    private Integer id;
    @Column("player_class")
    private PlayerClass playerClass;
    private short accuracy;
    private short evasion;
    @Column("_load")
    private int load;
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
    private List<ItemTemplate> items;

    @Override
    public void onLoad() {
        super.onLoad();
        setHpRegen(1.5f);
        setMpRegen(0.9f);
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
        ItemTemplate item = ItemTable.getInstance().getTemplate(itemId);
        if(nonNull(item)) {
            items.add(item);
        }
    }

    public List<ItemTemplate> getItems() {
        if(isNull(items)){
            loadItems();
        }
        return items;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public short getAccuracy() {
        return accuracy;
    }

    public short getEvasion() {
        return evasion;
    }

    public int getLoad() {
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

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public Race getRace() {
        return playerClass.getRace();
    }

    public int getClassLevel() {
        return playerClass.level();
    }
}
