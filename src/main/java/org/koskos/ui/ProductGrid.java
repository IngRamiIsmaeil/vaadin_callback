package org.koskos.ui;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;
import org.koskos.be.FilterProduct;
import org.koskos.be.MySQL;
import org.koskos.be.Product;
import org.koskos.be.ProductServiceDataProvider;

import java.util.logging.Logger;


public class ProductGrid extends Grid<Product>{
    Logger LOG = Logger.getLogger(ProductGrid.class.getSimpleName());
    private final MySQL mySQL;

    private CallbackDataProvider<Product, FilterProduct> dataProvider = null;

    private ConfigurableFilterDataProvider<Product, Void, FilterProduct> filterDataProvider = null;

    private final boolean withProductServiceDataProvider;
    private final ProductServiceDataProvider productServiceDataProvider;

    public ProductGrid(MySQL mySQL, boolean withProductServiceDataProvider) {

        super(Product.class);

        this.withProductServiceDataProvider = withProductServiceDataProvider;

        this.mySQL = mySQL;

        if(!this.withProductServiceDataProvider){
            dataProvider = DataProvider.fromFilteringCallbacks(
                    q->{
                        final FilterProduct filter = q.getFilter().orElse(null);
                        LOG.info("dataProvider fetch was calling ... (parameter q.limit) with Value (" + q.getLimit()+") (parameter q.offset) with Value(" + q.getOffset()+") (filter) with " +
                                "value ("+filter+")");
                        return  mySQL.getProductFromUntil(q.getLimit(), q.getOffset(), filter).stream();
                    },
                    q-> (int) mySQL.getCountProducts(new FilterProduct()));

            filterDataProvider = dataProvider.withConfigurableFilter();

            this.setDataProvider(filterDataProvider);

            this.productServiceDataProvider = null;

        }else{

            LOG.info("Setting Filterable Data Provider to Grid in Build In Modus");
            this.productServiceDataProvider = new ProductServiceDataProvider(mySQL);
            if(null != this.getDataProvider()){
                LOG.warning("Found no nullable DataProvider before: isInMemory: " + this.getDataProvider().isInMemory());
            }

            this.setDataProvider(this.productServiceDataProvider);
        }



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
        if(!this.withProductServiceDataProvider){
            tf.addValueChangeListener((HasValue.ValueChangeListener<String>) event -> {
                LOG.info("TextField was calling ... (ValueChangeListener) with Value (" + event.getValue()+")");
                int filterIdValue = 0;
                if(!event.getValue().isEmpty()){
                    try{
                        filterIdValue = Integer.parseInt(event.getValue());
                    }catch(NumberFormatException ex){

                    }
                    filterDataProvider.setFilter(new FilterProduct(filterIdValue,0.0f, null));

                }
            });
        }else{
            tf.addValueChangeListener(this.productServiceDataProvider);
        }

        return tf;
    }
}
