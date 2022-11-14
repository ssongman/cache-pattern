package com.ssongman;

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
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import com.ssongman.dto.CatalogDto;
import com.ssongman.repository.CatalogCacheRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class CacheCatalogApplicationTests {

	@Autowired
	private CatalogCacheRepository repostiory;
	
	@Autowired
	StringRedisTemplate redisTemplate;

	@Test
	void testRepo() {
		CatalogDto catalogDto = CatalogDto.builder()
				.productId("CATALOG-006")
				.productName("seoul")
				.stock(10)
				.unitPrice(100)
				.createdAt(new Date())
				.build();
		
		// 저장
		repostiory.save(catalogDto);
		
		// `keyspace:id` 값을 가져옴	
		log.info("[testRepo] repostiory = {}",repostiory.findById(catalogDto.getProductId()));
		
		// CatalogDto Entity 의 @RedisHash 에 정의되어 있는 keyspace (catalog) 에 속한 키의 갯수를 구함
		log.info("[testRepo] repostiory.count = {}",repostiory.count());
		
		// 삭제
//		repostiory.delete(catalogDto);
//		log.info("repostiory delete job was completed");
		
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

		stringStringListOperations.rightPushAll(key, " ", "s", "a", "b", "a");

		final String character_1 = stringStringListOperations.index(key, 1);

		log.info("[testList] character_1 = {}", character_1);

		final Long size = stringStringListOperations.size(key);

		log.info("[testList] size = {}", size);

		final List<String> ResultRange = stringStringListOperations.range(key, 0, 9);

		log.info("[testList] ResultRange = ", Arrays.toString(ResultRange.toArray()));

		log.info("[testList] Left Pop = {}", stringStringListOperations.leftPop(key));

		log.info("[testList] size = {}", stringStringListOperations.size(key));
	}
	//character_1 = e
	//size = 10
	//ResultRange = [H, e, l, l, o,  , s, a, b, a]
	
	@Test
	public void testSet() {
		String key = "set";
		SetOperations<String, String> stringStringSetOperations = redisTemplate.opsForSet();

		stringStringSetOperations.add(key, "H");
		stringStringSetOperations.add(key, "e");
		stringStringSetOperations.add(key, "l");
		stringStringSetOperations.add(key, "l");
		stringStringSetOperations.add(key, "o");

		Set<String> sabarada = stringStringSetOperations.members(key);

		log.info("[testSet] members = ", Arrays.toString(sabarada.toArray()));

		Long size = stringStringSetOperations.size(key);

		log.info("[testSet] size = {}", size);

		Cursor<String> cursor = stringStringSetOperations.scan(key,
				ScanOptions.scanOptions().match("*").count(3).build());

		while (cursor.hasNext()) {
			log.info("[testSet] cursor = {}", cursor.next());
		}
	}
	//members = [l, e, o, H]
	//size = 4
	//cursor = l
	//cursor = e
	//cursor = o
	//cursor = H
	
	
	
	@Test
	public void testSortedSet() {
		String key = "sortedSet";

		ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();

		stringStringZSetOperations.add(key, "H", 1);
		stringStringZSetOperations.add(key, "e", 5);
		stringStringZSetOperations.add(key, "l", 10);
		stringStringZSetOperations.add(key, "l", 15);
		stringStringZSetOperations.add(key, "o", 20);

		Set<String> range = stringStringZSetOperations.range(key, 0, 5);

		log.info("[testSortedSet] range = {}", Arrays.toString(range.toArray()));

		Long size = stringStringZSetOperations.size(key);

		log.info("[testSortedSet] size = {}", size);

		Set<String> scoreRange = stringStringZSetOperations.rangeByScore(key, 0, 13);

		log.info("[testSortedSet] scoreRange = {}", Arrays.toString(scoreRange.toArray()));
	}
	//range = [H, e, l, o]
	//size = 4
	//scoreRange = [H, e
	
	
	
	@Test
	public void testHash() {
		String key = "hash";

		HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();

		stringObjectObjectHashOperations.put(key, "Hello", "sabarada");
		stringObjectObjectHashOperations.put(key, "Hello2", "sabarada2");
		stringObjectObjectHashOperations.put(key, "Hello3", "sabarada3");

		Object hello = stringObjectObjectHashOperations.get(key, "Hello");

		log.info("[testHash] hello = {}", hello);

		Map<Object, Object> entries = stringObjectObjectHashOperations.entries(key);

		log.info("[testHash] entries = {}", entries.get("Hello2"));

		Long size = stringObjectObjectHashOperations.size(key);

		log.info("[testHash] size = {}", size);
	}
	//hello = sabarada
	//entries = sabarada2
	//size = 3
		

}
