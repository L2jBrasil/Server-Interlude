package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.IdFactoryStub;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

public interface IdFactoryRepository extends Repository<IdFactoryStub, Integer> {

    @Modifying
    @Query("DELETE FROM itemsonground WHERE object_id IN (SELECT object_id FROM items)")
    int deleteItemsOnGroundDuplicated();

    @Query("SELECT obj_id AS object_id FROM characters " +
            "UNION " +
              "SELECT object_id  FROM items " +
            "UNION " +
              "SELECT id AS object_id FROM clan_data " +
            "UNION " +
              "SELECT object_id FROM itemsonground")
    Iterable<Integer> findAllIds();

    @Modifying
    @Query("DELETE FROM character_friends WHERE character_friends.char_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterFriendInconsistency();

    @Modifying
    @Query("DELETE FROM character_hennas WHERE character_hennas.char_obj_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterHennasInconsistency();

    @Modifying
    @Query("DELETE FROM character_macroses WHERE character_macroses.char_obj_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterMacrosesInconsistency();

    @Modifying
    @Query("DELETE FROM character_quests WHERE character_quests.char_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterQuestsInconsistency();

    @Modifying
    @Query("DELETE FROM character_recipebook WHERE character_recipebook.char_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterRecipebookInconsistency();

    @Modifying
    @Query("DELETE FROM character_shortcuts WHERE character_shortcuts.char_obj_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterShortcutsInconsistency();

    @Modifying
    @Query("DELETE FROM character_skills WHERE character_skills.char_obj_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterSkillsInconsistency();

    @Modifying
    @Query("DELETE FROM character_skills_save WHERE character_skills_save.char_obj_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterSkillsSaveInconsistency();

    @Modifying
    @Query("DELETE FROM character_subclasses WHERE character_subclasses.char_obj_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteCharacterSubclassesInconsistency();

    @Modifying
    @Query("DELETE FROM cursed_weapons WHERE cursed_weapons.playerId NOT IN (SELECT obj_Id FROM characters)")
    int deleteCursedWeaponsInconsistency();

    @Modifying
    @Query("DELETE FROM heroes WHERE heroes.char_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteHeroesInconsistency();

    @Modifying
    @Query("DELETE FROM olympiad_nobles WHERE olympiad_nobles.char_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteOlympiadNoblesInconsistency();

    @Modifying
    @Query("DELETE FROM pets WHERE pets.item_obj_id NOT IN (SELECT object_id FROM items)")
    int deletePetsInconsistency();

    @Modifying
    @Query("DELETE FROM seven_signs WHERE seven_signs.char_obj_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteSevenSignsInconsistency();

    @Modifying
    @Query("DELETE FROM auction WHERE auction.id IN (SELECT id FROM clanhall WHERE ownerId <> 0)")
    int deleteAuctionInconsistency();

    @Modifying
    @Query("DELETE FROM auction_bid WHERE auctionId IN (SELECT id FROM clanhall WHERE ownerId <> 0)")
    int deleteAuctionBidInconsistency();

    @Modifying
    @Query("DELETE FROM clan_data WHERE clan_data.leader_id NOT IN (SELECT obj_Id FROM characters)")
    int deleteClanDataInconsistency();

    @Modifying
    @Query("DELETE FROM auction_bid WHERE auction_bid.bidderId NOT IN (SELECT id FROM clan_data)")
    int deleteAuctionBidderInconsistency();

    @Modifying
    @Query("DELETE FROM clanhall_functions WHERE clanhall_functions.hall_id NOT IN (SELECT id FROM clanhall WHERE ownerId <> 0)")
    int deleteClanHallFunctionsInconsistency();

    @Modifying
    @Query("DELETE FROM clan_privs WHERE clan_privs.clan_id NOT IN (SELECT id FROM clan_data)")
    int deleteClanPrivsInconsistency();

    @Modifying
    @Query("DELETE FROM clan_skills WHERE clan_skills.clan_id NOT IN (SELECT id FROM clan_data)")
    int deleteClanSkillsInconsistency();

    @Modifying
    @Query("DELETE FROM clan_subpledges WHERE clan_subpledges.clan_id NOT IN (SELECT id FROM clan_data)")
    int deleteClanSubpledgeInconsistency();

    @Modifying
    @Query("DELETE FROM clan_wars WHERE clan_wars.clan1 NOT IN (SELECT id FROM clan_data)")
    int deleteClanWarsInconsistency();

    @Modifying
    @Query("DELETE FROM clan_wars WHERE clan_wars.clan2 NOT IN (SELECT id FROM clan_data)")
    int deleteClanWarsUnderAttackInconsistency();

    @Modifying
    @Query("DELETE FROM siege_clans WHERE siege_clans.clan_id NOT IN (SELECT id FROM clan_data)")
    int deleteSiegeClansrInconsistency();

    @Modifying
    @Query("DELETE FROM items WHERE items.owner_id NOT IN (SELECT obj_Id FROM characters) AND items.owner_id NOT IN (SELECT id FROM clan_data);")
    int deleteItemsInconsistency();

    @Modifying
    @Query("DELETE FROM forums WHERE forums.forum_parent=2 AND forums.forum_owner_id  NOT IN (SELECT id FROM clan_data)")
    int deleteForumInconsistency();

    @Modifying
    @Query("DELETE FROM topic WHERE topic.topic_forum_id NOT IN (SELECT forum_id FROM forums)")
    int deleteTopicInconsistency();

    @Modifying
    @Query("DELETE FROM posts WHERE posts.post_forum_id NOT IN (SELECT forum_id FROM forums)")
    int deletePostInconsistency();

    @Modifying
    @Query("UPDATE clan_data SET auction_bid_at = 0 WHERE auction_bid_at NOT IN (SELECT auctionId FROM auction_bid)")
    void updateClanData();

    @Modifying
    @Query("UPDATE castle SET taxpercent=0 WHERE castle.id NOT IN (SELECT castle FROM clan_data)")
    void updateCastle();

    @Modifying
    @Query("UPDATE characters SET clanid=0 WHERE characters.clanid NOT IN (SELECT id FROM clan_data)")
    void updateCharacters();
}
