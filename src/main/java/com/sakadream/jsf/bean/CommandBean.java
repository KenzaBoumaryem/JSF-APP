package com.sakadream.jsf.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@ManagedBean
@RequestScoped
public class CommandBean {

    private String selectedProviderId;
    private String commandName;
    private List<ProductOrder> productList = new ArrayList<ProductOrder>();
    private List<Provider> providers;
    private List<Product> products;

    public CommandBean() {
        providers = fetchProviders();
        products = fetchProducts();
        productList.add(new ProductOrder(new Product(), 0,1)); // Initialize with one empty product order
    }

    // Getters and setters
    public String getSelectedProviderId() { return selectedProviderId; }
    public void setSelectedProviderId(String selectedProviderId) { this.selectedProviderId = selectedProviderId; }

    public String getCommandName() { return commandName; }
    public void setCommandName(String commandName) { this.commandName = commandName; }

    public List<ProductOrder> getProductList() { return productList; }
    public List<Provider> getProviders() { return providers; }
    public List<Product> getProducts() { return products; }

    // Add a new empty product order
    public void addProductOrder() {
        productList.add(new ProductOrder(new Product(), 0,1));
    }

    // Submit the command with selected products and quantities
    public String submitCommand() {
        // Process the command and save it
        return "confirmation";
    }

    private List<Provider> fetchProviders() {
        return Arrays.asList(
                new Provider("1", "Provider A"),
                new Provider("2", "Provider B")
        );
    }

    private List<Product> fetchProducts() {
        return Arrays.asList(
                new Product(1, "Product X", "Description X", 10.0, 100),
                new Product(2, "Product Y", "Description Y", 20.0, 200)
        );
    }

    // Inner classes for Provider and ProductOrder
    public static class Provider {
        private String id;
        private String name;

        public Provider(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }
        public String getName() { return name; }
    }

    // Class to represent a product and its quantity in an order
    public static class ProductOrder {
        private Product product;
        private int quantity;
        private int productId;

        public ProductOrder(Product product, int quantity,int productId) {
            this.product = product;
            this.quantity = quantity;
            this.productId = productId;
        }

        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
