package com.l2jbr.gameserver.model.entity.database;

import com.l2jbr.commons.database.annotation.Column;
import com.l2jbr.commons.database.model.Entity;
import org.springframework.data.annotation.Transient;

public abstract class CharTemplate extends Entity<Integer> {

    @Column("walk_spd")
    private short walkSpd;
    @Column("run_spd")
    private short runSpd;
    private short strength;
    private short constitution;
    private short dexterity;
    private short intelligence;
    private short witness;
    private short mentality;
    @Column("p_atk")
    private short pAtk;
    @Column("p_def")
    private int pDef;
    @Column("m_atk")
    private int mAtk;
    @Column("m_def")
    private int mDef;
    @Column("p_atk_spd")
    private short pAtkSpd;
    @Column("m_atk_spd")
    private short mAtkSpd;
    @Column("crit_rate")
    private short critRate;
    private float hp;
    private float mp;
    @Column("hp_regen")
    private float hpRegen;
    @Column("mp_regen")
    private float mpRegen;
    @Column("atk_range")
    private short atkRange;
    @Column("collision_radius")
    private float collisionRadius;
    @Column("collision_height")
    private float collisionHeight;

    @Transient
    private float mReuseRate = 1;
    @Transient
    private short shieldDef = 0;
    @Transient
    private short shieldRate = 0;
    @Transient
    private float mpConsumeRate = 0;
    @Transient
    private float hpConsumeRate =  0;
    @Transient
    private short breath = 100;
    @Transient
    private short aggression = 0;
    @Transient
    private short bleed =  0;
    @Transient
    private short poison = 0;
    @Transient
    private short stun = 0;
    @Transient
    private short root = 0;
    @Transient
    private short movement =  0;
    @Transient
    private short confusion =  0;
    @Transient
    private short  sleep =  0;
    @Transient
    private short fire =  0;
    @Transient
    private short wind =  0;
    @Transient
    private short water =  0;
    @Transient
    private short earth =  0;
    @Transient
    private short holy =  0;
    @Transient
    private short dark =  0;
    @Transient
    private float aggressionVuln =  1;
    @Transient
    private float bleedVuln =  1;
    @Transient
    private float poisonVuln =  1;
    @Transient
    private float stunVuln =  1;
    @Transient
    private float rootVuln =  1;
    @Transient
    private float movementVuln =  1;
    @Transient
    private float confusionVuln =  1;
    @Transient
    private float sleepVuln =  1;
    @Transient
    private float fireVuln =  1;
    @Transient
    private float windVuln =  1;
    @Transient
    private float waterVuln =  1;
    @Transient
    private float earthVuln =  1;
    @Transient
    private float holyVuln =  1;
    @Transient
    private float darkVuln =  1;


    public static CharTemplate objectTemplate(int hp, int pdef, int mdef, float collisionRadius, float collisionHeight) {
        CharTemplate template = new CharTemplate() {
            @Override
            public Integer getId() {
                return 0;
            }
        };
        template.hp = hp;
        template.pDef = pdef;
        template.mDef = mdef;
        template.collisionRadius = collisionRadius;
        template.collisionHeight = collisionHeight;
        template.hpRegen = 3.e-3f;
        return template;
    }

    public int getStrength() {
        return strength;
    }

    public short getConstitution() {
        return constitution;
    }

    public short getDexterity() {
        return dexterity;
    }

    public short getIntellienge() { return intelligence;  }

    public short getWitness() {
        return witness;
    }

    public short getMentality() {
        return mentality;
    }

    public short getpAtk() {
        return pAtk;
    }

    public int getpDef() {
        return pDef;
    }

    public int getMAtk() {
        return mAtk;
    }

    public int getMDef() {
        return mDef;
    }

    public short getPAtkSpd() {
        return pAtkSpd;
    }

    public short getMAtkSpd() {
        return mAtkSpd;
    }

    public short getCritRate() {
        return critRate;
    }

    public float getHp() {
        return hp;
    }

    public float getMp() {
        return mp;
    }

    public float getCollisionRadius() {
        return collisionRadius;
    }

    public float getCollisionHeight() {
        return collisionHeight;
    }

    public short getAtkRange() {
        return atkRange;
    }

    public short getWalkSpd() {
        return walkSpd;
    }

    public float getHpRegen() {
        return hpRegen;
    }

    protected void setHpRegen(float hpRegen) {
        this.hpRegen = hpRegen;
    }

    public float getMpRegen() {
        return mpRegen;
    }

    protected void setMpRegen(float mpRegen) {
        this.mpRegen = mpRegen;
    }

    public short getRunSpd() {
        return runSpd;
    }

    public short getAggression() {
        return aggression;
    }

    public short getBreath() {
        return breath;
    }

    public short getBleed() {
        return bleed;
    }

    public short getPoison() {
        return poison;
    }

    public short getStun() {
        return stun;
    }

    public short getRoot() {
        return root;
    }

    public short getMovement() {
        return movement;
    }

    public short getConfusion() {
        return confusion;
    }

    public short getSleep() {
        return sleep;
    }

    public short getFire() {
        return fire;
    }

    public short getWind() {
        return wind;
    }

    public short getWater() {
        return water;
    }

    public short getEarth() {
        return earth;
    }

    public short getHoly() {
        return holy;
    }

    public short getDark() {
        return dark;
    }

    public float getAggressionVuln() {
        return aggressionVuln;
    }

    public float getBleedVuln() {
        return bleedVuln;
    }

    public float getPoisonVuln() {
        return poisonVuln;
    }

    public float getStunVuln() {
        return stunVuln;
    }

    public float getRootVuln() {
        return rootVuln;
    }

    public float getMovementVuln() {
        return movementVuln;
    }

    public float getConfusionVuln() {
        return confusionVuln;
    }

    public float getSleepVuln() {
        return sleepVuln;
    }

    public float getFireVuln() {
        return fireVuln;
    }

    public float getWindVuln() {
        return windVuln;
    }

    public float getWaterVuln() {
        return waterVuln;
    }

    public float getEarthVuln() {
        return earthVuln;
    }

    public float getHolyVuln() {
        return holyVuln;
    }

    public float getDarkVuln() {
        return darkVuln;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getCp() {
        return 0;
    }

    public float getMReuseRate() {
        return mReuseRate;
    }
}
