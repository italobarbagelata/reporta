package cl.ibapps.reportapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cl.ibapps.reportapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specie.class);
        Specie specie1 = new Specie();
        specie1.setId(1L);
        Specie specie2 = new Specie();
        specie2.setId(specie1.getId());
        assertThat(specie1).isEqualTo(specie2);
        specie2.setId(2L);
        assertThat(specie1).isNotEqualTo(specie2);
        specie1.setId(null);
        assertThat(specie1).isNotEqualTo(specie2);
    }
}
