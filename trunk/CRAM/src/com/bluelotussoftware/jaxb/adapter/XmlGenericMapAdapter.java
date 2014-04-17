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
 * $Id: XmlGenericMapAdapter.java 399 2011-12-03 04:22:50Z jyeary $
 */
package com.bluelotussoftware.jaxb.adapter;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 * @version 1.0
 */
public class XmlGenericMapAdapter<K, V> extends XmlAdapter<MapType<K, V>, Map<K, V>> {

    @Override
    public Map<K, V> unmarshal(MapType<K, V> v) throws Exception {
        HashMap<K, V> map = new HashMap<K, V>();

        for (MapEntryType<K, V> mapEntryType : v.getEntry()) {
            map.put(mapEntryType.getKey(), mapEntryType.getValue());
        }
        return map;
    }

    @Override
    public MapType marshal(Map<K, V> v) throws Exception {
        MapType<K, V> mapType = new MapType<K, V>();

        for (Map.Entry<K, V> entry : v.entrySet()) {
            MapEntryType<K, V> mapEntryType = new MapEntryType<K, V>();
            mapEntryType.setKey(entry.getKey());
            mapEntryType.setValue(entry.getValue());
            mapType.getEntry().add(mapEntryType);
        }
        return mapType;
    }
}
