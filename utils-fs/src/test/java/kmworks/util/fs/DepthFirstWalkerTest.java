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
        pln("Dirs visited:    " + stats.getDirsVisited());
        pln("Dirs walked:     " + stats.getDirsWalked());
        pln("Files visited:   " + stats.getFilesVisited());
        pln("Files processed: " + stats.getFilesProcessed());
        pln("    processable: " + stats.getFilesProcessable());
        pln("Timing:         " + stats.getTiming() + " msec");        
    }
    
    @Test
    public void test_01() {
        Path rootDir = Paths.get("D:\\mirrors\\EARTH3\\slow_archive\\d-a-c\\assets-docs");
        DepthFirstWalker walker = new DepthFirstWalker(rootDir);
        walker.dirsExclude((path, level) -> level == 1 && !path.toString().endsWith("Telefon"));
        walker.filesExclude((path, level) -> level == 1);
        walker.walk((Path path, Object ctx) -> {
            pln(path.toString());
        }, null);
        printStats(walker.statistics());
    }

    @Test
    public void test_02() {
        Path rootDir = Paths.get("C:\\Windows");
        DepthFirstWalker walker = new DepthFirstWalker(rootDir);
        walker.walk();
        printStats(walker.statistics());
        // Performance: ~ 2.000 files/sec
    }

    @Test
    public void test_03() {
        Path rootDir = Paths.get("C:\\_\\dev\\ws");
        DepthFirstWalker walker = new DepthFirstWalker(rootDir);
        walker.walk();
        printStats(walker.statistics());
        // Visited: ~ 28.000 files in 1.500 dirs
        // Timing: 7.4 sec
        // Performance: ~ 2.500 files/sec
    }
    
}
