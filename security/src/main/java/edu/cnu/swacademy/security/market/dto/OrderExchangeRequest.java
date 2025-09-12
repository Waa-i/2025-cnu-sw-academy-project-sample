package edu.cnu.swacademy.security.market.dto;

public record OrderExchangeRequest(int orderId,int stockId,int price,int amount,String side,String createAt) {

}
