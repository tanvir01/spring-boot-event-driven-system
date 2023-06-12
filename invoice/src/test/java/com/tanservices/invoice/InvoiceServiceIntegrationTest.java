package com.tanservices.invoice;

import com.tanservices.invoice.consumer.Product;
import com.tanservices.invoice.consumer.ProductOrderInfo;
import com.tanservices.invoice.consumer.ProductOrderRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class InvoiceServiceIntegrationTest {

    @MockBean
    private InvoiceRepository invoiceRepository;

    @MockBean
    private PdfGenerator pdfGenerator;

    @Autowired
    private InvoiceService invoiceService;

    private final UUID invoiceId = UUID.randomUUID();
    private final UUID orderId = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        // Mock the behavior of the invoice repository
        Invoice mockInvoice = new Invoice(invoiceId, orderId, 100.0, createdAt, null);
        when(invoiceRepository.findByInvoiceId(invoiceId)).thenReturn(mockInvoice);
    }

    @Test
    public void testGetInvoiceById_ExistingInvoice_ReturnsInvoice() {
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);

        // Verify the result
        Assertions.assertEquals(invoiceId, invoice.getInvoiceId());
        Assertions.assertEquals(orderId, invoice.getOrderId());
        Assertions.assertEquals(100.0, invoice.getTotalPrice());
        Assertions.assertEquals(createdAt, invoice.getCreatedAt());
    }

    @Test
    public void testCreateInvoice_ValidProductOrderRequest_SavesInvoice() {
        // Prepare the input product order request
        List<ProductOrderInfo> productOrderInfos = Arrays.asList(
                new ProductOrderInfo(new Product("productId1", "Product 1", "This is dummy product1", 10.0, 20), 1),
                new ProductOrderInfo(new Product("productId2", "Product 2", "This is dummy product2", 15.0, 30), 2)
        );
        ProductOrderRequest productOrderRequest = new ProductOrderRequest(productOrderInfos, orderId, invoiceId, createdAt);

        invoiceService.createInvoice(productOrderRequest);

        // Verify the interaction with the invoice repository
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    public void testGetInvoicePdfById_ExistingInvoice_ReturnsPdfData() throws IOException {
        // Prepare the mock PDF data
        byte[] pdfData = "Sample PDF Data".getBytes();
        ByteArrayResource pdfResource = new ByteArrayResource(pdfData);

        // Mock the behavior of the pdf generator
        when(pdfGenerator.generateInvoicePdf(any(Invoice.class))).thenReturn(pdfData);

        ByteArrayResource result = invoiceService.getInvoicePdfById(invoiceId);

        // Verify the result
        Assertions.assertEquals(pdfResource, result);
    }
}
