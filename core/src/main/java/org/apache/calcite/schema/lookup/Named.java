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

import java.security.InvalidParameterException;

public class Named<T> {
  private final String name;
  private final T entity;

  public Named(String name, T entity) {
    this.name = name;
    if (entity == null) {
      throw new InvalidParameterException("table");
    }
    this.entity = entity;
  }

  public final String name() {
    return name;
  }

  public final T entity() {
    return entity;
  }

  public static <T> T entity(Named<T> named) {
    return named == null ? null : named.entity;
  }
}
