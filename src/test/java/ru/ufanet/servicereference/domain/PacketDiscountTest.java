package ru.ufanet.servicereference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ufanet.servicereference.web.rest.TestUtil;

class PacketDiscountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PacketDiscount.class);
        PacketDiscount packetDiscount1 = new PacketDiscount();
        packetDiscount1.setId(1L);
        PacketDiscount packetDiscount2 = new PacketDiscount();
        packetDiscount2.setId(packetDiscount1.getId());
        assertThat(packetDiscount1).isEqualTo(packetDiscount2);
        packetDiscount2.setId(2L);
        assertThat(packetDiscount1).isNotEqualTo(packetDiscount2);
        packetDiscount1.setId(null);
        assertThat(packetDiscount1).isNotEqualTo(packetDiscount2);
    }
}
