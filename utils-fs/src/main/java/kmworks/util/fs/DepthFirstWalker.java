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

import static com.google.common.base.Preconditions.*;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import javax.annotation.Nonnull;
import kmworks.util.config.PropertyMap;
import static kmworks.util.config.PropertyMap.kv;
import kmworks.util.lambda.Function2;
import kmworks.util.lambda.LambdaUtil;
import kmworks.util.lambda.Predicate2;
import kmworks.util.lambda.Runnable1;

/**
 *
 * @author cpl
 */
public final class DepthFirstWalker<T> {
    
    public static final String KEY$FOLLOW_SYMBOLIC_LINKS = "follow-symbolic-links";
    public static final String KEY$WALK_HIDDEN_DIRS = "walk-hidden-dirs";
    public static final String KEY$PROCESS_HIDDEN_FILES = "process-hidden-files";
    
    private final Path rootDir;
    private PropertyMap properties = PropertyMap.of(Arrays.asList(
            kv(KEY$FOLLOW_SYMBOLIC_LINKS, false),
            kv(KEY$WALK_HIDDEN_DIRS, false),
            kv(KEY$PROCESS_HIDDEN_FILES, false)
    ));
    private Predicate2<Path, Integer> dirsIncluded = (path, level) -> true;
    private Predicate2<Path, Integer> dirsExcluded = (path, level) -> false;
    private Predicate2<Path, Integer> dirConsidered;
    private Predicate2<Path, Integer> filesIncluded = (path, level) -> true;
    private Predicate2<Path, Integer> filesExcluded = (path, level) -> false;
    private Predicate2<Path, Integer> fileConsidered;
    private Function2<Path, T, T> fileProcessor = null;
    private T processingResult = null;
    private Statistics statistics;
    
