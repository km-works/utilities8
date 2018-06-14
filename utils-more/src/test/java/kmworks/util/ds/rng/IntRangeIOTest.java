package kmworks.util.ds.rng;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class IntRangeIOTest {

    private IntRange makeIntRange(int... values) {
        return IntRangeBuilder.noBounds().add(values).build();
    }

    private String writeToText(IntRange rng) throws IOException {
        StringWriter w = new StringWriter();
        IntRangeIO.toText(rng, w, 10);
        return w.toString();
    }

    private IntRange readFromText(String text) throws IOException {
        StringReader r = new StringReader(text);
        return IntRangeIO.fromText(r, 10);
    }

    private void pln(String s) {
        System.out.println(s);
    }

    @Test
    public void testToText_01() throws IOException {
        String result = writeToText(makeIntRange(5,6,8,10,11,15));
        pln(result);
        assertEquals("5-6\n8\n10-11\n15\n", result);
    }

    @Test
    public void testRoundtrip_01() throws IOException {
        IntRange rng = makeIntRange(5,6,8,10,11,15);
        String text = writeToText(rng);
        IntRange rng2 = readFromText(text);
        assertEquals(rng, rng2);
    }
}
