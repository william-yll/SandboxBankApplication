package com.william.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "transaction_id")
    @JsonIgnore
    private long id;

    @Builder.Default
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionTime = LocalDateTime.now();

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal preTransaction;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal transactionAmount;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal postTransaction;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;
}
