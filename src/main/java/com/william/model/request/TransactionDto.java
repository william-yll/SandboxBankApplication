package com.william.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private Long fromAccountId;
    private Long toAccountId;
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 99, fraction = 2)
    private BigDecimal amount;
}
