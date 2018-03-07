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

package org.apache.ignite.internal.client.impl;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import junit.framework.TestCase;
import org.apache.ignite.internal.client.GridClientCacheFlag;
import org.apache.ignite.internal.client.impl.connection.GridClientConnection;
import org.apache.ignite.internal.processors.rest.handlers.cache.GridCacheCommandHandler;
import org.apache.ignite.internal.util.typedef.F;

import static org.apache.ignite.internal.client.GridClientCacheFlag.KEEP_BINARIES;

/**
 * Tests conversions between GridClientCacheFlag.
 */
public class ClientCacheFlagsCodecTest extends TestCase {
    /**
     * Tests that each client flag will be correctly converted to server flag.
     */
    public void testEncodingDecodingFullness() {
        for (GridClientCacheFlag f : GridClientCacheFlag.values()) {
            int bits = GridClientConnection.encodeCacheFlags(EnumSet.of(f));

            assertTrue(bits != 0);

            Set<GridClientCacheFlag> out = GridCacheCommandHandler.parseCacheFlags(bits);

            assertTrue(out.contains(f));
        }
    }

    /**
     * Tests that groups of client flags can be correctly converted to corresponding server flag groups.
     */
    public void testGroupEncodingDecoding() {
        // All
        doTestGroup(GridClientCacheFlag.values());

        // None
        doTestGroup();
    }

    /**
     * @param flags Client flags to be encoded, decoded and checked.
     */
    private void doTestGroup(GridClientCacheFlag... flags) {
        EnumSet<GridClientCacheFlag> flagSet = F.isEmpty(flags)
            ? EnumSet.noneOf(GridClientCacheFlag.class)
            : EnumSet.copyOf(Arrays.asList(flags));

        int bits = GridClientConnection.encodeCacheFlags(flagSet);

        Set<GridClientCacheFlag> out = GridCacheCommandHandler.parseCacheFlags(bits);

        assertTrue(out.containsAll(flagSet));
    }
}
