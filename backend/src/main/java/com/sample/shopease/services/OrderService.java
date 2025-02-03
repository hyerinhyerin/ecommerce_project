package com.sample.shopease.services;

import com.sample.shopease.auth.entities.User;
import com.sample.shopease.dto.OrderRequest;
import com.sample.shopease.entities.*;
import com.sample.shopease.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ProductService productService;

  @Transactional
  public Order createOrder(OrderRequest orderRequest, Principal principal) throws Exception{
    User user = (User) userDetailsService.loadUserByUsername(principal.getName());
    Address address = (Address) user.getAddressList().stream().filter(address1 -> orderRequest.getAddressId().equals(address1.getId())).findFirst().orElseThrow(BadRequestException::new);

    Order order = Order.builder()
            .user(user)
            .address(address)
            .totalAmount(orderRequest.getTotalAmount())
            .orderDate(orderRequest.getOrderDate())
            .discount(orderRequest.getDiscount())
            .expectedDeliveryDate(orderRequest.getExpectedDeliveryDate())
            .paymentMethod(orderRequest.getPaymentMethod())
            .orderStatus(OrderStatus.PENDING)
            .build();

    List<OrderItem> orderItems = orderRequest.getOrderItemRequests().stream().map(orderItemRequest -> {
      try {
        Product product = productService.fetchProductById(orderItemRequest.getProductId());

//        ProductVariant productVariant = product.getProductVariants()
//                .stream()
//                .filter(pv -> pv.getId() == orderItemRequest.getProductVariantId())
//                .findFirst()
//                .orElseThrow(BadRequestException::new);

        return OrderItem.builder()
                .product(product)
                .productVariantId(orderItemRequest.getProductVariantId())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).toList();

    order.setOrderItemList(orderItems);

    Payment payment = new Payment();
    payment.setPaymentStatus(PaymentStatus.PENDING);
    payment.setPaymentDate(new Date());
    payment.setOrder(order);
    payment.setAmount(order.getTotalAmount());
    payment.setPaymentMethod("");
    order.setPayment(payment);

    return orderRepository.save(order);
  }
}
