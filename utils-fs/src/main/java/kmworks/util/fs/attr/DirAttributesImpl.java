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

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 * @author cpl
 */
public final class DirAttributesImpl extends AbstractFileAttributes {

    private static final String METADATA_FILENAME = ".metadata";
    private static final String METADATA_KV_SEP = ": ";

    private final File metadataFile;

    public DirAttributesImpl(Path dirPath) {
        super(dirPath);
        metadataFile = new File(dirPath.toFile(), METADATA_FILENAME);
        load();
    }

    @Override
    void load() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(metadataFile), StandardCharsets.UTF_8))) {
            in.lines().forEach(line -> {
                List<String> kv = Splitter.on(METADATA_KV_SEP).limit(2).splitToList(line);
                attributes.put(kv.get(0), new FileAttr(kv.get(1)));
            });
        } catch (Exception ex) {
        }
    }

    @Override
    public void close() {
        checkNotClosed();
        Map<String, String> attrsOut = ImmutableMap.copyOf(
                entries().stream()
                        .filter(e -> !e.getValue().isDeleted())
                        .map(e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey(), e.getValue().get()))
                        .collect(Collectors.toList())
        );
        write(attrsOut);
        setClosed();
    }

    private void write(Map<String, String> attrs) {
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(metadataFile), StandardCharsets.UTF_8))) {
            Set<String> keys = new TreeSet(attrs.keySet());
            for (String key : keys) {
                out.write(String.format("%s%s%s\n", key, METADATA_KV_SEP, attrs.get(key)));
            }
        } catch (Exception ex) {
        }
    }

}
