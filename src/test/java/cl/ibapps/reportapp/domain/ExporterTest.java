package cl.ibapps.reportapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cl.ibapps.reportapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExporterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exporter.class);
        Exporter exporter1 = new Exporter();
        exporter1.setId(1L);
        Exporter exporter2 = new Exporter();
        exporter2.setId(exporter1.getId());
        assertThat(exporter1).isEqualTo(exporter2);
        exporter2.setId(2L);
        assertThat(exporter1).isNotEqualTo(exporter2);
        exporter1.setId(null);
        assertThat(exporter1).isNotEqualTo(exporter2);
    }
}
