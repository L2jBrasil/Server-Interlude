//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.1-b171012.0423 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.08.04 às 08:41:51 AM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.l2jbr.gameserver.model.entity.xml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Operator_QNAME = new QName("http://la2j.org", "operator");
    private final static QName _And_QNAME = new QName("http://la2j.org", "and");
    private final static QName _Or_QNAME = new QName("http://la2j.org", "or");
    private final static QName _Not_QNAME = new QName("http://la2j.org", "not");
    private final static QName _Condition_QNAME = new QName("http://la2j.org", "condition");
    private final static QName _Using_QNAME = new QName("http://la2j.org", "using");
    private final static QName _Player_QNAME = new QName("http://la2j.org", "player");
    private final static QName _Game_QNAME = new QName("http://la2j.org", "game");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.l2jbr.gameserver.model.entity.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XmlStatConditionAND }
     * 
     */
    public XmlStatConditionAND createXmlStatConditionAND() {
        return new XmlStatConditionAND();
    }

    /**
     * Create an instance of {@link XmlStatConditionOR }
     * 
     */
    public XmlStatConditionOR createXmlStatConditionOR() {
        return new XmlStatConditionOR();
    }

    /**
     * Create an instance of {@link XmlStatConditionNOT }
     * 
     */
    public XmlStatConditionNOT createXmlStatConditionNOT() {
        return new XmlStatConditionNOT();
    }

    /**
     * Create an instance of {@link XmlStatUsingCondition }
     * 
     */
    public XmlStatUsingCondition createXmlStatUsingCondition() {
        return new XmlStatUsingCondition();
    }

    /**
     * Create an instance of {@link XmlStatPlayerCondition }
     * 
     */
    public XmlStatPlayerCondition createXmlStatPlayerCondition() {
        return new XmlStatPlayerCondition();
    }

    /**
     * Create an instance of {@link XmlStatGameCondition }
     * 
     */
    public XmlStatGameCondition createXmlStatGameCondition() {
        return new XmlStatGameCondition();
    }

    /**
     * Create an instance of {@link ItemList }
     * 
     */
    public ItemList createItemList() {
        return new ItemList();
    }

    /**
     * Create an instance of {@link ItemStat }
     * 
     */
    public ItemStat createItemStat() {
        return new ItemStat();
    }

    /**
     * Create an instance of {@link SkillList }
     * 
     */
    public SkillList createSkillList() {
        return new SkillList();
    }

    /**
     * Create an instance of {@link XmlTypeSkill }
     * 
     */
    public XmlTypeSkill createXmlTypeSkill() {
        return new XmlTypeSkill();
    }

    /**
     * Create an instance of {@link XmlTypeStat }
     * 
     */
    public XmlTypeStat createXmlTypeStat() {
        return new XmlTypeStat();
    }

    /**
     * Create an instance of {@link XmlStatCondition }
     * 
     */
    public XmlStatCondition createXmlStatCondition() {
        return new XmlStatCondition();
    }

    /**
     * Create an instance of {@link XmlItemSkill }
     * 
     */
    public XmlItemSkill createXmlItemSkill() {
        return new XmlItemSkill();
    }

    /**
     * Create an instance of {@link XmlTypeConsume }
     * 
     */
    public XmlTypeConsume createXmlTypeConsume() {
        return new XmlTypeConsume();
    }

    /**
     * Create an instance of {@link XmlTypeCharge }
     * 
     */
    public XmlTypeCharge createXmlTypeCharge() {
        return new XmlTypeCharge();
    }

    /**
     * Create an instance of {@link XmlTypeEnchant }
     * 
     */
    public XmlTypeEnchant createXmlTypeEnchant() {
        return new XmlTypeEnchant();
    }

    /**
     * Create an instance of {@link SkillCondition }
     * 
     */
    public SkillCondition createSkillCondition() {
        return new SkillCondition();
    }

    /**
     * Create an instance of {@link XmlTypeEffect }
     * 
     */
    public XmlTypeEffect createXmlTypeEffect() {
        return new XmlTypeEffect();
    }

    /**
     * Create an instance of {@link EffectList }
     * 
     */
    public EffectList createEffectList() {
        return new EffectList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlStatConditionOperator }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XmlStatConditionOperator }{@code >}
     */
    @XmlElementDecl(namespace = "http://la2j.org", name = "operator")
    public JAXBElement<XmlStatConditionOperator> createOperator(XmlStatConditionOperator value) {
        return new JAXBElement<XmlStatConditionOperator>(_Operator_QNAME, XmlStatConditionOperator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlStatConditionAND }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XmlStatConditionAND }{@code >}
     */
    @XmlElementDecl(namespace = "http://la2j.org", name = "and", substitutionHeadNamespace = "http://la2j.org", substitutionHeadName = "operator")
    public JAXBElement<XmlStatConditionAND> createAnd(XmlStatConditionAND value) {
        return new JAXBElement<XmlStatConditionAND>(_And_QNAME, XmlStatConditionAND.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlStatConditionOR }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XmlStatConditionOR }{@code >}
     */
    @XmlElementDecl(namespace = "http://la2j.org", name = "or", substitutionHeadNamespace = "http://la2j.org", substitutionHeadName = "operator")
    public JAXBElement<XmlStatConditionOR> createOr(XmlStatConditionOR value) {
        return new JAXBElement<XmlStatConditionOR>(_Or_QNAME, XmlStatConditionOR.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlStatConditionNOT }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XmlStatConditionNOT }{@code >}
     */
    @XmlElementDecl(namespace = "http://la2j.org", name = "not", substitutionHeadNamespace = "http://la2j.org", substitutionHeadName = "operator")
    public JAXBElement<XmlStatConditionNOT> createNot(XmlStatConditionNOT value) {
        return new JAXBElement<XmlStatConditionNOT>(_Not_QNAME, XmlStatConditionNOT.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlStatConditionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XmlStatConditionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://la2j.org", name = "condition")
    public JAXBElement<XmlStatConditionType> createCondition(XmlStatConditionType value) {
        return new JAXBElement<XmlStatConditionType>(_Condition_QNAME, XmlStatConditionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlStatUsingCondition }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XmlStatUsingCondition }{@code >}
     */
    @XmlElementDecl(namespace = "http://la2j.org", name = "using", substitutionHeadNamespace = "http://la2j.org", substitutionHeadName = "condition")
    public JAXBElement<XmlStatUsingCondition> createUsing(XmlStatUsingCondition value) {
        return new JAXBElement<XmlStatUsingCondition>(_Using_QNAME, XmlStatUsingCondition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlStatPlayerCondition }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XmlStatPlayerCondition }{@code >}
     */
    @XmlElementDecl(namespace = "http://la2j.org", name = "player", substitutionHeadNamespace = "http://la2j.org", substitutionHeadName = "condition")
    public JAXBElement<XmlStatPlayerCondition> createPlayer(XmlStatPlayerCondition value) {
        return new JAXBElement<XmlStatPlayerCondition>(_Player_QNAME, XmlStatPlayerCondition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlStatGameCondition }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link XmlStatGameCondition }{@code >}
     */
    @XmlElementDecl(namespace = "http://la2j.org", name = "game", substitutionHeadNamespace = "http://la2j.org", substitutionHeadName = "condition")
    public JAXBElement<XmlStatGameCondition> createGame(XmlStatGameCondition value) {
        return new JAXBElement<XmlStatGameCondition>(_Game_QNAME, XmlStatGameCondition.class, null, value);
    }

}
