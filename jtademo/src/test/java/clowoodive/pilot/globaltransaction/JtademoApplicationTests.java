package clowoodive.pilot.globaltransaction;

import clowoodive.pilot.globaltransaction.mapper.CommonDBMapper;
import clowoodive.pilot.globaltransaction.mapper.GameDBMapper;
import clowoodive.pilot.globaltransaction.mapper.ShardDB1Mapper;
import clowoodive.pilot.globaltransaction.mapper.ShardDB2Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JtademoApplicationTests {

	@Autowired
	GameDBMapper gameDBMapper;

	@Test
	void indivisualGame1Test() {
		GameImpl gameImpl = new GameImpl(gameDBMapper);
		var dto = gameImpl.selectUserBase(1106698028522030003L);

		System.out.println("indivisualGame1Test result dto.userIdx:" + (dto != null ? dto.user_idx : "not found"));

		assert (dto != null);
		if (dto != null)
			assert (dto.user_idx == 1106698028522030003L);
	}

	@Autowired
	CommonDBMapper commonDBMapper;

	@Test
	void annotaionCommonTest() {
		CommonImpl commonImpl = new CommonImpl(commonDBMapper);
		var dto = commonImpl.selectAccount(1106698028522030003L);

		System.out.println("annotaionCommonTest result dto.userIdx:" + (dto != null ? dto.user_idx : "not found"));

		assert (dto != null);
		if (dto != null)
			assert (dto.user_idx == 1106698028522030003L);
	}

	@Autowired
	ShardDB1Mapper shardDB1Mapper;
	@Autowired
	ShardDB2Mapper shardDB2Mapper;

	@Test
	void shardDBTest() {
		ShardImpl shardImpl = new ShardImpl(shardDB1Mapper, shardDB2Mapper);
		var shard1Dto = shardImpl.selectShard1UserBase(1106698028522030003L);
		var shard2Dto = shardImpl.selectShard2UserBase(1064816712260800002L);

		System.out.println("shardDBTest result shar1Dto.userIdx:" + (shard1Dto != null ? shard1Dto.user_idx : "not found"));
		System.out.println("shardDBTest result shar2Dto.userIdx:" + (shard2Dto != null ? shard2Dto.user_idx : "not found"));

		assert (shard1Dto != null);
		if (shard1Dto != null)
			assert (shard1Dto.user_idx == 1106698028522030003L);

		assert (shard2Dto != null);
		if (shard2Dto != null)
			assert (shard2Dto.user_idx == 1064816712260800002L);
	}
}
