package ru.ufanet.servicereference.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ufanet.servicereference.web.rest.TestUtil;

class ServiceOnLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceOnLocation.class);
        ServiceOnLocation serviceOnLocation1 = new ServiceOnLocation();
        serviceOnLocation1.setId(1L);
        ServiceOnLocation serviceOnLocation2 = new ServiceOnLocation();
        serviceOnLocation2.setId(serviceOnLocation1.getId());
        assertThat(serviceOnLocation1).isEqualTo(serviceOnLocation2);
        serviceOnLocation2.setId(2L);
        assertThat(serviceOnLocation1).isNotEqualTo(serviceOnLocation2);
        serviceOnLocation1.setId(null);
        assertThat(serviceOnLocation1).isNotEqualTo(serviceOnLocation2);
    }
}
