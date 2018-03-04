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

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author cpl
 */
public abstract class AbstractFileAttributes implements FileAttributes {
    
    private final Path path;
    private final boolean isFile;
    private boolean isClosed;
    
    protected final Map<String, FileAttr> attributes = new HashMap<>();
    
    protected AbstractFileAttributes(Path path) {
        this.path = path;
        this.isFile = path.toFile().isFile();
        this.isClosed = false;
    }
    
    abstract void load();
    
    public Path path() {
        return path;
    }
    
    public boolean isFilePath() {
        return isFile;
    }
    
    @Override
    public Set<String> keys() {
        return attributes.keySet();
    }
    
    @Override
    public Set<Map.Entry<String, FileAttr>> entries() {
        return attributes.entrySet();
    }

    @Override
    public boolean exists(String key) {
        FileAttr attr = attributes.get(key);
        return attr != null && !attr.isDeleted();
    }

    @Override
    public String get(String key) {
        FileAttr attr = attributes.get(key);
        return attr != null ? attr.get() : null;
    }

    @Override
    public void delete(String key) {
        checkNotClosed();
        FileAttr attr = attributes.get(key);
        if (attr != null && !attr.isDeleted()) {
            attr.set(null);
        }
    }

    @Override
    public void set(String key, String value) {
        checkNotClosed();
        FileAttr attr = attributes.get(key);
        if (attr == null) {
            attributes.put(key, new FileAttr(value));
        } else {
            attr.set(value);
        }
    }

    protected void setClosed() {
        this.isClosed = true;
    }
    
    protected void checkNotClosed() {
        if (this.isClosed) {
            throw new RuntimeException("FileAttributes already closed");
        }
    }
}
