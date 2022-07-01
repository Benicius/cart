package com.coffee.shop.starbux.cart.service;

import com.coffee.shop.starbux.cart.domains.Cart;
import com.coffee.shop.starbux.cart.domains.CartStatus;
import com.coffee.shop.starbux.cart.domains.ItemCart;
import com.coffee.shop.starbux.cart.domains.Product;
import com.coffee.shop.starbux.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart createCart(final String name,final boolean hasTopping, final Cart cart){
        /*validateItems(cart.getItems());
        validateName(name);*/

        Product productResponse = productService.getProduct(name, hasTopping);

        Cart cartTemp = lastCart();
        if (cartTemp.getId() == null){
            cartTemp = validatingHasTopping(hasTopping, productResponse, new Cart());
        } else if (!cartTemp.getItems().isEmpty() && cartTemp.getCartStatus() == CartStatus.PROGRESS){
            cartTemp = validatingHasTopping(hasTopping, productResponse, cartTemp);
        }

        return cartRepository.save(cartTemp);
    }

    private Cart lastCart(){
        Cart cart = new Cart();
        List<Cart> carts = cartRepository.findAll();

        if(!carts.isEmpty()){
            return carts.get(carts.size()-1);
        }
        return cart;
    }

    private Cart validatingHasTopping(final boolean hasTopping, final Product product, final Cart cart) {

        ItemCart item = new ItemCart();
        List<ItemCart> itemTemp = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        if(hasTopping){
            assert cart != null;
            List<ItemCart> items = cart.getItems();
            if(!items.isEmpty()){
                item =  items.get(items.size()-1);
                List<Product> productsTemp = item.getProducts();
                if(productsTemp.isEmpty()){
                    productsTemp.add(product);
                    item.setProducts(productsTemp);
                } else {
                    item.getProducts().add(product);
                }

                cart.getItems().add(item);
                return cart;
            }
        } else {
            products.add(product);
            item.setProducts(products);
            if (cart.getItems().isEmpty()){
                itemTemp.add(item);
                cart.setItems(itemTemp);
            } else {
                cart.getItems().add(item);
            }
        }


        return cart;
    }

    private void validateItems(final List<ItemCart> items) {
        if (ObjectUtils.isEmpty(items)){
            return;
        }
    }

    private void validateName(final String name){
        if (name.isEmpty()){
            return;
        }
    }


}
