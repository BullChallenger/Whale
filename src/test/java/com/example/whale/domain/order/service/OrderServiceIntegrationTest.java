package com.example.whale.domain.order.service;

import static com.example.whale.fixture.OrderFixture.returnPurchaseOrderDTO;
import static com.example.whale.fixture.ProductFixture.returnProductRegisterDTOValue;
import static com.example.whale.fixture.ShopFixture.returnRegisterShopRequestDTO;
import static com.example.whale.fixture.UserFixture.returnSignUpRequestDTO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.product.service.ProductService;
import com.example.whale.domain.shop.service.ShopService;
import com.example.whale.domain.user.service.UserService;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class OrderServiceIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void initialize() {
        userService.signUp(returnSignUpRequestDTO());
        shopService.register(returnRegisterShopRequestDTO());
    }

    @Test
    @Transactional
    @DisplayName(value = "[성공]_주문_생성")
    void createPurchaseOrder() {
        // given
        Product product = productService.register(returnProductRegisterDTOValue());

        // when
        Order purchaseOrder = orderService.createPurchaseOrder(returnPurchaseOrderDTO(product.getProductId()));

        // then
        assertThat(purchaseOrder.getTotalAmountOfOrder().compareTo(product.getProductPrice().getProductPrice())).isEqualTo(0);
        assertThat(purchaseOrder.getCustomerId()).isEqualTo(1L);
    }

}
