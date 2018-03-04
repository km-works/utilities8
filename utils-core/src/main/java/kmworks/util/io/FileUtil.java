/*
 * Copyright (C) 2005-2016 Christian P. Lerch <christian.p.lerch[at]gmail.com>
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
package kmworks.util.io;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nonnull;

/**
 *
 * @author Christian P. Lerch
 */
public final class FileUtil {
    
    private FileUtil() {}
    
    public static final String PATH_ELEM_SEP = File.pathSeparator;
    
    public static String stripExtension(@Nonnull final String fileName) {
        final int pos = fileName.lastIndexOf('.');
        return pos<=0 ? fileName : fileName.substring(0, pos);
    }

    public static File createTempDir() {
        return Files.createTempDir();
    }

    public static void removeTempDir(@Nonnull File tempDir) {
        checkNotNull(tempDir);
        deleteDirTree(tempDir);
    }

    public static void copyFile(@Nonnull File srcFile, @Nonnull File dstFile) throws IOException {
        checkNotNull(srcFile);
        checkNotNull(dstFile);
        Files.copy(srcFile, dstFile);
    }

    public static byte[] readFileBytes(@Nonnull File file) {
        checkNotNull(file);
        try {
            return Files.toByteArray(file);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String[] getPathElements(@Nonnull String pathStr) {
        checkNotNull(pathStr);
        pathStr = pathStr.trim();
        Path path = Paths.get(pathStr);
        if (pathStr.startsWith(PATH_ELEM_SEP)) {
            pathStr = pathStr.substring(1);
        }
        if (pathStr.endsWith(PATH_ELEM_SEP)) {
            pathStr = pathStr.substring(0, pathStr.length() - 1);
        }
        return pathStr.split(PATH_ELEM_SEP);
    }

    public static String getFileHash(@Nonnull File file) {
        checkNotNull(file);
        final Hasher hasher = Hashing.md5().newHasher();
        try {
            hasher.putBytes(Files.toByteArray(file));
            final byte[] md5 = hasher.hash().asBytes();
            return BaseEncoding.base16().encode(md5).toLowerCase();
        } catch (IOException ex) {
            final String errMsg = "File not found: " + file.getAbsolutePath();
            throw new RuntimeException(errMsg, ex);
        }
    }

    public static boolean deleteDirTree(@Nonnull File rootDirOrFile) {
        checkNotNull(rootDirOrFile);
        for (File f : Files.fileTraverser().depthFirstPostOrder(rootDirOrFile)) {
            final boolean rc = f.delete();
            if (!rc) return false;
        }
        return true;
    }

}
