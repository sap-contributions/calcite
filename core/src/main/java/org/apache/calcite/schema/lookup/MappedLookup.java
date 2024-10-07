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

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class MappedLookup<S,T> implements Lookup<T> {
  private final Lookup<S> lookup;
  private final BiFunction<S,String,T>  mapper;

  MappedLookup(Lookup<S> lookup, BiFunction<S,String,T> mapper) {
    this.lookup = lookup;
    this.mapper = mapper;
  }
  @Override
  public @Nullable T get(String name) {
    S entity = lookup.get(name);
    return entity == null ? null : mapper.apply(entity,name);
  }

  @Override
  public @Nullable Named<T> getIgnoreCase(String name) {
    Named<S> named = lookup.getIgnoreCase(name);
    return named == null ? null : new Named<>(named.name(),mapper.apply(named.entity(),named.name()));
  }

  @Override
  public @Nullable Set<String> getNames(LikePattern pattern) {
    return lookup.getNames(pattern);
  }
}