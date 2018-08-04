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
 * <p>Classe Java de xmlSkillType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="xmlSkillType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PDAM"/&gt;
 *     &lt;enumeration value="CONFUSE_MOB_ONLY"/&gt;
 *     &lt;enumeration value="BUFF"/&gt;
 *     &lt;enumeration value="CHARGEDAM"/&gt;
 *     &lt;enumeration value="CHARGE"/&gt;
 *     &lt;enumeration value="SUMMON"/&gt;
 *     &lt;enumeration value="AGGREDUCE_CHAR"/&gt;
 *     &lt;enumeration value="AGGREDUCE"/&gt;
 *     &lt;enumeration value="BLOW"/&gt;
 *     &lt;enumeration value="UNPOISON"/&gt;
 *     &lt;enumeration value="UNLOCK"/&gt;
 *     &lt;enumeration value="AGGDAMAGE"/&gt;
 *     &lt;enumeration value="UNBLEED"/&gt;
 *     &lt;enumeration value="SWEEP"/&gt;
 *     &lt;enumeration value="HEAL"/&gt;
 *     &lt;enumeration value="DRAIN"/&gt;
 *     &lt;enumeration value="MDAM"/&gt;
 *     &lt;enumeration value="FAKE_DEATH"/&gt;
 *     &lt;enumeration value="FEAR"/&gt;
 *     &lt;enumeration value="REFLECT"/&gt;
 *     &lt;enumeration value="STUN"/&gt;
 *     &lt;enumeration value="DEBUFF"/&gt;
 *     &lt;enumeration value="BLEED"/&gt;
 *     &lt;enumeration value="POISON"/&gt;
 *     &lt;enumeration value="ROOT"/&gt;
 *     &lt;enumeration value="HEAL_PERCENT"/&gt;
 *     &lt;enumeration value="NOT_DONE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "xmlSkillType")
@XmlEnum
public enum XmlSkillType {

    PDAM,
    CONFUSE_MOB_ONLY,
    BUFF,
    CHARGEDAM,
    CHARGE,
    SUMMON,
    AGGREDUCE_CHAR,
    AGGREDUCE,
    BLOW,
    UNPOISON,
    UNLOCK,
    AGGDAMAGE,
    UNBLEED,
    SWEEP,
    HEAL,
    DRAIN,
    MDAM,
    FAKE_DEATH,
    FEAR,
    REFLECT,
    STUN,
    DEBUFF,
    BLEED,
    POISON,
    ROOT,
    HEAL_PERCENT,
    NOT_DONE;

    public String value() {
        return name();
    }

    public static XmlSkillType fromValue(String v) {
        return valueOf(v);
    }

}
