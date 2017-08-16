package net.sf.saxon.charcode;

import java.util.Arrays;

/**
 * This class defines properties of the cp1250 Central Europe character set,
 * as defined at <a href="http://www.microsoft.com/globaldev/reference/sbcs/1250.htm">http://www.microsoft.com/globaldev/reference/sbcs/1250.htm</a>.
 * @author Michael Kay, with advice from Jirka Kocek
 */

public class CP1250CharacterSet implements CharacterSet{

    public static final CP1250CharacterSet INSTANCE = new CP1250CharacterSet();

    private CP1250CharacterSet() {}

    public static CP1250CharacterSet getInstance() {
        return INSTANCE;
    }

    private static final boolean[] C;

    static {

        C = new boolean[740];

//        for (int i=0; i<127; i++) {
//            c[i] = true;
//        }
        Arrays.fill(C, 0, 127, true);
//        for (int i=128; i<740; i++) {
//            c[i] = false;
//        }
        Arrays.fill(C, 127, 740, false);

        C[160] = true;
        C[164] = true;
        C[166] = true;
        C[167] = true;
        C[168] = true;
        C[169] = true;
        C[171] = true;
        C[172] = true;
        C[173] = true;
        C[174] = true;
        C[176] = true;
        C[177] = true;
        C[180] = true;
        C[181] = true;
        C[182] = true;
        C[183] = true;
        C[184] = true;
        C[187] = true;
        C[193] = true;
        C[194] = true;
        C[196] = true;
        C[199] = true;
        C[201] = true;
        C[203] = true;
        C[205] = true;
        C[206] = true;
        C[211] = true;
        C[212] = true;
        C[214] = true;
        C[215] = true;
        C[218] = true;
        C[220] = true;
        C[221] = true;
        C[223] = true;
        C[225] = true;
        C[226] = true;
        C[228] = true;
        C[231] = true;
        C[233] = true;
        C[235] = true;
        C[237] = true;
        C[238] = true;
        C[243] = true;
        C[244] = true;
        C[246] = true;
        C[247] = true;
        C[250] = true;
        C[252] = true;
        C[253] = true;
        C[258] = true;
        C[259] = true;
        C[260] = true;
        C[261] = true;
        C[262] = true;
        C[263] = true;
        C[268] = true;
        C[269] = true;
        C[270] = true;
        C[271] = true;
        C[272] = true;
        C[273] = true;
        C[280] = true;
        C[281] = true;
        C[282] = true;
        C[283] = true;
        C[313] = true;
        C[314] = true;
        C[317] = true;
        C[318] = true;
        C[321] = true;
        C[322] = true;
        C[323] = true;
        C[324] = true;
        C[327] = true;
        C[328] = true;
        C[336] = true;
        C[337] = true;
        C[340] = true;
        C[341] = true;
        C[344] = true;
        C[345] = true;
        C[346] = true;
        C[347] = true;
        C[350] = true;
        C[351] = true;
        C[352] = true;
        C[353] = true;
        C[354] = true;
        C[355] = true;
        C[356] = true;
        C[357] = true;
        C[366] = true;
        C[367] = true;
        C[368] = true;
        C[369] = true;
        C[377] = true;
        C[378] = true;
        C[379] = true;
        C[380] = true;
        C[381] = true;
        C[382] = true;
        C[711] = true;
        C[728] = true;
        C[729] = true;
        C[731] = true;
        C[733] = true;
    }

    @Override
    public final boolean inCharset(int ch) {
        return (ch < 740 && C[ch]) ||
                (ch > 8210 && (
                    ch==8211 ||
                    ch==8212 ||
                    ch==8216 ||
                    ch==8217 ||
                    ch==8218 ||
                    ch==8220 ||
                    ch==8221 ||
                    ch==8222 ||
                    ch==8224 ||
                    ch==8225 ||
                    ch==8226 ||
                    ch==8230 ||
                    ch==8240 ||
                    ch==8249 ||
                    ch==8250 ||
                    ch==8364 ||
                    ch==8482    ));
    }

}
//
// The contents of this file are subject to the Mozilla Public License Version 1.0 (the "License");
// you may not use this file except in compliance with the License. You may obtain a copy of the
// License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the License for the specific language governing rights and limitations under the License.
//
// The Original Code is: all this file.
//
// The Initial Developer of the Original Code is Michael H. Kay
//
// Portions created by (your name) are Copyright (C) (your legal entity). All Rights Reserved.
//
// Contributor(s): none.
//
