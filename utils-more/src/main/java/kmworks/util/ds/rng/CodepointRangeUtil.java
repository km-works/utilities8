/*
 * Copyright (C) 2005-2016 Christian P. Lerch <christian.p.lerch[at]gmail.com>
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.ds.rng;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.PeekingIterator;
import kmworks.util.ds.rng.impl.BitsetCodepointRange;
import kmworks.util.lambda.LambdaUtil;
import kmworks.util.lambda.Predicate1;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Christian P. Lerch
 */
public class CodepointRangeUtil {

  public static CodepointPredicate of(@Nonnull final Predicate1<Integer> p) {
    Objects.requireNonNull(p);
    return new CodepointPredicate() {
      @Override
      public boolean contains(int value) {
        return p.test(value);
      }
    };
  }

  public static CodepointPredicate negate(@Nonnull final CodepointPredicate p) {
    Objects.requireNonNull(p);
    return of(LambdaUtil.negate(asPredicate(p)));
  }

  public static CodepointPredicate  or(@Nonnull final CodepointPredicate p1, @Nonnull final CodepointPredicate p2) {
    Objects.requireNonNull(p1);
    Objects.requireNonNull(p2);
    return of(LambdaUtil.or(asPredicate(p1),asPredicate(p2)));
  }

  public static CodepointPredicate  and(@Nonnull final CodepointPredicate p1, @Nonnull final CodepointPredicate p2) {
    Objects.requireNonNull(p1);
    Objects.requireNonNull(p2);
    return of(LambdaUtil.and(asPredicate(p1),asPredicate(p2)));
  }

  public static CodepointPredicate without(@Nonnull final CodepointPredicate p1, @Nonnull final CodepointPredicate p2) {
    Objects.requireNonNull(p1);
    Objects.requireNonNull(p2);
    return of(LambdaUtil.andNot(asPredicate(p1),asPredicate(p2)));
  }

  public static Predicate1<Integer> asPredicate(@Nonnull final CodepointPredicate p) {
    Objects.requireNonNull(p);
    return i -> p.contains(i);
  }

  public static List<Integer> codepointsFrom(CharSequence s) {
    ImmutableList.Builder<Integer> builder = new ImmutableList.Builder<>();
    int i=0;
    while (i < s.length()) {
      builder.add((int)s.charAt(i));
      i += Character.charCount(i);
    }
    return builder.build();
  }
  
  /**
   * Internalize a CodepointRange from an external textual representation.
   * The external textual representation should comply to the following garmmar:
   * <pre>
   * start = ws line+ EOF
   * line = codepoint ( '-' codepoint)? ws
   * codepoint = decNumber |  hexNumber
   * decNumber = decDigit+
   * decDigit = [0-9]
   * hexNumber = '0x' hexDigit+
   * hexDigit = [a-fA-F0-9]
   * ws = ( wschar+ | comment )*
   * comment = '#' (!nl _)* nl
   * wschar = [ \t\r\n]
   * nl = '\r'? '\n'
   * </pre>
   * @param r a Reader for reading text input
   * @param radix
   * @return
   * @throws IOException 
   */
  public static CodepointRange fromText(Reader r, int radix) throws IOException
  { 
    final ImmutableSortedSet.Builder<Integer> builder = new ImmutableSortedSet.Builder<>(IntRange.COMPARATOR);
    final BufferedReader br = new BufferedReader(r);
    String line;
    while ((line = br.readLine()) != null) {
      line = line.trim();
      if (line.length() > 0 && !line.startsWith("#")) { // skip empty & comment lines
        if (line.contains("-")) { // line contains a codepoint range
          final String[] range = line.split("\\s*-\\s*");
          final int lo = Integer.parseInt(range[0], radix);
          final int hi = Integer.parseInt(stripTrailingComment(range[1]), radix);
          for (int i = lo; i <= hi; i++) {
            builder.add(i);
          }
        } else {  // line contains a single codepoint
          builder.add(Integer.parseInt(stripTrailingComment(line), radix));
        }          
      }
    }
    return BitsetCodepointRange.of(builder.build());
  }
  
  public static void toText(CodepointRange set, Writer w, int radix) throws IOException
  {
    final PeekingIterator<Integer> iter = set.iterator();

    while (iter.hasNext()) {
      int curr = iter.next();
      int ende = curr;
      while (iter.hasNext() && iter.peek().equals(ende+1)) {
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
  
  public static void externalize(BitsetCodepointRange set, OutputStream out) throws IOException
  {
    ObjectOutputStream oos = new ObjectOutputStream(out);
    oos.writeObject(set);
    oos.flush();
  }
  
  public static CodepointRange internalize(InputStream is) throws IOException, ClassNotFoundException
  {
	    ObjectInputStream ois = new ObjectInputStream(is);  	    
	    return (BitsetCodepointRange)ois.readObject();
  }
  
  /*
      Private helpers
  */
  private static String stripTrailingComment(String line) 
  {
    if (line.contains("#")) { // strip trailing comments
      line = line.substring(0, line.indexOf('#'));
    }
    return line.trim();
  }
  
}
