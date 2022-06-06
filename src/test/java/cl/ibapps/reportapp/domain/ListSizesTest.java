package cl.ibapps.reportapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cl.ibapps.reportapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ListSizesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ListSizes.class);
        ListSizes listSizes1 = new ListSizes();
        listSizes1.setId(1L);
        ListSizes listSizes2 = new ListSizes();
        listSizes2.setId(listSizes1.getId());
        assertThat(listSizes1).isEqualTo(listSizes2);
        listSizes2.setId(2L);
        assertThat(listSizes1).isNotEqualTo(listSizes2);
        listSizes1.setId(null);
        assertThat(listSizes1).isNotEqualTo(listSizes2);
    }
}
