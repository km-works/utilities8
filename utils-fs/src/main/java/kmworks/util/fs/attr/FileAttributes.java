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
import java.util.Map;
import java.util.Set;

/**
 *
 * @author cpl
 */
public interface FileAttributes {

    static FileAttributes of(Path path) {
        if (path.toFile().isFile()) {
            return new FileAttributesImpl(path);
        } else {
            return new DirAttributesImpl(path);
        }
    }

    boolean exists(String key);

    Set<String> keys();
    
    Set<Map.Entry<String, FileAttr>> entries();

    void delete(String key);

    String get(String key);

    void set(String key, String value);

    void close();
}
