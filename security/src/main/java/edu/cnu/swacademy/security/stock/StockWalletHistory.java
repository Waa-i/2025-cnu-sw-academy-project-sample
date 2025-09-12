package edu.cnu.swacademy.security.stock;

import edu.cnu.swacademy.security.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE stock_wallet_history SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "stock_wallet_history")
@Entity
public class StockWalletHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "INT UNSIGNED")
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private StockWallet stockWallet;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TransactionType txType;

  @Column(nullable = false, columnDefinition = "INT UNSIGNED")
  private int txAmount;

  @Column(nullable = false, length = 100)
  private String txNote;

  @Column(nullable = false, columnDefinition = "INT UNSIGNED")
  private int reserve;

  public StockWalletHistory(StockWallet stockWallet, TransactionType txType, int txAmount, String txNote, int reserve) {
    this.stockWallet = stockWallet;
    this.txType = txType;
    this.txAmount = txAmount;
    this.txNote = txNote;
    this.reserve = reserve;
  }
}
