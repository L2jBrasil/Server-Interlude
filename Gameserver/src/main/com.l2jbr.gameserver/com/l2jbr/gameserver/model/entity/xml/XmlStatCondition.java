//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.1-b171012.0423 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.08.04 às 08:41:51 AM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de xmlStatCondition complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="xmlStatCondition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element ref="{http://la2j.org}operator"/&gt;
 *         &lt;element ref="{http://la2j.org}condition"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlStatCondition", propOrder = {
    "operator",
    "condition"
})
public class XmlStatCondition {

    @XmlElementRef(name = "operator", namespace = "http://la2j.org", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends XmlStatConditionOperator> operator;
    @XmlElementRef(name = "condition", namespace = "http://la2j.org", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends XmlStatConditionType> condition;

    /**
     * Obtém o valor da propriedade operator.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionAND }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionNOT }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionOR }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionOperator }{@code >}
     *     
     */
    public JAXBElement<? extends XmlStatConditionOperator> getOperator() {
        return operator;
    }

    /**
     * Define o valor da propriedade operator.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionAND }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionNOT }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionOR }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionOperator }{@code >}
     *     
     */
    public void setOperator(JAXBElement<? extends XmlStatConditionOperator> value) {
        this.operator = value;
    }

    /**
     * Obtém o valor da propriedade condition.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XmlStatPlayerCondition }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatUsingCondition }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatGameCondition }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionType }{@code >}
     *     
     */
    public JAXBElement<? extends XmlStatConditionType> getCondition() {
        return condition;
    }

    /**
     * Define o valor da propriedade condition.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XmlStatPlayerCondition }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatUsingCondition }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatGameCondition }{@code >}
     *     {@link JAXBElement }{@code <}{@link XmlStatConditionType }{@code >}
     *     
     */
    public void setCondition(JAXBElement<? extends XmlStatConditionType> value) {
        this.condition = value;
    }

}
