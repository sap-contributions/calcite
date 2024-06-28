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
package org.apache.calcite.schema;

import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.rel.type.RelProtoDataType;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * A case sensitive/insensitive lookup for tables or functions
 *
 */
public interface Lookup<T> {
  /**
   * Returns a named entity with a given name, or null if not found.
   *
   * @param name Name
   * @return Entity, or null
   */
  @Nullable  T get(String name) ;

  /**
   * Returns a named entity with a given name ignoring the case, or null if not found.
   *
   * @param name Name
   * @return Entity, or null
   */
  @Nullable  Named<T> getIgnoreCase(String name) ;

  /**
   * Returns the names of the entities in matching pattern.
   *
   * @return Names of the entities
   */
  Set<String> getNames(LikePattern pattern);

  static <T> Named<T> get(Lookup<T> lookup, String name, boolean caseSensitive) {
    if ( caseSensitive) {
      T entry = lookup.get(name);
      if ( entry == null ) {
        return null;
      }
      return new Named<T>(name,entry);
    }
    return lookup.getIgnoreCase(name);
  }
}
