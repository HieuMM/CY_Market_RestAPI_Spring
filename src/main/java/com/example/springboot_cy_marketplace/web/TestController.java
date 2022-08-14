package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.repository.IUserRoomChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

    @GetMapping("test1")
    public String testTemplate1(){
        return "index";
    }
    @GetMapping("test2")
    public String testTemplate2(){
        return "paypal-checkout-success";
    }
    @GetMapping("test3")
    public String testTemplate3(){
        return "checkout-vnpay-success";
    }
    @GetMapping("test4")
    public String testTemplate4(){
        return "template-email";
    }

    @GetMapping("/vnpay-return")
    public String vnpayReturn(Model model){

//        model.addAttribute("vnp_Amount",vnp_Amount);
//        model.addAttribute("vnp_BankCode",vnp_BankCode);
//        model.addAttribute("vnp_TxnRef",vnp_TxnRef);
//        model.addAttribute("vnp_OrderInfo",vnp_OrderInfo);
//        model.addAttribute("vnp_BankTranNo",vnp_BankTranNo);
//        model.addAttribute("vnp_TransactionNo",vnp_TransactionNo);

        model.addAttribute("vnp_Amount","100000000");
        model.addAttribute("vnp_BankCode","NCB");
        model.addAttribute("vnp_TxnRef","54988456");
        model.addAttribute("vnp_OrderInfo","test payment 2");
        model.addAttribute("vnp_BankTranNo","VNP13777614");
        model.addAttribute("vnp_TransactionNo","13777614");
        return "vnpay-checkout-success";
    }

}
