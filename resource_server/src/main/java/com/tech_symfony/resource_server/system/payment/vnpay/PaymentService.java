package com.tech_symfony.resource_server.system.payment.vnpay;


public interface PaymentService<T, R> {

    /**
     * Creates a bill or payment request for the provided entity.
     *
     * @param paymentEntity the entity representing payment details, such as donation or purchase.
     * @return a unique identifier for the created bill or request.
     */
    String createBill(T paymentEntity);

    /**
     * Verifies the payment for the provided entity.
     *
     * @param paymentEntity the entity representing payment details.
     * @return a result object containing verification details and status.
     */
    R verifyPay(T paymentEntity);

}

