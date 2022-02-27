package ru.ufanet.servicereference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ufanet.servicereference.web.rest.TestUtil;

class PromotionOnLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromotionOnLocation.class);
        PromotionOnLocation promotionOnLocation1 = new PromotionOnLocation();
        promotionOnLocation1.setId(1L);
        PromotionOnLocation promotionOnLocation2 = new PromotionOnLocation();
        promotionOnLocation2.setId(promotionOnLocation1.getId());
        assertThat(promotionOnLocation1).isEqualTo(promotionOnLocation2);
        promotionOnLocation2.setId(2L);
        assertThat(promotionOnLocation1).isNotEqualTo(promotionOnLocation2);
        promotionOnLocation1.setId(null);
        assertThat(promotionOnLocation1).isNotEqualTo(promotionOnLocation2);
    }
}
