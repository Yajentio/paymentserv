package com.example.paymentgateway.paymentgateway.Service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.example.paymentgateway.paymentgateway.Entity.PaymentEntity;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ReceiptService {

    public byte[] generateReceipt(PaymentEntity payment) {

        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

            document.add(new Paragraph("PAYMENT RECEIPT", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Order ID: " + payment.getOrderId(), normalFont));
            document.add(new Paragraph("Payment ID: " + payment.getRazorpayPaymentId(), normalFont));
            document.add(new Paragraph("Amount: ₹" + (payment.getAmount() / 100.0), normalFont));
            document.add(new Paragraph("Currency: " + payment.getCurrency(), normalFont));
            document.add(new Paragraph("Status: " + payment.getStatus(), normalFont));
            document.add(new Paragraph("Payment Method: " + payment.getPaymentMethod(), normalFont));
            document.add(new Paragraph("Card Type: " + payment.getCardType(), normalFont));
            document.add(new Paragraph("Date: " + payment.getCreatedAt(), normalFont));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Receipt generation failed", e);
        }
    }
}

