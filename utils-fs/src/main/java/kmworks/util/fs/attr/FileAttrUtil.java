/*
 *  Copyright (C) 2005-2018 Christian P. Lerch, Vienna, Austria.
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
package kmworks.util.fs.attr;

import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;
import java.util.Optional;
import kmworks.util.StringUtil;

/**
 *
 * @author cpl
 */
public final class FileAttrUtil {
    
    private FileAttrUtil() {}
    
    public static UserDefinedFileAttributeView getViewFor(Path path) {
        return Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
    }
    
    public static List<String> list(Path path) throws IOException {
        return list(getViewFor(path));
    }
    
    public static boolean exists(Path path, String attrName) throws IOException {
        return exists(getViewFor(path), attrName);
    }
    
    public static int size(Path path, String attrName) throws IOException {
        return size(getViewFor(path), attrName);
    }
    
    public static Optional<String> read(Path path, String attrName) throws IOException {
        return read(getViewFor(path), attrName);
    }
    
    public static void write(Path path, String attrName, String attrValue) throws IOException {
        write(getViewFor(path), attrName, attrValue);
    }
    
    public static void delete(Path path, String attrName) throws IOException {
        delete(getViewFor(path), attrName);
    }
    
    /*
        User-defined file attribute helpers
    */
    public static boolean exists(UserDefinedFileAttributeView view, String attrName) 
            throws IOException {
        return list(view).contains(attrName);
    }
    
    public static List<String> list(UserDefinedFileAttributeView view) throws IOException {
        return view.list();
    }
    
    public static int size(UserDefinedFileAttributeView view, String attrName) 
            throws IOException {
        return exists(view, attrName) ? view.size(attrName) : -1;
    }

    public static Optional<String> read(UserDefinedFileAttributeView view, String attrName) 
            throws IOException {
        if (exists(view, attrName)) {
            ByteBuffer buffer = ByteBuffer.allocate(view.size(attrName));
            view.read(attrName, buffer);
            buffer.flip();
            return Optional.of(StringUtil.Convert.fromByteBuffer(buffer, UTF_8));
        } else {
            return Optional.empty();
        }        
    }
    
    public static void write(UserDefinedFileAttributeView view, String attrName, String attrValue) 
            throws IOException {
        view.write(attrName, StringUtil.Convert.toByteBuffer(attrValue, UTF_8));
    }
    
    public static void delete(UserDefinedFileAttributeView view, String attrName) throws IOException {
        if (exists(view, attrName)) {
            view.delete(attrName);
        }
    }
    
}
