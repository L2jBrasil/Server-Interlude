package com.l2jbr.gameserver.templates;

public enum BodyPart {
    NONE(0x0000),
    UNDERWEAR(0x0001),
    RIGHT_EAR(0x0002),
    LEFT_EAR(0x0004),
    EAR(0X0006),
    NECK(0x0008),
    RIGHT_FINGER(0x0010),
    LEFT_FINGER(0x0020),
    FINGER(0x0030),
    HEAD(0x0040),
    RIGHT_HAND(0x0080),
    LEFT_HAND(0x0100),
    GLOVES(0x0200),
    CHEST(0x0400),
    LEGS(0x0800),
    FEET(0x1000),
    BACK(0x2000),
    TWO_HAND(0x4000),
    FULL_ARMOR(0x8000),
    HAIR(0x010000),
    WOLF(0x020000),
    HATCHLING(0x100000),
    STRIDER(0x200000),
    BABYPET(0x400000),
    FACE(0x040000),
    DHAIR(0x080000);

    private final int id;

    BodyPart(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static BodyPart fromId(int  id) {
        for (BodyPart bodyPart : values()) {
            if(bodyPart.getId() == id) {
                return bodyPart;
            }
        }
        return null;
    }
}
