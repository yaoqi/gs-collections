/*
 * Copyright 2014 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.block.factory;

import java.util.Comparator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.impl.block.function.checked.CheckedFunction2;
import com.gs.collections.impl.block.function.checked.ThrowingFunction2;

/**
 * Contains factory methods for creating {@link Function2} instances.
 */
public final class Functions2
{
    private static final Function2<Integer, Integer, Integer> INTEGER_ADDITION = new IntegerAddition();

    private Functions2()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T, V, P> Function2<T, P, V> fromFunction(Function<? super T, ? extends V> function)
    {
        return new FunctionAdapter<T, P, V>(function);
    }

    public static <T, V, P> Function2<T, P, V> throwing(ThrowingFunction2<T, P, V> throwingFunction2)
    {
        return new ThrowingFunction2Adapter<T, P, V>(throwingFunction2);
    }

    public static Function2<Integer, Integer, Integer> integerAddition()
    {
        return INTEGER_ADDITION;
    }

    public static <T> Function2<T, T, T> min(Comparator<? super T> comparator)
    {
        return new MinFunction2<T>(comparator);
    }

    public static <T> Function2<T, T, T> max(Comparator<? super T> comparator)
    {
        return new MaxFunction2<T>(comparator);
    }

    public static <T, V extends Comparable<? super V>> Function2<T, T, T> minBy(Function<? super T, ? extends V> function)
    {
        return new MinByFunction2<T, V>(function);
    }

    public static <T, V extends Comparable<? super V>> Function2<T, T, T> maxBy(Function<? super T, ? extends V> function)
    {
        return new MaxByFunction2<T, V>(function);
    }

    private static final class FunctionAdapter<T, P, V> implements Function2<T, P, V>
    {
        private static final long serialVersionUID = 1L;
        private final Function<? super T, ? extends V> function;

        private FunctionAdapter(Function<? super T, ? extends V> function)
        {
            this.function = function;
        }

        public V value(T each, P parameter)
        {
            return this.function.valueOf(each);
        }
    }

    private static class IntegerAddition implements Function2<Integer, Integer, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer aggregate, Integer value)
        {
            return aggregate + value;
        }
    }

    private static final class ThrowingFunction2Adapter<T, P, V> extends CheckedFunction2<T, P, V>
    {
        private static final long serialVersionUID = 1L;
        private final ThrowingFunction2<T, P, V> throwingFunction2;

        private ThrowingFunction2Adapter(ThrowingFunction2<T, P, V> throwingFunction2)
        {
            this.throwingFunction2 = throwingFunction2;
        }

        public V safeValue(T argument1, P argument2) throws Exception
        {
            return this.throwingFunction2.safeValue(argument1, argument2);
        }
    }

    private static final class MinFunction2<T> implements Function2<T, T, T>
    {
        private static final long serialVersionUID = 1L;
        private final Comparator<? super T> comparator;

        private MinFunction2(Comparator<? super T> comparator)
        {
            this.comparator = comparator;
        }

        public T value(T argument1, T argument2)
        {
            return this.comparator.compare(argument1, argument2) > 0 ? argument2 : argument1;
        }
    }

    private static final class MaxFunction2<T> implements Function2<T, T, T>
    {
        private static final long serialVersionUID = 1L;
        private final Comparator<? super T> comparator;

        private MaxFunction2(Comparator<? super T> comparator)
        {
            this.comparator = comparator;
        }

        public T value(T argument1, T argument2)
        {
            return this.comparator.compare(argument1, argument2) < 0 ? argument2 : argument1;
        }
    }

    private static final class MinByFunction2<T, V extends Comparable<? super V>> implements Function2<T, T, T>
    {
        private static final long serialVersionUID = 1L;
        private final Function<? super T, ? extends V> function;

        private MinByFunction2(Function<? super T, ? extends V> function)
        {
            this.function = function;
        }

        public T value(T argument1, T argument2)
        {
            V first = this.function.valueOf(argument1);
            V second = this.function.valueOf(argument2);
            return first.compareTo(second) > 0 ? argument2 : argument1;
        }
    }

    private static final class MaxByFunction2<T, V extends Comparable<? super V>> implements Function2<T, T, T>
    {
        private static final long serialVersionUID = 1L;
        private final Function<? super T, ? extends V> function;

        private MaxByFunction2(Function<? super T, ? extends V> function)
        {
            this.function = function;
        }

        public T value(T argument1, T argument2)
        {
            V first = this.function.valueOf(argument1);
            V second = this.function.valueOf(argument2);
            return first.compareTo(second) < 0 ? argument2 : argument1;
        }
    }
}
