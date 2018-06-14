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
package kmworks.util.internal.commons.lang3;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.Writer;
import kmworks.util.ArrayUtil;
import kmworks.util.strings.StringTransformer;

/**
 * Executes a sequence of translators one after the other. Execution ends whenever 
 * the first translator consumes codepoints from the input.
 */
public class AggregateTranslator extends CharSequenceTranslator {

    private final StringTransformer[] translators;

    /**
     * Specify the translators to be used at creation time. 
     *
     * @param translators CharSequenceTranslator array to aggregate
     */
    public AggregateTranslator(final StringTransformer... translators) {
        this.translators = ArrayUtil.clone(translators);
    }

    /**
     * The first translator to consume codepoints from the input is the 'winner'. 
     * Execution stops with the number of consumed codepoints being returned. 
     * {@inheritDoc}
     */
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        for (final StringTransformer translator : translators) {
            final int consumed = translator.translate(input, index, out);
            if(consumed != 0) {
                return consumed;
            }
        }
        return 0;
    }

}
