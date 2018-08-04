//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.3.1-b171012.0423 
// Consulte <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2018.08.04 às 08:41:51 AM BRT 
//


package com.l2jbr.gameserver.model.entity.xml;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.Duration;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java de xmlTypeSkill complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="xmlTypeSkill"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="listMagicLevel" type="{http://la2j.org}numberList"/&gt;
 *         &lt;element name="listMpConsume" type="{http://la2j.org}numberList"/&gt;
 *         &lt;element name="listPower" type="{http://la2j.org}numberList"/&gt;
 *         &lt;element name="listMpInitialConsume" type="{http://la2j.org}numberList" minOccurs="0"/&gt;
 *         &lt;element name="listEnchantMagicLevel" type="{http://la2j.org}numberList" minOccurs="0"/&gt;
 *         &lt;element name="listEnchantMpConsume" type="{http://la2j.org}numberList" minOccurs="0"/&gt;
 *         &lt;element name="listEnchantPower" type="{http://la2j.org}numberList" minOccurs="0"/&gt;
 *         &lt;element name="skillType" type="{http://la2j.org}xmlSkillType"/&gt;
 *         &lt;element name="operateType" type="{http://la2j.org}xmlSkillOperateType"/&gt;
 *         &lt;element name="target" type="{http://la2j.org}skillTargetType"/&gt;
 *         &lt;element name="power" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="magicLevel" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="mpConsume" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="reuseDelay" type="{http://www.w3.org/2001/XMLSchema}duration"/&gt;
 *         &lt;element name="hitTime" type="{http://www.w3.org/2001/XMLSchema}duration"/&gt;
 *         &lt;element name="castRange" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="effectRange" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="weaponsAllowed" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="mpInitialConsume" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="baseCritRate" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="SSBoost" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="skillRadius" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="coolTime" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/&gt;
 *         &lt;element name="enchant1" type="{http://la2j.org}xmlTypeEnchant" minOccurs="0"/&gt;
 *         &lt;element name="enchant2" type="{http://la2j.org}xmlTypeEnchant" minOccurs="0"/&gt;
 *         &lt;element name="cond" type="{http://la2j.org}skillCondition" minOccurs="0"/&gt;
 *         &lt;element name="conditionValue" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="condition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="effects" type="{http://la2j.org}effectList" minOccurs="0"/&gt;
 *         &lt;element name="charge" type="{http://la2j.org}xmlTypeCharge" minOccurs="0"/&gt;
 *         &lt;element name="consume" type="{http://la2j.org}xmlTypeConsume" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="levels" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *       &lt;attribute name="enchants" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *       &lt;attribute name="isMagic" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="ignoreShield" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="overHit" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlTypeSkill", propOrder = {

})
public class XmlTypeSkill {

    @XmlList
    @XmlElement(required = true)
    protected List<BigInteger> listMagicLevel;
    @XmlList
    @XmlElement(required = true)
    protected List<BigInteger> listMpConsume;
    @XmlList
    @XmlElement(required = true)
    protected List<BigInteger> listPower;
    @XmlList
    protected List<BigInteger> listMpInitialConsume;
    @XmlList
    protected List<BigInteger> listEnchantMagicLevel;
    @XmlList
    protected List<BigInteger> listEnchantMpConsume;
    @XmlList
    protected List<BigInteger> listEnchantPower;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected XmlSkillType skillType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected XmlSkillOperateType operateType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected SkillTargetType target;
    @XmlElement(required = true)
    protected String power;
    @XmlElement(required = true)
    protected String magicLevel;
    @XmlElement(required = true)
    protected String mpConsume;
    @XmlElement(required = true)
    protected Duration reuseDelay;
    @XmlElement(required = true)
    protected Duration hitTime;
    @XmlElement(required = true)
    protected BigInteger castRange;
    @XmlElement(required = true)
    protected BigInteger effectRange;
    protected BigInteger weaponsAllowed;
    protected String mpInitialConsume;
    protected BigInteger baseCritRate;
    @XmlElement(name = "SSBoost")
    protected BigDecimal ssBoost;
    protected BigInteger skillRadius;
    protected Duration coolTime;
    protected XmlTypeEnchant enchant1;
    protected XmlTypeEnchant enchant2;
    protected SkillCondition cond;
    protected BigInteger conditionValue;
    protected BigInteger condition;
    protected EffectList effects;
    protected XmlTypeCharge charge;
    protected XmlTypeConsume consume;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger id;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "levels", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger levels;
    @XmlAttribute(name = "enchants")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger enchants;
    @XmlAttribute(name = "isMagic")
    protected Boolean isMagic;
    @XmlAttribute(name = "ignoreShield")
    protected Boolean ignoreShield;
    @XmlAttribute(name = "overHit")
    protected Boolean overHit;

