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
package kmworks.util.io;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cpl
 */
public class FileUtilTest {
    
    public FileUtilTest() {
    }

    @Test
    public void testStripExtension_01() {
        assertEquals(".gitignore", FileUtil.stripExtension(".gitignore"));
    }

    @Test
    public void testStripExtension_02() {
        assertEquals("abc", FileUtil.stripExtension("abc.gitignore"));
    }

    @Test
    public void testStripExtension_03() {
        assertEquals("abc", FileUtil.stripExtension("abc"));
    }

    private static final String TEST_ROOT = "C:/_/dev/testing/deleteDirTree";

    @Test
    public void testDeleteDirTree00() {
        final File rootDirOrFile = new File(TEST_ROOT, "test00.txt");
        doDeleteDirTreeTest(rootDirOrFile);
    }
    
    @Test
    public void testDeleteDirTree01() {
        final File rootDirOrFile = new File(TEST_ROOT, "test01");
        doDeleteDirTreeTest(rootDirOrFile);
    }
    
    @Test
    public void testDeleteDirTree02() {
        final File rootDirOrFile = new File(TEST_ROOT, "test02");
        doDeleteDirTreeTest(rootDirOrFile);
    }
    
    @Test
    public void testDeleteDirTree03() {
        final File rootDirOrFile = new File(TEST_ROOT, "test03");
        doDeleteDirTreeTest(rootDirOrFile);
    }
    
    private static void doDeleteDirTreeTest(File rootDirOrFile) {
        assertTrue(rootDirOrFile.exists());
        assertTrue(FileUtil.deleteDirTree(rootDirOrFile));
        assertFalse(rootDirOrFile.exists());        
    }
            
}
