//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.0-b170531.0717 
// Consulte <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.07.16 às 07:09:51 PM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de xmlTypeItemStat complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="xmlTypeItemStat"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="stat" type="{http://la2j.org}xmlTypeStat" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="skill" type="{http://la2j.org}xmlItemSkill" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlTypeItemStat", propOrder = {
    "stat",
    "skill"
})
public class XmlTypeItemStat {

    protected List<XmlTypeStat> stat;
    protected List<XmlItemSkill> skill;
    @XmlAttribute(name = "id", required = true)
    protected int id;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the stat property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlTypeStat }
     * 
     * 
     */
    public List<XmlTypeStat> getStat() {
        if (stat == null) {
            stat = new ArrayList<XmlTypeStat>();
        }
        return this.stat;
    }

    /**
     * Gets the value of the skill property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the skill property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSkill().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlItemSkill }
     * 
     * 
     */
    public List<XmlItemSkill> getSkill() {
        if (skill == null) {
            skill = new ArrayList<XmlItemSkill>();
        }
        return this.skill;
    }

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
     * Obtém o valor da propriedade name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define o valor da propriedade name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
