package edu.cnu.swacademy.exchange.utils;

public final class TickSizeUtils {
    private TickSizeUtils() {}

    public static int getTickSize(int price) {
        int zone = getTickZone(price);
        return switch (zone) {
            case 0 -> 1;
            case 1 -> 5;
            case 2 -> 10;
            case 3 -> 50;
            case 4 -> 100;
            case 5 -> 500;
            default -> 1000;
        };
    }
    private static int getTickZone(int price) {
        if(price < 2000) return 0;
        else if(price < 5000) return 1;
        else if(price < 20000) return 2;
        else if(price < 50000) return 3;
        else if(price < 200000) return 4;
        else if(price < 500000) return 5;
        else return 6;
    }
}
