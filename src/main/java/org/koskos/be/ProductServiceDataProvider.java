package org.koskos.be;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.*;
import com.vaadin.shared.Registration;
import com.vaadin.shared.data.sort.SortDirection;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Class, which implements DataBackend Provider(needed with Predicate) we can extends every component to it
 * Note we implement Filter TextField ValueChange here to take affect with declared DataProvider
 */
@SuppressWarnings("serial")
public class ProductServiceDataProvider implements BackEndDataProvider<Product, ProductFilter>,
        HasValue.ValueChangeListener<String> {
    Logger LOG = Logger.getLogger(ProductServiceDataProvider.class.getSimpleName());
    // this will be set to Component as DataProvider(with configured Filter mechanism)
    private ConfigurableFilterDataProvider<Product, ProductFilter, ProductFilter> dProv;


    // Constructor with service. we can inject too here
    public ProductServiceDataProvider(MySQL nServ) {

        initDataProvider (nServ);
    }

    private void initDataProvider (MySQL nServ) {
        // fetch Functionality
        final CallbackDataProvider.FetchCallback<Product, ProductFilter> fcb = (CallbackDataProvider.FetchCallback<Product, ProductFilter>)
                query -> {
                    LOG.info("Fetch Callback with offset {"+query.getOffset()+"} & with limit {"+query.getLimit()+"} & with filter predicate {"+query.getFilter()+"} & with sort {"+query.getSortOrders()+"}");
                    return nServ.getProductFromUntil(query.getOffset(), query.getLimit(), getSorter(query.getSortOrders()), getFilter(query.getFilter())).stream();
                };

        final CallbackDataProvider.CountCallback<Product, ProductFilter> ccb = (CallbackDataProvider.CountCallback<Product, ProductFilter>)
                query -> nServ.getCountProducts(getFilter(query.getFilter()));

        final CallbackDataProvider<Product, ProductFilter> cpdProv = new CallbackDataProvider(fcb, ccb);

        dProv = cpdProv.withConfigurableFilter((t, u) -> u);
    }

    private Comparator<Product> getSorter(List<QuerySortOrder> sortOrders) {

        QuerySortOrder sort = sortOrders.size() > 0 ? sortOrders.get(0) : null;
        if(null == sort)
            return null;
        int ascending = sort != null && sort.getDirection() == SortDirection.DESCENDING ? -1 : 1;

        return (o1, o2) -> o1.getId() - o2.getId() * ascending;
    }

    private ProductFilter getFilter(Optional<ProductFilter> f) {

        ProductFilter filter;
        if (f.isPresent())
            filter = new ProductFilter (f.get());
        else
            filter = new ProductFilter (null);
        return filter;
    }

    @Override
    public int size(Query<Product, ProductFilter> query) {
        return dProv.size(query);
    }

    @Override
    public Stream<Product> fetch(Query<Product, ProductFilter> query) {
        return dProv.fetch(query);
    }

    @Override
    public void refreshItem(Product item) {
        dProv.refreshItem(item);
    }

    @Override
    public void refreshAll() {
        dProv.refreshAll();
    }

    @Override
    public Registration addDataProviderListener(DataProviderListener<Product> listener) {
        return dProv.addDataProviderListener(listener);
    }

    @Override
    public void valueChange(HasValue.ValueChangeEvent<String> event) {
        final String componentId = event.getComponent().getId();

        if(!componentId.equals("id"))
            return;

        String filterText = event.getValue ();
        if(!isNumeric(filterText))
            return;

        Predicate <Product> filter = t -> {
            if (t.getId() == Integer.valueOf(filterText))
                return true;
            else
                return false;
        };

        dProv.setFilter(new ProductFilter (filter));
    }

    @Override
    public void setSortOrders(List<QuerySortOrder> sortOrders) {

    }

    private boolean isNumeric(final String value){
        if(null == value || value.trim().isEmpty())
            return false;
        for(char c : value.toCharArray())
            if(!Character.isDigit(c))
                return false;
        return true;
    }

    public ConfigurableFilterDataProvider<Product, ProductFilter, ProductFilter> getdProv() {
        return dProv;
    }
}