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
package kmworks.util.tuple;

import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;
import kmworks.util.ObjectUtil;
import kmworks.util.base.CompareUtil;
import kmworks.util.base.TotalOrder;

/**
 *
 * @author Christian P. Lerch
 */
public final class Tuple2<T1, T2> extends Product2<T1, T2> {
  
  private static final long serialVersionUID = 1L;

  @Nullable private final T1 v1;
  @Nullable private final T2 v2;

  Tuple2(@Nullable T1 v1, @Nullable T2 v2) {
    this.v1 = checkSerializable(v1, 1);
    this.v2 = checkSerializable(v2, 2);
  }

  @Override 
  @Nullable public T1 _1() { return v1; }

  @Override 
  @Nullable public T2 _2() { return v2; }

  @Override public int hashCode() {
    return ObjectUtil.hash(Tuple2.class, v1, v2);
  }

  @Override public boolean equals(Object that) {
    if (this == that) return true;
    if (!(that instanceof Tuple2<?,?>)) return false;
    Tuple2<?,?> cast = (Tuple2<?,?>) that;
    return  v1 == null ? cast.v1 == null : v1.equals(cast.v1) && 
            v2 == null ? cast.v2 == null : v2.equals(cast.v2);
  }
  
  @Override public String toString() {
    return String.format("#(%s, %s)", v1, v2);
  }
  
  /*
   * Provide a comparator for Tuple2, ordered by the natural order of the elements (the leftmost
   * elements have the highest sort priority).
   */
  public static <T1 extends Comparable<? super T1>, T2 extends Comparable<? super T2>>
      TotalOrder<Tuple2<? extends T1, ? extends T2>> comparator() {
    return new Tuple2Comparator<>(CompareUtil.<T1>naturalOrder(), CompareUtil.<T2>naturalOrder());
  }
  
  public static <T1, T2> TotalOrder<Tuple2<? extends T1, ? extends T2>>
      comparator(Comparator<? super T1> comp1, Comparator<? super T2> comp2) {
    return new Tuple2Comparator<>(comp1, comp2);
  }
  
  private static final class Tuple2Comparator<T1, T2> 
          extends TotalOrder<Tuple2<? extends T1, ? extends T2>> 
          implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Comparator<? super T1> _comp1;
    private final Comparator<? super T2> _comp2;
    public Tuple2Comparator(Comparator<? super T1> comp1, Comparator<? super T2> comp2) {
      _comp1 = comp1;
      _comp2 = comp2;
    }
    @Override
    public int compare(Tuple2<? extends T1, ? extends T2> p1, Tuple2<? extends T1, ? extends T2> p2) {
      return CompareUtil.compare(_comp1, p1._1(), p2._1(), _comp2, p1._2(), p2._2());
    }
    @Override
    public boolean equals(Object o) {
      if (this == o) { return true; }
      else if (!(o instanceof Tuple2Comparator<?,?>)) { return false; }
      else {
        Tuple2Comparator<?,?> cast = (Tuple2Comparator<?,?>) o;
        return _comp1.equals(cast._comp1) && _comp2.equals(cast._comp2);
      }
    }
    @Override
    public int hashCode() { return ObjectUtil.hash(Tuple2Comparator.class, _comp1, _comp2); }
  }

}
