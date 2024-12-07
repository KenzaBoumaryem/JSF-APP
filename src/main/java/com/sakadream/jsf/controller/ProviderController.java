package com.sakadream.jsf.controller;

import com.sakadream.jsf.bean.Provider;
import com.sakadream.jsf.bean.ProductProvider;
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

    private List<Provider> providers = new ArrayList<>();
    private Provider selectedProvider;
    private Map<Long, Boolean> selectedProducts = new HashMap<>();

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

    // Ajoutez les produits sélectionnés au panier
    public void addToCart(ProductProvider product) {
        if (cart.stream().noneMatch(p -> p.getId() == product.getId())) {
            cart.add(product);
        }
    }

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

            // Add selected products to the cart
            for (ProductProvider product : selected) {
                if (cart.stream().noneMatch(p -> p.getId() == product.getId())) {
                    cart.add(product);
                }
            }
        }

        // Clear selection
        selectedProducts.clear();
    }




    // Getters et Setters
    public List<ProductProvider> getCart() {
        return cart;
    }

    public void setCart(List<ProductProvider> cart) {
        this.cart = cart;
    }

}
