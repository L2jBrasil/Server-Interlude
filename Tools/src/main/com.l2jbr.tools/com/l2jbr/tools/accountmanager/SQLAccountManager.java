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
package com.l2jbr.tools.accountmanager;

import com.l2jbr.commons.Base64;
import com.l2jbr.commons.Config;
import com.l2jbr.commons.Server;
import com.l2jbr.commons.database.AccountRepository;
import com.l2jbr.commons.database.DatabaseAccess;
import com.l2jbr.commons.database.model.Account;
import com.l2jbr.gameserver.model.entity.database.repository.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;


/**
 * This class SQL Account Manager
 *
 * @author netimperia
 * @version $Revision: 2.3.2.1.2.3 $ $Date: 2005/08/08 22:47:12 $
 */
public class SQLAccountManager {
    private static String _uname = "";
    private static String _pass = "";
    private static String _level = "";
    private static String _mode = "";

    public static void main(String[] args) throws SQLException, IOException, NoSuchAlgorithmException {
        Server.serverMode = Server.MODE_LOGINSERVER;
        Config.load();

        System.out.println("Please choose an option:");
        System.out.println("");
        System.out.println("1 - Create new account or update existing one (change pass and access level).");
        System.out.println("2 - Change access level.");
        System.out.println("3 - Delete existing account.");
        System.out.println("4 - List accounts & access levels.");
        System.out.println("5 - Exit.");
        LineNumberReader _in = new LineNumberReader(new InputStreamReader(System.in));

        do {
            System.out.print("Your choice: ");
            _mode = _in.readLine();
        } while(!Pattern.matches("^[1-5]$", _mode));

        if (Pattern.matches("^[1-3]$", _mode)) {
            do {
                System.out.print("Username: ");
                String line = _in.readLine();
                if ((line != null) && !line.isEmpty()) {
                    _uname = line.toLowerCase();
                }
            } while (_uname.isEmpty());
        }

        if (_mode.equals("1")) {
            do {
                System.out.print("Password: ");
                _pass = _in.readLine();
            } while (_pass.isEmpty());
        }

        if (Pattern.matches("^[1-2]$", _mode)) {
            do {
                System.out.print("Access level: ");
                _level = _in.readLine();
            } while (_level.isEmpty());
        }

        if (_mode.equals("1")) {
            addOrUpdateAccount(_uname, _pass, _level);
        } else if (_mode.equals("2")) {
            changeAccountLevel(_uname, _level);
        } else if (_mode.equals("3")) {
            System.out.print("Do you really want to delete this account ? Y/N : ");
            String yesno = _in.readLine();
            if ((yesno != null) && yesno.equals("Y")) {
                deleteAccount(_uname);
            }
        } else if (_mode.equals("4")) {
            printAccInfo();
        }
        return;
    }

    private static void printAccInfo() {
        int count = 0;
        AccountRepository repository = DatabaseAccess.getRepository(AccountRepository.class);

        for (Account account : repository.findAll()) {
            System.out.println(String.format("%s -> %d", account.getId(), account.getAccessLevel()));
            count++;
        }
        System.out.println("Number of accounts: " + count + ".");
    }

    private static void addOrUpdateAccount(String login, String password, String level) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] newpass  = password.getBytes(StandardCharsets.UTF_8);
        newpass = md.digest(newpass);

        AccountRepository repository = DatabaseAccess.getRepository(AccountRepository.class);
        repository.createOrUpdateAccount(login, Base64.encodeBytes(newpass), Short.valueOf(level));
    }

    private static void changeAccountLevel(String login, String level) {
        AccountRepository repository = DatabaseAccess.getRepository(AccountRepository.class);

        repository.findById(login).ifPresentOrElse( account -> {
                account.setAccessLevel(Short.valueOf(level));
                repository.save(account);
                System.out.println("Account " + login + " has been updated.");
            },
                () -> System.out.println("Account " + login + " does not exist.")
        );
    }

    private static void deleteAccount(String login) throws SQLException {
        AccountRepository repository = DatabaseAccess.getRepository(AccountRepository.class);
        Optional<Account> optionalAccount = repository.findById(login);

        if(optionalAccount.isPresent()) {

            CharacterRepository characterRepository = DatabaseAccess.getRepository(CharacterRepository.class);
            ClanRepository clanRepository = DatabaseAccess.getRepository(ClanRepository.class);
            ClanPrivsRepository clanPrivsRepository = DatabaseAccess.getRepository(ClanPrivsRepository.class);
            ClanSubpledgesRepository subpledgesRepository = DatabaseAccess.getRepository(ClanSubpledgesRepository.class);
            ClanWarsRepository warsRepository = DatabaseAccess.getRepository(ClanWarsRepository.class);

            characterRepository.findAllByAccountName(login).forEach(character -> {
                clanRepository.findByLeaderId(character.getObjectId()).ifPresent(clanData -> {
                    int clanId = clanData.getId();
                    // Clan Leader
                    System.out.println("Deleting clan " + clanData.getName() +  ".");

                    // Remove All From clan
                    warsRepository.deleteByClan(clanId);
                    characterRepository.removeClanId(clanId);
                    clanPrivsRepository.deleteById(clanId);
                    subpledgesRepository.deleteById(clanId);
                    clanRepository.delete(clanData);
                });

                System.out.println("Deleting character " + character.getCharName());

                CharacterSkillsRepository skillsRepository = DatabaseAccess.getRepository(CharacterSkillsRepository.class);
                skillsRepository.deleteById(character.getObjectId());

                CharacterShortcutsRepository shortcutsRepository = DatabaseAccess.getRepository(CharacterShortcutsRepository.class);
                shortcutsRepository.deleteById(character.getObjectId());

                ItemRepository itemRepository = DatabaseAccess.getRepository(ItemRepository.class);
                itemRepository.deleteByOwner(character.getObjectId());

                CharacterRecipebookRepository recipebookRepository = DatabaseAccess.getRepository(CharacterRecipebookRepository.class);
                recipebookRepository.deleteAllByCharacter(character.getObjectId());

                CharacterQuestsRepository characterQuestsRepository = DatabaseAccess.getRepository(CharacterQuestsRepository.class);
                characterQuestsRepository.deleteById(character.getObjectId());

                CharacterMacrosesRepository characterMacrosesRepository = DatabaseAccess.getRepository(CharacterMacrosesRepository.class);
                characterMacrosesRepository.deleteById(character.getObjectId());

                CharacterFriendRepository characterFriendRepository = DatabaseAccess.getRepository(CharacterFriendRepository.class);
                characterFriendRepository.deleteById(character.getObjectId());

                MerchantLeaseRepository leaseRepository = DatabaseAccess.getRepository(MerchantLeaseRepository.class);
                leaseRepository.deleteByPlayer(character.getObjectId());

                // characters
                characterRepository.delete(character);

            });
            // Get Accounts ID

            // Delete Account
            repository.delete(optionalAccount.get());

            System.out.println("Account " + login + " has been deleted.");


        } else {
            System.out.println("Account " + login + " does not exist.");
        }

    }

}
