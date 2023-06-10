package com.tanservices.invoice;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;
import com.tanservices.invoice.consumer.ProductOrderInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGenerator {

    private static final int LINE_HEIGHT = 12;
    private static final int INITIAL_Y_OFFSET = 700;

    public byte[] generateInvoicePdf(Invoice invoice) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                contentStream.newLineAtOffset(100, INITIAL_Y_OFFSET);
                contentStream.showText("Invoice ID: " + invoice.getInvoiceId());
                contentStream.newLine();

                contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                contentStream.showText("Order ID: " + invoice.getOrderId());
                contentStream.newLine();

                contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                contentStream.showText("Total Price: $ " + invoice.getTotalPrice());
                contentStream.newLine();

                contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                contentStream.showText("Created At: " + invoice.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                contentStream.newLine();

                contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.newLine();
                contentStream.showText("Product Details:");
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 10);

                List<ProductOrderInfo> productOrderInfos = invoice.getProductOrderInfos();
                for (ProductOrderInfo productOrderInfo : productOrderInfos) {

                    contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                    contentStream.showText("- Product Name: " + productOrderInfo.product().name());
                    contentStream.newLine();

                    contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                    contentStream.showText("  Quantity: " + productOrderInfo.requestQuantity());
                    contentStream.newLine();
                }

                contentStream.endText();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}
