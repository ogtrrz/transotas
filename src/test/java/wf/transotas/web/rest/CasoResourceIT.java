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
import wf.transotas.domain.Caso;
import wf.transotas.repository.CasoRepository;
import wf.transotas.repository.search.CasoSearchRepository;
import wf.transotas.service.criteria.CasoCriteria;
import wf.transotas.service.dto.CasoDTO;
import wf.transotas.service.mapper.CasoMapper;

/**
 * Integration tests for the {@link CasoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CasoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/casos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/casos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CasoRepository casoRepository;

    @Autowired
    private CasoMapper casoMapper;

    @Autowired
    private CasoSearchRepository casoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCasoMockMvc;

    private Caso caso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caso createEntity(EntityManager em) {
        Caso caso = new Caso()
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
        return caso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caso createUpdatedEntity(EntityManager em) {
        Caso caso = new Caso()
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
        return caso;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        casoSearchRepository.deleteAll();
        assertThat(casoSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        caso = createEntity(em);
    }

    @Test
    @Transactional
    void createCaso() throws Exception {
        int databaseSizeBeforeCreate = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());
        // Create the Caso
        CasoDTO casoDTO = casoMapper.toDto(caso);
        restCasoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casoDTO)))
            .andExpect(status().isCreated());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Caso testCaso = casoList.get(casoList.size() - 1);
        assertThat(testCaso.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCaso.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testCaso.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testCaso.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testCaso.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testCaso.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testCaso.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testCaso.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testCaso.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testCaso.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testCaso.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void createCasoWithExistingId() throws Exception {
        // Create the Caso with an existing ID
        caso.setId(1L);
        CasoDTO casoDTO = casoMapper.toDto(caso);

        int databaseSizeBeforeCreate = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCasoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCasos() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList
        restCasoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caso.getId().intValue())))
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
    void getCaso() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get the caso
        restCasoMockMvc
            .perform(get(ENTITY_API_URL_ID, caso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caso.getId().intValue()))
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
    void getCasosByIdFiltering() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        Long id = caso.getId();

        defaultCasoShouldBeFound("id.equals=" + id);
        defaultCasoShouldNotBeFound("id.notEquals=" + id);

        defaultCasoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCasoShouldNotBeFound("id.greaterThan=" + id);

        defaultCasoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCasoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCasosByExtra1IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra1 equals to DEFAULT_EXTRA_1
        defaultCasoShouldBeFound("extra1.equals=" + DEFAULT_EXTRA_1);

        // Get all the casoList where extra1 equals to UPDATED_EXTRA_1
        defaultCasoShouldNotBeFound("extra1.equals=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllCasosByExtra1IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra1 in DEFAULT_EXTRA_1 or UPDATED_EXTRA_1
        defaultCasoShouldBeFound("extra1.in=" + DEFAULT_EXTRA_1 + "," + UPDATED_EXTRA_1);

        // Get all the casoList where extra1 equals to UPDATED_EXTRA_1
        defaultCasoShouldNotBeFound("extra1.in=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllCasosByExtra1IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra1 is not null
        defaultCasoShouldBeFound("extra1.specified=true");

        // Get all the casoList where extra1 is null
        defaultCasoShouldNotBeFound("extra1.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra1ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra1 contains DEFAULT_EXTRA_1
        defaultCasoShouldBeFound("extra1.contains=" + DEFAULT_EXTRA_1);

        // Get all the casoList where extra1 contains UPDATED_EXTRA_1
        defaultCasoShouldNotBeFound("extra1.contains=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllCasosByExtra1NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra1 does not contain DEFAULT_EXTRA_1
        defaultCasoShouldNotBeFound("extra1.doesNotContain=" + DEFAULT_EXTRA_1);

        // Get all the casoList where extra1 does not contain UPDATED_EXTRA_1
        defaultCasoShouldBeFound("extra1.doesNotContain=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllCasosByExtra2IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra2 equals to DEFAULT_EXTRA_2
        defaultCasoShouldBeFound("extra2.equals=" + DEFAULT_EXTRA_2);

        // Get all the casoList where extra2 equals to UPDATED_EXTRA_2
        defaultCasoShouldNotBeFound("extra2.equals=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllCasosByExtra2IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra2 in DEFAULT_EXTRA_2 or UPDATED_EXTRA_2
        defaultCasoShouldBeFound("extra2.in=" + DEFAULT_EXTRA_2 + "," + UPDATED_EXTRA_2);

        // Get all the casoList where extra2 equals to UPDATED_EXTRA_2
        defaultCasoShouldNotBeFound("extra2.in=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllCasosByExtra2IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra2 is not null
        defaultCasoShouldBeFound("extra2.specified=true");

        // Get all the casoList where extra2 is null
        defaultCasoShouldNotBeFound("extra2.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra2ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra2 contains DEFAULT_EXTRA_2
        defaultCasoShouldBeFound("extra2.contains=" + DEFAULT_EXTRA_2);

        // Get all the casoList where extra2 contains UPDATED_EXTRA_2
        defaultCasoShouldNotBeFound("extra2.contains=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllCasosByExtra2NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra2 does not contain DEFAULT_EXTRA_2
        defaultCasoShouldNotBeFound("extra2.doesNotContain=" + DEFAULT_EXTRA_2);

        // Get all the casoList where extra2 does not contain UPDATED_EXTRA_2
        defaultCasoShouldBeFound("extra2.doesNotContain=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllCasosByExtra3IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra3 equals to DEFAULT_EXTRA_3
        defaultCasoShouldBeFound("extra3.equals=" + DEFAULT_EXTRA_3);

        // Get all the casoList where extra3 equals to UPDATED_EXTRA_3
        defaultCasoShouldNotBeFound("extra3.equals=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllCasosByExtra3IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra3 in DEFAULT_EXTRA_3 or UPDATED_EXTRA_3
        defaultCasoShouldBeFound("extra3.in=" + DEFAULT_EXTRA_3 + "," + UPDATED_EXTRA_3);

        // Get all the casoList where extra3 equals to UPDATED_EXTRA_3
        defaultCasoShouldNotBeFound("extra3.in=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllCasosByExtra3IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra3 is not null
        defaultCasoShouldBeFound("extra3.specified=true");

        // Get all the casoList where extra3 is null
        defaultCasoShouldNotBeFound("extra3.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra3ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra3 contains DEFAULT_EXTRA_3
        defaultCasoShouldBeFound("extra3.contains=" + DEFAULT_EXTRA_3);

        // Get all the casoList where extra3 contains UPDATED_EXTRA_3
        defaultCasoShouldNotBeFound("extra3.contains=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllCasosByExtra3NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra3 does not contain DEFAULT_EXTRA_3
        defaultCasoShouldNotBeFound("extra3.doesNotContain=" + DEFAULT_EXTRA_3);

        // Get all the casoList where extra3 does not contain UPDATED_EXTRA_3
        defaultCasoShouldBeFound("extra3.doesNotContain=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllCasosByExtra4IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra4 equals to DEFAULT_EXTRA_4
        defaultCasoShouldBeFound("extra4.equals=" + DEFAULT_EXTRA_4);

        // Get all the casoList where extra4 equals to UPDATED_EXTRA_4
        defaultCasoShouldNotBeFound("extra4.equals=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllCasosByExtra4IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra4 in DEFAULT_EXTRA_4 or UPDATED_EXTRA_4
        defaultCasoShouldBeFound("extra4.in=" + DEFAULT_EXTRA_4 + "," + UPDATED_EXTRA_4);

        // Get all the casoList where extra4 equals to UPDATED_EXTRA_4
        defaultCasoShouldNotBeFound("extra4.in=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllCasosByExtra4IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra4 is not null
        defaultCasoShouldBeFound("extra4.specified=true");

        // Get all the casoList where extra4 is null
        defaultCasoShouldNotBeFound("extra4.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra4ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra4 contains DEFAULT_EXTRA_4
        defaultCasoShouldBeFound("extra4.contains=" + DEFAULT_EXTRA_4);

        // Get all the casoList where extra4 contains UPDATED_EXTRA_4
        defaultCasoShouldNotBeFound("extra4.contains=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllCasosByExtra4NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra4 does not contain DEFAULT_EXTRA_4
        defaultCasoShouldNotBeFound("extra4.doesNotContain=" + DEFAULT_EXTRA_4);

        // Get all the casoList where extra4 does not contain UPDATED_EXTRA_4
        defaultCasoShouldBeFound("extra4.doesNotContain=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllCasosByExtra5IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra5 equals to DEFAULT_EXTRA_5
        defaultCasoShouldBeFound("extra5.equals=" + DEFAULT_EXTRA_5);

        // Get all the casoList where extra5 equals to UPDATED_EXTRA_5
        defaultCasoShouldNotBeFound("extra5.equals=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllCasosByExtra5IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra5 in DEFAULT_EXTRA_5 or UPDATED_EXTRA_5
        defaultCasoShouldBeFound("extra5.in=" + DEFAULT_EXTRA_5 + "," + UPDATED_EXTRA_5);

        // Get all the casoList where extra5 equals to UPDATED_EXTRA_5
        defaultCasoShouldNotBeFound("extra5.in=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllCasosByExtra5IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra5 is not null
        defaultCasoShouldBeFound("extra5.specified=true");

        // Get all the casoList where extra5 is null
        defaultCasoShouldNotBeFound("extra5.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra5ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra5 contains DEFAULT_EXTRA_5
        defaultCasoShouldBeFound("extra5.contains=" + DEFAULT_EXTRA_5);

        // Get all the casoList where extra5 contains UPDATED_EXTRA_5
        defaultCasoShouldNotBeFound("extra5.contains=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllCasosByExtra5NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra5 does not contain DEFAULT_EXTRA_5
        defaultCasoShouldNotBeFound("extra5.doesNotContain=" + DEFAULT_EXTRA_5);

        // Get all the casoList where extra5 does not contain UPDATED_EXTRA_5
        defaultCasoShouldBeFound("extra5.doesNotContain=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllCasosByExtra6IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra6 equals to DEFAULT_EXTRA_6
        defaultCasoShouldBeFound("extra6.equals=" + DEFAULT_EXTRA_6);

        // Get all the casoList where extra6 equals to UPDATED_EXTRA_6
        defaultCasoShouldNotBeFound("extra6.equals=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllCasosByExtra6IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra6 in DEFAULT_EXTRA_6 or UPDATED_EXTRA_6
        defaultCasoShouldBeFound("extra6.in=" + DEFAULT_EXTRA_6 + "," + UPDATED_EXTRA_6);

        // Get all the casoList where extra6 equals to UPDATED_EXTRA_6
        defaultCasoShouldNotBeFound("extra6.in=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllCasosByExtra6IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra6 is not null
        defaultCasoShouldBeFound("extra6.specified=true");

        // Get all the casoList where extra6 is null
        defaultCasoShouldNotBeFound("extra6.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra6ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra6 contains DEFAULT_EXTRA_6
        defaultCasoShouldBeFound("extra6.contains=" + DEFAULT_EXTRA_6);

        // Get all the casoList where extra6 contains UPDATED_EXTRA_6
        defaultCasoShouldNotBeFound("extra6.contains=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllCasosByExtra6NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra6 does not contain DEFAULT_EXTRA_6
        defaultCasoShouldNotBeFound("extra6.doesNotContain=" + DEFAULT_EXTRA_6);

        // Get all the casoList where extra6 does not contain UPDATED_EXTRA_6
        defaultCasoShouldBeFound("extra6.doesNotContain=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllCasosByExtra7IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra7 equals to DEFAULT_EXTRA_7
        defaultCasoShouldBeFound("extra7.equals=" + DEFAULT_EXTRA_7);

        // Get all the casoList where extra7 equals to UPDATED_EXTRA_7
        defaultCasoShouldNotBeFound("extra7.equals=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllCasosByExtra7IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra7 in DEFAULT_EXTRA_7 or UPDATED_EXTRA_7
        defaultCasoShouldBeFound("extra7.in=" + DEFAULT_EXTRA_7 + "," + UPDATED_EXTRA_7);

        // Get all the casoList where extra7 equals to UPDATED_EXTRA_7
        defaultCasoShouldNotBeFound("extra7.in=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllCasosByExtra7IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra7 is not null
        defaultCasoShouldBeFound("extra7.specified=true");

        // Get all the casoList where extra7 is null
        defaultCasoShouldNotBeFound("extra7.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra7ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra7 contains DEFAULT_EXTRA_7
        defaultCasoShouldBeFound("extra7.contains=" + DEFAULT_EXTRA_7);

        // Get all the casoList where extra7 contains UPDATED_EXTRA_7
        defaultCasoShouldNotBeFound("extra7.contains=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllCasosByExtra7NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra7 does not contain DEFAULT_EXTRA_7
        defaultCasoShouldNotBeFound("extra7.doesNotContain=" + DEFAULT_EXTRA_7);

        // Get all the casoList where extra7 does not contain UPDATED_EXTRA_7
        defaultCasoShouldBeFound("extra7.doesNotContain=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllCasosByExtra8IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra8 equals to DEFAULT_EXTRA_8
        defaultCasoShouldBeFound("extra8.equals=" + DEFAULT_EXTRA_8);

        // Get all the casoList where extra8 equals to UPDATED_EXTRA_8
        defaultCasoShouldNotBeFound("extra8.equals=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllCasosByExtra8IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra8 in DEFAULT_EXTRA_8 or UPDATED_EXTRA_8
        defaultCasoShouldBeFound("extra8.in=" + DEFAULT_EXTRA_8 + "," + UPDATED_EXTRA_8);

        // Get all the casoList where extra8 equals to UPDATED_EXTRA_8
        defaultCasoShouldNotBeFound("extra8.in=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllCasosByExtra8IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra8 is not null
        defaultCasoShouldBeFound("extra8.specified=true");

        // Get all the casoList where extra8 is null
        defaultCasoShouldNotBeFound("extra8.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra8ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra8 contains DEFAULT_EXTRA_8
        defaultCasoShouldBeFound("extra8.contains=" + DEFAULT_EXTRA_8);

        // Get all the casoList where extra8 contains UPDATED_EXTRA_8
        defaultCasoShouldNotBeFound("extra8.contains=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllCasosByExtra8NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra8 does not contain DEFAULT_EXTRA_8
        defaultCasoShouldNotBeFound("extra8.doesNotContain=" + DEFAULT_EXTRA_8);

        // Get all the casoList where extra8 does not contain UPDATED_EXTRA_8
        defaultCasoShouldBeFound("extra8.doesNotContain=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllCasosByExtra9IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra9 equals to DEFAULT_EXTRA_9
        defaultCasoShouldBeFound("extra9.equals=" + DEFAULT_EXTRA_9);

        // Get all the casoList where extra9 equals to UPDATED_EXTRA_9
        defaultCasoShouldNotBeFound("extra9.equals=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllCasosByExtra9IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra9 in DEFAULT_EXTRA_9 or UPDATED_EXTRA_9
        defaultCasoShouldBeFound("extra9.in=" + DEFAULT_EXTRA_9 + "," + UPDATED_EXTRA_9);

        // Get all the casoList where extra9 equals to UPDATED_EXTRA_9
        defaultCasoShouldNotBeFound("extra9.in=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllCasosByExtra9IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra9 is not null
        defaultCasoShouldBeFound("extra9.specified=true");

        // Get all the casoList where extra9 is null
        defaultCasoShouldNotBeFound("extra9.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra9ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra9 contains DEFAULT_EXTRA_9
        defaultCasoShouldBeFound("extra9.contains=" + DEFAULT_EXTRA_9);

        // Get all the casoList where extra9 contains UPDATED_EXTRA_9
        defaultCasoShouldNotBeFound("extra9.contains=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllCasosByExtra9NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra9 does not contain DEFAULT_EXTRA_9
        defaultCasoShouldNotBeFound("extra9.doesNotContain=" + DEFAULT_EXTRA_9);

        // Get all the casoList where extra9 does not contain UPDATED_EXTRA_9
        defaultCasoShouldBeFound("extra9.doesNotContain=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllCasosByExtra10IsEqualToSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra10 equals to DEFAULT_EXTRA_10
        defaultCasoShouldBeFound("extra10.equals=" + DEFAULT_EXTRA_10);

        // Get all the casoList where extra10 equals to UPDATED_EXTRA_10
        defaultCasoShouldNotBeFound("extra10.equals=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllCasosByExtra10IsInShouldWork() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra10 in DEFAULT_EXTRA_10 or UPDATED_EXTRA_10
        defaultCasoShouldBeFound("extra10.in=" + DEFAULT_EXTRA_10 + "," + UPDATED_EXTRA_10);

        // Get all the casoList where extra10 equals to UPDATED_EXTRA_10
        defaultCasoShouldNotBeFound("extra10.in=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllCasosByExtra10IsNullOrNotNull() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra10 is not null
        defaultCasoShouldBeFound("extra10.specified=true");

        // Get all the casoList where extra10 is null
        defaultCasoShouldNotBeFound("extra10.specified=false");
    }

    @Test
    @Transactional
    void getAllCasosByExtra10ContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra10 contains DEFAULT_EXTRA_10
        defaultCasoShouldBeFound("extra10.contains=" + DEFAULT_EXTRA_10);

        // Get all the casoList where extra10 contains UPDATED_EXTRA_10
        defaultCasoShouldNotBeFound("extra10.contains=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllCasosByExtra10NotContainsSomething() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        // Get all the casoList where extra10 does not contain DEFAULT_EXTRA_10
        defaultCasoShouldNotBeFound("extra10.doesNotContain=" + DEFAULT_EXTRA_10);

        // Get all the casoList where extra10 does not contain UPDATED_EXTRA_10
        defaultCasoShouldBeFound("extra10.doesNotContain=" + UPDATED_EXTRA_10);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCasoShouldBeFound(String filter) throws Exception {
        restCasoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caso.getId().intValue())))
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

        // Check, that the count call also returns 1
        restCasoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCasoShouldNotBeFound(String filter) throws Exception {
        restCasoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCasoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCaso() throws Exception {
        // Get the caso
        restCasoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaso() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        int databaseSizeBeforeUpdate = casoRepository.findAll().size();
        casoSearchRepository.save(caso);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());

        // Update the caso
        Caso updatedCaso = casoRepository.findById(caso.getId()).get();
        // Disconnect from session so that the updates on updatedCaso are not directly saved in db
        em.detach(updatedCaso);
        updatedCaso
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
        CasoDTO casoDTO = casoMapper.toDto(updatedCaso);

        restCasoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        Caso testCaso = casoList.get(casoList.size() - 1);
        assertThat(testCaso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCaso.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testCaso.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testCaso.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testCaso.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testCaso.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCaso.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testCaso.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testCaso.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testCaso.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testCaso.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Caso> casoSearchList = IterableUtils.toList(casoSearchRepository.findAll());
                Caso testCasoSearch = casoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCasoSearch.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
                assertThat(testCasoSearch.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
                assertThat(testCasoSearch.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
                assertThat(testCasoSearch.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
                assertThat(testCasoSearch.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
                assertThat(testCasoSearch.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
                assertThat(testCasoSearch.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
                assertThat(testCasoSearch.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
                assertThat(testCasoSearch.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
                assertThat(testCasoSearch.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
                assertThat(testCasoSearch.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
            });
    }

    @Test
    @Transactional
    void putNonExistingCaso() throws Exception {
        int databaseSizeBeforeUpdate = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());
        caso.setId(count.incrementAndGet());

        // Create the Caso
        CasoDTO casoDTO = casoMapper.toDto(caso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaso() throws Exception {
        int databaseSizeBeforeUpdate = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());
        caso.setId(count.incrementAndGet());

        // Create the Caso
        CasoDTO casoDTO = casoMapper.toDto(caso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaso() throws Exception {
        int databaseSizeBeforeUpdate = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());
        caso.setId(count.incrementAndGet());

        // Create the Caso
        CasoDTO casoDTO = casoMapper.toDto(caso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(casoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCasoWithPatch() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        int databaseSizeBeforeUpdate = casoRepository.findAll().size();

        // Update the caso using partial update
        Caso partialUpdatedCaso = new Caso();
        partialUpdatedCaso.setId(caso.getId());

        partialUpdatedCaso
            .descripcion(UPDATED_DESCRIPCION)
            .extra1(UPDATED_EXTRA_1)
            .extra2(UPDATED_EXTRA_2)
            .extra4(UPDATED_EXTRA_4)
            .extra5(UPDATED_EXTRA_5)
            .extra7(UPDATED_EXTRA_7)
            .extra10(UPDATED_EXTRA_10);

        restCasoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaso))
            )
            .andExpect(status().isOk());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        Caso testCaso = casoList.get(casoList.size() - 1);
        assertThat(testCaso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCaso.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testCaso.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testCaso.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testCaso.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testCaso.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCaso.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testCaso.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testCaso.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testCaso.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testCaso.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void fullUpdateCasoWithPatch() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);

        int databaseSizeBeforeUpdate = casoRepository.findAll().size();

        // Update the caso using partial update
        Caso partialUpdatedCaso = new Caso();
        partialUpdatedCaso.setId(caso.getId());

        partialUpdatedCaso
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

        restCasoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaso))
            )
            .andExpect(status().isOk());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        Caso testCaso = casoList.get(casoList.size() - 1);
        assertThat(testCaso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCaso.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testCaso.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testCaso.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testCaso.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testCaso.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCaso.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testCaso.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testCaso.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testCaso.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testCaso.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void patchNonExistingCaso() throws Exception {
        int databaseSizeBeforeUpdate = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());
        caso.setId(count.incrementAndGet());

        // Create the Caso
        CasoDTO casoDTO = casoMapper.toDto(caso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, casoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaso() throws Exception {
        int databaseSizeBeforeUpdate = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());
        caso.setId(count.incrementAndGet());

        // Create the Caso
        CasoDTO casoDTO = casoMapper.toDto(caso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaso() throws Exception {
        int databaseSizeBeforeUpdate = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());
        caso.setId(count.incrementAndGet());

        // Create the Caso
        CasoDTO casoDTO = casoMapper.toDto(caso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(casoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caso in the database
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCaso() throws Exception {
        // Initialize the database
        casoRepository.saveAndFlush(caso);
        casoRepository.save(caso);
        casoSearchRepository.save(caso);

        int databaseSizeBeforeDelete = casoRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the caso
        restCasoMockMvc
            .perform(delete(ENTITY_API_URL_ID, caso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caso> casoList = casoRepository.findAll();
        assertThat(casoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(casoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCaso() throws Exception {
        // Initialize the database
        caso = casoRepository.saveAndFlush(caso);
        casoSearchRepository.save(caso);

        // Search the caso
        restCasoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + caso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caso.getId().intValue())))
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
