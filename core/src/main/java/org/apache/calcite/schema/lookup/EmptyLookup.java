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

import com.google.common.collect.ImmutableSet;

import org.apache.calcite.schema.lookup.LikePattern;
import org.apache.calcite.schema.lookup.Lookup;
import org.apache.calcite.schema.lookup.Named;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

/**
 * An abstract base class for lookups. implementing case insensitive lookup
 *
 */
class EmptyLookup<T> implements Lookup<T> {

  static Lookup<?> INSTANCE = new EmptyLookup<>();

  @Override
  public @Nullable T get(String name) {
    return null;
  }

  @Override
  public @Nullable Named<T> getIgnoreCase(String name) {
    return null;
  }

  @Override
  public Set<String> getNames(LikePattern pattern) {
    return ImmutableSet.of();
  }
}
