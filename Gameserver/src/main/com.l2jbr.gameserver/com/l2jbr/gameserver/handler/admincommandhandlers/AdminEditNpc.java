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
import com.l2jbr.commons.util.Util;
import com.l2jbr.gameserver.TradeController;
import com.l2jbr.gameserver.cache.HtmCache;
import com.l2jbr.gameserver.datatables.ItemTable;
import com.l2jbr.gameserver.datatables.NpcTable;
import com.l2jbr.gameserver.handler.IAdminCommandHandler;
import com.l2jbr.gameserver.model.L2DropCategory;
import com.l2jbr.gameserver.model.L2DropData;
import com.l2jbr.gameserver.model.actor.instance.L2PcInstance;
import com.l2jbr.gameserver.model.entity.database.*;
import com.l2jbr.gameserver.model.entity.database.repository.DropListRepository;
import com.l2jbr.gameserver.model.entity.database.repository.MerchantBuyListRepository;
import com.l2jbr.gameserver.model.entity.database.repository.NpcRepository;
import com.l2jbr.gameserver.serverpackets.NpcHtmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import static java.util.Objects.isNull;


/**
 * @author terry Window - Preferences - Java - Code Style - Code Templates
 */
public class AdminEditNpc implements IAdminCommandHandler {
    private static Logger _log = LoggerFactory.getLogger(AdminEditChar.class.getName());
    private final static int PAGE_LIMIT = 7;

    private static final String[] ADMIN_COMMANDS =
            {
                    "admin_edit_npc",
                    "admin_save_npc",
                    "admin_show_droplist",
                    "admin_edit_drop",
                    "admin_add_drop",
                    "admin_del_drop",
                    "admin_showShop",
                    "admin_showShopList",
                    "admin_addShopItem",
                    "admin_delShopItem",
                    "admin_editShopItem",
                    "admin_close_window"
            };
    private static final int REQUIRED_LEVEL = Config.GM_NPC_EDIT;
    private static final int REQUIRED_LEVEL2 = Config.GM_NPC_VIEW;

