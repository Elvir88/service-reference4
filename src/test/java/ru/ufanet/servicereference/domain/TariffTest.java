package ru.ufanet.servicereference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ufanet.servicereference.web.rest.TestUtil;

class TariffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tariff.class);
        Tariff tariff1 = new Tariff();
        tariff1.setId(1L);
        Tariff tariff2 = new Tariff();
        tariff2.setId(tariff1.getId());
        assertThat(tariff1).isEqualTo(tariff2);
        tariff2.setId(2L);
        assertThat(tariff1).isNotEqualTo(tariff2);
        tariff1.setId(null);
        assertThat(tariff1).isNotEqualTo(tariff2);
    }
}
