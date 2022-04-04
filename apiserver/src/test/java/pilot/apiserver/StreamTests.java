package pilot.apiserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class StreamTest {
    int field1;
    int field2;
    int field3;

    public List<Integer> getFieldList() {
        return Arrays.asList(field1, field2, field3);
    }

}

@SpringBootTest
class StreamTests {

    @Test
    void streamFlatMap() {
        var streamTest1 = new StreamTest();
        streamTest1.field1 = 11;
        streamTest1.field2 = 12;
        streamTest1.field3 = 13;

        var streamTest2 = new StreamTest();
        streamTest2.field1 = 21;
        streamTest2.field2 = 22;
        streamTest2.field3 = 23;
        
        var streamTestList = Arrays.asList(streamTest1, streamTest2);
        var flatFieldList = streamTestList.stream().flatMap(fm -> fm.getFieldList().stream()).collect(Collectors.toList());

        assertTrue(flatFieldList.size() == 5);
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
