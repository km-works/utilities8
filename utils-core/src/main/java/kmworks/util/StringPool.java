/*
 * Copyright (C) 2005-2017 Christian P. Lerch, Vienna, Austria.
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
package kmworks.util;

/**
 * @author Christian P. Lerch
 */
public final class StringPool {

  private StringPool() {}
  
  public static final char TAB_CH           = '\t'; // U+0009   (horizontal tabulation, character tabulation, HT)
  public static final char NEWLINE_CH       = '\n'; // U+000A   (line feed, LF, end of line, EOL)
  public static final char LF_CH            = NEWLINE_CH;
  public static final char CR_CH            = '\r';
  
  public static final char SPACE_CH         = ' ';  // U+0020   
  public static final char EXCLAM_CH        = '!';  // U+0021   (exclamation mark, factorial, bang)
  public static final char DQUOTE_CH        = '"';  // U+0022   (quotation mark)
  public static final char PERCENT_CH       = '%';  // U+0025   (percent sign)
  public static final char SQUOTE_CH        = '\''; // U+0027   (apostrophe)
  public static final char COMMA_CH         = ',';  // U+002C   (decimal separator)
  public static final char DOT_CH           = '.';  // U+002E   (full stop, period, decimal point)

  public static final String NEWLINE        = "\n";
  public static final String TAB            = "\t";

  public static final String COMMA          = ".";
  public static final String DQUOTE         = "\"";
  public static final String SQUOTE         = "'";
  public static final String DOT            = ".";
  public static final String HASH           = "#";
  public static final String MINUS          = "-";
  public static final String SPACE          = " ";
  public static final String BLANK          = SPACE;
  public static final String CURLY_OPEN     = "{";
  public static final String CURLY_CLOSE    = "}";
  
  public static final String EMPTY_STRING   = "";
  public static final String NULL_STRING    = "null";
  public static final String BOOLEAN_TRUE   = String.valueOf(true);
  public static final String BOOLEAN_FALSE  = String.valueOf(false);
  public static final String UTF8_STRING    = "UTF-8";

}
