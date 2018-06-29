package com.l2jbr.gameserver.model.entity.database.repository;

import com.l2jbr.gameserver.model.entity.database.ClanSkills;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClanSkillRepository extends CrudRepository<ClanSkills, Integer> {

    @Query("SELECT * FROM clan_skills WHERE clan_id=:clan")
    Iterable<ClanSkills> findAllByClan(@Param("clan") int clanId);

    @Query("UPDATE clan_skills SET skill_level=:level WHERE skill_id=:skill AND clan_id=:clan")
    int updateSkillLevel(@Param("clan") int clanId, @Param("skill") int skillId, @Param("level") int skillLevel);
}
