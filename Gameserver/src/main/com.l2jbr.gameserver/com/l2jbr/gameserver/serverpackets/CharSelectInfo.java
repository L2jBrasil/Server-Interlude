/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.gameserver.serverpackets;

import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.model.CharSelectInfoPackage;
import com.l2jbr.gameserver.model.Inventory;
import com.l2jbr.gameserver.model.L2Clan;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Character;
import com.l2jbr.gameserver.model.entity.database.repository.AugmentationsRepository;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterRepository;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterSubclassesRepository;
import com.l2jbr.gameserver.network.L2GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * This class ...
 *
 * @version $Revision: 1.8.2.4.2.6 $ $Date: 2005/04/06 16:13:46 $
 */
public class CharSelectInfo extends L2GameServerPacket {
    // d SdSddddddddddffddddddddddddddddddddddddddddddddddddddddddddddffd
    private static final String _S__1F_CHARSELECTINFO = "[S] 1F CharSelectInfo";

    private static Logger _log = LoggerFactory.getLogger(CharSelectInfo.class.getName());

    private final String _loginName;

    private final int _sessionId;

    private int _activeId;

    private final CharSelectInfoPackage[] _characterPackages;

    /**
     * @param loginName
     * @param sessionId
     */
    public CharSelectInfo(String loginName, int sessionId) {
        _sessionId = sessionId;
        _loginName = loginName;
        _characterPackages = loadCharacterSelectInfo();
        _activeId = -1;
    }

    public CharSelectInfo(String loginName, int sessionId, int activeId) {
        _sessionId = sessionId;
        _loginName = loginName;
        _characterPackages = loadCharacterSelectInfo();
        _activeId = activeId;
    }

    public CharSelectInfoPackage[] getCharInfo() {
        return _characterPackages;
    }