    @Override
    public boolean useAdminCommand(String command, L2PcInstance activeChar) {// TODO: Tokenize and protect arguments parsing. Externalize HTML.
        if (!Config.ALT_PRIVILEGES_ADMIN) {
            if (!((checkLevel(activeChar.getAccessLevel()) || checkLevel2(activeChar.getAccessLevel())) && activeChar.isGM())) {
                return false;
            }
        }

        if (command.startsWith("admin_showShop ")) {
            String[] args = command.split(" ");
            if (args.length > 1) {
                showShop(activeChar, Integer.parseInt(command.split(" ")[1]));
            }
        } else if (command.startsWith("admin_showShopList ")) {
            String[] args = command.split(" ");
            if (args.length > 2) {
                showShopList(activeChar, Integer.parseInt(command.split(" ")[1]), Integer.parseInt(command.split(" ")[2]));
            }
        } else if (command.startsWith("admin_edit_npc ")) {
            try {
                String[] commandSplit = command.split(" ");
                int npcId = Integer.valueOf(commandSplit[1]);
                NpcTemplate npc = NpcTable.getInstance().getTemplate(npcId);
                showNpcProperty(activeChar, npc);
            } catch (Exception e) {
                activeChar.sendMessage("Wrong usage: //edit_npc <npcId>");
            }
        } else if (command.startsWith("admin_show_droplist ")) {
            int npcId = 0;
            try {
                npcId = Integer.parseInt(command.substring(20).trim());
            } catch (Exception e) {
            }

            if (npcId > 0) {
                showNpcDropList(activeChar, npcId);
            } else {
                activeChar.sendMessage("Usage: //show_droplist <npc_id>");
            }
        } else if (!Config.ALT_PRIVILEGES_ADMIN && !(checkLevel(activeChar.getAccessLevel()) && activeChar.isGM())) {
            return false;
        } else if (command.startsWith("admin_addShopItem ")) {
            String[] args = command.split(" ");
            if (args.length > 1) {
                addShopItem(activeChar, args);
            }
        } else if (command.startsWith("admin_delShopItem ")) {
            String[] args = command.split(" ");
            if (args.length > 2) {
                delShopItem(activeChar, args);
            }
        } else if (command.startsWith("admin_editShopItem ")) {
            String[] args = command.split(" ");
            if (args.length > 2) {
                editShopItem(activeChar, args);
            }
        } else if (command.startsWith("admin_save_npc ")) {
            try {
                save_npc_property(activeChar, command);
            } catch (StringIndexOutOfBoundsException e) {
            }
        } else if (command.startsWith("admin_edit_drop ")) {
            int npcId = -1, itemId = 0, category = -1000;
            try {
                StringTokenizer st = new StringTokenizer(command.substring(16).trim());
                if (st.countTokens() == 3) {
                    try {
                        npcId = Integer.parseInt(st.nextToken());
                        itemId = Integer.parseInt(st.nextToken());
                        category = Integer.parseInt(st.nextToken());
                        showEditDropData(activeChar, npcId, itemId, category);
                    } catch (Exception e) {
                    }
                } else if (st.countTokens() == 6) {
                    try {
                        npcId = Integer.parseInt(st.nextToken());
                        itemId = Integer.parseInt(st.nextToken());
                        category = Integer.parseInt(st.nextToken());
                        int min = Integer.parseInt(st.nextToken());
                        int max = Integer.parseInt(st.nextToken());
                        int chance = Integer.parseInt(st.nextToken());

                        updateDropData(activeChar, npcId, itemId, min, max, category, chance);
                    } catch (Exception e) {
                        _log.debug("admin_edit_drop parements error: " + command);
                    }
                } else {
                    activeChar.sendMessage("Usage: //edit_drop <npc_id> <item_id> <category> [<min> <max> <chance>]");
                }
            } catch (StringIndexOutOfBoundsException e) {
                activeChar.sendMessage("Usage: //edit_drop <npc_id> <item_id> <category> [<min> <max> <chance>]");
            }
        } else if (command.startsWith("admin_add_drop ")) {
            int npcId = -1;
            try {
                StringTokenizer st = new StringTokenizer(command.substring(15).trim());
                if (st.countTokens() == 1) {
                    try {
                        String[] input = command.substring(15).split(" ");
                        if (input.length < 1) {
                            return true;
                        }
                        npcId = Integer.parseInt(input[0]);
                    } catch (Exception e) {
                    }

                    if (npcId > 0) {
                        NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
                        showAddDropData(activeChar, npcData);
                    }
                } else if (st.countTokens() == 6) {
                    try {
                        npcId = Integer.parseInt(st.nextToken());
                        int itemId = Integer.parseInt(st.nextToken());
                        int category = Integer.parseInt(st.nextToken());
                        int min = Integer.parseInt(st.nextToken());
                        int max = Integer.parseInt(st.nextToken());
                        int chance = Integer.parseInt(st.nextToken());

                        addDropData(activeChar, npcId, itemId, min, max, category, chance);
                    } catch (Exception e) {
                        _log.debug("admin_add_drop parements error: " + command);
                    }
                } else {
                    activeChar.sendMessage("Usage: //add_drop <npc_id> [<item_id> <category> <min> <max> <chance>]");
                }
            } catch (StringIndexOutOfBoundsException e) {
                activeChar.sendMessage("Usage: //add_drop <npc_id> [<item_id> <category> <min> <max> <chance>]");
            }
        } else if (command.startsWith("admin_del_drop ")) {
            int npcId = -1, itemId = -1, category = -1000;
            try {
                String[] input = command.substring(15).split(" ");
                if (input.length >= 3) {
                    npcId = Integer.parseInt(input[0]);
                    itemId = Integer.parseInt(input[1]);
                    category = Integer.parseInt(input[2]);
                }
            } catch (Exception e) {
            }

            if (npcId > 0) {
                deleteDropData(activeChar, npcId, itemId, category);
            } else {
                activeChar.sendMessage("Usage: //del_drop <npc_id> <item_id> <category>");
            }
        }
        return true;
    }

