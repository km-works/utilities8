package kmworks.util.ds.rng;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.PeekingIterator;

import java.io.*;

public class IntRangeIO {

    /**
     * Internalize a IntRange from an external textual representation.
     * The external textual representation must comply to the following grammar:
     * <pre>
     * start = ws line+ EOF
     * line = value ( '-' value)? ws
     * value = decNumber |  hexNumber
     * decNumber = decDigit+
     * decDigit = [0-9]
     * hexNumber = '0x' hexDigit+
     * hexDigit = [a-fA-F0-9]
     * ws = ( wschar+ | comment )*
     * comment = '#' (!nl _)* nl
     * wschar = [ \t\r\n]
     * nl = '\r'? '\n'
     * </pre>
     *
     * @param r     a Reader for reading text input
     * @param radix
     * @return
     * @throws IOException
     */
    public static IntRange fromText(Reader r, int radix, IntRange.Bounds bounds) throws IOException {
        final ImmutableSet.Builder<Integer> builder = new ImmutableSet.Builder<>();
        final BufferedReader br = new BufferedReader(r);
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0 && !line.startsWith("#")) { // skip empty & comment lines
                if (line.contains("-")) { // line contains a value range
                    final String[] range = line.split("\\s*-\\s*");
                    final int lo = Integer.parseInt(range[0], radix);
                    final int hi = Integer.parseInt(stripTrailingComment(range[1]), radix);
                    for (int i = lo; i <= hi; i++) {
                        builder.add(i);
                    }
                } else {  // line contains a single value
                    builder.add(Integer.parseInt(stripTrailingComment(line), radix));
                }
            }
        }
        return IntRangeBuilder.withBounds(bounds).addAll(builder.build()).build();
    }

    public static IntRange fromText(Reader r, int radix) throws IOException {
        return fromText(r, radix, IntRange.NO_BOUNDS);
    }

    public static void toText(IntRange set, Writer w, int radix) throws IOException {
        final PeekingIterator<Integer> iter = set.iterator();

        while (iter.hasNext()) {
            int curr = iter.next();
            int ende = curr;
            while (iter.hasNext() && iter.peek().equals(ende + 1)) {
                ende = iter.next();
            }
            if (ende == curr) {
                writeCodepoint(w, curr, radix);
                writeln(w);
            } else {
                writeCodepoint(w, curr, radix);
                w.write('-');
                writeCodepoint(w, ende, radix);
                writeln(w);
            }
        }
    }

    private static void writeCodepoint(Writer w, int cp, int radix) throws IOException {
        w.write(Integer.toString(cp, radix));
    }

    private static void writeln(Writer w) throws IOException {
        w.write("\n");
    }

    public static void externalize(IntRange set, OutputStream out) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(set);
        oos.flush();
    }

    public static IntRange internalize(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        return (IntRange) ois.readObject();
    }

    /*
        Private helpers
    */
    private static String stripTrailingComment(String line) {
        if (line.contains("#")) { // strip trailing comments
            line = line.substring(0, line.indexOf('#'));
        }
        return line.trim();
    }

}
