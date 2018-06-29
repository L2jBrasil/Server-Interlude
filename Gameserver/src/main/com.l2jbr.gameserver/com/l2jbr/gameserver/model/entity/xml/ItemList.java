package com.l2jbr.gameserver.model.entity.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ItemList {

    @XmlElement(name = "item")
    private List<ItemStat> items;



    public class ItemStat {
        private int id;
        private String name;
    }



}