    /**
     * Gets the value of the listMagicLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listMagicLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListMagicLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getListMagicLevel() {
        if (listMagicLevel == null) {
            listMagicLevel = new ArrayList<BigInteger>();
        }
        return this.listMagicLevel;
    }

    /**
     * Gets the value of the listMpConsume property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listMpConsume property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListMpConsume().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getListMpConsume() {
        if (listMpConsume == null) {
            listMpConsume = new ArrayList<BigInteger>();
        }
        return this.listMpConsume;
    }

    /**
     * Gets the value of the listPower property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listPower property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListPower().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getListPower() {
        if (listPower == null) {
            listPower = new ArrayList<BigInteger>();
        }
        return this.listPower;
    }

    /**
     * Gets the value of the listMpInitialConsume property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listMpInitialConsume property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListMpInitialConsume().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getListMpInitialConsume() {
        if (listMpInitialConsume == null) {
            listMpInitialConsume = new ArrayList<BigInteger>();
        }
        return this.listMpInitialConsume;
    }

    /**
     * Gets the value of the listEnchantMagicLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listEnchantMagicLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListEnchantMagicLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getListEnchantMagicLevel() {
        if (listEnchantMagicLevel == null) {
            listEnchantMagicLevel = new ArrayList<BigInteger>();
        }
        return this.listEnchantMagicLevel;
    }

    /**
     * Gets the value of the listEnchantMpConsume property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listEnchantMpConsume property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListEnchantMpConsume().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getListEnchantMpConsume() {
        if (listEnchantMpConsume == null) {
            listEnchantMpConsume = new ArrayList<BigInteger>();
        }
        return this.listEnchantMpConsume;
    }

    /**
     * Gets the value of the listEnchantPower property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listEnchantPower property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListEnchantPower().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getListEnchantPower() {
        if (listEnchantPower == null) {
            listEnchantPower = new ArrayList<BigInteger>();
        }
        return this.listEnchantPower;
    }

    /**
     * Obtém o valor da propriedade skillType.
     * 
     * @return
     *     possible object is
     *     {@link XmlSkillType }
     *     
     */
    public XmlSkillType getSkillType() {
        return skillType;
    }

    /**
     * Define o valor da propriedade skillType.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlSkillType }
     *     
     */
    public void setSkillType(XmlSkillType value) {
        this.skillType = value;
    }

    /**
     * Obtém o valor da propriedade operateType.
     * 
     * @return
     *     possible object is
     *     {@link XmlSkillOperateType }
     *     
     */
    public XmlSkillOperateType getOperateType() {
        return operateType;
    }

    /**
     * Define o valor da propriedade operateType.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlSkillOperateType }
     *     
     */
    public void setOperateType(XmlSkillOperateType value) {
        this.operateType = value;
    }

    /**
     * Obtém o valor da propriedade target.
     * 
     * @return
     *     possible object is
     *     {@link SkillTargetType }
     *     
     */
    public SkillTargetType getTarget() {
        return target;
    }

