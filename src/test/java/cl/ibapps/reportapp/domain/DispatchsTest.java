package cl.ibapps.reportapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cl.ibapps.reportapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DispatchsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dispatchs.class);
        Dispatchs dispatchs1 = new Dispatchs();
        dispatchs1.setId(1L);
        Dispatchs dispatchs2 = new Dispatchs();
        dispatchs2.setId(dispatchs1.getId());
        assertThat(dispatchs1).isEqualTo(dispatchs2);
        dispatchs2.setId(2L);
        assertThat(dispatchs1).isNotEqualTo(dispatchs2);
        dispatchs1.setId(null);
        assertThat(dispatchs1).isNotEqualTo(dispatchs2);
    }
}
