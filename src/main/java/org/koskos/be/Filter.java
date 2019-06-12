package org.koskos.be;

import java.util.function.Predicate;

public interface Filter extends Predicate <Product> {
    // boolean test(Number n); - provided by predicate
}
