package integTest.com.kncept.junit.dataprovider;

import static com.kncept.junit.dataprovider.testfactory.TestFactoryCallback.instanceProvider;
import static java.time.DayOfWeek.valueOf;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.kncept.junit.dataprovider.ParameterSource;
import com.kncept.junit.dataprovider.ParameterisedTest;

/**
 * This Test class is a simple example that demonstrates how this library can be used.
 *
 */
public class ExampleUseCasesTest {

	@Test
	public void itsOkayToMixNormalTestAnnotations(){
		assertTrue(true);
	}
	
	@TestFactory
	public Collection<DynamicTest> testFactory() {
		return instanceProvider(this);
	}
	
	@ParameterisedTest(source = "daysOfTheWeek")
	public void isADayOFTheWeek(String dayName) {
		DayOfWeek dayOfWeek = valueOf(dayName.toUpperCase());
		assertNotNull(dayOfWeek);
	}
	
	@ParameterisedTest(source = "daysOfTheWeek")
	@DisplayName("day is propercase: %s")
	public void startsWithACapitalLetter(String dayName) {
		assertEquals(
				dayName.substring(0, 1).toUpperCase(),
				dayName.substring(0, 1));
		assertEquals(
				dayName.substring(1).toLowerCase(),
				dayName.substring(1));
	}
	
	@ParameterisedTest(source = "daysOfTheWeek")
	@Disabled
	public void canTranslateToLatin(String dayName) {
		Assertions.fail("This test is disabled");
		/*
		dies Solis
		dies Lunae
		dies Martis
		dies Mercurii
		dies Iovis
		dies Veneris
		dies Saturni
		 */
	}
	
	
	@ParameterSource(name = "daysOfTheWeek")
	public static List<Object[]> daysOfTheWeek() {
		return asList(
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
