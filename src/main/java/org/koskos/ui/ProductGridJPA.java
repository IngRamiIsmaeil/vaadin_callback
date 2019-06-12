package org.koskos.ui;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;
import org.koskos.be.DB_Product_Entity;
import org.koskos.be.FilterProduct;
import org.koskos.be.JPAMySql;

import java.util.List;
import java.util.logging.Logger;

public class ProductGridJPA extends Grid<DB_Product_Entity>{

    private final static transient Logger LOG = Logger.getLogger(ProductGrid.class.getSimpleName());

    private final JPAMySql jpaMySql = JPAMySql.getInstance();

    private CallbackDataProvider<DB_Product_Entity, FilterProduct> dataProvider = null;

    private ConfigurableFilterDataProvider<DB_Product_Entity, Void, FilterProduct> filterDataProvider = null;

    public ProductGridJPA() {
        super(DB_Product_Entity.class);

        dataProvider = DataProvider.fromFilteringCallbacks(
                q->{
                    // fetch
                    final List<QuerySortOrder> sortOrders = q.getSortOrders();
                    final FilterProduct filter = q.getFilter().orElse(null);
                    final int limit = q.getLimit();
                    final int offset = q.getOffset();
                    if(null == filter || filter.getFilterById()<= 0)
                        return jpaMySql.getAllProductList(offset,limit).stream();
                    else
                        return jpaMySql.getAllProductsListById(filter.getFilterById(),offset, limit).stream();
                },
                q->{
                    // count
                    final List<QuerySortOrder> sortOrders = q.getSortOrders();
                    final FilterProduct filter = q.getFilter().orElse(null);
                    final int limit = q.getLimit();
                    final int offset = q.getOffset();
                    if(null == filter || filter.getFilterById()<= 0)
                        return jpaMySql.getCountProducts();
                    else
                        return jpaMySql.getCountProductsById(filter.getFilterById());
                }
        );
        filterDataProvider = dataProvider.withConfigurableFilter();

        this.setDataProvider(filterDataProvider);
        initialize();
    }

    private void initialize(){
        final HeaderRow headerRow = this.addHeaderRowAt(1);
        final HeaderCell id = headerRow.getCell("id");
        id.setComponent(createSearchTFID());
    }

    private TextField createSearchTFID() {
        final TextField tf = new TextField("");
        tf.setId("id");
        tf.addStyleName(ValoTheme.TEXTAREA_TINY);
        tf.addStyleName(ValoTheme.TEXTAREA_ALIGN_CENTER);
        tf.setValueChangeMode(ValueChangeMode.LAZY);
        tf.addValueChangeListener((HasValue.ValueChangeListener<String>) event -> {
            LOG.info("TextField was calling ... (ValueChangeListener) with Value (" + event.getValue()+")");
            int filterIdValue = 0;
            if(!event.getValue().isEmpty()){
                try{
                    filterIdValue = Integer.parseInt(event.getValue());
                }catch(NumberFormatException ex){
                    filterIdValue = 0;
                }
            }
            else{
                filterIdValue = 0;
            }
            filterDataProvider.setFilter(new FilterProduct(filterIdValue,0.0f, null));
        });

        return tf;
    }
}
