-- =====================================
-- 샘플 데이터: user 테이블
-- =====================================
INSERT INTO `user` (`id`, `created_at`, `deleted_at`, `updated_at`, `email`, `name`, `password`) VALUES
(1, NOW(), NULL, NOW(), 'user1@example.com', '민환', 'password1'),
(2, NOW(), NULL, NOW(), 'user2@example.com', '지민', 'password2'),
(3, NOW(), NULL, NOW(), 'user3@example.com', '수빈', 'password3'),
(4, NOW(), NULL, NOW(), 'user4@example.com', '현우', 'password4'),
(5, NOW(), NULL, NOW(), 'user5@example.com', '서윤', 'password5'),
(6, NOW(), NULL, NOW(), 'user6@example.com', '예린', 'password6'),
(7, NOW(), NULL, NOW(), 'user7@example.com', '태호', 'password7'),
(8, NOW(), NULL, NOW(), 'user8@example.com', '지후', 'password8'),
(9, NOW(), NULL, NOW(), 'user9@example.com', '민재', 'password9'),
(10, NOW(), NULL, NOW(), 'user10@example.com', '하린', 'password10');

-- =====================================
-- 샘플 데이터: authentication 테이블
-- =====================================
INSERT INTO `authentication` (`id`, `created_at`, `deleted_at`, `updated_at`, `expired_at`, `refresh_token`, `user_id`) VALUES
(1, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token1', 1),
(2, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token2', 2),
(3, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token3', 3),
(4, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token4', 4),
(5, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token5', 5),
(6, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token6', 6),
(7, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token7', 7),
(8, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token8', 8),
(9, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token9', 9),
(10, NOW(), NULL, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'token10', 10);

-- =====================================
-- 샘플 데이터: cash_wallet 테이블
-- =====================================
INSERT INTO `cash_wallet` (`id`, `created_at`, `deleted_at`, `updated_at`, `account_number`, `deposit`, `is_blocked`, `reserve`, `user_id`) VALUES
(1, NOW(), NULL, NOW(), 'ACC0001', 100000, 0, 0, 1),
(2, NOW(), NULL, NOW(), 'ACC0002', 200000, 0, 0, 2),
(3, NOW(), NULL, NOW(), 'ACC0003', 150000, 0, 0, 3),
(4, NOW(), NULL, NOW(), 'ACC0004', 50000, 0, 0, 4),
(5, NOW(), NULL, NOW(), 'ACC0005', 300000, 0, 0, 5),
(6, NOW(), NULL, NOW(), 'ACC0006', 120000, 0, 0, 6),
(7, NOW(), NULL, NOW(), 'ACC0007', 80000, 0, 0, 7),
(8, NOW(), NULL, NOW(), 'ACC0008', 90000, 0, 0, 8),
(9, NOW(), NULL, NOW(), 'ACC0009', 70000, 0, 0, 9),
(10, NOW(), NULL, NOW(), 'ACC0010', 60000, 0, 0, 10);

-- =====================================
-- 샘플 데이터: stock 테이블
-- =====================================
INSERT INTO `stock` (`id`, `created_at`, `deleted_at`, `updated_at`, `code`, `name`) VALUES
(1, NOW(), NULL, NOW(), 'STK001', '삼성전자'),
(2, NOW(), NULL, NOW(), 'STK002', 'LG전자'),
(3, NOW(), NULL, NOW(), 'STK003', 'SK하이닉스'),
(4, NOW(), NULL, NOW(), 'STK004', '카카오'),
(5, NOW(), NULL, NOW(), 'STK005', '네이버'),
(6, NOW(), NULL, NOW(), 'STK006', '현대차'),
(7, NOW(), NULL, NOW(), 'STK007', '기아'),
(8, NOW(), NULL, NOW(), 'STK008', '셀트리온'),
(9, NOW(), NULL, NOW(), 'STK009', '포스코'),
(10, NOW(), NULL, NOW(), 'STK010', 'LG화학');

-- =====================================
-- 샘플 데이터: cash_wallet_history 테이블
-- =====================================
INSERT INTO `cash_wallet_history` (`id`, `created_at`, `deleted_at`, `updated_at`, `reserve`, `tx_amount`, `tx_note`, `tx_type`, `cash_wallet_id`) VALUES
(1, NOW(), NULL, NOW(), 0, 100000, 'Initial deposit', 'DEPOSIT', 1),
(2, NOW(), NULL, NOW(), 0, 50000, 'Trade payment', 'TRADE_PAYMENT', 2),
(3, NOW(), NULL, NOW(), 0, 150000, 'Deposit', 'DEPOSIT', 3),
(4, NOW(), NULL, NOW(), 0, 20000, 'Withdrawal', 'WITHDRAWAL', 4),
(5, NOW(), NULL, NOW(), 0, 30000, 'Refund', 'TRADE_REFUND', 5),
(6, NOW(), NULL, NOW(), 0, 120000, 'Deposit', 'DEPOSIT', 6),
(7, NOW(), NULL, NOW(), 0, 50000, 'Trade payment', 'TRADE_PAYMENT', 7),
(8, NOW(), NULL, NOW(), 0, 70000, 'Deposit', 'DEPOSIT', 8),
(9, NOW(), NULL, NOW(), 0, 60000, 'Withdrawal', 'WITHDRAWAL', 9),
(10, NOW(), NULL, NOW(), 0, 90000, 'Deposit', 'DEPOSIT', 10);

-- =====================================
-- stock_wallet 테이블
-- =====================================
INSERT INTO `stock_wallet` (`id`, `created_at`, `deleted_at`, `updated_at`, `deposit`, `is_blocked`, `reserve`, `stock_id`, `user_id`) VALUES
(1, NOW(), NULL, NOW(), 100, 0, 0, 1, 1),
(2, NOW(), NULL, NOW(), 50, 0, 0, 2, 2),
(3, NOW(), NULL, NOW(), 200, 0, 0, 3, 3),
(4, NOW(), NULL, NOW(), 150, 0, 0, 4, 4),
(5, NOW(), NULL, NOW(), 300, 0, 0, 5, 5),
(6, NOW(), NULL, NOW(), 120, 0, 0, 6, 6),
(7, NOW(), NULL, NOW(), 80, 0, 0, 7, 7),
(8, NOW(), NULL, NOW(), 90, 0, 0, 8, 8),
(9, NOW(), NULL, NOW(), 70, 0, 0, 9, 9),
(10, NOW(), NULL, NOW(), 60, 0, 0, 10, 10);

-- =====================================
-- 샘플 데이터: order 테이블
-- =====================================
INSERT INTO `order` (`id`, `created_at`, `deleted_at`, `updated_at`, `amount`, `canceled_amount`, `price`, `side`, `unfilled_amount`, `stock_id`, `user_id`) VALUES
(1, NOW(), NULL, NOW(), 100, 0, 5000, 'BUY', 100, 1, 1),
(2, NOW(), NULL, NOW(), 50, 0, 5100, 'SELL', 50, 2, 2),
(3, NOW(), NULL, NOW(), 200, 0, 3000, 'BUY', 200, 3, 3),
(4, NOW(), NULL, NOW(), 150, 0, 3200, 'SELL', 150, 4, 4),
(5, NOW(), NULL, NOW(), 120, 0, 4000, 'BUY', 120, 5, 5),
(6, NOW(), NULL, NOW(), 80, 0, 3500, 'SELL', 80, 6, 6),
(7, NOW(), NULL, NOW(), 60, 0, 4500, 'BUY', 60, 7, 7),
(8, NOW(), NULL, NOW(), 90, 0, 4700, 'SELL', 90, 8, 8),
(9, NOW(), NULL, NOW(), 110, 0, 4800, 'BUY', 110, 9, 9),
(10, NOW(), NULL, NOW(), 70, 0, 5000, 'SELL', 70, 10, 10);

-- =====================================
-- 샘플 데이터: match 테이블
-- =====================================
INSERT INTO `match` (`id`, `created_at`, `deleted_at`, `updated_at`, `maker_order_id`, `stock_id`, `taker_order_id`) VALUES
(1, NOW(), NULL, NOW(), 1, 1, 2),
(2, NOW(), NULL, NOW(), 3, 3, 4),
(3, NOW(), NULL, NOW(), 5, 5, 6),
(4, NOW(), NULL, NOW(), 7, 7, 8),
(5, NOW(), NULL, NOW(), 9, 9, 10),
(6, NOW(), NULL, NOW(), 2, 2, 1),
(7, NOW(), NULL, NOW(), 4, 4, 3),
(8, NOW(), NULL, NOW(), 6, 6, 5),
(9, NOW(), NULL, NOW(), 8, 8, 7),
(10, NOW(), NULL, NOW(), 10, 10, 9);

-- =====================================
-- 샘플 데이터: market_status 테이블
-- =====================================
INSERT INTO `market_status` (`id`, `created_at`, `deleted_at`, `updated_at`, `closing_price`, `highest_price`, `lower_limit_price`, `lowest_price`, `opening_price`, `reference_price`, `trading_amount`, `trading_date`, `trading_volume`, `upper_limit_price`, `stock_id`) VALUES
(1, NOW(), NULL, NOW(), 5100, 5200, 4800, 4900, 5000, 5000, 1000000, CURDATE(), 200, 5200, 1),
(2, NOW(), NULL, NOW(), 5100, 5150, 4800, 4900, 5000, 5000, 500000, CURDATE(), 100, 5200, 2),
(3, NOW(), NULL, NOW(), 3000, 3050, 2900, 2950, 3000, 3000, 800000, CURDATE(), 150, 3100, 3),
(4, NOW(), NULL, NOW(), 3200, 3250, 3000, 3100, 3150, 3150, 600000, CURDATE(), 120, 3300, 4),
(5, NOW(), NULL, NOW(), 4000, 4100, 3800, 3900, 3950, 3950, 700000, CURDATE(), 140, 4200, 5),
(6, NOW(), NULL, NOW(), 3500, 3600, 3300, 3400, 3450, 3450, 650000, CURDATE(), 130, 3700, 6),
(7, NOW(), NULL, NOW(), 4500, 4600, 4300, 4400, 4450, 4450, 750000, CURDATE(), 160, 4700, 7),
(8, NOW(), NULL, NOW(), 4700, 4800, 4500, 4600, 4650, 4650, 800000, CURDATE(), 170, 4900, 8),
(9, NOW(), NULL, NOW(), 4800, 4900, 4600, 4700, 4750, 4750, 850000, CURDATE(), 180, 5000, 9),
(10, NOW(), NULL, NOW(), 5000, 5100, 4800, 4900, 4950, 4950, 900000, CURDATE(), 190, 5200, 10);

-- =====================================
-- 샘플 데이터: stock_wallet_history 테이블
-- =====================================
INSERT INTO `stock_wallet_history` (`id`, `created_at`, `deleted_at`, `updated_at`, `reserve`, `tx_amount`, `tx_note`, `tx_type`, `stock_wallet_id`) VALUES
(1, NOW(), NULL, NOW(), 0, 100, 'Initial deposit', 'BUY_ORDER', 1),
(2, NOW(), NULL, NOW(), 0, 50, 'Dividend received', 'DIVIDEND', 2),
(3, NOW(), NULL, NOW(), 0, 200, 'Initial deposit', 'BUY_ORDER', 3),
(4, NOW(), NULL, NOW(), 0, 150, 'Sell order executed', 'SELL_ORDER', 4),
(5, NOW(), NULL, NOW(), 0, 120, 'Buy order executed', 'BUY_ORDER', 5),
(6, NOW(), NULL, NOW(), 0, 80, 'Dividend received', 'DIVIDEND', 6),
(7, NOW(), NULL, NOW(), 0, 60, 'Sell order executed', 'SELL_ORDER', 7),
(8, NOW(), NULL, NOW(), 0, 90, 'Buy order executed', 'BUY_ORDER', 8),
(9, NOW(), NULL, NOW(), 0, 110, 'Trade delivery', 'TRADE_DELIVERY', 9),
(10, NOW(), NULL, NOW(), 0, 70, 'Sell order executed', 'SELL_ORDER', 10);
