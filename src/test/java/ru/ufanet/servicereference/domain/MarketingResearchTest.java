package ru.ufanet.servicereference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ufanet.servicereference.web.rest.TestUtil;

class MarketingResearchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketingResearch.class);
        MarketingResearch marketingResearch1 = new MarketingResearch();
        marketingResearch1.setId(1L);
        MarketingResearch marketingResearch2 = new MarketingResearch();
        marketingResearch2.setId(marketingResearch1.getId());
        assertThat(marketingResearch1).isEqualTo(marketingResearch2);
        marketingResearch2.setId(2L);
        assertThat(marketingResearch1).isNotEqualTo(marketingResearch2);
        marketingResearch1.setId(null);
        assertThat(marketingResearch1).isNotEqualTo(marketingResearch2);
    }
}
