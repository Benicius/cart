package com.coffee.shop.starbux.cart.service;

import com.coffee.shop.starbux.cart.domains.Cart;
import com.coffee.shop.starbux.cart.domains.CartStatus;
import com.coffee.shop.starbux.cart.domains.ItemCart;
import com.coffee.shop.starbux.cart.domains.Product;
import com.coffee.shop.starbux.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
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

        Product productResponse = productService.getProduct(name, hasTopping);
        Cart cartTemp = lastCart();

        if (cartTemp.getId() == null){
            cartTemp = validatingHasTopping(hasTopping, productResponse, new Cart());
        } else if (!cartTemp.getItems().isEmpty() && cartTemp.getCartStatus() == CartStatus.PROGRESS){
            cartTemp = validatingHasTopping(hasTopping, productResponse, cartTemp);
        }

        List<ItemCart> itemCarts = calculateItemPrice(cartTemp.getItems());

        cartTemp.setItems(itemCarts);
        BigDecimal cartPrice = calculateCartPrice(itemCarts);
        cartTemp.setCartPrice(cartPrice);

        Integer cartQuantity = calculateCartQuantity(itemCarts);
        cartTemp.setCartQuantity(cartQuantity);

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

    private BigDecimal calculateCartPrice(List<ItemCart> itemCarts) {

        BigDecimal itemPrice = new BigDecimal(BigInteger.ZERO);
        for (ItemCart itemCart : itemCarts) {
            itemPrice = itemCart.getPrice().add(itemPrice);
        }

        return itemPrice;
    }

    private Integer calculateCartQuantity(List<ItemCart> itemCarts) {

        int quantity = 0;
        for (ItemCart itemCart : itemCarts) {
            quantity = quantity + itemCart.getQuantity();
        }
        return quantity;
    }
}
