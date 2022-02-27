package ru.ufanet.servicereference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ufanet.servicereference.web.rest.TestUtil;

class ServiceInPacketDiscountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceInPacketDiscount.class);
        ServiceInPacketDiscount serviceInPacketDiscount1 = new ServiceInPacketDiscount();
        serviceInPacketDiscount1.setId(1L);
        ServiceInPacketDiscount serviceInPacketDiscount2 = new ServiceInPacketDiscount();
        serviceInPacketDiscount2.setId(serviceInPacketDiscount1.getId());
        assertThat(serviceInPacketDiscount1).isEqualTo(serviceInPacketDiscount2);
        serviceInPacketDiscount2.setId(2L);
        assertThat(serviceInPacketDiscount1).isNotEqualTo(serviceInPacketDiscount2);
        serviceInPacketDiscount1.setId(null);
        assertThat(serviceInPacketDiscount1).isNotEqualTo(serviceInPacketDiscount2);
    }
}
