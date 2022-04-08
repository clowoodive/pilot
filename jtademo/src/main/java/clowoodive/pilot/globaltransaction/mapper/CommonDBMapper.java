package clowoodive.pilot.globaltransaction.mapper;

import clowoodive.pilot.globaltransaction.dto.AccountDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@CommonDBScan
public interface CommonDBMapper {
    @Select("SELECT * FROM account WHERE user_idx=#{user_idx}")
    AccountDto getAccountByUserIdx(@Param("user_idx") long user_idx);
}
