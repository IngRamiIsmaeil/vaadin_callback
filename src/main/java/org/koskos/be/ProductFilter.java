package org.koskos.be;

import com.vaadin.server.SerializablePredicate;

import java.util.function.Predicate;

// implementation of Filter als Predicate<Typed with Product>
public class ProductFilter implements Filter, SerializablePredicate<Product> {

    private Predicate<Product> predicate;

    ProductFilter(Predicate <Product> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(Product n) {
        if (predicate != null)
            return predicate.test(n);
        else
            return true;
    }
}