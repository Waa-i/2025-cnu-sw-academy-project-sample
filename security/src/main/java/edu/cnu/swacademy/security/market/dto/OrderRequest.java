package edu.cnu.swacademy.security.market.dto;

import edu.cnu.swacademy.security.market.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull
    private Long stockId;

    @NotNull
    private String side;

    @Min(1)
    private int price;

    @Min(1)
    private int quantity;
}
