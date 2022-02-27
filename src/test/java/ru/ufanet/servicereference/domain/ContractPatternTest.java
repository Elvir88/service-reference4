package ru.ufanet.servicereference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ufanet.servicereference.web.rest.TestUtil;

class ContractPatternTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractPattern.class);
        ContractPattern contractPattern1 = new ContractPattern();
        contractPattern1.setId(1L);
        ContractPattern contractPattern2 = new ContractPattern();
        contractPattern2.setId(contractPattern1.getId());
        assertThat(contractPattern1).isEqualTo(contractPattern2);
        contractPattern2.setId(2L);
        assertThat(contractPattern1).isNotEqualTo(contractPattern2);
        contractPattern1.setId(null);
        assertThat(contractPattern1).isNotEqualTo(contractPattern2);
    }
}
