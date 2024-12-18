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

import org.apache.calcite.util.NameMap;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;

/**
 * This class can be used to make a snapshot of a lookups.
 *
 * @param <T> Element Type
 */
public class CachedLookup<T> implements Lookup<T> {

  private final Lookup<T> delegate;
  private Lookup<T> cachedDelegate = null;
  private boolean enabled = true;

  public CachedLookup(Lookup<T> delegate) {
    this.delegate = delegate;
  }

  @Override public @Nullable T get(final String name) {
    return delegate().get(name);
  }

  @Override public @Nullable Named<T> getIgnoreCase(final String name) {
    return delegate().getIgnoreCase(name);
  }

  @Override public Set<String> getNames(final LikePattern pattern) {
    return delegate().getNames(pattern);
  }

  private Lookup<T> delegate() {
    if (!enabled) {
      return delegate;
    }
    if (cachedDelegate == null) {
      synchronized (this) {
        if (cachedDelegate == null) {
          NameMap<T> map = new NameMap<>();
          for (String name : delegate.getNames(LikePattern.any())) {
            map.put(name, delegate.get(name));
          }
          cachedDelegate = new NameMapLookup<>(map);
        }
      }
    }
    return cachedDelegate;
  }

  public void enable(boolean enabled) {
    if (!enabled) {
      cachedDelegate = null;
    }
    this.enabled = enabled;
  }

}
