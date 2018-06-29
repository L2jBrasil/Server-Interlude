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
package com.l2jbr.gameserver.handler.admincommandhandlers;

import com.l2jbr.commons.Config;
import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.gameserver.handler.IAdminCommandHandler;
import com.l2jbr.gameserver.model.GMAudit;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.Character;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterRepository;
import com.l2jbr.gameserver.model.entity.database.repository.CharacterShortcutsRepository;
import com.l2jbr.gameserver.model.entity.database.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


/**
 * This class handles following admin commands: - delete = deletes target
 * @version $Revision: 1.1.2.6.2.3 $ $Date: 2005/04/11 10:05:59 $
 */
public class AdminRepairChar implements IAdminCommandHandler
{
	private static Logger _log = LoggerFactory.getLogger(AdminRepairChar.class.getName());
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_restore",
		"admin_repair"
	};
	
	private static final int REQUIRED_LEVEL = Config.GM_CHAR_EDIT;
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (!Config.ALT_PRIVILEGES_ADMIN)
		{
			if (!(checkLevel(activeChar.getAccessLevel()) && activeChar.isGM()))
			{
				return false;
			}
		}
		
		String target = (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target");
		GMAudit.auditGMAction(activeChar.getName(), command, target, "");
		
		handleRepair(command);
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private boolean checkLevel(int level)
	{
		return (level >= REQUIRED_LEVEL);
	}
	
	private void handleRepair(String command) {
		String[] parts = command.split(" ");
		if (parts.length != 2) {
			return;
		}

        CharacterRepository characterRepository = DatabaseAccess.getRepository(CharacterRepository.class);
        Optional<Character> optionalCharacter = characterRepository.findByCharName(parts[1]);

        optionalCharacter.ifPresent(character -> {
            character.updateLocation(-84318, 244579, -3730);

            CharacterShortcutsRepository shortcutsRepository = DatabaseAccess.getRepository(CharacterShortcutsRepository.class);
            shortcutsRepository.deleteById(character.getObjectId());

            ItemRepository itemRepository = DatabaseAccess.getRepository(ItemRepository.class);
            itemRepository.updateLocationByOwner(character.getId(), "INVENTORY");

            characterRepository.save(character);
        });

	}
}
