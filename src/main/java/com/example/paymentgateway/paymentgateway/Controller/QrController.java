package com.example.paymentgateway.paymentgateway.Controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentgateway.paymentgateway.DTO.QrRequest;
import com.example.paymentgateway.paymentgateway.Service.QrCodeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrCodeService qrCodeService;

    @PostMapping(
            value = "/upi",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> generateQr(@RequestBody QrRequest request) {

        String upiData =
                "upi://pay?pa=kishores0412-2@okicici" +
                "&pn=Yajentio Software" +
                "&am=" + request.getAmount() +
                "&cu=INR" +
                "&tn=" + request.getOrderId();

        byte[] qrImage =
                qrCodeService.generateQrCode(
                        upiData,
                        request.getOrderId(),
                        request.getAmount()
                );

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }
}
