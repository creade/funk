/*
 * Copyright (C) 2011 Funk committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.javafunk.funk;

import org.javafunk.funk.datastructures.TwoTuple;
import org.javafunk.funk.functors.Predicate;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.listWith;

public class EagerFilterRejectPartitionTest {
    @Test
    public void shouldOnlyReturnThoseElementsMatchingTheSuppliedPredicate() {
        // Given
        Iterable<Integer> inputs = listWith(1, 2, 3, 4, 5, 6);
        Collection<Integer> expectedOutput = listWith(2, 4, 6);

        // When
        Collection<Integer> actualOutput = Eager.filter(inputs, new Predicate<Integer>() {
            @Override
            public boolean evaluate(Integer item) {
                return isEven(item);
            }

            private boolean isEven(Integer item) {
                return item % 2 == 0;
            }
        });

        // Then
        assertThat(actualOutput, is(expectedOutput));
    }

    @Test
    public void shouldOnlyReturnThoseElementsThatDoNotMatchTheSuppliedPredicate() {
        // Given
        Iterable<Integer> inputs = listWith(1, 2, 3, 4, 5, 6);
        Collection<Integer> expectedOutput = listWith(1, 3, 5);

        // When
        Collection<Integer> actualOutput = Eager.reject(inputs, new Predicate<Integer>() {
            @Override
            public boolean evaluate(Integer item) {
                return isEven(item);
            }

            private boolean isEven(Integer item) {
                return item % 2 == 0;
            }
        });

        // Then
        assertThat(actualOutput, is(expectedOutput));
    }

    @Test
    public void shouldReturnTheMatchingElementsFirstAndTheNonMatchingElementsSecond() {
        // Given
        Iterable<String> input = listWith("a", "b", "c", "d", "e", "f", "g", "h");
        Collection<String> expectedMatchingItems = listWith("a", "b", "c", "d");
        Collection<String> expectedNonMatchingItems = listWith("e", "f", "g", "h");

        // When
        TwoTuple<Collection<String>, Collection<String>> partitionResults = Eager.partition(input,
                new Predicate<String>() {
                    public boolean evaluate(String item) {
                        return item.compareTo("e") < 0;
                    }
                });

        // Then
        Collection<String> actualMatchingItems = partitionResults.first();
        Collection<String> actualNonMatchingItems = partitionResults.second();

        assertThat(actualMatchingItems, is(expectedMatchingItems));
        assertThat(actualNonMatchingItems, is(expectedNonMatchingItems));
    }
}