    @Override
    protected final void writeImpl() {
        int size = (_characterPackages.length);

        writeByte(0x13);
        writeInt(size);

        long lastAccess = 0L;

        if (_activeId == -1) {
            for (int i = 0; i < size; i++) {
                if (lastAccess < _characterPackages[i].getLastAccess()) {
                    lastAccess = _characterPackages[i].getLastAccess();
                    _activeId = i;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            CharSelectInfoPackage charInfoPackage = _characterPackages[i];

            writeString(charInfoPackage.getName());
            writeInt(charInfoPackage.getCharId());
            writeString(_loginName);
            writeInt(_sessionId);
            writeInt(charInfoPackage.getClanId());
            writeInt(0x00); // ??

            writeInt(charInfoPackage.getSex());
            writeInt(charInfoPackage.getRace());

            if (charInfoPackage.getClassId() == charInfoPackage.getBaseClassId()) {
                writeInt(charInfoPackage.getClassId());
            } else {
                writeInt(charInfoPackage.getBaseClassId());
            }

            writeInt(0x01); // active ??

            writeInt(0x00); // x
            writeInt(0x00); // y
            writeInt(0x00); // z

            writeDouble(charInfoPackage.getCurrentHp()); // hp cur
            writeDouble(charInfoPackage.getCurrentMp()); // mp cur

            writeInt(charInfoPackage.getSp());
            writeLong(charInfoPackage.getExp());
            writeInt(charInfoPackage.getLevel());

            writeInt(charInfoPackage.getKarma()); // karma
            writeInt(0x00);
            writeInt(0x00);
            writeInt(0x00);
            writeInt(0x00);
            writeInt(0x00);
            writeInt(0x00);
            writeInt(0x00);
            writeInt(0x00);
            writeInt(0x00);

            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_DHAIR));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_REAR));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LEAR));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_NECK));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RFINGER));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LFINGER));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HEAD));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LHAND));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_GLOVES));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_CHEST));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LEGS));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_FEET));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_BACK));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_LRHAND));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_HAIR));
            writeInt(charInfoPackage.getPaperdollObjectId(Inventory.PAPERDOLL_FACE));

            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_DHAIR));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_REAR));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LEAR));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_NECK));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RFINGER));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LFINGER));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HEAD));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_RHAND));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LHAND));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_GLOVES));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_CHEST));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LEGS));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_FEET));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_BACK));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_LRHAND));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_HAIR));
            writeInt(charInfoPackage.getPaperdollItemId(Inventory.PAPERDOLL_FACE));

            writeInt(charInfoPackage.getHairStyle());
            writeInt(charInfoPackage.getHairColor());
            writeInt(charInfoPackage.getFace());

            writeDouble(charInfoPackage.getMaxHp()); // hp max
            writeDouble(charInfoPackage.getMaxMp()); // mp max

            long deleteTime = charInfoPackage.getDeleteTimer();
            int deletedays = 0;
            if (deleteTime > 0) {
                deletedays = (int) ((deleteTime - System.currentTimeMillis()) / 1000);
            }
            writeInt(deletedays); // days left before
            // delete .. if != 0
            // then char is inactive
            writeInt(charInfoPackage.getClassId());
            if (i == _activeId) {
                writeInt(0x01);
            } else {
                writeInt(0x00); // c3 auto-select char
            }

            writeByte(charInfoPackage.getEnchantEffect() > 127 ? 127 : charInfoPackage.getEnchantEffect());

            writeInt(charInfoPackage.getAugmentationId());
        }
    }

    private CharSelectInfoPackage[] loadCharacterSelectInfo() {
        List<CharSelectInfoPackage> characterList = new LinkedList<>();

        CharacterRepository repository = DatabaseAccess.getRepository(CharacterRepository.class);
        repository.findAllByAccountName(_loginName).forEach(character -> {
            CharSelectInfoPackage charInfoPackage = restoreChar(character);
            if (charInfoPackage != null) {
                characterList.add(charInfoPackage);
            }
        });

        return characterList.toArray(new CharSelectInfoPackage[characterList.size()]);

    }

    private void loadCharacterSubclassInfo(CharSelectInfoPackage charInfopackage, int ObjectId, int activeClassId) {
        CharacterSubclassesRepository repository = DatabaseAccess.getRepository(CharacterSubclassesRepository.class);
        repository.findByClassId(ObjectId, activeClassId).ifPresent(characterSubclasse -> {
            charInfopackage.setExp(characterSubclasse.getExp());
            charInfopackage.setSp(characterSubclasse.getSp());
            charInfopackage.setLevel(characterSubclasse.getLevel());
        });
    }

    private CharSelectInfoPackage restoreChar(Character character) {
        int objectId = character.getObjectId();

        // See if the char must be deleted
        long deleteTime = character.getDeleteTime();
        if (deleteTime > 0) {
            if (System.currentTimeMillis() > deleteTime) {
                L2PcInstance cha = L2PcInstance.load(objectId);
                L2Clan clan = cha.getClan();
                if (clan != null) {
                    clan.removeClanMember(cha.getName(), 0);
                }

                L2GameClient.deleteCharByObjId(objectId);
                return null;
            }
        }

        String name = character.getCharName();

        CharSelectInfoPackage charInfopackage = new CharSelectInfoPackage(objectId, name);
        charInfopackage.setLevel(character.getLevel());
        charInfopackage.setMaxHp(character.getMaxHp());
        charInfopackage.setCurrentHp(character.getCurrentHp());
        charInfopackage.setMaxMp(character.getMaxMp());
        charInfopackage.setCurrentMp(character.getCurrentMp());
        charInfopackage.setKarma(character.getKarma());

        charInfopackage.setFace(character.getFace());
        charInfopackage.setHairStyle(character.getHairStyle());
        charInfopackage.setHairColor(character.getHairColor());
        charInfopackage.setSex(character.getSex());

        charInfopackage.setExp(character.getExperience());
        charInfopackage.setSp(character.getSp());
        charInfopackage.setClanId(character.getClanId());

        charInfopackage.setRace(character.getRace());

        final int baseClassId = character.getBaseClass();
        final int activeClassId = character.getClassId();

        // if is in subclass, load subclass exp, sp, lvl info
        if (baseClassId != activeClassId) {
            loadCharacterSubclassInfo(charInfopackage, objectId, activeClassId);
        }

        charInfopackage.setClassId(activeClassId);

        // Get the augmentation id for equipped weapon
        int weaponObjId = charInfopackage.getPaperdollObjectId(Inventory.PAPERDOLL_LRHAND);
        if (weaponObjId < 1) {
            weaponObjId = charInfopackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND);
        }

        if (weaponObjId > 0) {
            AugmentationsRepository repository = DatabaseAccess.getRepository(AugmentationsRepository.class);
            repository.findById(weaponObjId).ifPresent(augmentation -> {
                charInfopackage.setAugmentationId(augmentation.getAttributes());
            });
        }

        /*
         * Check if the base class is set to zero and alse doesn't match with the current active class, otherwise send the base class ID. This prevents chars created before base class was introduced from being displayed incorrectly.
         */
        if ((baseClassId == 0) && (activeClassId > 0)) {
            charInfopackage.setBaseClassId(activeClassId);
        } else {
            charInfopackage.setBaseClassId(baseClassId);
        }

        charInfopackage.setDeleteTimer(deleteTime);
        charInfopackage.setLastAccess(character.getLastAccess());

        return charInfopackage;
    }

    @Override
    public String getType() {
        return _S__1F_CHARSELECTINFO;
    }
}
