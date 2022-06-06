package cl.ibapps.reportapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cl.ibapps.reportapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinalOverallTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinalOverall.class);
        FinalOverall finalOverall1 = new FinalOverall();
        finalOverall1.setId(1L);
        FinalOverall finalOverall2 = new FinalOverall();
        finalOverall2.setId(finalOverall1.getId());
        assertThat(finalOverall1).isEqualTo(finalOverall2);
        finalOverall2.setId(2L);
        assertThat(finalOverall1).isNotEqualTo(finalOverall2);
        finalOverall1.setId(null);
        assertThat(finalOverall1).isNotEqualTo(finalOverall2);
    }
}
