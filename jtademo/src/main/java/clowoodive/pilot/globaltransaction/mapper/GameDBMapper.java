package clowoodive.pilot.globaltransaction.mapper;

import clowoodive.pilot.globaltransaction.dto.UserBaseDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface GameDBMapper {
    @Select("SELECT * FROM user_base WHERE user_idx=#{user_idx}")
    UserBaseDto getUserBase(@Param("user_idx") long userIdx);
}
