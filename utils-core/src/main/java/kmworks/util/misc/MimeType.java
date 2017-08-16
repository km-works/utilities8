/*
 *   Copyright (C) 2012 Christian P. Lerch, Vienna, Austria.
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
import java.util.Properties;

/**
 *
 * * @author Christian P. Lerch <christian.p.lerch[at]gmail.com>
 */
public final class MimeType {

  private static final Properties Ext_MimeType_Map = loadMimeTypes();

  private MimeType() { // prevent instantiation
  }

  public static String fromFileName(final String fileName) {
    final String ext = StringUtil.getFileExt(fileName);
    if (ext == null) {
      return null;
    }
    final String mimeType = Ext_MimeType_Map.getProperty(ext, null);
    return mimeType;
  }

  private static Properties loadMimeTypes() {
    final Properties result = new Properties();
    try {
      result.load(MimeType.class.getClassLoader().getResourceAsStream("mime-types.properties"));
    } catch (IOException ex) { /* SKIP */ }
    return result;
  }
}

