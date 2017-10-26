/*
 *  Copyright (C) 2005-2017 Christian P. Lerch, Vienna, Austria.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 3 of the License, or (at your option)
 *  any later version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.config;

import java.io.StringReader;
import kmworks.util.parsing.ParseError;
import kmworks.util.parsing.ParseResult;
import kmworks.util.parsing.SemanticValue;
import kmworks.util.parsing.SimpleParserState;

/**
 *
 * @author cpl
 */
public class PropertyValueJsonParser {

    private final SimpleParserState ps;

    public PropertyValueJsonParser(String source) {
        ps = new SimpleParserState(new StringReader(source), source.length());
    }

}
