package edu.cnu.swacademy.security.order;

import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.stock.Stock;
import edu.cnu.swacademy.security.user.User;
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
@SQLDelete(sql = "UPDATE `order` SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "`order`")
@Entity
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "INT UNSIGNED")
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Stock stock;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderSide side;

  @Column(nullable = false, columnDefinition = "INT UNSIGNED")
  private int price;

  @Column(nullable = false, columnDefinition = "INT UNSIGNED")
  private int amount;

  @Column(nullable = false, columnDefinition = "INT UNSIGNED")
  private int unfilledAmount;

  @Column(nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
  private int canceledAmount = 0;
}
