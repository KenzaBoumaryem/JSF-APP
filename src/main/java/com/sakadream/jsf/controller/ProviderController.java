package com.sakadream.jsf.controller;

import com.sakadream.jsf.bean.Provider;
import com.sakadream.jsf.bean.ProductProvider;
import com.sakadream.jsf.service.CommandService;
import com.sakadream.jsf.service.ProviderService;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@SessionScoped
public class ProviderController {

    @EJB
    private ProviderService providerService;
    @EJB
    private CommandService commandService;
    private List<Provider> providers = new ArrayList<>();
    private Provider selectedProvider;
    private Map<Long, Boolean> selectedProducts = new HashMap<>();
    private Map<Long, Integer> selectedQuantities = new HashMap<>();

    public List<Provider> loadProviders() {
        providers = providerService.getAllProviders();
        return providers;
    }

    public void selectProvider(Provider provider) {
        selectedProvider = provider;
        selectedProducts.clear(); // Réinitialiser la sélection précédente
    }

    // Getters et Setters
    public List<Provider> getProviders() {
        return providers;
    }

    public Provider getSelectedProvider() {
        return selectedProvider;
    }

    public void setSelectedProvider(Provider selectedProvider) {
        this.selectedProvider = selectedProvider;
    }

    public Map<Long, Boolean> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(Map<Long, Boolean> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    private List<ProductProvider> cart = new ArrayList<>();



    // Supprimez un produit du panier
    public void removeFromCart(ProductProvider product) {
        cart = cart.stream()
                .filter(p -> p.getId() != product.getId())
                .collect(Collectors.toList());
    }

    // Méthode existante pour soumettre la sélection
    public void submitSelection() {
        if (selectedProvider != null) {
            List<ProductProvider> selected = selectedProvider.getProducts().stream()
                    .filter(product -> selectedProducts.getOrDefault(product.getId(), false))
                    .collect(Collectors.toList());

            // Add selected products to the cart with their quantity
            for (ProductProvider product : selected) {
                Integer quantity = selectedQuantities.getOrDefault(product.getId(), 1);

                if (quantity <= 0) {
                    quantity = 1;
                }

                if (cart.stream().noneMatch(p -> p.getId() == product.getId())) {
                    // Add product to cart with selected quantity and provider
                    cart.add(new ProductProvider((long) product.getId(), product.getName(), product.getDescription(),
                            product.getPrice(), quantity, selectedProvider));
                }
            }
        }

        selectedProducts.clear();
        selectedQuantities.clear();
    }

    public void confirmOrder() {
            if (cart != null && !cart.isEmpty()) {
                for (ProductProvider product : cart) {
                    commandService.saveCommand(product);
                }
                cart.clear();
            }
    }

    public Map<Long, Integer> getSelectedQuantities() {
        return selectedQuantities;
    }

    public void setSelectedQuantities(Map<Long, Integer> selectedQuantities) {
        this.selectedQuantities = selectedQuantities;
    }
    // Getters et Setters
    public List<ProductProvider> getCart() {
        return cart;
    }
    public void setCart(List<ProductProvider> cart) {
        this.cart = cart;
    }

}