    /**
     * Define o valor da propriedade target.
     * 
     * @param value
     *     allowed object is
     *     {@link SkillTargetType }
     *     
     */
    public void setTarget(SkillTargetType value) {
        this.target = value;
    }

    /**
     * Obtém o valor da propriedade power.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPower() {
        return power;
    }

    /**
     * Define o valor da propriedade power.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPower(String value) {
        this.power = value;
    }

    /**
     * Obtém o valor da propriedade magicLevel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMagicLevel() {
        return magicLevel;
    }

    /**
     * Define o valor da propriedade magicLevel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMagicLevel(String value) {
        this.magicLevel = value;
    }

    /**
     * Obtém o valor da propriedade mpConsume.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMpConsume() {
        return mpConsume;
    }

    /**
     * Define o valor da propriedade mpConsume.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMpConsume(String value) {
        this.mpConsume = value;
    }

    /**
     * Obtém o valor da propriedade reuseDelay.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getReuseDelay() {
        return reuseDelay;
    }

    /**
     * Define o valor da propriedade reuseDelay.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setReuseDelay(Duration value) {
        this.reuseDelay = value;
    }

    /**
     * Obtém o valor da propriedade hitTime.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getHitTime() {
        return hitTime;
    }

    /**
     * Define o valor da propriedade hitTime.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setHitTime(Duration value) {
        this.hitTime = value;
    }

    /**
     * Obtém o valor da propriedade castRange.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCastRange() {
        return castRange;
    }

    /**
     * Define o valor da propriedade castRange.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCastRange(BigInteger value) {
        this.castRange = value;
    }

    /**
     * Obtém o valor da propriedade effectRange.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEffectRange() {
        return effectRange;
    }

    /**
     * Define o valor da propriedade effectRange.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEffectRange(BigInteger value) {
        this.effectRange = value;
    }

    /**
     * Obtém o valor da propriedade weaponsAllowed.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWeaponsAllowed() {
        return weaponsAllowed;
    }

    /**
     * Define o valor da propriedade weaponsAllowed.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWeaponsAllowed(BigInteger value) {
        this.weaponsAllowed = value;
    }

    /**
     * Obtém o valor da propriedade mpInitialConsume.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMpInitialConsume() {
        return mpInitialConsume;
    }

    /**
     * Define o valor da propriedade mpInitialConsume.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMpInitialConsume(String value) {
        this.mpInitialConsume = value;
    }

    /**
     * Obtém o valor da propriedade baseCritRate.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBaseCritRate() {
        return baseCritRate;
    }

    /**
     * Define o valor da propriedade baseCritRate.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBaseCritRate(BigInteger value) {
        this.baseCritRate = value;
    }

    /**
     * Obtém o valor da propriedade ssBoost.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSSBoost() {
        return ssBoost;
    }

    /**
     * Define o valor da propriedade ssBoost.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSSBoost(BigDecimal value) {
        this.ssBoost = value;
    }

    /**
     * Obtém o valor da propriedade skillRadius.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSkillRadius() {
        return skillRadius;
    }

    /**
     * Define o valor da propriedade skillRadius.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSkillRadius(BigInteger value) {
        this.skillRadius = value;
    }

    /**
     * Obtém o valor da propriedade coolTime.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getCoolTime() {
        return coolTime;
    }

    /**
     * Define o valor da propriedade coolTime.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setCoolTime(Duration value) {
        this.coolTime = value;
    }

    /**
     * Obtém o valor da propriedade enchant1.
     * 
     * @return
     *     possible object is
     *     {@link XmlTypeEnchant }
     *     
     */
    public XmlTypeEnchant getEnchant1() {
        return enchant1;
    }

    /**
     * Define o valor da propriedade enchant1.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlTypeEnchant }
     *     
     */
    public void setEnchant1(XmlTypeEnchant value) {
        this.enchant1 = value;
    }

