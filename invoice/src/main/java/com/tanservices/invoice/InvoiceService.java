package com.tanservices.invoice;

import com.tanservices.invoice.consumer.ProductOrderInfo;
import com.tanservices.invoice.consumer.ProductOrderRequest;
import com.tanservices.invoice.exception.InvoiceNotFoundException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PdfGenerator pdfGenerator;


    public InvoiceService(InvoiceRepository invoiceRepository, PdfGenerator pdfGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.pdfGenerator = pdfGenerator;
    }

    public Invoice getInvoiceById(UUID invoiceId) {
        Optional<Invoice> optionalOrder = Optional.ofNullable(invoiceRepository.findByInvoiceId(invoiceId));
        return optionalOrder.orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
    }

    public void createInvoice(ProductOrderRequest productOrderRequest) {
        List<ProductOrderInfo> productOrderInfos = productOrderRequest.productOrderInfos();
        UUID orderUUID = productOrderRequest.orderUUID();
        UUID invoiceUUID = productOrderRequest.invoiceUUID();
        LocalDateTime createdAt = productOrderRequest.createdAt();

        double totalPrice = productOrderInfos.stream()
                .mapToDouble(productOrderInfo -> productOrderInfo.requestQuantity() * productOrderInfo.product().price())
                .sum();
        totalPrice = Math.round(totalPrice * 100.0) / 100.0; // round the value to 2 decimal places

        invoiceRepository.save(new Invoice(invoiceUUID, orderUUID, totalPrice, createdAt, productOrderInfos));
    }

    public ByteArrayResource getInvoicePdfById(UUID invoiceId) throws IOException {
        byte[] pdfData = pdfGenerator.generateInvoicePdf(this.getInvoiceById(invoiceId));
        return new ByteArrayResource(pdfData);
    }
}
