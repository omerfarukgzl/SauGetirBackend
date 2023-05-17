package com.saugetir.SauGetir.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saugetir.SauGetir.request.EncryptedPaymentRequest;
import com.saugetir.SauGetir.response.PaymentResponse;
import com.saugetir.SauGetir.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping("/payment")
    public PaymentResponse payment(HttpServletRequest request,@RequestBody EncryptedPaymentRequest encryptedPaymentRequest){
        String signature = request.getHeader("x-signature");
        String randomKey = request.getHeader("x-rnd-key");
        return paymentService.payment(encryptedPaymentRequest,signature,randomKey);

    }


}
