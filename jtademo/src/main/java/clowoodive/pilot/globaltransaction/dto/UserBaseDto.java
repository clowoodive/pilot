package clowoodive.pilot.globaltransaction.dto;

import java.time.LocalDateTime;

public class UserBaseDto {
    public long user_idx;
    public int main_job_type;
    public int avatar_id;
    public LocalDateTime create_date;

    public int lv;
    public long exp;
    public int vip_lv;
    public int vip_exp;
    // public int familiar_lv;
    // public int familiar_point;

    public long guild_idx; // 길드 아이디
    public int portrait_id;
    public int equip_slot_id; // 장비 슬롯 id
    public boolean starter_mission_complete;
}
