package ru.ufanet.servicereference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ufanet.servicereference.web.rest.TestUtil;

class TariffGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TariffGroup.class);
        TariffGroup tariffGroup1 = new TariffGroup();
        tariffGroup1.setId(1L);
        TariffGroup tariffGroup2 = new TariffGroup();
        tariffGroup2.setId(tariffGroup1.getId());
        assertThat(tariffGroup1).isEqualTo(tariffGroup2);
        tariffGroup2.setId(2L);
        assertThat(tariffGroup1).isNotEqualTo(tariffGroup2);
        tariffGroup1.setId(null);
        assertThat(tariffGroup1).isNotEqualTo(tariffGroup2);
    }
}
