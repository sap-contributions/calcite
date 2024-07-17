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

import org.apache.calcite.linq4j.function.Predicate1;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SimpleLookup<T> extends IgnoreCaseLookup<T> {

  private final Function<String,T> get;
  private final Supplier<Set<String>> getNames;

  public SimpleLookup(Function<String,T> get, Supplier<Set<String>> getNames) {
    this.get = get;
    this.getNames = getNames;
  }

  @SuppressWarnings("deprecation")
  @Nullable
  @Override
  public T get(String name) {
    return get.apply(name);
  }

  @SuppressWarnings("deprecation")
  @Override
  public @Nullable Set<String> getNames(LikePattern pattern) {
    final Predicate1<String> matcher = pattern.matcher();
    return getNames.get().stream()
        .filter(name -> matcher.apply(name))
        .collect(Collectors.toSet());
  }
}
