package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.VnPayConfig;
import com.example.springboot_cy_marketplace.dto.PaymentDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/pay/vnpay")
public class VnPayResource {

    @PostMapping("/payment")
    public ResponseEntity<?> vnpayPayment(@RequestBody PaymentDTO paymentDTO) throws IOException {
        String vnp_OrderInfo = paymentDTO.getDescription();
        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String bank_code = paymentDTO.getBankCode();

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "ATM";
        String vnp_IpAddr = "0:0:0:0:0:0:0:1";
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
        int amount = paymentDTO.getAmount() * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(dt);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        Calendar cldvnp_ExpireDate = Calendar.getInstance();
        cldvnp_ExpireDate.add(Calendar.SECOND, 30);
        Date vnp_ExpireDateD = cldvnp_ExpireDate.getTime();
        String vnp_ExpireDate = formatter.format(vnp_ExpireDateD);

        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                //hashData.append(fieldValue); //sử dụng và 2.0.0 và 2.0.1 checksum sha256
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())); //sử dụng v2.1.0  check sum sha512
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        return ResponseEntity.ok().body(paymentUrl);
    }

    @PostMapping("/query")
    public ResponseEntity<?> vnpayQuery(
            @RequestParam(value = "vnp_OrderInfo") String vnp_OrderInfo,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef
    ) throws IOException {

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "querydr");
        vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo + vnp_TxnRef);
//        vnp_Params.put("vnp_TransDate", vnp_TransDate);
        vnp_Params.put("vnp_IpAddr", "0:0:0:0:0:0:0:1");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_TransDate", vnp_CreateDate);
        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_apiUrl + "?" + queryUrl;
        return ResponseEntity.ok().body(paymentUrl);
    }

    @PostMapping("/return")
    public ResponseEntity<?> vnpayReturn(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "vnp_Amount") int vnp_Amount,
            @RequestParam(value = "vnp_BankCode") String vnp_BankCode,
            @RequestParam(value = "vnp_BankTranNo") String vnp_BankTranNo,
            @RequestParam(value = "vnp_CardType") String vnp_CardType,
            @RequestParam(value = "vnp_OrderInfo") String vnp_OrderInfo,
            @RequestParam(value = "vnp_PayDate") String vnp_PayDate,
            @RequestParam(value = "vnp_ResponseCode") String vnp_ResponseCode,
            @RequestParam(value = "vnp_TmnCode") String vnp_TmnCode,
            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo,
            @RequestParam(value = "vnp_TransactionStatus") String vnp_TransactionStatus,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(value = "vnp_SecureHash") String vnp_SecureHash
    ){
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("email", email);
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_BankCode", vnp_BankCode);
        vnp_Params.put("vnp_BankTranNo", vnp_BankTranNo);
        vnp_Params.put("vnp_CardType", vnp_CardType);
        vnp_Params.put("vnp_PayDate", vnp_PayDate);
        vnp_Params.put("vnp_ResponseCode", vnp_ResponseCode);
        vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
        vnp_Params.put("vnp_TransactionStatus", vnp_TransactionStatus);
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);
        vnp_Params.put("vnp_Command", "return");
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);



        return ResponseEntity.ok().body(vnp_Params);
    }
}
