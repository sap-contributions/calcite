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

package org.apache.calcite.linq4j;

import org.apache.calcite.linq4j.function.Function1;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MergeJoinNotNullEnumerable<T, K> extends AbstractEnumerable<T> {
  private final Enumerable<T> enumerable;
  private final Function1<T, K> keySelector;
  private boolean skipNulls = true;

  private MergeJoinNotNullEnumerable(Enumerable<T> enumerable, Function1<T, K> keySelector) {
    this.enumerable = enumerable;
    this.keySelector = keySelector;
  }

  public static <T, K> MergeJoinNotNullEnumerable<T, K> create(Enumerable<T> enumerable, Function1<T, K> keySelector) {
    return new MergeJoinNotNullEnumerable<>(enumerable, keySelector);
  }

  @Override
  public Enumerator<T> enumerator() {
    Enumerator<T> enumerator = enumerable.enumerator();
    return new Enumerator<T>() {
      @Override
      public T current() {
        return enumerator.current();
      }

      @Override
      public boolean moveNext() {
        boolean next = enumerator.moveNext();
        if (!skipNulls) {
          return next;
        }
        while (next) {
          K key = keySelector.apply(enumerator.current());
          if (key != null) {
            if (key instanceof Object[]) {
              if(Arrays.stream((Object[]) key).noneMatch(Objects::isNull)) {
                break;
              }
            } else if (key instanceof List) {
              if (((List<?>) key).stream().noneMatch(Objects::isNull)) {
                break;
              }
            } else {
              break;
            }
          }
          next = enumerator.moveNext();
        }
        skipNulls = false;
        return next;
      }

      @Override
      public void reset() {
        enumerator.reset();
      }

      @Override
      public void close() {
        enumerator.close();
      }
    };
  }
}
