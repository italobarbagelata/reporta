package cl.ibapps.reportapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cl.ibapps.reportapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PackagesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Packages.class);
        Packages packages1 = new Packages();
        packages1.setId(1L);
        Packages packages2 = new Packages();
        packages2.setId(packages1.getId());
        assertThat(packages1).isEqualTo(packages2);
        packages2.setId(2L);
        assertThat(packages1).isNotEqualTo(packages2);
        packages1.setId(null);
        assertThat(packages1).isNotEqualTo(packages2);
    }
}
