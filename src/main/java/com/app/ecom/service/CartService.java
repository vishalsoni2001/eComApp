package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequest request) {

        //Looking for Product
       Optional<Product> productOpt=productRepository.findById(request.getProductId());
       if(productOpt.isEmpty())
       {
           return false;
       }

       //If stock exists
       Product product=productOpt.get();
       if(product.getStockQuantity() < request.getQuantity())
       {
           return false;
       }

       Optional<User> userOpt=userRepository.findById(Long.valueOf(userId));
       if(userOpt.isEmpty())
       {
           return false;
       }

       //If we have reached here means stock exists ,prod exists as well as user exists
       User user=userOpt.get();

       CartItem existingCartItem=cartItemRepository.findByUserAndProduct(user,product);

       if(existingCartItem!=null)
       {
           //update the quantity and save
           existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
           existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
           cartItemRepository.save(existingCartItem);
       }
       else
       {
           //create new cart item
           CartItem cartItem=new CartItem();
           cartItem.setUser(user);
           cartItem.setProduct(product);
           cartItem.setQuantity(request.getQuantity());
           cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
           cartItemRepository.save(cartItem);
       }

        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {

        Optional<Product> productOpt=productRepository.findById(productId);
        Optional<User> userOpt=userRepository.findById(Long.valueOf(userId));

        if(productOpt.isPresent() && userOpt.isPresent())
        {
            cartItemRepository.deleteByUserAndProduct(userOpt.get(),productOpt.get());
            return true;
        }

        return false;
    }

    public List<CartItem> getCart(String userId) {

        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }

    public void clearCart(String userId) {

        userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser

        );
    }
}