    /**
     * Obtém o valor da propriedade enchant2.
     * 
     * @return
     *     possible object is
     *     {@link XmlTypeEnchant }
     *     
     */
    public XmlTypeEnchant getEnchant2() {
        return enchant2;
    }

    /**
     * Define o valor da propriedade enchant2.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlTypeEnchant }
     *     
     */
    public void setEnchant2(XmlTypeEnchant value) {
        this.enchant2 = value;
    }

    /**
     * Obtém o valor da propriedade cond.
     * 
     * @return
     *     possible object is
     *     {@link SkillCondition }
     *     
     */
    public SkillCondition getCond() {
        return cond;
    }

    /**
     * Define o valor da propriedade cond.
     * 
     * @param value
     *     allowed object is
     *     {@link SkillCondition }
     *     
     */
    public void setCond(SkillCondition value) {
        this.cond = value;
    }

    /**
     * Obtém o valor da propriedade conditionValue.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getConditionValue() {
        return conditionValue;
    }

    /**
     * Define o valor da propriedade conditionValue.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setConditionValue(BigInteger value) {
        this.conditionValue = value;
    }

    /**
     * Obtém o valor da propriedade condition.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCondition() {
        return condition;
    }

    /**
     * Define o valor da propriedade condition.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCondition(BigInteger value) {
        this.condition = value;
    }

    /**
     * Obtém o valor da propriedade effects.
     * 
     * @return
     *     possible object is
     *     {@link EffectList }
     *     
     */
    public EffectList getEffects() {
        return effects;
    }

    /**
     * Define o valor da propriedade effects.
     * 
     * @param value
     *     allowed object is
     *     {@link EffectList }
     *     
     */
    public void setEffects(EffectList value) {
        this.effects = value;
    }

    /**
     * Obtém o valor da propriedade charge.
     * 
     * @return
     *     possible object is
     *     {@link XmlTypeCharge }
     *     
     */
    public XmlTypeCharge getCharge() {
        return charge;
    }

    /**
     * Define o valor da propriedade charge.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlTypeCharge }
     *     
     */
    public void setCharge(XmlTypeCharge value) {
        this.charge = value;
    }

    /**
     * Obtém o valor da propriedade consume.
     * 
     * @return
     *     possible object is
     *     {@link XmlTypeConsume }
     *     
     */
    public XmlTypeConsume getConsume() {
        return consume;
    }

    /**
     * Define o valor da propriedade consume.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlTypeConsume }
     *     
     */
    public void setConsume(XmlTypeConsume value) {
        this.consume = value;
    }

    /**
     * Obtém o valor da propriedade id.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Define o valor da propriedade id.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setId(BigInteger value) {
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

    /**
     * Obtém o valor da propriedade levels.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLevels() {
        return levels;
    }

    /**
     * Define o valor da propriedade levels.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLevels(BigInteger value) {
        this.levels = value;
    }

    /**
     * Obtém o valor da propriedade enchants.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEnchants() {
        return enchants;
    }

    /**
     * Define o valor da propriedade enchants.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEnchants(BigInteger value) {
        this.enchants = value;
    }

    /**
     * Obtém o valor da propriedade isMagic.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsMagic() {
        if (isMagic == null) {
            return false;
        } else {
            return isMagic;
        }
    }

    /**
     * Define o valor da propriedade isMagic.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMagic(Boolean value) {
        this.isMagic = value;
    }

    /**
     * Obtém o valor da propriedade ignoreShield.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIgnoreShield() {
        if (ignoreShield == null) {
            return false;
        } else {
            return ignoreShield;
        }
    }

    /**
     * Define o valor da propriedade ignoreShield.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreShield(Boolean value) {
        this.ignoreShield = value;
    }

    /**
     * Obtém o valor da propriedade overHit.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOverHit() {
        if (overHit == null) {
            return false;
        } else {
            return overHit;
        }
    }

    /**
     * Define o valor da propriedade overHit.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOverHit(Boolean value) {
        this.overHit = value;
    }

}
