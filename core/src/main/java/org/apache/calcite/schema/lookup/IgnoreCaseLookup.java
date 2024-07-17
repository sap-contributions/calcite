/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.schema.lookup;

import org.apache.calcite.schema.lookup.LikePattern;
import org.apache.calcite.schema.lookup.Lookup;
import org.apache.calcite.schema.lookup.Named;
import org.apache.calcite.util.NameMap;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * An abstract base class for lookups. implementing case insensitive lookup
 *
 */
public abstract class IgnoreCaseLookup<T> implements Lookup<T> {

  private NameMap<String> nameMap;

  public IgnoreCaseLookup() {
  }

  /**
   * Returns a named entity with a given name, or null if not found.
   *
   * @param name Name
   * @return Entity, or null
   */
  @Nullable public abstract T get(String name) ;

  /**
   * Returns a named entity with a given name ignoring the case, or null if not found.
   *
   * @param name Name
   * @return Entity, or null
   */
  @Override @Nullable  public Named<T> getIgnoreCase(String name){
    Map.Entry<String, String> entry = getNameMap(false).range(name, false).firstEntry();
    if (entry == null) {
       entry = getNameMap(true).range(name, false).firstEntry();
       if ( entry == null) {
         return null;
       }
    }
    T result = get(entry.getValue());
    return result == null ? null : new Named<>(entry.getKey(),result);
  }

  @Nullable public abstract Set<String> getNames(LikePattern pattern);

  private NameMap<String> getNameMap(boolean forceReload) {
    if ( nameMap == null || forceReload) {
      synchronized (this) {
        if ( nameMap == null || forceReload) {
          NameMap<String> tmp = new NameMap<>();
          for (String name : getNames(LikePattern.any())) {
            tmp.put(name, name);
          }
          nameMap = tmp;
        }
      }
    }
    return nameMap;
  }
}
