//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.1-b171012.0423 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.08.04 às 08:41:51 AM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de xmlTypeEnumStat.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="xmlTypeEnumStat"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;whiteSpace value="collapse"/&gt;
 *     &lt;enumeration value="MAX_HP"/&gt;
 *     &lt;enumeration value="MAX_MP"/&gt;
 *     &lt;enumeration value="MAX_CP"/&gt;
 *     &lt;enumeration value="REGENERATE_HP_RATE"/&gt;
 *     &lt;enumeration value="REGENERATE_CP_RATE"/&gt;
 *     &lt;enumeration value="REGENERATE_MP_RATE"/&gt;
 *     &lt;enumeration value="RECHARGE_MP_RATE"/&gt;
 *     &lt;enumeration value="HEAL_EFFECTIVNESS"/&gt;
 *     &lt;enumeration value="PHYSIC_DEFENCE"/&gt;
 *     &lt;enumeration value="MAGIC_DEFENCE"/&gt;
 *     &lt;enumeration value="PHYSIC_ATTACK"/&gt;
 *     &lt;enumeration value="MAGIC_ATTACK"/&gt;
 *     &lt;enumeration value="PHYSIC_ATTACK_SPEED"/&gt;
 *     &lt;enumeration value="MAGIC_ATTACK_SPEED"/&gt;
 *     &lt;enumeration value="MAGIC_REUSE_RATE"/&gt;
 *     &lt;enumeration value="SHIELD_DEFENCE"/&gt;
 *     &lt;enumeration value="CRITICAL_DAMAGE"/&gt;
 *     &lt;enumeration value="PVP_PHYSIC_DAMAGE"/&gt;
 *     &lt;enumeration value="PVP_MAGIC_DAMAGE"/&gt;
 *     &lt;enumeration value="PVP_PHYS_SKILL_DMG"/&gt;
 *     &lt;enumeration value="EVASION_RATE"/&gt;
 *     &lt;enumeration value="SHIELD_RATE"/&gt;
 *     &lt;enumeration value="CRITICAL_RATE"/&gt;
 *     &lt;enumeration value="BLOW_RATE"/&gt;
 *     &lt;enumeration value="LETHAL_RATE"/&gt;
 *     &lt;enumeration value="MCRITICAL_RATE"/&gt;
 *     &lt;enumeration value="EXPSP_RATE"/&gt;
 *     &lt;enumeration value="ATTACK_CANCEL"/&gt;
 *     &lt;enumeration value="ACCURACY"/&gt;
 *     &lt;enumeration value="PHYSIC_ATTACK_RANGE"/&gt;
 *     &lt;enumeration value="MAGIC_ATTACK_RANGE"/&gt;
 *     &lt;enumeration value="PHYSIC_ATTACK_ANGLE"/&gt;
 *     &lt;enumeration value="ATTACK_COUNT_MAX"/&gt;
 *     &lt;enumeration value="RUN_SPEED"/&gt;
 *     &lt;enumeration value="WALK_SPEED"/&gt;
 *     &lt;enumeration value="STAT_STR"/&gt;
 *     &lt;enumeration value="STAT_CON"/&gt;
 *     &lt;enumeration value="STAT_DEX"/&gt;
 *     &lt;enumeration value="STAT_INT"/&gt;
 *     &lt;enumeration value="STAT_WIT"/&gt;
 *     &lt;enumeration value="STAT_MEN"/&gt;
 *     &lt;enumeration value="BREATH"/&gt;
 *     &lt;enumeration value="AGGRESSION"/&gt;
 *     &lt;enumeration value="BLEED"/&gt;
 *     &lt;enumeration value="POISON"/&gt;
 *     &lt;enumeration value="STUN"/&gt;
 *     &lt;enumeration value="ROOT"/&gt;
 *     &lt;enumeration value="MOVEMENT"/&gt;
 *     &lt;enumeration value="CONFUSION"/&gt;
 *     &lt;enumeration value="SLEEP"/&gt;
 *     &lt;enumeration value="FIRE"/&gt;
 *     &lt;enumeration value="WIND"/&gt;
 *     &lt;enumeration value="WATER"/&gt;
 *     &lt;enumeration value="EARTH"/&gt;
 *     &lt;enumeration value="HOLY"/&gt;
 *     &lt;enumeration value="DARK"/&gt;
 *     &lt;enumeration value="AGGRESSION_VULN"/&gt;
 *     &lt;enumeration value="BLEED_VULN"/&gt;
 *     &lt;enumeration value="POISON_VULN"/&gt;
 *     &lt;enumeration value="STUN_VULN"/&gt;
 *     &lt;enumeration value="PARALYZE_VULN"/&gt;
 *     &lt;enumeration value="ROOT_VULN"/&gt;
 *     &lt;enumeration value="SLEEP_VULN"/&gt;
 *     &lt;enumeration value="CONFUSION_VULN"/&gt;
 *     &lt;enumeration value="MOVEMENT_VULN"/&gt;
 *     &lt;enumeration value="FIRE_VULN"/&gt;
 *     &lt;enumeration value="WIND_VULN"/&gt;
 *     &lt;enumeration value="WATER_VULN"/&gt;
 *     &lt;enumeration value="EARTH_VULN"/&gt;
 *     &lt;enumeration value="HOLY_VULN"/&gt;
 *     &lt;enumeration value="DARK_VULN"/&gt;
 *     &lt;enumeration value="CANCEL_VULN"/&gt;
 *     &lt;enumeration value="DERANGEMENT_VULN"/&gt;
 *     &lt;enumeration value="DEBUFF_VULN"/&gt;
 *     &lt;enumeration value="NONE_WPN_VULN"/&gt;
 *     &lt;enumeration value="SWORD_WPN_VULN"/&gt;
 *     &lt;enumeration value="BLUNT_WPN_VULN"/&gt;
 *     &lt;enumeration value="DAGGER_WPN_VULN"/&gt;
 *     &lt;enumeration value="BOW_WPN_VULN"/&gt;
 *     &lt;enumeration value="POLE_WPN_VULN"/&gt;
 *     &lt;enumeration value="ETC_WPN_VULN"/&gt;
 *     &lt;enumeration value="FIST_WPN_VULN"/&gt;
 *     &lt;enumeration value="DUAL_WPN_VULN"/&gt;
 *     &lt;enumeration value="DUALFIST_WPN_VULN"/&gt;
 *     &lt;enumeration value="REFLECT_DAMAGE_PERCENT"/&gt;
 *     &lt;enumeration value="REFLECT_SKILL_MAGIC"/&gt;
 *     &lt;enumeration value="REFLECT_SKILL_PHYSIC"/&gt;
 *     &lt;enumeration value="ABSORB_DAMAGE_PERCENT"/&gt;
 *     &lt;enumeration value="TRANSFER_DAMAGE_PERCENT"/&gt;
 *     &lt;enumeration value="MAX_LOAD"/&gt;
 *     &lt;enumeration value="PATK_PLANTS"/&gt;
 *     &lt;enumeration value="PATK_INSECTS"/&gt;
 *     &lt;enumeration value="PATK_ANIMALS"/&gt;
 *     &lt;enumeration value="PATK_MONSTERS"/&gt;
 *     &lt;enumeration value="PATK_DRAGONS"/&gt;
 *     &lt;enumeration value="PATK_UNDEAD"/&gt;
 *     &lt;enumeration value="PDEF_UNDEAD"/&gt;
 *     &lt;enumeration value="ATTACK_REUSE"/&gt;
 *     &lt;enumeration value="INV_LIM"/&gt;
 *     &lt;enumeration value="WH_LIM"/&gt;
 *     &lt;enumeration value="FREIGHT_LIM"/&gt;
 *     &lt;enumeration value="P_SELL_LIM"/&gt;
 *     &lt;enumeration value="P_BUY_LIM"/&gt;
 *     &lt;enumeration value="REC_D_LIM"/&gt;
 *     &lt;enumeration value="REC_C_LIM"/&gt;
 *     &lt;enumeration value="MP_CONSUME_RATE"/&gt;
 *     &lt;enumeration value="HP_CONSUME_RATE"/&gt;
 *     &lt;enumeration value="MP_CONSUME"/&gt;
 *     &lt;enumeration value="SOULSHOT_COUNT"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "xmlTypeEnumStat")
