package edu.cnu.swacademy.security.market;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.market.dto.OrderRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> placeOrder(HttpServletRequest request, @Valid @RequestBody OrderRequest orderRequest) throws SecurityException {
        int userId = (int) request.getAttribute("user_id");
        orderService.placeOrder(userId, orderRequest);
        return ResponseEntity.ok().build();
    }
}
