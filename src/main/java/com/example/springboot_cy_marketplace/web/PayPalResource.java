package com.example.springboot_cy_marketplace.web;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.example.springboot_cy_marketplace.dto.OrderDetailDTO;
import com.example.springboot_cy_marketplace.services.PayPalServices;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/pay/paypal")
public class PayPalResource {
    @Autowired
    PayPalServices payPalServices;

    public static final String SUCCESS_URL = "/success";
    public static final String CANCEL_URL = "/cancel";

    @Value("${server.host.fe.user}")
    public String homePage;

    @PostMapping(value = "/process")
    public Object processingPayPal(@RequestBody OrderDetailDTO order){
        try {
            Payment payment = payPalServices.createPayment(order.getTotalPrice(), order.getCurrency(), order.getPaymentMethod(),
                    order.getIntent(), order.getDescription(), homePage + CANCEL_URL,
                    homePage + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalServices.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return homePage;
    }
}