    public DepthFirstWalker(@Nonnull Path rootDir) {
        this(rootDir, null);
    }
    public DepthFirstWalker(@Nonnull Path rootDir, PropertyMap properties) {
        checkNotNull(rootDir);
        if (!rootDir.toFile().isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + rootDir.toString());
        }
        this.rootDir = rootDir;
        this.properties = PropertyMap.merge(this.properties, properties);
    }
    
    public DepthFirstWalker dirsInclude(Predicate2<Path, Integer> filter) {
        this.dirsIncluded = filter;
        return this;
    }
    
    public DepthFirstWalker dirsExclude(Predicate2<Path, Integer> filter) {
        this.dirsExcluded = filter;
        return this;
    }
    
    public DepthFirstWalker filesInclude(Predicate2<Path, Integer> filter) {
        this.filesIncluded = filter;
        return this;
    }
    
    public DepthFirstWalker filesExclude(Predicate2<Path, Integer> filter) {
        this.filesExcluded = filter;
        return this;
    }
    
    public void walk() {
        walk(null, null);
    }
    
    public void walk(Runnable1<Path> fileProcessor) {
        walk((Path arg1, T arg2) -> {
            fileProcessor.run(arg1);
            return null;
        }, null);
    }
    
    public void walk(Function2<Path, T, T> fileProcessor, T initialProcessorContext) {
        this.fileProcessor = fileProcessor;
        this.processingResult = initialProcessorContext;
        this.dirConsidered = LambdaUtil.and(dirsIncluded, LambdaUtil.negate(dirsExcluded));
        this.fileConsidered = LambdaUtil.and(filesIncluded, LambdaUtil.negate(filesExcluded));
        this.statistics  = new Statistics();
        walking(rootDir, 0);
        statistics.incDirsWalked(1);
        statistics.finish();
    }
    
    public T getResult() {
        return processingResult;
    }
    
    public Statistics statistics() {
        return statistics;
    }
    
    private void walking(Path path, int level) {
        try (DirectoryStream<Path> content = Files.newDirectoryStream(path)) {
            for (Path entry : content) {  // alphabetically ordered by item.getFileName
                if (isDirectory(entry)) {
                    statistics.incDirsVisited(1);
                    if (canProcessFile(entry, level+1, dirConsidered)) {
                        statistics.incDirsProcessable(1);
                        if (fileProcessor != null) {
                            T tempResult = fileProcessor.apply(entry, processingResult);
                            if (tempResult != null) processingResult = tempResult;
                            statistics.incDirsProcessed(1);
                        }
                    }
                    if (canWalkDirectory(entry, level+1)) {
                        walking(entry, level+1);
                        statistics.incDirsWalked(1);
                    }
                }
                if (isRegularFile(entry)) {
                    statistics.incFilesVisited(1);
                    if (canProcessFile(entry, level+1, fileConsidered)) {
                        statistics.incFilesProcessable(1);
                        if (fileProcessor != null) {
                            T tempResult = fileProcessor.apply(entry, processingResult);
                            if (tempResult != null) processingResult = tempResult;
                            statistics.incFilesProcessed(1);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // IOException can never be thrown by the iteration, but only by newDirectoryStream
        }        
    }
    
    private boolean isDirectory(Path path) {
        return properties.getAsBoolean(KEY$FOLLOW_SYMBOLIC_LINKS)
                ? Files.isDirectory(path)
                : Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
    }
    
    private boolean isRegularFile(Path path) {
        return properties.getAsBoolean(KEY$FOLLOW_SYMBOLIC_LINKS)
                ? Files.isRegularFile(path)
                : Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS);
    }
    
    private boolean canWalkDirectory(Path path, int level) {
        final File dir = path.toFile();
        try {
             return dir.canRead() 
                     && dirConsidered.test(path, level) 
                     && (!dir.isHidden() || dir.isHidden() && properties.getAsBoolean(KEY$PROCESS_HIDDEN_FILES));
        } catch (Exception ex) {
            return false;
        }
    }
    
    private boolean canProcessFile(Path path, int level, Predicate2<Path, Integer> filesConsidered) {
        final File file = path.toFile();
        try {
            return file.canRead() 
                    && filesConsidered.test(path, level) 
                    && (!file.isHidden() || file.isHidden() && properties.getAsBoolean(KEY$PROCESS_HIDDEN_FILES));
        } catch (Exception ex) {
            return false;
        }
    }
    
    public static class Statistics {
        
        private final long startTime;
        private long endTime = 0;
        private int filesVisited = 0;
        private int filesProcessable = 0;
        private int filesProcessed = 0;
        private int dirsVisited = 0;
        private int dirsProcessable = 0;
        private int dirsProcessed = 0;
        private int dirsWalked = 0;
        
        private Statistics() {
            startTime = System.currentTimeMillis();
        }
        
        public void incFilesVisited(int inc) {
            filesVisited += inc;
        }
        
        public void incFilesProcessed(int inc) {
            filesProcessed += inc;
        }
        
        public void incFilesProcessable(int inc) {
            filesProcessable += inc;
        }
        
        public void incDirsVisited(int inc) {
            dirsVisited += inc;
        }
        
        public void incDirsProcessed(int inc) {
            dirsProcessed += inc;
        }
        
        public void incDirsProcessable(int inc) {
            dirsProcessable += inc;
        }
        
         public void incDirsWalked(int inc) {
            dirsWalked += inc;
        }
        
        public void finish() {
            endTime = System.currentTimeMillis();
        }

        public int getFilesVisited() {
            return filesVisited;
        }

        public int getFilesProcessable() {
            return filesProcessable;
        }

        public int getFilesProcessed() {
            return filesProcessed;
        }

        public int getDirsVisited() {
            return dirsVisited;
        }
        
        public int getDirsProcessable() {
            return dirsProcessable;
        }

        public int getDirsProcessed() {
            return dirsProcessed;
        }

        public int getDirsWalked() {
            return dirsWalked;
        }
        
        public long getTiming() {
            endTime = System.currentTimeMillis();
            return endTime - startTime;
        }
    }
}
