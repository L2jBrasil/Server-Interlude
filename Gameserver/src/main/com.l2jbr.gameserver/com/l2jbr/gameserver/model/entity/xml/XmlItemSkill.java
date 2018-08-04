//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.1-b171012.0423 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.08.04 às 08:41:51 AM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de xmlItemSkill complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="xmlItemSkill"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="level" type="{http://www.w3.org/2001/XMLSchema}int" default="1" /&gt;
 *       &lt;attribute name="chance" type="{http://www.w3.org/2001/XMLSchema}int" default="100" /&gt;
 *       &lt;attribute name="triggerType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlItemSkill")
public class XmlItemSkill {

    @XmlAttribute(name = "id", required = true)
    protected int id;
    @XmlAttribute(name = "level")
    protected Integer level;
    @XmlAttribute(name = "chance")
    protected Integer chance;
    @XmlAttribute(name = "triggerType", required = true)
    protected String triggerType;

    /**
     * Obtém o valor da propriedade id.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Define o valor da propriedade id.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Obtém o valor da propriedade level.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getLevel() {
        if (level == null) {
            return  1;
        } else {
            return level;
        }
    }

    /**
     * Define o valor da propriedade level.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLevel(Integer value) {
        this.level = value;
    }

    /**
     * Obtém o valor da propriedade chance.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getChance() {
        if (chance == null) {
            return  100;
        } else {
            return chance;
        }
    }

    /**
     * Define o valor da propriedade chance.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setChance(Integer value) {
        this.chance = value;
    }

    /**
     * Obtém o valor da propriedade triggerType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTriggerType() {
        return triggerType;
    }

    /**
     * Define o valor da propriedade triggerType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTriggerType(String value) {
        this.triggerType = value;
    }

}
