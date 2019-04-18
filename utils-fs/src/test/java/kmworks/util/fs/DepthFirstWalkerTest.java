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

import java.nio.file.Path;
import java.nio.file.Paths;
import kmworks.util.fs.attr.FileAttributes;
import kmworks.util.misc.UUIDGenerator;
import org.junit.Test;

/**
 *
 * @author cpl
 */
public class DepthFirstWalkerTest {
    
    public DepthFirstWalkerTest() {
    }

    private void pln(String s) {
        System.out.println(s);
    }
    
    private void printStats(DepthFirstWalker.Statistics stats) {
        pln("Dirs visited   : " + stats.getDirsVisited());
        pln("Dirs walked    : " + stats.getDirsWalked());
        pln("Dirs processed : " + stats.getDirsProcessed());
        pln("    processable: " + stats.getDirsProcessable());
        pln("Files visited  : " + stats.getFilesVisited());
        pln("Files processed: " + stats.getFilesProcessed());
        pln("    processable: " + stats.getFilesProcessable());
        pln("Timing         : " + stats.getTiming() + " msec");        
    }
    
    @Test
    public void testSetFileID() {
        // Walk 
        Path rootDir = Paths.get("C:/_/data/d-a-c/knowledge/IT/TOPICS/SW-PROD");
        
        DepthFirstWalker<Void> walker = new DepthFirstWalker<>(rootDir);
        // dir-name starts with alphanumeric char
        walker.dirsInclude((path, level) -> path.getFileName().toString().matches("\\p{Alnum}.*"));
        // first-level dirs only
        walker.dirsExclude((path, level) -> level > 1); 
        // no files
        walker.filesExclude((path, level) -> level >= 1);

       walker.walk((Path path) -> { // type Runnable1<Path> worker
            FileAttributes fileAttrs = FileAttributes.of(path);
            if (!fileAttrs.exists("FILE_ID")) {
                fileAttrs.set("FILE_ID", UUIDGenerator.generate().toString());
            }
            fileAttrs.set("IS_A", path.getParent().getFileName().toString() /* == "SW-PROD" */);
            fileAttrs.close();
        });
        
        printStats(walker.statistics());
    }

    @Test
    public void testRenameAttr() {
        Path rootDir = Paths.get("C:/_/data/d-a-c/knowledge/IT/TOPICS/SW-PROD");
        String fromAttrName = "WWW_HOME";
        String toAttrName = "WWW";
        
        DepthFirstWalker<Void> walker = new DepthFirstWalker<>(rootDir);
        walker.dirsInclude((path, level) -> path.getFileName().toString().matches("\\p{Alnum}.*"));
        walker.dirsExclude((path, level) -> level > 1);
        walker.filesExclude((path, level) -> level >= 1);

        walker.walk((Path path) -> { // type Runnable1<Path> worker
            FileAttributes fileAttrs = FileAttributes.of(path);
            if (fileAttrs.exists(fromAttrName)) {
                String homeUrl = fileAttrs.get(fromAttrName);
                fileAttrs.delete(fromAttrName);
                fileAttrs.set(toAttrName, homeUrl);
            }
            fileAttrs.close();
        });
        
        printStats(walker.statistics());
    }

    @Test
    public void test_02() {
        Path rootDir = Paths.get("C:/Windows");
        DepthFirstWalker<Void> walker = new DepthFirstWalker(rootDir);
        walker.dirsExclude((path, level) -> level > 1);
        walker.walk();
        printStats(walker.statistics());
        // Performance: ~ 2.000 files/sec
    }

    @Test
    public void test_03() {
        Path rootDir = Paths.get("C:/_/dev/ws");
        DepthFirstWalker<Void> walker = new DepthFirstWalker(rootDir);
        walker.walk();
        printStats(walker.statistics());
        // Visited: ~ 28.000 files in 1.500 dirs
        // Timing: 7.4 sec
        // Performance: ~ 2.500 files/sec
    }
    
}
