package org.koskos;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.koskos.be.MySQL;
import org.koskos.be.Product;
import org.koskos.ui.ProductGrid;
import org.koskos.ui.ProductGridJPA;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();
        ProductGridJPA components = new ProductGridJPA();
        components.setSizeFull();
        layout.addComponent(components);

        /*ProductGrid productGrid = new ProductGrid(new MySQL(), false);
        productGrid.setSizeFull();
        layout.addComponents(productGrid);*/
        /*ProductGrid productGrid2 = new ProductGrid(new MySQL(), true);
        productGrid2.setSizeFull();
        layout.addComponent(productGrid2);*/
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