    private void editShopItem(L2PcInstance activeChar, String[] args) {
        int tradeListID = Integer.parseInt(args[1]);
        int itemID = Integer.parseInt(args[2]);
        MerchantShop tradeList = TradeController.getInstance().getBuyList(tradeListID);

        ItemTemplate item = ItemTable.getInstance().getTemplate(itemID);
        if (tradeList.getPriceForItemId(itemID) < 0) {
            return;
        }

        if (args.length > 3) {
            int price = Integer.parseInt(args[3]);
            int order = findOrderTradeList(itemID, tradeList.getPriceForItemId(itemID), tradeListID);

            tradeList.changePrice(itemID, Integer.parseInt(args[3]));
            updateTradeList(itemID, price, tradeListID, order);

            activeChar.sendMessage("Updated price for {} " + item.getName() + " in Trade List {}" + tradeListID);
            showShopList(activeChar, tradeListID, 1);
            return;
        }

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder();
        replyMSG.append("<html><title>Merchant Shop Item Edit</title>");
        replyMSG.append("<body>");
        replyMSG.append("<br>Edit an entry in merchantList.");
        replyMSG.append("<br>Editing Item: " + item.getName());
        replyMSG.append("<table>");
        replyMSG.append("<tr><td width=100>Property</td><td width=100>Edit Field</td><td width=100>Old Value</td></tr>");
        replyMSG.append("<tr><td><br></td><td></td></tr>");
        replyMSG.append("<tr><td>Price</td><td><edit var=\"price\" width=80></td><td>" + tradeList.getPriceForItemId(itemID) + "</td></tr>");
        replyMSG.append("</table>");
        replyMSG.append("<center><br><br><br>");
        replyMSG.append("<button value=\"Save\" action=\"bypass -h admin_editShopItem " + tradeListID + " " + itemID + " $price\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("<br><button value=\"Back\" action=\"bypass -h admin_showShopList " + tradeListID + " 1\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("</center>");
        replyMSG.append("</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    private void delShopItem(L2PcInstance activeChar, String[] args) {
        int tradeListID = Integer.parseInt(args[1]);
        int itemID = Integer.parseInt(args[2]);
        MerchantShop tradeList = TradeController.getInstance().getBuyList(tradeListID);

        if (tradeList.getPriceForItemId(itemID) < 0) {
            return;
        }

        if (args.length > 3) {
            int order = findOrderTradeList(itemID, tradeList.getPriceForItemId(itemID), tradeListID);

            tradeList.removeItem(itemID);
            deleteTradeList(tradeListID, order);

            activeChar.sendMessage("Deleted " + ItemTable.getInstance().getTemplate(itemID).getName() + " from Trade List " + tradeListID);
            showShopList(activeChar, tradeListID, 1);
            return;
        }

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder();
        replyMSG.append("<html><title>Merchant Shop Item Delete</title>");
        replyMSG.append("<body>");
        replyMSG.append("<br>Delete entry in merchantList.");
        replyMSG.append("<br>Item to Delete: " + ItemTable.getInstance().getTemplate(itemID).getName());
        replyMSG.append("<table>");
        replyMSG.append("<tr><td width=100>Property</td><td width=100>Value</td></tr>");
        replyMSG.append("<tr><td><br></td><td></td></tr>");
        replyMSG.append("<tr><td>Price</td><td>" + tradeList.getPriceForItemId(itemID) + "</td></tr>");
        replyMSG.append("</table>");
        replyMSG.append("<center><br><br><br>");
        replyMSG.append("<button value=\"Confirm\" action=\"bypass -h admin_delShopItem " + tradeListID + " " + itemID + " 1\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("<br><button value=\"Back\" action=\"bypass -h admin_showShopList " + tradeListID + " 1\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("</center>");
        replyMSG.append("</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    private void addShopItem(L2PcInstance activeChar, String[] args) {
        int tradeListId = Integer.parseInt(args[1]);

        MerchantShop tradeList = TradeController.getInstance().getBuyList(tradeListId);
        if (tradeList == null) {
            activeChar.sendMessage("TradeList not found!");
            return;
        }

        if (args.length > 3) {
            int order = tradeList.getItems().size() + 1; // last item order + 1
            int itemId = Integer.parseInt(args[2]);
            int price = Integer.parseInt(args[3]);

            MerchantItem item = new MerchantItem(itemId, price, tradeListId, order);
            item.setPrice(price);
            item.setCount(-1);
            tradeList.addItem(item);
            storeTradeList(itemId, price, tradeListId, order);

            ItemTemplate itemTemplate = ItemTable.getInstance().getTemplate(itemId);
            activeChar.sendMessage("Added " + itemTemplate.getName() + " to Trade List " + tradeList.getId());
            showShopList(activeChar, tradeListId, 1);
            return;
        }

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder();
        replyMSG.append("<html><title>Merchant Shop Item Add</title>");
        replyMSG.append("<body>");
        replyMSG.append("<br>Add a new entry in merchantList.");
        replyMSG.append("<table>");
        replyMSG.append("<tr><td width=100>Property</td><td>Edit Field</td></tr>");
        replyMSG.append("<tr><td><br></td><td></td></tr>");
        replyMSG.append("<tr><td>ItemID</td><td><edit var=\"itemID\" width=80></td></tr>");
        replyMSG.append("<tr><td>Price</td><td><edit var=\"price\" width=80></td></tr>");
        replyMSG.append("</table>");
        replyMSG.append("<center><br><br><br>");
        replyMSG.append("<button value=\"Save\" action=\"bypass -h admin_addShopItem " + tradeListId + " $itemID $price\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("<br><button value=\"Back\" action=\"bypass -h admin_showShopList " + tradeListId + " 1\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("</center>");
        replyMSG.append("</body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    private void showShopList(L2PcInstance activeChar, int tradeListID, int page) {
        MerchantShop tradeList = TradeController.getInstance().getBuyList(tradeListID);
        if ((page > ((tradeList.getItems().size() / PAGE_LIMIT) + 1)) || (page < 1)) {
            return;
        }

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        StringBuilder html = itemListHtml(tradeList, page);

        adminReply.setHtml(html.toString());
        activeChar.sendPacket(adminReply);

    }

    private StringBuilder itemListHtml(MerchantShop tradeList, int page) {
        StringBuilder replyMSG = new StringBuilder();

        replyMSG.append("<html><title>Merchant Shop List Page: " + page + "</title>");
        replyMSG.append("<body>");
        replyMSG.append("<br>Edit, add or delete entries in a merchantList.");
        replyMSG.append("<table>");
        replyMSG.append("<tr><td width=150>Item Name</td><td width=60>Price</td><td width=40>Delete</td></tr>");
        int start = ((page - 1) * PAGE_LIMIT);
        int end = Math.min(((page - 1) * PAGE_LIMIT) + (PAGE_LIMIT - 1), tradeList.getItems().size() - 1);
        for (MerchantItem item : tradeList.getItems(start, end + 1)) {
            ItemTemplate template = ItemTable.getInstance().getTemplate(item.getItemId());
            if(isNull(template)) {
                continue;
            }
            replyMSG.append("<tr><td><a action=\"bypass -h admin_editShopItem " + tradeList.getId() + " " + item.getItemId() + "\">" + template.getName() + "</a></td>");
            replyMSG.append("<td>" + item.getPrice() + "</td>");
            replyMSG.append("<td><button value=\"Del\" action=\"bypass -h admin_delShopItem " + tradeList.getId() + " " + item.getItemId() + "\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
            replyMSG.append("</tr>");
        }// */
        replyMSG.append("<tr>");
        int min = 1;
        int max = (tradeList.getItems().size() / PAGE_LIMIT) + 1;
        if (page > 1) {
            replyMSG.append("<td><button value=\"Page" + (page - 1) + "\" action=\"bypass -h admin_showShopList " + tradeList.getId() + " " + (page - 1) + "\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        }
        if (page < max) {
            if (page <= min) {
                replyMSG.append("<td></td>");
            }
            replyMSG.append("<td><button value=\"Page" + (page + 1) + "\" action=\"bypass -h admin_showShopList " + tradeList.getId() + " " + (page + 1) + "\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
        }
        replyMSG.append("</tr><tr><td>.</td></tr>");
        replyMSG.append("</table>");
        replyMSG.append("<center>");
        replyMSG.append("<button value=\"Add\" action=\"bypass -h admin_addShopItem " + tradeList.getId() + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("<button value=\"Close\" action=\"bypass -h admin_close_window\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("</center></body></html>");

        return replyMSG;
    }

    private void showShop(L2PcInstance activeChar, int merchantID) {
        List<MerchantShop> tradeLists = getTradeLists(merchantID);
        if (tradeLists == null) {
            activeChar.sendMessage("Unknown npc template ID" + merchantID);
            return;
        }

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder("<html><title>Merchant Shop Lists</title>");
        replyMSG.append("<body>");
        replyMSG.append("<br>Select a list to view");
        replyMSG.append("<table>");
        replyMSG.append("<tr><td>Mecrchant List ID</td></tr>");

        for (MerchantShop tradeList : tradeLists) {
            if (tradeList != null) {
                replyMSG.append("<tr><td><a action=\"bypass -h admin_showShopList " + tradeList.getId() + " 1\">Trade List " + tradeList.getId() + "</a></td></tr>");
            }
        }

        replyMSG.append("</table>");
        replyMSG.append("<center>");
        replyMSG.append("<button value=\"Close\" action=\"bypass -h admin_close_window\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("</center></body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    private void storeTradeList(int itemID, int price, int tradeListID, int order) {
        MerchantBuyListRepository repository = DatabaseAccess.getRepository(MerchantBuyListRepository.class);
        MerchantItem buyList = new MerchantItem(itemID, price, tradeListID, order);
        repository.save(buyList);
    }

    private void updateTradeList(int itemID, int price, int tradeListID, int order) {
        MerchantBuyListRepository repository = DatabaseAccess.getRepository(MerchantBuyListRepository.class);
        repository.updatePriceByItem(tradeListID, itemID, order, price);
    }

    private void deleteTradeList(int tradeListID, int order) {
        MerchantBuyListRepository repository = DatabaseAccess.getRepository(MerchantBuyListRepository.class);
        repository.deleteByOrder(tradeListID, order);
    }

    private int findOrderTradeList(int itemID, int price, int tradeListID) {
        MerchantBuyListRepository repository = DatabaseAccess.getRepository(MerchantBuyListRepository.class);
        return repository.findOrderByItemAndPrice(tradeListID, itemID, price).orElse(0);
    }

    private List<MerchantShop> getTradeLists(int merchantID) {
        String target = "npc_%objectId%_Buy";

        String content = HtmCache.getInstance().getHtm("data/html/merchant/" + merchantID + ".htm");

        if (content == null) {
            content = HtmCache.getInstance().getHtm("data/html/merchant/30001.htm");
            if (content == null) {
                return null;
            }
        }

        List<MerchantShop> tradeLists = new LinkedList<>();

        String[] lines = content.split("\n");
        int pos = 0;

        for (String line : lines) {
            pos = line.indexOf(target);
            if (pos >= 0) {
                int tradeListID = Integer.decode((line.substring(pos + target.length() + 1)).split("\"")[0]);
                tradeLists.add(TradeController.getInstance().getBuyList(tradeListID));
            }
        }
        return tradeLists;
    }

    private boolean checkLevel(int level) {
        return (level >= REQUIRED_LEVEL);
    }

    private boolean checkLevel2(int level) {
        return (level >= REQUIRED_LEVEL2);
    }

    @Override
    public String[] getAdminCommandList() {
        return ADMIN_COMMANDS;
    }

    private void showNpcProperty(L2PcInstance activeChar, NpcTemplate npc) {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        String content = HtmCache.getInstance().getHtm("data/html/admin/editnpc.htm");

        if (content != null) {
            adminReply.setHtml(content);
            adminReply.replace("%npcId%", String.valueOf(npc.getId()));
            adminReply.replace("%templateId%", String.valueOf(npc.getTemplateId()));
            adminReply.replace("%name%", npc.getName());
            adminReply.replace("%serverSideName%", npc.isServerSideName() ? "1" : "0");
            adminReply.replace("%title%", npc.getTitle());
            adminReply.replace("%serverSideTitle%", npc.isServerSideTitle() ? "1" : "0");
            adminReply.replace("%collisionRadius%", String.valueOf(npc.getCollisionRadius()));
            adminReply.replace("%collisionHeight%", String.valueOf(npc.getCollisionHeight()));
            adminReply.replace("%level%", String.valueOf(npc.getLevel()));
            adminReply.replace("%sex%", String.valueOf(npc.getSex()));
            adminReply.replace("%type%", String.valueOf(npc.getType()));
            adminReply.replace("%attackRange%", String.valueOf(npc.getAtkRange()));
            adminReply.replace("%hp%", String.valueOf(npc.getHp()));
            adminReply.replace("%mp%", String.valueOf(npc.getMp()));
            adminReply.replace("%hpRegen%", String.valueOf(npc.getHpRegen()));
            adminReply.replace("%mpRegen%", String.valueOf(npc.getMpRegen()));
            adminReply.replace("%str%", String.valueOf(npc.getStrength()));
            adminReply.replace("%con%", String.valueOf(npc.getConstitution()));
            adminReply.replace("%dex%", String.valueOf(npc.getDexterity()));
            adminReply.replace("%int%", String.valueOf(npc.getIntellienge()));
            adminReply.replace("%wit%", String.valueOf(npc.getWitness()));
            adminReply.replace("%men%", String.valueOf(npc.getMentality()));
            adminReply.replace("%exp%", String.valueOf(npc.getExp()));
            adminReply.replace("%sp%", String.valueOf(npc.getSp()));
            adminReply.replace("%pAtk%", String.valueOf(npc.getpAtk()));
            adminReply.replace("%pDef%", String.valueOf(npc.getpDef()));
            adminReply.replace("%mAtk%", String.valueOf(npc.getMAtk()));
            adminReply.replace("%mDef%", String.valueOf(npc.getMDef()));
            adminReply.replace("%pAtkSpd%", String.valueOf(npc.getPAtkSpd()));
            adminReply.replace("%aggro%", String.valueOf(npc.getAggro()));
            adminReply.replace("%mAtkSpd%", String.valueOf(npc.getMAtkSpd()));
            adminReply.replace("%rHand%", String.valueOf(npc.getRhand()));
            adminReply.replace("%lHand%", String.valueOf(npc.getLhand()));
            adminReply.replace("%armor%", String.valueOf(npc.getArmor()));
            adminReply.replace("%walkSpd%", String.valueOf(npc.getWalkSpd()));
            adminReply.replace("%runSpd%", String.valueOf(npc.getRunSpd()));
            adminReply.replace("%factionId%", npc.getFactionId() == null ? "" : npc.getFactionId());
            adminReply.replace("%factionRange%", String.valueOf(npc.getFactionRange()));
            adminReply.replace("%isUndead%", npc.isUndead() ? "1" : "0");
            adminReply.replace("%absorbLevel%", String.valueOf(npc.getAbsorbLevel()));
        } else {
            adminReply.setHtml("<html><head><body>File not found: data/html/admin/editnpc.htm</body></html>");
        }
        activeChar.sendPacket(adminReply);
    }

    private void save_npc_property(L2PcInstance activeChar, String command) {
        String[] commandSplit = command.split(" ");

        if (commandSplit.length < 4) {
            return;
        }

        int npcId = Integer.parseInt(commandSplit[1]);

        String statToSet = commandSplit[2];
        String value = commandSplit[3];

        if (commandSplit.length > 4) {
            for (int i = 0; i < (commandSplit.length - 3); i++) {
                value += " " + commandSplit[i + 4];
            }
        }

        NpcTemplate npc = NpcTable.getInstance().getTemplate(npcId);
        final String statValue = value;

        Util.getField(statToSet, NpcTemplate.class).ifPresent(f -> {
            if(isNull(npc)) {
                return;
            }
            try {
                f.trySetAccessible();
                if ("sex".equalsIgnoreCase(statToSet)) {
                    switch (statValue) {
                        case "0":
                            f.set(npc, "male");
                            break;
                        case "1":
                            f.set(npc, "female");
                            break;
                        default:
                            f.set(npc, "etc");
                    }
                } else if("type".equalsIgnoreCase(statToSet)) {
                    Class.forName("com.l2jbr.gameserver.model.actor.instance." + statValue + "Instance");
                    f.set(npc, statValue);
                } else if("absorb_level".equalsIgnoreCase(statToSet)) {
                    int intValue = Integer.parseInt(statValue);
                    if(intValue < 0 || intValue > 12) {
                        intValue = 0;
                    }
                    f.set(npc, intValue);
                } else {
                    f.set(npc, f.getType().cast(statValue));
                }
                NpcRepository repository = DatabaseAccess.getRepository(NpcRepository.class);
                repository.save(npc);
            }catch (Exception e) {
                _log.warn("Error saving new npc value", e);
            }
        });

        NpcTable.getInstance().reloadNpc(npcId);
        showNpcProperty(activeChar, npc);
    }

    private void showNpcDropList(L2PcInstance activeChar, int npcId) {
        NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
        if (npcData == null) {
            activeChar.sendMessage("unknown npc template id" + npcId);
            return;
        }

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder("<html><title>NPC: " + npcData.getName() + "(" + npcData.getId() + ") 's drop manage</title>");
        replyMSG.append("<body>");
        replyMSG.append("<br>Notes: click[drop_id]to show the detail of drop data,click[del] to delete the drop data!");
        replyMSG.append("<table>");
        replyMSG.append("<tr><td>npc_id itemId category</td><td>item[id]</td><td>type</td><td>del</td></tr>");

        for (L2DropCategory cat : npcData.getDropCategories().values()) {
            for (L2DropData drop : cat.getAllDrops()) {
                replyMSG.append("<tr><td><a action=\"bypass -h admin_edit_drop " + npcData.getId() + " " + drop.getItemId() + " " + cat.getCategoryType() + "\">" + npcData.getId() + " " + drop.getItemId() + " " + cat.getCategoryType() + "</a></td>" + "<td>" + ItemTable.getInstance().getTemplate(drop.getItemId()).getName() + "[" + drop.getItemId() + "]" + "</td><td>" + (drop.isQuestDrop() ? "Q" : (cat.isSweep() ? "S" : "D")) + "</td><td>" + "<a action=\"bypass -h admin_del_drop " + npcData.getId() + " " + drop.getItemId() + " " + cat.getCategoryType() + "\">del</a></td></tr>");
            }
        }

        replyMSG.append("</table>");
        replyMSG.append("<center>");
        replyMSG.append("<button value=\"Add DropData\" action=\"bypass -h admin_add_drop " + npcId + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("<button value=\"Close\" action=\"bypass -h admin_close_window\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("</center></body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);

    }

    private void showEditDropData(L2PcInstance activeChar, int npcId, int itemId, int category) {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder("<html><title>the detail of dropdata: (" + npcId + " " + itemId + " " + category + ")</title>");
        replyMSG.append("<body>");

        DropListRepository repository = DatabaseAccess.getRepository(DropListRepository.class);
        repository.findByNpcItemAndCategory(npcId, itemId, category).ifPresent(dropList -> {
            replyMSG.append("<table>");
            replyMSG.append("<tr><td>Appertain of NPC</td><td>" + NpcTable.getInstance().getTemplate(npcId).getName() + "</td></tr>");
            replyMSG.append("<tr><td>ItemName</td><td>" + ItemTable.getInstance().getTemplate(itemId).getName() + "(" + itemId + ")</td></tr>");
            replyMSG.append("<tr><td>Category</td><td>" + ((category == -1) ? "sweep" : Integer.toString(category)) + "</td></tr>");
            replyMSG.append("<tr><td>MIN(" + dropList.getMin() + ")</td><td><edit var=\"min\" width=80></td></tr>");
            replyMSG.append("<tr><td>MAX(" + dropList.getMax() + ")</td><td><edit var=\"max\" width=80></td></tr>");
            replyMSG.append("<tr><td>CHANCE(" +  dropList.getChance() + ")</td><td><edit var=\"chance\" width=80></td></tr>");
            replyMSG.append("</table>");

            replyMSG.append("<center>");
            replyMSG.append("<button value=\"Save Modify\" action=\"bypass -h admin_edit_drop " + npcId + " " + itemId + " " + category + " $min $max $chance\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
            replyMSG.append("<br><button value=\"DropList\" action=\"bypass -h admin_show_droplist " + npcId + "\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
            replyMSG.append("</center>");
        });

        replyMSG.append("</body></html>");
        adminReply.setHtml(replyMSG.toString());

        activeChar.sendPacket(adminReply);
    }

    private void showAddDropData(L2PcInstance activeChar, NpcTemplate npcData) {
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

        StringBuilder replyMSG = new StringBuilder("<html><title>Add dropdata to " + npcData.getName() + "(" + npcData.getId() + ")</title>");
        replyMSG.append("<body>");
        replyMSG.append("<table>");
        replyMSG.append("<tr><td>Item-Id</td><td><edit var=\"itemId\" width=80></td></tr>");
        replyMSG.append("<tr><td>MIN</td><td><edit var=\"min\" width=80></td></tr>");
        replyMSG.append("<tr><td>MAX</td><td><edit var=\"max\" width=80></td></tr>");
        replyMSG.append("<tr><td>CATEGORY(sweep=-1)</td><td><edit var=\"category\" width=80></td></tr>");
        replyMSG.append("<tr><td>CHANCE(0-1000000)</td><td><edit var=\"chance\" width=80></td></tr>");
        replyMSG.append("</table>");

        replyMSG.append("<center>");
        replyMSG.append("<button value=\"SAVE\" action=\"bypass -h admin_add_drop " + npcData.getId() + " $itemId $category $min $max $chance\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("<br><button value=\"DropList\" action=\"bypass -h admin_show_droplist " + npcData.getId() + "\"  width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("</center>");
        replyMSG.append("</body></html>");
        adminReply.setHtml(replyMSG.toString());

        activeChar.sendPacket(adminReply);
    }

    private void updateDropData(L2PcInstance activeChar, int npcId, int itemId, int min, int max, int category, int chance) {
        if(npcId > 0) {
            DropListRepository repository = DatabaseAccess.getRepository(DropListRepository.class);
            repository.updateDrop(npcId, itemId, category, min, max, chance);

            reLoadNpcDropList(npcId);

            NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
            StringBuilder replyMSG = new StringBuilder("<html><title>Drop data modify complete!</title>");
            replyMSG.append("<body>");
            replyMSG.append("<center><button value=\"DropList\" action=\"bypass -h admin_show_droplist " + npcId + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
            replyMSG.append("</body></html>");

            adminReply.setHtml(replyMSG.toString());
            activeChar.sendPacket(adminReply);
        }
    }

    private void addDropData(L2PcInstance activeChar, int npcId, int itemId, int min, int max, int category, int chance) {
        DropListRepository repository = DatabaseAccess.getRepository(DropListRepository.class);
        DropList drop = new DropList(npcId, itemId, min, max, category, chance);
        repository.save(drop);

        reLoadNpcDropList(npcId);

        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        StringBuilder replyMSG = new StringBuilder("<html><title>Add drop data complete!</title>");
        replyMSG.append("<body>");
        replyMSG.append("<center><button value=\"Continue add\" action=\"bypass -h admin_add_drop " + npcId + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("<br><br><button value=\"DropList\" action=\"bypass -h admin_show_droplist " + npcId + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
        replyMSG.append("</center></body></html>");

        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);
    }

    private void deleteDropData(L2PcInstance activeChar, int npcId, int itemId, int category) {
        if (npcId > 0) {
            DropListRepository repository = DatabaseAccess.getRepository(DropListRepository.class);
            repository.deleteByNpcItemAndCategory(npcId, itemId, category);

            reLoadNpcDropList(npcId);

            NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
            StringBuilder replyMSG = new StringBuilder("<html><title>Delete drop data(" + npcId + ", " + itemId + ", " + category + ")complete</title>");
            replyMSG.append("<body>");
            replyMSG.append("<center><button value=\"DropList\" action=\"bypass -h admin_show_droplist " + npcId + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
            replyMSG.append("</body></html>");

            adminReply.setHtml(replyMSG.toString());
            activeChar.sendPacket(adminReply);
        }
    }

    private void reLoadNpcDropList(int npcId) {
        NpcTemplate npcData = NpcTable.getInstance().getTemplate(npcId);
        if (isNull(npcData)) {
            return;
        }

        npcData.clearAllDropData();

        DropListRepository repository = DatabaseAccess.getRepository(DropListRepository.class);
        repository.findAllByNpc(npcId).forEach(dropList -> {
            L2DropData dropData = new L2DropData(dropList);
            npcData.addDropData(dropData, dropList.getCategory());
        });
    }
}