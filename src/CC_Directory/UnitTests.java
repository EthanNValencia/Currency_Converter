/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        CC_Directory.WebReader wr = new CC_Directory.WebReader();
        assertEquals("1234", wr.editString(test));
    }

    /***
     * Verifies that the Currency.getName() method is functioning correctly.
     */
    @Test
    public void testGetName(){
        Currency cur = new Currency("USD", "1");
        assertEquals("USD", cur.getName());
    }

    /***
     * Verifies that the Currency.getRate() method functions correctly.
     */
    @Test
    public void testGetRate(){
        Currency cur = new Currency("USD", "1");
        assertEquals("1", cur.getRate());
    }

    /***
     * Verifies that the Currency constructor object functions correctly.
     */
    @Test
    public void testCurrencyConstructor(){
        Currency cur = new Currency("USD", "1");
        assertEquals(cur, cur);
    }

    /***
     * Verifies that the Calculation.calculateRate() is working properly.
     */
    @Test
    public void testCalculateRate(){
        Currency USD = new Currency("USD", "1");
        Currency COP = new Currency("COP", "3000");
        Calculation cur = new Calculation(USD, COP);
        assertEquals("3000.00000", cur.getFormatRate());
    }

    /***
     *
     */
    @Test
    public void testGetInputConversion_NULL(){
        Currency USD = new Currency("USD", "1");
        Currency COP = new Currency("COP", "3000");
        Calculation cur = new Calculation(USD, COP); // The input conversion has not been calculated.
        assertEquals(null, cur.getInputConversion());
    }

    /***
     *
     */
    public void testGetInputConversion(){
        Currency USD = new Currency("USD", "1");
        Currency COP = new Currency("COP", "3000");
        Calculation cur = new Calculation(USD, COP);
        //assertEquals();
    }
}