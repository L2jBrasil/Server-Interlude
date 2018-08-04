//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.1-b171012.0423 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.08.04 às 08:41:51 AM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;


/**
 * <p>Classe Java de xmlTypeEffect complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="xmlTypeEffect"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="stat" type="{http://la2j.org}xmlTypeStat" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="time" use="required" type="{http://www.w3.org/2001/XMLSchema}duration" /&gt;
 *       &lt;attribute name="count" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="stackType" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="stackOrder" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlTypeEffect", propOrder = {
    "stat"
})
public class XmlTypeEffect {

    protected List<XmlTypeStat> stat;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "time", required = true)
    protected Duration time;
    @XmlAttribute(name = "count")
    protected BigInteger count;
    @XmlAttribute(name = "stackType")
    protected String stackType;
    @XmlAttribute(name = "stackOrder")
    protected String stackOrder;

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

    /**
     * Obtém o valor da propriedade time.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getTime() {
        return time;
    }

    /**
     * Define o valor da propriedade time.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setTime(Duration value) {
        this.time = value;
    }

    /**
     * Obtém o valor da propriedade count.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * Define o valor da propriedade count.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCount(BigInteger value) {
        this.count = value;
    }

    /**
     * Obtém o valor da propriedade stackType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStackType() {
        return stackType;
    }

    /**
     * Define o valor da propriedade stackType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStackType(String value) {
        this.stackType = value;
    }

    /**
     * Obtém o valor da propriedade stackOrder.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStackOrder() {
        return stackOrder;
    }

    /**
     * Define o valor da propriedade stackOrder.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStackOrder(String value) {
        this.stackOrder = value;
    }

}
