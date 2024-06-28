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

package org.apache.calcite.schema.impl;

import com.google.common.base.Suppliers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.google.common.util.concurrent.UncheckedExecutionException;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.adapter.jdbc.JdbcTable;
import org.apache.calcite.linq4j.function.Predicate1;
import org.apache.calcite.schema.LikePattern;
import org.apache.calcite.schema.Lookup;
import org.apache.calcite.schema.Named;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.Table;
import org.apache.calcite.util.NameMap;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This class can be used to cache lookups
 * @param <T>
 */
public class CachingLookup<T> implements Lookup<T> {
  private final Lookup<T> delegate;

  private final LoadingCache<String, T> cache;
  private final LoadingCache<String, Named<T>> cacheIgnoreCase;

  public CachingLookup(Lookup<T> delegate){
    this.delegate = delegate;
    this.cache = CacheBuilder.newBuilder()
        .expireAfterAccess(1, TimeUnit.HOURS)
        .build(new CacheLoader<String, T>() {
          @Override
          public T load(String name) throws Exception {
            return Optional.ofNullable(delegate.get(name)).orElseThrow(() -> new EntryNotFoundException());
          }
        });
    this.cacheIgnoreCase = CacheBuilder.newBuilder()
        .expireAfterAccess(1, TimeUnit.HOURS)
        .build(new CacheLoader<String, Named<T>>() {
          @Override
          public Named<T> load(String name) throws Exception {
            return Optional.ofNullable(delegate.getIgnoreCase(name)).orElseThrow(() -> new EntryNotFoundException());
          }
        });
  }

  @Override
  public @Nullable T get(String name) {
    try {
      return cache.get(name);
    } catch ( UncheckedExecutionException e) {
      if ( e.getCause() instanceof EntryNotFoundException) {
        return null;
      }
      throw e;
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @Nullable Named<T> getIgnoreCase(String name) {
    try {
      return cacheIgnoreCase.get(name);
    } catch ( UncheckedExecutionException e) {
      if ( e.getCause() instanceof EntryNotFoundException) {
        return null;
      }
      throw e;
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Set<String> getNames(LikePattern pattern) {
    return delegate.getNames(pattern);
  }

  public void invalidate(String name) {
    cache.invalidate(name);
    cacheIgnoreCase.invalidate(name);
  }

  public void invalidateAll() {
    cache.invalidateAll();
    cacheIgnoreCase.invalidateAll();
  }
}
