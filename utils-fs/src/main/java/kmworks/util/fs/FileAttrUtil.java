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

import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import kmworks.util.StringPool;
import kmworks.util.StringUtil;

/**
 *
 * @author cpl
 */
public final class FileAttrUtil {
    
    private FileAttrUtil() {}
    
    public static UserDefinedFileAttributeView userDefinedAttrView(Path path) {
        return Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
    }
    
    public static String userDefinedAttrValue(UserDefinedFileAttributeView view, String attrName) throws IOException {
        if (view.list().contains(attrName)) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(view.size(attrName));
            view.read(attrName, buffer);
            buffer.flip();
            return StringUtil.Convert.fromByteBuffer(buffer, UTF_8);
        } else {
            return StringPool.EMPTY_STRING;
        }
    }    
    
}
