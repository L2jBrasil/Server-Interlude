package com.l2jbr.gameserver.model.entity.xml;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.xml.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

public class ItemStatsReader extends XMLReader<ItemList> {

    private final Map<Integer, ItemStat> itemStats;

    public ItemStatsReader() throws JAXBException {
        itemStats  = new HashMap<>();
    }

    public ItemStat getItemStat(int itemId) {
        return itemStats.get(itemId);
    }

    @Override
    protected void processEntity(ItemList entity) {
        entity.getItem().forEach(itemStat -> itemStats.put(itemStat.getId(), itemStat));
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
