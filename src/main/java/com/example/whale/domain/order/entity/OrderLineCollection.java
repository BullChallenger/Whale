package com.example.whale.domain.order.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLineCollection {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineEntity> orderLines = new ArrayList<>();

    public void addOrderLineInOrder(OrderEntity order, List<OrderLineEntity> orderLines) {
        this.orderLines.addAll(orderLines);
        orderLines.stream().forEach(orderLine -> orderLine.insertInOrder(order));
    }

    protected BigDecimal calculateTotalAmountOfOrder() {
        return this.orderLines.stream().map(OrderLineEntity::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
