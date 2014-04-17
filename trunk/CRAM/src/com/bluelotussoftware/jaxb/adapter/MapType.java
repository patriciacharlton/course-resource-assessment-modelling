/*
 * Copyright 2011 John Yeary <jyeary@bluelotussoftware.com>.
 * Copyright 2011 Bluelotus Software, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: MapType.java 399 2011-12-03 04:22:50Z jyeary $
 */
package com.bluelotussoftware.jaxb.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 * @version 1.0
 */
public class MapType<K, V> {

    private List<MapEntryType<K, V>> entry = new ArrayList<MapEntryType<K, V>>();

    public MapType() {
    }

    public MapType(Map<K, V> map) {
        for (Map.Entry<K, V> e : map.entrySet()) {
            entry.add(new MapEntryType<K, V>(e));
        }
    }

    public List<MapEntryType<K, V>> getEntry() {
        return entry;
    }

    public void setEntry(List<MapEntryType<K, V>> entry) {
        this.entry = entry;
    }
}
