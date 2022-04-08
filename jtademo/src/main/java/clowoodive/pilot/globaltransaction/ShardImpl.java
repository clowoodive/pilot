package clowoodive.pilot.globaltransaction;

import clowoodive.pilot.globaltransaction.dto.AccountDto;
import clowoodive.pilot.globaltransaction.dto.UserBaseDto;
import clowoodive.pilot.globaltransaction.mapper.CommonDBMapper;
import clowoodive.pilot.globaltransaction.mapper.ShardDB1Mapper;
import clowoodive.pilot.globaltransaction.mapper.ShardDB2Mapper;

public class ShardImpl {
    private final ShardDB1Mapper shardDB1Mapper;
    private final ShardDB2Mapper shardDB2Mapper;

    public ShardImpl(ShardDB1Mapper shardDB1Mapper, ShardDB2Mapper shardDB2Mapper){
        this.shardDB1Mapper = shardDB1Mapper;
        this.shardDB2Mapper = shardDB2Mapper;
    }

    public UserBaseDto selectShard1UserBase(long userIdx){
        return this.shardDB1Mapper.getUserBase(userIdx);
    }

    public UserBaseDto selectShard2UserBase(long userIdx){
        return this.shardDB2Mapper.getUserBase(userIdx);
    }
}
