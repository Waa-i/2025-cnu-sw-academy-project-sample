package edu.cnu.swacademy.security.market.exception;

import edu.cnu.swacademy.security.common.ErrorCode;
import edu.cnu.swacademy.security.common.SecurityException;

public class MarketAlreadyOpenException extends SecurityException {
    public MarketAlreadyOpenException() {
        super(ErrorCode.MARKET_ALREADY_OPEN);
    }
}
