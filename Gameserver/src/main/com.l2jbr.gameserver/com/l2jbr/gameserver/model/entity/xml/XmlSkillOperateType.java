//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.0-b170531.0717 
// Consulte <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.07.18 às 02:22:37 PM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de xmlSkillOperateType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="xmlSkillOperateType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OP_ACTIVE"/&gt;
 *     &lt;enumeration value="OP_TOGGLE"/&gt;
 *     &lt;enumeration value="OP_PASSIVE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "xmlSkillOperateType")
@XmlEnum
public enum XmlSkillOperateType {

    OP_ACTIVE,
    OP_TOGGLE,
    OP_PASSIVE;

    public String value() {
        return name();
    }

    public static XmlSkillOperateType fromValue(String v) {
        return valueOf(v);
    }

}
