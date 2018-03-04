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
package kmworks.util.fs.attr;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Set;
import kmworks.util.StringUtil;

/**
 *
 * @author cpl
 */
public final class FileAttributesImpl extends AbstractFileAttributes {

    private final UserDefinedFileAttributeView view;

    public FileAttributesImpl(Path filePath) {
        super(filePath);
        view = FileAttrUtil.getViewFor(filePath);
        load();
    }

    @Override
    void load() {
        try {
            Set<String> keys = Sets.newHashSet(view.list());
            for (String key : keys) {
                String value = readAttr(key);
                attributes.put(key, new FileAttr(value));
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error listing file attributes from: " + path().toString(), ex);
        }
    }

    @Override
    public void close() {
        checkNotClosed();
        try {
            for (String key : keys()) {
                FileAttr attr = attributes.get(key);
                if (attr.isDeleted()) {
                    view.delete(key);
                } else {
                    if (attr.isModified()) {
                        view.write(key, StringUtil.Convert.toByteBuffer(attr.get(), UTF_8));
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            setClosed();
        }
    }

    private String readAttr(String key) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(view.size(key));
            view.read(key, buffer);
            buffer.flip();
            return StringUtil.Convert.fromByteBuffer(buffer, UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file attribute " + key + " from: " + path().toString(), ex);
        }
    }

}
