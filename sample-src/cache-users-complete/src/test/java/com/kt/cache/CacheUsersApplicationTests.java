package com.kt.cache;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import com.kt.cache.dto.UsersDto;
import com.kt.cache.repository.UsersCacheRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class CacheUsersApplicationTests {

	@Autowired
	private UsersCacheRepository repostiory;
	
	@Autowired
    StringRedisTemplate redisTemplate;

	@Test
	void testRepo() {
		UsersDto usersDto = UsersDto.builder()
				.userId("USER-0006")
				.userName("Song")
				.age(20)
				.height(180)
				.createdAt(new Date())
				.build();
		
		// 저장
		repostiory.save(usersDto);
		
		// `keyspace:id` 값을 가져옴	
		log.info("[testRepo] repostiory = {}", repostiory.findById(usersDto.getUserId()));
		
		// CatalogDto Entity 의 @RedisHash 에 정의되어 있는 keyspace (catalog) 에 속한 키의 갯수를 구함
		log.info("[testRepo] repostiory.count = {}", repostiory.count());
        
		// 삭제
//		repostiory.delete(catalogDto);
//		log.info("[testRepo] repostiory delete job was completed");
	}
	

	@Test
	public void testStrings() {
		final String key = "string";

		final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();

		stringStringValueOperations.set(key, "1"); // redis set 명령어
		final String result_1 = stringStringValueOperations.get(key); // redis get 명령어

		log.info("[testStrings] result_1 = {}", result_1);

		stringStringValueOperations.increment(key); // redis incr 명령어
		final String result_2 = stringStringValueOperations.get(key);

		log.info("[testStrings] result_2 = {}", result_2);
	}
//result_1 = 1
//result_2 = 2
	

	@Test
	public void testList() {
		final String key = "list";

		final ListOperations<String, String> stringStringListOperations = redisTemplate.opsForList();

		stringStringListOperations.rightPush(key, "H");
		stringStringListOperations.rightPush(key, "e");
		stringStringListOperations.rightPush(key, "l");
		stringStringListOperations.rightPush(key, "l");
		stringStringListOperations.rightPush(key, "o");
		stringStringListOperations.rightPushAll(key, " ", "w", "o", "r", "l", "d");

		final String character_1 = stringStringListOperations.index(key, 1);
		log.info("[testList] character_1 = {}", character_1);

		log.info("[testList] size = {}", stringStringListOperations.size(key));
		log.info("[testList] Left Pop = {}", stringStringListOperations.leftPop(key));
		log.info("[testList] size = {}", stringStringListOperations.size(key));
	}

	@Test
	public void testSet() {
		String key = "set";
		SetOperations<String, String> stringStringSetOperations = redisTemplate.opsForSet();

		stringStringSetOperations.add(key, "H");
		stringStringSetOperations.add(key, "e");
		stringStringSetOperations.add(key, "l");
		stringStringSetOperations.add(key, "l");
		stringStringSetOperations.add(key, "o");

		Set<String> setMembers = stringStringSetOperations.members(key);
		log.info("[testSet] members = ", Arrays.toString(setMembers.toArray()));

		Long size = stringStringSetOperations.size(key);
		log.info("[testSet] size = {}", size);

		Cursor<String> cursor = stringStringSetOperations.scan(key,
				ScanOptions.scanOptions().match("*").count(3).build());

		while (cursor.hasNext()) {
			log.info("[testSet] cursor = {}", cursor.next());
		}
	}

//[testSet] members = 
//[testSet] size = 4
//[testSet] cursor = H
//[testSet] cursor = o
//[testSet] cursor = l
//[testSet] cursor = e


	@Test
	public void testSortedSet() {
		String key = "sortedSet";

		ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();

		stringStringZSetOperations.add(key, "H", 1);
		stringStringZSetOperations.add(key, "e", 2);
		stringStringZSetOperations.add(key, "l", 3);
		stringStringZSetOperations.add(key, "l", 4);
		stringStringZSetOperations.add(key, "o", 5);

		Set<String> range = stringStringZSetOperations.range(key, 0, 5);
		log.info("[testSortedSet] range = {}", Arrays.toString(range.toArray()));

		Long size = stringStringZSetOperations.size(key);
		log.info("[testSortedSet] size = {}", size);

		Set<String> scoreRange = stringStringZSetOperations.rangeByScore(key, 0, 3);
		log.info("[testSortedSet] scoreRange = {}", Arrays.toString(scoreRange.toArray()));
	}
	//[testSortedSet] range = [H, e, l, o]
	//[testSortedSet] size = 4
	//[testSortedSet] scoreRange = [H, e]
	
	@Test
	public void testHash() {
		String key = "hash";

		HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();

		stringObjectObjectHashOperations.put(key, "subkey1", "data1");
		stringObjectObjectHashOperations.put(key, "subkey2", "data2");
		stringObjectObjectHashOperations.put(key, "subkey3", "data3");

		Object data = stringObjectObjectHashOperations.get(key, "subkey1");
		log.info("[testHash] subkey1 = {}", data);

		Map<Object, Object> entries = stringObjectObjectHashOperations.entries(key);
		log.info("[testHash] entries subkey2 = {}", entries.get("subkey2"));

		Long size = stringObjectObjectHashOperations.size(key);
		log.info("[testHash] size = {}", size);
	}
	//[testHash] Hello = sabarada
	//[testHash] entries = sabarada2
	//[testHash] size = 3
	
}
