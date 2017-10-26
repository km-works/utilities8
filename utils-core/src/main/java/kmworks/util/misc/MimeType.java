/*
 *   Copyright (C) 2012-2017 Christian P. Lerch, Vienna, Austria.
 *
 *   This file is part of kmworks-util - a versatile Java utilites library.
 *
 *   kmworks-util is free software: you can use, modify and redistribute it under
 *   the terms of the GNU General Public License version 3 as published by
 *   the Free Software Foundation, Inc. <http://fsf.org/>
 *
 *   kmworks-util is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *   version 3 for details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with this distribution. If not, see <http://km-works.eu/res/GPL.txt> or
 *   <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package kmworks.util.misc;

import kmworks.util.StringUtil;
import java.io.IOException;
import javax.annotation.Nonnull;
import kmworks.util.config.PropertyMap;
import kmworks.util.config.PropertyMapConverter;

/**
 *
 * * @author Christian P. Lerch
 */
public final class MimeType {

    private static final PropertyMap Ext_MimeType_Map = loadMimeTypes();

    private MimeType() { // prevent instantiation
    }

    public static String fromFileName(@Nonnull final String fileName) {
        final String ext = StringUtil.getFileExt(fileName);
        if (ext == null) {
            return null;
        }
        final String mimeType = Ext_MimeType_Map.getOrElse(ext, null);
        return mimeType;
    }

    private static PropertyMap loadMimeTypes() {
        
        java.util.Properties result = new java.util.Properties();
        try {
            result.load(MimeType.class.getClassLoader().getResourceAsStream("mime-types.properties"));
        } catch (IOException ex) {
            /* SHOULD NOT HAPPEN */
        }
        return PropertyMapConverter.fromProperties(result);
    }
}
