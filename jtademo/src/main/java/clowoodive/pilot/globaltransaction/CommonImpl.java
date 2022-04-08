package clowoodive.pilot.globaltransaction;

import clowoodive.pilot.globaltransaction.dto.AccountDto;
import clowoodive.pilot.globaltransaction.mapper.CommonDBMapper;

public class CommonImpl {
    private final CommonDBMapper commonDBMapper;

    public CommonImpl(CommonDBMapper commonDBMapper){
        this.commonDBMapper = commonDBMapper;
    }

    public AccountDto selectAccount(long userIdx){
        return this.commonDBMapper.getAccountByUserIdx(userIdx);
    }
}
