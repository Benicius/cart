package com.coffee.shop.starbux.cart.service;

import com.coffee.shop.starbux.cart.domains.*;
import com.coffee.shop.starbux.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
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

    public Cart createCart(final CartRequest cart){

        Cart cartTemp = lastCart();

        if (cart.getCartStatus() == null || cart.getCartStatus().equals(CartStatus.PROGRESS.toString())){

            Product productResponse = productService.getProduct(cart.getName(), cart.isHasTopping());

            if (cartTemp.getId() == null){
                cartTemp = validatingHasTopping(cart.isHasTopping(), productResponse, new Cart());
            } else if (!cartTemp.getItems().isEmpty()){
                cartTemp = validatingHasTopping(cart.isHasTopping(), productResponse, cartTemp);
            }

            List<ItemCart> itemCarts = calculateItemPrice(cartTemp.getItems());
            cartTemp.setItems(itemCarts);

            Integer cartQuantity = calculateCartQuantity(itemCarts, cart.getQuantity());
            cartTemp.setCartQuantity(cartQuantity);

            BigDecimal cartPrice = calculateCartPrice(itemCarts);
            cartTemp.setCartPrice(cartPrice);
        }
        if (cart.getCartStatus() != null){
            cartTemp.setCartStatus(CartStatus.valueOf(cart.getCartStatus()));
        }
        return cartRepository.save(cartTemp);
    }

    private Cart lastCart(){
        Cart cart = new Cart();
        List<Cart> carts = cartRepository.findAll();

        if(!carts.isEmpty()){
            cart = carts.get(carts.size()-1);
        }
        if (!cart.getCartStatus().equals(CartStatus.PROGRESS)){
            cart = new Cart();
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
                item.getProducts().add(product);
            }
        } else {
            products.add(product);
            item.setProducts(products);
            if (cart.getItems() == null || cart.getItems().isEmpty()){
                itemTemp.add(item);
                cart.setItems(itemTemp);
            } else {
                cart.getItems().add(item);
            }
        }
        return cart;
    }

    private List<ItemCart> calculateItemPrice(final List<ItemCart> items) {

        for (ItemCart item : items) {
            Double priceProduct = 0.00;
            List<Product> products = item.getProducts();
            for (Product product : products) {
                priceProduct += product.getPrice();
            }
            item.setPrice(BigDecimal.valueOf(priceProduct));
        }
        return items;
    }

    private BigDecimal calculateCartPrice(final List<ItemCart> itemCarts) {

        BigDecimal itemPrice = new BigDecimal(BigInteger.ZERO);
        for (ItemCart itemCart : itemCarts) {
            itemPrice = itemCart.getPrice()
                    .add(itemPrice)
                    .multiply(new BigDecimal(itemCart.getQuantity()));
        }

        return itemPrice;
    }

    private Integer calculateCartQuantity(final List<ItemCart> itemCarts, final Integer cartQuantity) {

        if(cartQuantity !=null){
            ItemCart item = itemCarts.get(itemCarts.size()-1);
            item.setQuantity(cartQuantity);
        }
        int quantity = 0;
        for (ItemCart itemCart : itemCarts) {
            quantity = quantity + itemCart.getQuantity();
        }
        return quantity;
    }
}
