package com.example.paymentgateway.paymentgateway.Service;


import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.paymentgateway.paymentgateway.Entity.UpiQrTransaction;
import com.example.paymentgateway.paymentgateway.Repository.UpiQrRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
@Service
public class QrCodeService {

    private final UpiQrRepository repository;

    public QrCodeService(UpiQrRepository repository) {
        this.repository = repository;
    }

	public byte[] generateQrCode(String text,
            String orderId,
            Double amount) {{


try {
int width = 300;
int height = 300;

BitMatrix matrix = new MultiFormatWriter()
.encode(text, BarcodeFormat.QR_CODE, width, height);

ByteArrayOutputStream out = new ByteArrayOutputStream();
MatrixToImageWriter.writeToStream(matrix, "PNG", out);

byte[] imageBytes = out.toByteArray();

// ✅ SAVE TO DATABASE
UpiQrTransaction txn = UpiQrTransaction.builder()
.orderId(orderId)
.amount(amount)
.upiString(text)
.qrImage(imageBytes)
.status("CREATED")
.createdAt(LocalDateTime.now())
.build();

repository.save(txn);

return imageBytes;

} catch (Exception e) {
throw new RuntimeException("QR generation failed", e);
}
            }
	}
	
	
	public void markPaymentSuccess(String orderId) {

        UpiQrTransaction txn = repository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        // ✅ Prevent duplicate updates
        if ("SUCCESS".equals(txn.getStatus())) {
            System.out.println("Already processed");
            return;
        }

        txn.setStatus("SUCCESS");

        repository.save(txn);
    }

    public void markPaymentFailed(String orderId) {

        repository.findByOrderId(orderId)
                .ifPresent(txn -> {
                    txn.setStatus("FAILED");
                    repository.save(txn);
                });
    }
}





