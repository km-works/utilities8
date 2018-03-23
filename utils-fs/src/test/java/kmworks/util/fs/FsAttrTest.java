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

import java.nio.file.FileSystems;
import java.nio.file.Path;
import kmworks.util.fs.attr.FileAttributes;
import kmworks.util.misc.UUIDGenerator;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author cpl
 */
public class FsAttrTest {
    
    private static final Path FILE_PATH = FileSystems.getDefault().getPath("C:\\_\\dev\\TEMP\\test", "txt1.txt");
    private static final Path DIR_PATH = FileSystems.getDefault().getPath("C:\\_\\dev\\TEMP\\test\\");
    private static final String ATTR_NAME = "FILE_ID";

    public FsAttrTest() {
    }

    private void pln(String s) {
        System.out.println(s);
    }
    
    @Test
    public void testFileAttributes_01() throws Exception {
        String attrVal = UUIDGenerator.generate().toString();
        FileAttributes fileAttrs = FileAttributes.of(FILE_PATH);
        fileAttrs.set(ATTR_NAME, attrVal);
        fileAttrs.close();
        FileAttributes fileAttrs2 = FileAttributes.of(FILE_PATH);
        String readVal = fileAttrs2.get(ATTR_NAME);
        fileAttrs2.close();
        assertEquals(attrVal, readVal);
    }

    @Test
    public void testDirAttributes_01() {
        String attrVal = UUIDGenerator.generate().toString();
        FileAttributes fileAttrs = FileAttributes.of(DIR_PATH);
        fileAttrs.set(ATTR_NAME, attrVal);
        fileAttrs.close();
        FileAttributes fileAttrs2 = FileAttributes.of(DIR_PATH);
        String readVal = fileAttrs2.get(ATTR_NAME);
        fileAttrs2.close();
        assertEquals(attrVal, readVal);
    }

}
