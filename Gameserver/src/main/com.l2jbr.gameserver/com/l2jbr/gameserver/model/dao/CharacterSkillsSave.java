package com.l2jbr.gameserver.model.dao;

import com.l2jbr.commons.database.dao.DAO;

public class CharacterSkillsSave implements DAO {

    private int char_obj_id;
    private int skill_id;
    private int skill_level;
    private int effect_count;
    private int effect_cur_time;
    private int reuse_delay;
    private int restore_type;
    private int class_index;
    private int buff_index;
}
