package net.sf.saxon.charcode;

import java.util.Arrays;

/*

Copyright (C) 2006 Hewlett-Packard Development Company, L.P.

The contents of this file are subject to the Mozilla Public License Version 1.1
(the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
the specific language governing rights and limitations under the License.

The Original Code is: all this file
The Initial Developer of the Original Code is Lauren Ward. All Rights Reserved.
Contributor(s): Integrated into Saxon by Michael Kay

*************************
Author:
  Lauren Ward
Date:
  February 01, 2006
Address:
  Hewlett-Packard Company
  3404 East Harmony Road
  Fort Collins, CO 80528-9599
Revision:
  1.0 - Initial creation

Description: This class implements the PluggableCharacterSet to support
iso-8859-9 encoding.  The character mapping was obtained by extracting the
Unicode values from an iconv character table (iso89=ucs2) available on HP-UX 11.23.

The class was tested by transforming a document with ISO-8859-9
set as the output encoding, converting ISO-8859-9 output to utf-8 using iconv,
and then comparing converted content to the same transformed document with utf-8 set
as the output encoding.

*/


public class ISO88599CharacterSet implements CharacterSet {

    private static ISO88599CharacterSet INSTANCE = new ISO88599CharacterSet();
    
    private ISO88599CharacterSet() {}

    public static ISO88599CharacterSet getInstance() {
        return INSTANCE;
    }

	private static final boolean C[];

	static {
		C = new boolean[360];

//		for (int i=0; i<=25; ++i) { c[i] = true; }
//		for (int i=27; i<=207; ++i) { c[i] = true; }
//		for (int i=209; i<=220; ++i) { c[i] = true; }
//		for (int i=223; i<=239; ++i) { c[i] = true; }
//		for (int i=241; i<=252; ++i) { c[i] = true; }
//		c[255] = true;
//		for (int i=286; i<=287; ++i) { c[i] = true; }
//		for (int i=304; i<=305; ++i) { c[i] = true; }
//		for (int i=350; i<=351; ++i) { c[i] = true; }

        Arrays.fill(C, 0, 256, true);
        C[26] = false;
        C[208] = false;
        C[221] = false;
        C[222] = false;
        C[240] = false;
        C[253] = false;
        C[254] = false;
        C[286] = true;
        C[287] = true;
        C[304] = true;
        C[305] = true;
        C[350] = true;
        C[351] = true;
	}

	// Determine if it is a valid character
    @Override
	public final boolean inCharset(int ch) {
		return ch < 360 && C[ch];
	}

	public final String getEncodingName() {
		// Canonical Name for java.io and java.lang API
		return "ISO8859_9";
	}
}


