//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.0-b170531.0717 
// Consulte <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.07.18 às 02:22:37 PM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element ref="{http://la2j.org}operator"/&gt;
 *         &lt;element ref="{http://la2j.org}condition"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlStatConditionAND", propOrder = {
    "operatorOrCondition"
})
public class XmlStatConditionAND
    extends XmlStatConditionOperator
{

    @XmlElementRefs({
        @XmlElementRef(name = "operator", namespace = "http://la2j.org", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "condition", namespace = "http://la2j.org", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> operatorOrCondition;

    /**
     * Gets the value of the operatorOrCondition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the operatorOrCondition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOperatorOrCondition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link XmlStatConditionAND }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatConditionNOT }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatConditionOR }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatConditionOperator }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatPlayerCondition }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatGameCondition }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatUsingCondition }{@code >}
     * {@link JAXBElement }{@code <}{@link XmlStatConditionType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getOperatorOrCondition() {
        if (operatorOrCondition == null) {
            operatorOrCondition = new ArrayList<JAXBElement<?>>();
        }
        return this.operatorOrCondition;
    }

}
