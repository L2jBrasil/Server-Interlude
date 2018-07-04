package com.l2jbr.gameserver.model.entity.xml;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.util.Util;
import com.l2jbr.commons.xml.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStatsReader extends XMLReader<ItemList> {

    private final Map<Integer, List<XmlTypeStat>> itemStats;
    private ItemStatsReader instance = null;

    private ItemStatsReader() throws JAXBException {
        itemStats  = new HashMap<>();
    }

    public ItemStatsReader getInstance() throws JAXBException {
        if(Util.isNull(instance)) {
            instance = new ItemStatsReader();
        }
        return  instance;
    }

    public List<XmlTypeStat> getItemStat(int itemId) {
        return itemStats.get(itemId);
    }

    @Override
    protected void processEntity(ItemList entity) {
        entity.getItem().forEach(itemStat ->
            itemStats.put(itemStat.getId(), itemStat.getStat())
        );
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
