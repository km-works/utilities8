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
package kmworks.util.lambda;

import java.io.Serializable;
import kmworks.util.iter.IterUtil;

/**
 *
 * @author Christian P. Lerch
 */
public final class LambdaUtil {
  
  /** Produce the conjunction ({@code &&}) of {@code p1} and {@code p2}. */
  public static <T> Predicate1<T> and(Predicate1<? super T> p1, Predicate1<? super T> p2) {
    return new AndPredicate<>(IterUtil.<Predicate1<? super T>>make(p1, p2));
  }
  
  /** Produce the conjunction ({@code &&}) of the given predicates. */
  public static <T> Predicate1<T> and(Iterable<? extends Predicate1<? super T>> preds) {
    return new AndPredicate<>(preds);
  }
  
  private static final class AndPredicate<T> implements Predicate1<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private final Iterable<? extends Predicate1<? super T>> _preds;
    public AndPredicate(Iterable<? extends Predicate1<? super T>> preds) { _preds = preds; }
    @Override public boolean test(T arg) {
      for (Predicate1<? super T> p : _preds) { if (!p.test(arg)) { return false; } }
      return true;
    }
  }
  
  /** Produce the disjunction ({@code ||}) of {@code p1} and {@code p2}. */
  public static <T> Predicate1<T> or(Predicate1<? super T> p1, Predicate1<? super T> p2) {
    return new OrPredicate<>(IterUtil.<Predicate1<? super T>>make(p1, p2));
  }
  
  /** Produce the disjunction ({@code ||}) of the given predicates. */
  public static <T> Predicate1<T> or(Iterable<? extends Predicate1<? super T>> preds) {
    return new OrPredicate<>(preds);
  }
  
  private static final class OrPredicate<T> implements Predicate1<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private final Iterable<? extends Predicate1<? super T>> _preds;
    public OrPredicate(Iterable<? extends Predicate1<? super T>> preds) { _preds = preds; }
    @Override public boolean test(T arg) {
      for (Predicate1<? super T> p : _preds) { if (p.test(arg)) { return true; } }
      return false;
    }
  }
  
  /** Produce the negation ({@code !}) of {@code pred}. */
  public static <T> Predicate1<T> negate(Predicate1<? super T> pred) {
    return new NegationPredicate<>(pred);
  }
  
  private static final class NegationPredicate<T> implements Predicate1<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private final Predicate1<? super T> _p;
    public NegationPredicate(Predicate1<? super T> p) { _p = p; }
    @Override public boolean test(T arg) { return !_p.test(arg); }
  }

  /** Produce the negation ({@code !}) of {@code pred}. */
  public static <T> Predicate1<T> andNot(Predicate1<? super T> p1, Predicate1<? super T> p2) {
    return new AndNotPredicate<>(p1, p2);
  }
  
  private static final class AndNotPredicate<T> implements Predicate1<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private final Predicate1<? super T> _p1;
    private final Predicate1<? super T> _p2;
    public AndNotPredicate(Predicate1<? super T> p1, Predicate1<? super T> p2) { _p1 = p1; _p2 = p2; }
    @Override public boolean test(T arg) {
      return and(_p1, negate(_p2)).test(arg);
    }
  }

}
