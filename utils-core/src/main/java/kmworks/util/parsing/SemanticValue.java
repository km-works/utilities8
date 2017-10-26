/*
 * xtc - The eXTensible Compiler
 * Copyright (C) 2004-2008 Robert Grimm
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package kmworks.util.parsing;

/**
 * A semantic value.
 *
 * @author Robert Grimm
 * @version $Revision: 1.23 $
 */
public final class SemanticValue extends ParseResult {

  /** The actual value. */
  public final Object value;

  /**
   * The embedded parse error.  An embedded parse error is the most
   * specific parse error encountered during the parse leading to this
   * semantic value (typically, returned by an unsuccessful option).
   * It is used to possibly replace a less specific parse error
   * generated while continuing to parse the input.  This field must
   * not be <code>null</code>; instead, a {@link ParseError#DEFAULT
   * dummy parse error} should be used.
   */
  public final ParseError error;

  /**
   * Create a new semantic value.
   *
   * @param value The value.
   * @param index The index into the rest of the input.
   */
  public SemanticValue(final Object value, final int index) {
    this(value, index, ParseError.DEFAULT);
  }

  /**
   * Create a new semantic value.
   *
   * @param value The value.
   * @param index The index into the rest of the input.
   * @param error The embedded parse error.
   */
  public SemanticValue(final Object value, final int index,
                       final ParseError error) {
    super(index);
    this.value = value;
    this.error = error;
  }
  
  public static SemanticValue VOID(final int index, final ParseError error) {
      return new SemanticValue(null, index, error);
  }

  public static SemanticValue VOID(final int index) {
      return new SemanticValue(null, index, ParseError.DEFAULT);
  }

  @Override
  public boolean hasValue() {
    return true;
  }

  @Override
  public boolean hasValue(final String s) {
    return s.equals(this.value);
  }

  @Override
  public boolean hasValueIgnoreCase(final String s) {
    return s.equalsIgnoreCase(this.value.toString());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T semanticValue() {
    return (T)value;
  }

  @Override
  public ParseError parseError() {
    return error;
  }

  @Override
  public ParseError confine(final ParseError error) {
    return this.error.index() <= error.index() ? error : this.error;
  }

  @Override
  public ParseError select(final ParseError error, final int index) {
    return this.error.index() <= index || this.error.index() <= error.index() ?
      error : this.error;
  }

  @Override
  public SemanticValue createValue(final Object value, final ParseError error) {
    return value == this.value && error == this.error ?
      this : new SemanticValue(value, index(), error);
  }

}
