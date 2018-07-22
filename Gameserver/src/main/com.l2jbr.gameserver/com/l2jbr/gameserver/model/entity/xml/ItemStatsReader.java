package com.l2jbr.gameserver.model.entity.xml;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.xml.XMLReader;
import com.l2jbr.gameserver.model.entity.database.ItemTemplate;
import com.l2jbr.gameserver.skills.Stats;
import com.l2jbr.gameserver.skills.conditions.*;
import com.l2jbr.gameserver.skills.conditions.ConditionPlayerState.CheckPlayerState;
import com.l2jbr.gameserver.skills.funcs.FuncTemplate;
import com.l2jbr.gameserver.skills.funcs.Lambda;
import com.l2jbr.gameserver.skills.funcs.LambdaConst;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class ItemStatsReader extends XMLReader<ItemList> {

    private final Map<Integer, ItemStat> itemStats;

    public ItemStatsReader() throws JAXBException {
        itemStats  = new HashMap<>();
    }

    private ItemStat getItemStat(int itemId) {
        return itemStats.get(itemId);
    }

    @Override
    protected void processEntity(ItemList entity) {
        entity.getItem().forEach(itemStat -> itemStats.put(itemStat.getId(), itemStat));
    }

    public void attach(ItemTemplate item) {
        ItemStat itemStat = getItemStat(item.getId());
        if(isNull(itemStat)) {
            return;
        }
        for(XmlTypeStat statType : itemStat.getStat()) {
            Condition condition = parseConditions(statType.getConditions());
            Lambda lambda = new LambdaConst(statType.getValue());
            Stats stat = Stats.valueOf(statType.getName().name());
            FuncTemplate funcTemplate = new FuncTemplate(condition, null, statType.getFunction(), stat, statType.getOrder(), lambda);
            item.addFunction(funcTemplate);
        }
    }

    private Condition parseConditions(XmlStatCondition conditions) {
        if(isNull(conditions)) {
            return null;
        }
        if(!isNull(conditions.getOperator())) {
            return constructConditionWithOperator(conditions.getOperator().getValue());
        }
        return constructCondition(conditions.getCondition().getValue());
    }


    private Condition constructConditionWithOperator(XmlStatConditionOperator operator) {
        if(operator instanceof XmlStatConditionAND) {
            return parseConditionAnd((XmlStatConditionAND) operator);
        } else if(operator instanceof XmlStatConditionOR) {
            return parseConditionOr((XmlStatConditionOR) operator);
        } else if(operator instanceof XmlStatConditionNOT) {
            return parseConditionNot((XmlStatConditionNOT) operator);
        }
        return null;
    }

    private Condition parseConditionNot(XmlStatConditionNOT operator) {
        if(!isNull(operator.getOperator())) {
            return new ConditionLogicNot(constructConditionWithOperator(operator.getOperator().getValue()));
        }
        return new ConditionLogicNot(constructCondition(operator.getCondition().getValue()));
    }

    private Condition parseConditionOr(XmlStatConditionOR operator) {
        ConditionLogicOr or = new ConditionLogicOr();
        operator.getOperator().forEach(op -> or.add(constructConditionWithOperator(op.getValue())));
        operator.getCondition().forEach(condition -> or.add(constructCondition(condition.getValue())));
        return or;
    }

    private Condition parseConditionAnd(XmlStatConditionAND operator) {
        ConditionLogicAnd and = new ConditionLogicAnd();
        operator.getOperator().forEach(op -> and.add(constructConditionWithOperator(op.getValue())));
        operator.getCondition().forEach(condition -> and.add(constructCondition(condition.getValue())));
        return and;
    }

    private Condition constructCondition(XmlStatConditionType condition) {
        if(condition instanceof XmlStatUsingCondition) {
            return parseUsingCondition((XmlStatUsingCondition)condition);
        } else if(condition instanceof XmlStatPlayerCondition) {
            return parsePlayerCondition((XmlStatPlayerCondition) condition);
        } else if(condition instanceof XmlStatGameCondition) {
            return parseGameCondition((XmlStatGameCondition) condition);
        }
        return null;
    }

    private Condition parseGameCondition(XmlStatGameCondition condition) {
        switch (condition.getKind().toLowerCase()) {
            case "chance": {
                int chance = Integer.parseInt(condition.getValue());
                return new ConditionGameChance(chance);
            }
            case "skill": {
                boolean value = Boolean.parseBoolean(condition.getValue());
                return new ConditionWithSkill(value);
            }
        }
        return null;
    }

    private Condition parsePlayerCondition(XmlStatPlayerCondition condition) {
        switch (condition.getKind().toLowerCase()) {
            case "behind": {
                boolean value = Boolean.parseBoolean(condition.getValue());
                return new ConditionPlayerState(CheckPlayerState.BEHIND, value);
            }
            case "hp": {
                int hp = Integer.parseInt(condition.getValue());
                return new ConditionPlayerHp(hp);
            }
        }
        return null;
    }

    private Condition parseUsingCondition(XmlStatUsingCondition condition) {
        switch (condition.getKind()) {
            case "slotitem":
                return new ConditionSlotItemId(condition.getSlot(), condition.getItem(), condition.getEnchantment());
        }
        return null;
    }

    @Override
    protected JAXBContext getJAXBContext() throws JAXBException {
        return JAXBContext.newInstance(ItemList.class);
    }

    @Override
    public String getSchemaFilePath() {
        return Config.DATAPACK_ROOT.getAbsolutePath().concat("/data/stats/item.xsd");
    }

    @Override
    public String[] getXmlFileDirectories() {
        return new String[] { "data/stats/armor", "data/stats/weapon" };
    }
}
