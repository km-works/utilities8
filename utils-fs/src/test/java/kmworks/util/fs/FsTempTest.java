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
package kmworks.util.fs;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import kmworks.util.StringUtil;
import static kmworks.util.fs.FileAttrUtil.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cpl
 */
public class FsTempTest {
    
    public FsTempTest() {
    }
    
    private void pln(String s) {
        System.out.println(s);
    }
    
    @Test
    public void test1() throws Exception {
        Path path = Paths.get("D:\\temp\\test","txt1.txt");
        assertTrue(path.toFile().exists());
        pln("\nFile: " + path.toString());
        pln("  UD-Attributes: ");
        
        UserDefinedFileAttributeView view = userDefinedAttrView(path);
        for (String attrName : view.list()) {
            pln("    " + attrName + ": " + userDefinedAttrValue(view, attrName));
        }
        
        String attrName = "FILE_ID";
        String attrVal = "12345678";
        view.write(attrName, StringUtil.Convert.toByteBuffer(attrVal, UTF_8));    //StandardCharsets.UTF_8.encode(attrVal1)
    }
    
}