@XmlEnum
public enum XmlTypeEnumStat {

    MAX_HP,
    MAX_MP,
    MAX_CP,
    REGENERATE_HP_RATE,
    REGENERATE_CP_RATE,
    REGENERATE_MP_RATE,
    RECHARGE_MP_RATE,
    HEAL_EFFECTIVNESS,
    PHYSIC_DEFENCE,
    MAGIC_DEFENCE,
    PHYSIC_ATTACK,
    MAGIC_ATTACK,
    PHYSIC_ATTACK_SPEED,
    MAGIC_ATTACK_SPEED,
    MAGIC_REUSE_RATE,
    SHIELD_DEFENCE,
    CRITICAL_DAMAGE,
    PVP_PHYSIC_DAMAGE,
    PVP_MAGIC_DAMAGE,
    PVP_PHYS_SKILL_DMG,
    EVASION_RATE,
    SHIELD_RATE,
    CRITICAL_RATE,
    BLOW_RATE,
    LETHAL_RATE,
    MCRITICAL_RATE,
    EXPSP_RATE,
    ATTACK_CANCEL,
    ACCURACY,
    PHYSIC_ATTACK_RANGE,
    MAGIC_ATTACK_RANGE,
    PHYSIC_ATTACK_ANGLE,
    ATTACK_COUNT_MAX,
    RUN_SPEED,
    WALK_SPEED,
    STAT_STR,
    STAT_CON,
    STAT_DEX,
    STAT_INT,
    STAT_WIT,
    STAT_MEN,
    BREATH,
    AGGRESSION,
    BLEED,
    POISON,
    STUN,
    ROOT,
    MOVEMENT,
    CONFUSION,
    SLEEP,
    FIRE,
    WIND,
    WATER,
    EARTH,
    HOLY,
    DARK,
    AGGRESSION_VULN,
    BLEED_VULN,
    POISON_VULN,
    STUN_VULN,
    PARALYZE_VULN,
    ROOT_VULN,
    SLEEP_VULN,
    CONFUSION_VULN,
    MOVEMENT_VULN,
    FIRE_VULN,
    WIND_VULN,
    WATER_VULN,
    EARTH_VULN,
    HOLY_VULN,
    DARK_VULN,
    CANCEL_VULN,
    DERANGEMENT_VULN,
    DEBUFF_VULN,
    NONE_WPN_VULN,
    SWORD_WPN_VULN,
    BLUNT_WPN_VULN,
    DAGGER_WPN_VULN,
    BOW_WPN_VULN,
    POLE_WPN_VULN,
    ETC_WPN_VULN,
    FIST_WPN_VULN,
    DUAL_WPN_VULN,
    DUALFIST_WPN_VULN,
    REFLECT_DAMAGE_PERCENT,
    REFLECT_SKILL_MAGIC,
    REFLECT_SKILL_PHYSIC,
    ABSORB_DAMAGE_PERCENT,
    TRANSFER_DAMAGE_PERCENT,
    MAX_LOAD,
    PATK_PLANTS,
    PATK_INSECTS,
    PATK_ANIMALS,
    PATK_MONSTERS,
    PATK_DRAGONS,
    PATK_UNDEAD,
    PDEF_UNDEAD,
    ATTACK_REUSE,
    INV_LIM,
    WH_LIM,
    FREIGHT_LIM,
    P_SELL_LIM,
    P_BUY_LIM,
    REC_D_LIM,
    REC_C_LIM,
    MP_CONSUME_RATE,
    HP_CONSUME_RATE,
    MP_CONSUME,
    SOULSHOT_COUNT;

    public String value() {
        return name();
    }

    public static XmlTypeEnumStat fromValue(String v) {
        return valueOf(v);
    }

}
