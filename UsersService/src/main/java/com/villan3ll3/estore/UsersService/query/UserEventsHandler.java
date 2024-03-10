package com.villan3ll3.estore.UsersService.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.villan3ll3.estore.Core.model.PaymentDetails;
import com.villan3ll3.estore.Core.model.User;
import com.villan3ll3.estore.Core.query.FetchUserPaymentDetailsQuery;

@Component
public class UserEventsHandler {

    @QueryHandler
    public User fetchUser(FetchUserPaymentDetailsQuery query) {

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("SERGEY KARGOPOLOV")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        return User.builder()
                .firstName("Sergey")
                .lastName("Kargopolov")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();
    }
}
