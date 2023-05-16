package com.saugetir.SauGetir.controller;

import com.saugetir.SauGetir.request.EncryptedPaymentRequest;
import com.saugetir.SauGetir.request.PaymentRequest;
import com.saugetir.SauGetir.response.PaymentResponse;
import com.saugetir.SauGetir.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/v1/saugetir")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment")
    public String payment(){
        return paymentService.payment();
    }

    @PostMapping("/payment2")
    public PaymentResponse payment2(@RequestBody EncryptedPaymentRequest encryptedPaymentRequest){
        return paymentService.payment2(encryptedPaymentRequest);
    }


}
