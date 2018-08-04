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
 * <p>Classe Java de xmlTypeStat complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="xmlTypeStat"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="conditions" type="{http://la2j.org}xmlStatCondition" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="function" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="name" use="required" type="{http://la2j.org}xmlTypeEnumStat" /&gt;
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}float" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlTypeStat", propOrder = {
    "conditions"
})
public class XmlTypeStat {

    protected XmlStatCondition conditions;
    @XmlAttribute(name = "order", required = true)
    protected int order;
    @XmlAttribute(name = "function", required = true)
    protected String function;
    @XmlAttribute(name = "name", required = true)
    protected XmlTypeEnumStat name;
    @XmlAttribute(name = "value", required = true)
    protected float value;

    /**
     * Obtém o valor da propriedade conditions.
     * 
     * @return
     *     possible object is
     *     {@link XmlStatCondition }
     *     
     */
    public XmlStatCondition getConditions() {
        return conditions;
    }

    /**
     * Define o valor da propriedade conditions.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlStatCondition }
     *     
     */
    public void setConditions(XmlStatCondition value) {
        this.conditions = value;
    }

    /**
     * Obtém o valor da propriedade order.
     * 
     */
    public int getOrder() {
        return order;
    }

    /**
     * Define o valor da propriedade order.
     * 
     */
    public void setOrder(int value) {
        this.order = value;
    }

    /**
     * Obtém o valor da propriedade function.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunction() {
        return function;
    }

    /**
     * Define o valor da propriedade function.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunction(String value) {
        this.function = value;
    }

    /**
     * Obtém o valor da propriedade name.
     * 
     * @return
     *     possible object is
     *     {@link XmlTypeEnumStat }
     *     
     */
    public XmlTypeEnumStat getName() {
        return name;
    }

    /**
     * Define o valor da propriedade name.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlTypeEnumStat }
     *     
     */
    public void setName(XmlTypeEnumStat value) {
        this.name = value;
    }

    /**
     * Obtém o valor da propriedade value.
     * 
     */
    public float getValue() {
        return value;
    }

    /**
     * Define o valor da propriedade value.
     * 
     */
    public void setValue(float value) {
        this.value = value;
    }

}
