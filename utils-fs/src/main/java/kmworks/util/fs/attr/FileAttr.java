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

import javax.annotation.Nullable;

/**
 *
 * @author cpl
 */
public class FileAttr {
    
    private String value;
    private boolean isModified;
    
    public FileAttr(@Nullable String value) {
        this.value = value;
        isModified = false;
    }
    
    public String get() {
        return this.value;
    }
    
    public void set(@Nullable String value) {
        this.value = value;
        isModified = true;
    }
    
    public void setDeleted() {
        this.value = null;
        isModified = true;
    }
    
    public boolean isDeleted() {
        return value ==  null;
    }
    
    public boolean isModified() {
        return isModified;
    }
}
