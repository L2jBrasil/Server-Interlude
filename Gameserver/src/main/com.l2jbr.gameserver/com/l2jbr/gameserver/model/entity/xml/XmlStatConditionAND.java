//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.1-b171012.0423 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.08.04 às 08:41:51 AM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de xmlStatConditionAND complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="xmlStatConditionAND"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://la2j.org}xmlStatConditionOperator"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://la2j.org}operator" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://la2j.org}condition" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlStatConditionAND", propOrder = {
    "operator",
    "condition"
})
public class XmlStatConditionAND
    extends XmlStatConditionOperator
{

    @XmlElementRef(name = "operator", namespace = "http://la2j.org", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends XmlStatConditionOperator>> operator;
    @XmlElementRef(name = "condition", namespace = "http://la2j.org", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends XmlStatConditionType>> condition;

    /**
     * Gets the value of the operator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link XmlStatConditionAND }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatConditionNOT }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatConditionOR }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatConditionOperator }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends XmlStatConditionOperator>> getOperator() {
        if (operator == null) {
            operator = new ArrayList<JAXBElement<? extends XmlStatConditionOperator>>();
        }
        return this.operator;
    }

    /**
     * Gets the value of the condition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the condition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCondition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link XmlStatPlayerCondition }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatUsingCondition }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatGameCondition }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatConditionType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends XmlStatConditionType>> getCondition() {
        if (condition == null) {
            condition = new ArrayList<JAXBElement<? extends XmlStatConditionType>>();
        }
        return this.condition;
    }

}
