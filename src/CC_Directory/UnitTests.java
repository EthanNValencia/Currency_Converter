/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/***
 * Unit tests
 */
class UnitTests {

    /***
     * Tests that the WebReader.editString() is functioning properly.
     */
    @Test
    public void testEditString_1(){
        String test = "FOO1234foo";
        WebReader wr = new WebReader();
        assertEquals("1234", wr.editString(test));
    }

    /***
     * Verifies that the Currency.getName() method is functioning correctly.
     */
    @Test
    public void testGetName(){
        Currency cur = new Currency("USD", "1", "United States");
        assertEquals("USD", cur.getName());
    }

    /***
     * Verifies that the Currency.getRate() method functions correctly.
     */
    @Test
    public void testGetRate(){
        Currency cur = new Currency("USD", "1", "United States");
        assertEquals("1", cur.getRate());
    }

    /***
     * Verifies that the Currency constructor object functions correctly.
     */
    @Test
    public void testCurrencyConstructor(){
        Currency cur = new Currency("USD", "1", "United States");
        assertEquals(cur, cur);
    }

    /***
     * Verifies that the Calculation.calculateRate() is working properly.
     */
    @Test
    public void testCalculateRate(){
        Currency USD = new Currency("USD", "1", "United States");
        Currency COP = new Currency("COP", "3000", "Colombia");
        Calculation cur = new Calculation(USD, COP);
        assertEquals("3000.00000", cur.getFormatRate());
    }

    /***
     * Verifies that calculation object instantiation and inputConversion are not unified events.
     */
    @Test
    public void testGetInputConversion_NULL(){
        Currency USD = new Currency("USD", "1", "United States");
        Currency COP = new Currency("COP", "3000", "Colombia");
        Calculation cur = new Calculation(USD, COP); // The input conversion has not been calculated.
        assertEquals(null, cur.getInputConversion());
    }

    /***
     * Verifies that the Calculation.inputConversion is functioning as anticipated.
     */
    @Test
    public void testGetInputConversion(){
        Currency USD = new Currency("USD", "1", "United States");
        Currency COP = new Currency("COP", "3000", "Colombia");
        Calculation cur = new Calculation(USD, COP);
        assertEquals(null, cur.getInputConversion());
    }

    /***
     * Verifies that the WebReader.checkLineContent() method is detecting characters of interest.
     */
    @Test
    public void testCheckLineContent(){
        String text = "Fet ctl00_M_lblToAmount Five Eight ASD";
        assertTrue(WebReader.checkLineContent(text));
    }

    /***
     * Verifies that the WebReader.editString() is removing and retaining the desired characters.
     */
    @Test
    public void testEditString(){
        String text = "Fet ctl00_M_lblToAmount Five Eight ASD 5000";
        assertEquals("5000", WebReader.editString(text));
    }

    /***
     * Verifies that the Calculation.getDisplayRate1 is functioning correctly.
     */
    @Test
    public void testGetDisplayRate1(){
        Currency X = new Currency("XXX", "1");
        Currency Y = new Currency("YYY", "3000");
        Calculation testCalc = new Calculation(X, Y);
        assertEquals("1", testCalc.getDisplayRate1());
    }

    /***
     * Verifies that the Calculation.getDisplayRate2 is functioning correctly.
     */
    @Test
    public void testGetDisplayRate2(){
        Currency X = new Currency("XXX", "2");
        Currency Y = new Currency("YYY", "3000");
        Calculation testCalc = new Calculation(X, Y);
        assertEquals("1500.000", testCalc.getDisplayRate2());
    }

    /***
     * Tests Calculation.getDisplayRate1 and Calculation.getDisplayRate2 consecutively.
     */
    @Test
    public void testGetDisplayRates(){
        Currency X = new Currency("XXX", "5");
        Currency Y = new Currency("YYY", "3000");
        Calculation testCalc = new Calculation(X, Y);
        assertEquals("1", testCalc.getDisplayRate1());
        assertEquals("600.000", testCalc.getDisplayRate2());
    }

    /***
     * Tests that the Calculation.getFormatRate() is functioning correctly.
     */
    @Test
    public void testGetFormatRate(){
        Currency X = new Currency("XXX", "5");
        Currency Y = new Currency("YYY", "3000");
        Calculation testCalc = new Calculation(X, Y);
        assertEquals("600.000", testCalc.getFormatRate());
    }

    /***
     * Tests that the Calculation.convertCurrency() is functioning correctly.
     */
    @Test
    public void testConvertCurrency(){
        Currency X = new Currency("XXX", "1");
        Currency Y = new Currency("YYY", "3000");
        Calculation testCalc = new Calculation(X, Y);
        testCalc.convertCurrency(2);
        assertEquals("6,000.00", testCalc.getInputConversion());
    }


}