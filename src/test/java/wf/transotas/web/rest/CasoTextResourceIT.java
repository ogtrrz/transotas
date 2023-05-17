package wf.transotas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import wf.transotas.IntegrationTest;
import wf.transotas.domain.CasoText;
import wf.transotas.repository.CasoTextRepository;
import wf.transotas.repository.search.CasoTextSearchRepository;
import wf.transotas.service.dto.CasoTextDTO;
import wf.transotas.service.mapper.CasoTextMapper;

/**
 * Integration tests for the {@link CasoTextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CasoTextResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_1 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_1 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_2 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_2 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_3 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_3 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_4 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_4 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_5 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_5 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_6 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_6 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_7 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_7 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_8 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_8 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_9 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_9 = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_10 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_10 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/caso-texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/caso-texts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CasoTextRepository casoTextRepository;

    @Autowired
    private CasoTextMapper casoTextMapper;

    @Autowired
    private CasoTextSearchRepository casoTextSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCasoTextMockMvc;

    private CasoText casoText;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasoText createEntity(EntityManager em) {
        CasoText casoText = new CasoText()
            .descripcion(DEFAULT_DESCRIPCION)
            .extra1(DEFAULT_EXTRA_1)
            .extra2(DEFAULT_EXTRA_2)
            .extra3(DEFAULT_EXTRA_3)
            .extra4(DEFAULT_EXTRA_4)
            .extra5(DEFAULT_EXTRA_5)
            .extra6(DEFAULT_EXTRA_6)
            .extra7(DEFAULT_EXTRA_7)
            .extra8(DEFAULT_EXTRA_8)
            .extra9(DEFAULT_EXTRA_9)
            .extra10(DEFAULT_EXTRA_10);
        return casoText;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasoText createUpdatedEntity(EntityManager em) {
        CasoText casoText = new CasoText()
            .descripcion(UPDATED_DESCRIPCION)
            .extra1(UPDATED_EXTRA_1)
            .extra2(UPDATED_EXTRA_2)
            .extra3(UPDATED_EXTRA_3)
            .extra4(UPDATED_EXTRA_4)
            .extra5(UPDATED_EXTRA_5)
            .extra6(UPDATED_EXTRA_6)
            .extra7(UPDATED_EXTRA_7)
            .extra8(UPDATED_EXTRA_8)
            .extra9(UPDATED_EXTRA_9)
            .extra10(UPDATED_EXTRA_10);
        return casoText;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        casoTextSearchRepository.deleteAll();
        assertThat(casoTextSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        casoText = createEntity(em);
    }

    @Test
    @Transactional
    void createCasoText() throws Exception {
        int databaseSizeBeforeCreate = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        // Create the CasoText
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(casoText);
        restCasoTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casoTextDTO)))
            .andExpect(status().isCreated());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        CasoText testCasoText = casoTextList.get(casoTextList.size() - 1);
        assertThat(testCasoText.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCasoText.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testCasoText.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testCasoText.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testCasoText.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testCasoText.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testCasoText.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testCasoText.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testCasoText.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testCasoText.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testCasoText.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void createCasoTextWithExistingId() throws Exception {
        // Create the CasoText with an existing ID
        casoText.setId(1L);
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(casoText);

        int databaseSizeBeforeCreate = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCasoTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casoTextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCasoTexts() throws Exception {
        // Initialize the database
        casoTextRepository.saveAndFlush(casoText);

        // Get all the casoTextList
        restCasoTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casoText.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].extra1").value(hasItem(DEFAULT_EXTRA_1)))
            .andExpect(jsonPath("$.[*].extra2").value(hasItem(DEFAULT_EXTRA_2)))
            .andExpect(jsonPath("$.[*].extra3").value(hasItem(DEFAULT_EXTRA_3)))
            .andExpect(jsonPath("$.[*].extra4").value(hasItem(DEFAULT_EXTRA_4)))
            .andExpect(jsonPath("$.[*].extra5").value(hasItem(DEFAULT_EXTRA_5)))
            .andExpect(jsonPath("$.[*].extra6").value(hasItem(DEFAULT_EXTRA_6)))
            .andExpect(jsonPath("$.[*].extra7").value(hasItem(DEFAULT_EXTRA_7)))
            .andExpect(jsonPath("$.[*].extra8").value(hasItem(DEFAULT_EXTRA_8)))
            .andExpect(jsonPath("$.[*].extra9").value(hasItem(DEFAULT_EXTRA_9)))
            .andExpect(jsonPath("$.[*].extra10").value(hasItem(DEFAULT_EXTRA_10)));
    }

    @Test
    @Transactional
    void getCasoText() throws Exception {
        // Initialize the database
        casoTextRepository.saveAndFlush(casoText);

        // Get the casoText
        restCasoTextMockMvc
            .perform(get(ENTITY_API_URL_ID, casoText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(casoText.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.extra1").value(DEFAULT_EXTRA_1))
            .andExpect(jsonPath("$.extra2").value(DEFAULT_EXTRA_2))
            .andExpect(jsonPath("$.extra3").value(DEFAULT_EXTRA_3))
            .andExpect(jsonPath("$.extra4").value(DEFAULT_EXTRA_4))
            .andExpect(jsonPath("$.extra5").value(DEFAULT_EXTRA_5))
            .andExpect(jsonPath("$.extra6").value(DEFAULT_EXTRA_6))
            .andExpect(jsonPath("$.extra7").value(DEFAULT_EXTRA_7))
            .andExpect(jsonPath("$.extra8").value(DEFAULT_EXTRA_8))
            .andExpect(jsonPath("$.extra9").value(DEFAULT_EXTRA_9))
            .andExpect(jsonPath("$.extra10").value(DEFAULT_EXTRA_10));
    }

    @Test
    @Transactional
    void getNonExistingCasoText() throws Exception {
        // Get the casoText
        restCasoTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCasoText() throws Exception {
        // Initialize the database
        casoTextRepository.saveAndFlush(casoText);

        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();
        casoTextSearchRepository.save(casoText);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());

        // Update the casoText
        CasoText updatedCasoText = casoTextRepository.findById(casoText.getId()).get();
        // Disconnect from session so that the updates on updatedCasoText are not directly saved in db
        em.detach(updatedCasoText);
        updatedCasoText
            .descripcion(UPDATED_DESCRIPCION)
            .extra1(UPDATED_EXTRA_1)
            .extra2(UPDATED_EXTRA_2)
            .extra3(UPDATED_EXTRA_3)
            .extra4(UPDATED_EXTRA_4)
            .extra5(UPDATED_EXTRA_5)
            .extra6(UPDATED_EXTRA_6)
            .extra7(UPDATED_EXTRA_7)
            .extra8(UPDATED_EXTRA_8)
            .extra9(UPDATED_EXTRA_9)
            .extra10(UPDATED_EXTRA_10);
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(updatedCasoText);

        restCasoTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casoTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casoTextDTO))
            )
            .andExpect(status().isOk());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        CasoText testCasoText = casoTextList.get(casoTextList.size() - 1);
        assertThat(testCasoText.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCasoText.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testCasoText.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testCasoText.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testCasoText.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testCasoText.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCasoText.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testCasoText.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testCasoText.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testCasoText.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testCasoText.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CasoText> casoTextSearchList = IterableUtils.toList(casoTextSearchRepository.findAll());
                CasoText testCasoTextSearch = casoTextSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCasoTextSearch.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
                assertThat(testCasoTextSearch.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
                assertThat(testCasoTextSearch.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
                assertThat(testCasoTextSearch.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
                assertThat(testCasoTextSearch.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
                assertThat(testCasoTextSearch.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
                assertThat(testCasoTextSearch.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
                assertThat(testCasoTextSearch.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
                assertThat(testCasoTextSearch.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
                assertThat(testCasoTextSearch.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
                assertThat(testCasoTextSearch.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
            });
    }

    @Test
    @Transactional
    void putNonExistingCasoText() throws Exception {
        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        casoText.setId(count.incrementAndGet());

        // Create the CasoText
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(casoText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasoTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casoTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casoTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCasoText() throws Exception {
        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        casoText.setId(count.incrementAndGet());

        // Create the CasoText
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(casoText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasoTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casoTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCasoText() throws Exception {
        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        casoText.setId(count.incrementAndGet());

        // Create the CasoText
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(casoText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasoTextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casoTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCasoTextWithPatch() throws Exception {
        // Initialize the database
        casoTextRepository.saveAndFlush(casoText);

        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();

        // Update the casoText using partial update
        CasoText partialUpdatedCasoText = new CasoText();
        partialUpdatedCasoText.setId(casoText.getId());

        partialUpdatedCasoText.extra3(UPDATED_EXTRA_3).extra5(UPDATED_EXTRA_5).extra8(UPDATED_EXTRA_8);

        restCasoTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasoText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasoText))
            )
            .andExpect(status().isOk());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        CasoText testCasoText = casoTextList.get(casoTextList.size() - 1);
        assertThat(testCasoText.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCasoText.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testCasoText.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testCasoText.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testCasoText.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testCasoText.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCasoText.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testCasoText.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testCasoText.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testCasoText.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testCasoText.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void fullUpdateCasoTextWithPatch() throws Exception {
        // Initialize the database
        casoTextRepository.saveAndFlush(casoText);

        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();

        // Update the casoText using partial update
        CasoText partialUpdatedCasoText = new CasoText();
        partialUpdatedCasoText.setId(casoText.getId());

        partialUpdatedCasoText
            .descripcion(UPDATED_DESCRIPCION)
            .extra1(UPDATED_EXTRA_1)
            .extra2(UPDATED_EXTRA_2)
            .extra3(UPDATED_EXTRA_3)
            .extra4(UPDATED_EXTRA_4)
            .extra5(UPDATED_EXTRA_5)
            .extra6(UPDATED_EXTRA_6)
            .extra7(UPDATED_EXTRA_7)
            .extra8(UPDATED_EXTRA_8)
            .extra9(UPDATED_EXTRA_9)
            .extra10(UPDATED_EXTRA_10);

        restCasoTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasoText.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasoText))
            )
            .andExpect(status().isOk());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        CasoText testCasoText = casoTextList.get(casoTextList.size() - 1);
        assertThat(testCasoText.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCasoText.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testCasoText.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testCasoText.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testCasoText.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testCasoText.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCasoText.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testCasoText.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testCasoText.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testCasoText.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testCasoText.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void patchNonExistingCasoText() throws Exception {
        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        casoText.setId(count.incrementAndGet());

        // Create the CasoText
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(casoText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasoTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, casoTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casoTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCasoText() throws Exception {
        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        casoText.setId(count.incrementAndGet());

        // Create the CasoText
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(casoText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasoTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casoTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCasoText() throws Exception {
        int databaseSizeBeforeUpdate = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        casoText.setId(count.incrementAndGet());

        // Create the CasoText
        CasoTextDTO casoTextDTO = casoTextMapper.toDto(casoText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasoTextMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(casoTextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CasoText in the database
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCasoText() throws Exception {
        // Initialize the database
        casoTextRepository.saveAndFlush(casoText);
        casoTextRepository.save(casoText);
        casoTextSearchRepository.save(casoText);

        int databaseSizeBeforeDelete = casoTextRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the casoText
        restCasoTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, casoText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CasoText> casoTextList = casoTextRepository.findAll();
        assertThat(casoTextList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCasoText() throws Exception {
        // Initialize the database
        casoText = casoTextRepository.saveAndFlush(casoText);
        casoTextSearchRepository.save(casoText);

        // Search the casoText
        restCasoTextMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + casoText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casoText.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].extra1").value(hasItem(DEFAULT_EXTRA_1)))
            .andExpect(jsonPath("$.[*].extra2").value(hasItem(DEFAULT_EXTRA_2)))
            .andExpect(jsonPath("$.[*].extra3").value(hasItem(DEFAULT_EXTRA_3)))
            .andExpect(jsonPath("$.[*].extra4").value(hasItem(DEFAULT_EXTRA_4)))
            .andExpect(jsonPath("$.[*].extra5").value(hasItem(DEFAULT_EXTRA_5)))
            .andExpect(jsonPath("$.[*].extra6").value(hasItem(DEFAULT_EXTRA_6)))
            .andExpect(jsonPath("$.[*].extra7").value(hasItem(DEFAULT_EXTRA_7)))
            .andExpect(jsonPath("$.[*].extra8").value(hasItem(DEFAULT_EXTRA_8)))
            .andExpect(jsonPath("$.[*].extra9").value(hasItem(DEFAULT_EXTRA_9)))
            .andExpect(jsonPath("$.[*].extra10").value(hasItem(DEFAULT_EXTRA_10)));
    }
}
