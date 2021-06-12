/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/***
 * Unit tests for WebReader.java
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

}