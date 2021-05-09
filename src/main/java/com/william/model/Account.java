package com.william.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "account_id")
    private long id;

    private BigDecimal balance;

    @OneToMany(mappedBy = "account", targetEntity = Transaction.class)
    @ToString.Exclude
    List<Transaction> transactions;

}
