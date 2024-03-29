package pilot.apiserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@SpringBootTest
class ApiserverApplicationTests {

	@Test
	void faltMap() {
		class Dto {
			public long a;
			public long b;
		}

		Dto d1 = new Dto();
		d1.a = 5;
		d1.b = 4;

		Dto d2 = new Dto();
		d2.a = 3;
		d2.b = 2;

		List<Dto> dtoList = Arrays.asList(d1, d2);
		List<Long> allList = dtoList.stream().flatMapToLong(f -> LongStream.of(f.a, f.b)).collect(Collectors.);

		}
	}

	@Test
	void contextLoads() {
		var serverZoneId = ZoneId.systemDefault();
		var baseTimeZoneId = ZoneOffset.ofHours(8);


		var nowTime = convertOnBaseTimeZone(LocalDateTime.now(), 8);
		var startTime = convertOnBaseTimeZone(LocalDateTime.of(2021, 11, 4, 0, 30), 8);
		var endTime = convertOnBaseTimeZone(LocalDateTime.of(2021, 11, 5, 19, 0), 8);

		if(startTime.isBefore(nowTime))
			System.out.println("aa");

		var gapDay = getGapDay(LocalDateTime.of(2021, 11, 4, 0, 30), LocalDateTime.now(), 8);
		var gapDay2 = getGapDay(LocalDateTime.of(2021, 11, 4, 0, 30), LocalDateTime.now(), 9);

		var gapDay3 = getGapDay(LocalDateTime.of(2021, 11, 3, 0, 30), LocalDateTime.now(), 8);

		if(startTime.toLocalDate().isEqual(nowTime.toLocalDate()))
			System.out.println("11111111111111111");

		if(startTime.plusDays(1).toLocalDate().isEqual(nowTime.toLocalDate()))
			System.out.println("2222222222222222222");

		var aa = startTime.toLocalDateTime();
	}

	public static ZonedDateTime convertOnBaseTimeZone(LocalDateTime dateTime, int baseTimeZone) {
		var serverZoneId = ZoneId.systemDefault();
		var baseTimeZoneId = ZoneOffset.ofHours(baseTimeZone);
		return dateTime.atZone(serverZoneId).withZoneSameInstant(baseTimeZoneId);
	}

	public static int getGapDay(LocalDateTime startAt, LocalDateTime endAt, int baseTimeZone) {
		var serverZoneId = ZoneId.systemDefault();
		var baseTimeZoneId = ZoneOffset.ofHours(baseTimeZone);

		// LocalDateTime과 같은 ZonedDateTime
		var zonedStartAt = startAt.atZone(serverZoneId).withZoneSameInstant(baseTimeZoneId);
		var zonedEndAt = endAt.atZone(serverZoneId).withZoneSameInstant(baseTimeZoneId);

		// ZonedDateTime의 00시
		zonedStartAt = ZonedDateTime.of(
				zonedStartAt.toLocalDate(),
				LocalTime.of(00, 00, 00),
				ZoneOffset.ofHours(baseTimeZone)
		);

		zonedEndAt = ZonedDateTime.of(
				zonedEndAt.toLocalDate(),
				LocalTime.of(00, 00, 00),
				ZoneOffset.ofHours(baseTimeZone)
		);

		var day = ChronoUnit.DAYS.between(zonedStartAt, zonedEndAt);
		return (int)day;

		// var period = Period.between(zonedStartAt.toLocalDate(), zonedEndAt.toLocalDate());
		// return period.getDays();
	}

	@Test
	void logicTest(){
		long a = 1;
		long b= 0;

		long c = a/b;
	}

}
