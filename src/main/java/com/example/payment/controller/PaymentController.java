package com.example.payment.controller;

import com.example.payment.service.PaystackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class PaymentController {
    private PaystackService paystackService;

    public PaymentController(PaystackService paystackService) {
        this.paystackService = paystackService;
    }
    @PostMapping("/pay")
    //Model interface is used to pass data between the controller and the view.
    public String initiatePayment(@RequestParam double amount,
                                  @RequestParam String email,
                                  @RequestParam String callbackUrl,
                                  Model model) {
        try {
            String referenceCode = paystackService.initializePayment(amount, email, callbackUrl);
            model.addAttribute("referenceCode", referenceCode);
            model.addAttribute("amount", amount);
            model.addAttribute("email", email);
//            model.addAttribute("currency",Currency);

        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }

        return "payment";
    }

    @GetMapping("/payment/callback")
    public String handleCallback(@RequestParam("reference") String referenceCode, Model model) {
        // Handle payment callback from Paystack
        // You can verify the payment status using the reference code
        model.addAttribute("referenceCode", referenceCode);
        return "payment_callback";
    }
}
