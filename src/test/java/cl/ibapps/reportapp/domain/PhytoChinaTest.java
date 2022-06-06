package cl.ibapps.reportapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cl.ibapps.reportapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhytoChinaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhytoChina.class);
        PhytoChina phytoChina1 = new PhytoChina();
        phytoChina1.setId(1L);
        PhytoChina phytoChina2 = new PhytoChina();
        phytoChina2.setId(phytoChina1.getId());
        assertThat(phytoChina1).isEqualTo(phytoChina2);
        phytoChina2.setId(2L);
        assertThat(phytoChina1).isNotEqualTo(phytoChina2);
        phytoChina1.setId(null);
        assertThat(phytoChina1).isNotEqualTo(phytoChina2);
    }
}
