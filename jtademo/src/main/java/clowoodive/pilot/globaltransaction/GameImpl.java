package clowoodive.pilot.globaltransaction;

import clowoodive.pilot.globaltransaction.dto.UserBaseDto;
import clowoodive.pilot.globaltransaction.mapper.GameDBMapper;

public class GameImpl {
    private final GameDBMapper gameDBMapper;

    public GameImpl(GameDBMapper gameDBMapper){
        this.gameDBMapper = gameDBMapper;
    }

    public UserBaseDto selectUserBase(long userIdx){
        return this.gameDBMapper.getUserBase(userIdx);
    }
}
