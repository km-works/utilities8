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
package kmworks.util.cp;

import kmworks.util.lambda.LambdaUtil;
import kmworks.util.lambda.Predicate1;

/**
 *
 * @author Christian P. Lerch
 */
public abstract class CodepointPredicate {
  
  public static CodepointPredicate of(final Predicate1<Integer> pred1) {
    return new CodepointPredicate() {
      @Override public boolean contains(int cp) {
        return pred1.test(cp);
      }
    };
  }
  
  public abstract boolean contains(int cp);
  
  public final CodepointPredicate negate() {
    return of(LambdaUtil.negate(asPredicate()));
  }
  
  public final CodepointPredicate or(final CodepointPredicate pred) {
    return of(LambdaUtil.or(asPredicate(), pred.asPredicate()));
  }
  
  public final CodepointPredicate and(final CodepointPredicate pred) {
    return of(LambdaUtil.and(asPredicate(), pred.asPredicate()));
  }
  
  public final CodepointPredicate without(final CodepointPredicate pred) {
    return of(LambdaUtil.andNot(asPredicate(), pred.asPredicate()));
  }
  
  public final Predicate1<Integer> asPredicate() {
    return i -> contains(i);
  }
  
}
