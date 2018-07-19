//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.0-b170531.0717 
// Consulte <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.07.18 às 02:22:37 PM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de xmlStatUsingCondition complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="xmlStatUsingCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://la2j.org}xmlStatConditionType"&gt;
 *       &lt;attribute name="kind" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="slot" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="item" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="enchantment" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlStatUsingCondition")
public class XmlStatUsingCondition
    extends XmlStatConditionType
{

    @XmlAttribute(name = "kind", required = true)
    protected String kind;
    @XmlAttribute(name = "slot")
    protected Integer slot;
    @XmlAttribute(name = "item")
    protected Integer item;
    @XmlAttribute(name = "enchantment")
    protected Integer enchantment;

    /**
     * Obtém o valor da propriedade kind.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKind() {
        return kind;
    }

    /**
     * Define o valor da propriedade kind.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKind(String value) {
        this.kind = value;
    }

    /**
     * Obtém o valor da propriedade slot.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSlot() {
        return slot;
    }

    /**
     * Define o valor da propriedade slot.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSlot(Integer value) {
        this.slot = value;
    }

    /**
     * Obtém o valor da propriedade item.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getItem() {
        return item;
    }

    /**
     * Define o valor da propriedade item.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setItem(Integer value) {
        this.item = value;
    }

    /**
     * Obtém o valor da propriedade enchantment.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEnchantment() {
        return enchantment;
    }

    /**
     * Define o valor da propriedade enchantment.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEnchantment(Integer value) {
        this.enchantment = value;
    }

}
