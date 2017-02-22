package integTest.com.kncept.junit.dataprovider;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.kncept.junit.dataprovider.ParameterSource;
import com.kncept.junit.dataprovider.ParameterisedTest;
import com.kncept.junit.dataprovider.testfactory.TestFactoryCallback;

public class ExampleUseCasesTest {

	@Test
	public void itsOkayToMixNormalTestAnnotations(){
		Assertions.assertTrue(true);
	}
	
	@TestFactory
	public Collection<DynamicTest> testFactory() {
		return TestFactoryCallback.instanceProvider(this);
	}
	
	@ParameterisedTest(source = "daysOfTheWeek")
	@DisplayName("isADayOFTheWeek: %s")
	public void isADayOFTheWeek(String dayName) {
		DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayName.toUpperCase());
		Assertions.assertNotNull(dayOfWeek);
	}
	
	
	@ParameterSource(name = "daysOfTheWeek")
	public static List<Object[]> daysOfTheWeek() {
		return Arrays.asList(
				new Object[]{"Sunday"},
				new Object[]{"Monday"},
				new Object[]{"Tuesday"},
				new Object[]{"Wednesday"},
				new Object[]{"Thursday"},
				new Object[]{"Friday"},
				new Object[]{"Saturday"}
				);
	}
	
}
