package com.l2jbr.gameserver.model.entity.xml;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.xml.XMLReader;
import org.xml.sax.SAXException;

public class ItemStatsReader extends XMLReader {


    public ItemStatsReader() throws SAXException {

    }

    @Override
    public String getSchemaFilePath() {
        return Config.DATAPACK_ROOT.getAbsolutePath().concat("/data/stats/item.xsd");
    }
}
