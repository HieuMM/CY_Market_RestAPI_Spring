package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VnPayResultDTO {
    private String vnp_TxnRef;
    private String vnp_BankTranNo;
    private String vnp_TransactionNo;
    private String vnp_Amount;
    private String vnp_BankCode;
    private String vnp_ResponseCode;
}
