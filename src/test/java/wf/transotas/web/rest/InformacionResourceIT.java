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
import wf.transotas.IntegrationTest;
import wf.transotas.domain.Informacion;
import wf.transotas.domain.Reportes;
import wf.transotas.repository.InformacionRepository;
import wf.transotas.repository.search.InformacionSearchRepository;
import wf.transotas.service.criteria.InformacionCriteria;
import wf.transotas.service.dto.InformacionDTO;
import wf.transotas.service.mapper.InformacionMapper;

/**
 * Integration tests for the {@link InformacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InformacionResourceIT {

    private static final Integer DEFAULT_COMENTARIOS = 1;
    private static final Integer UPDATED_COMENTARIOS = 2;
    private static final Integer SMALLER_COMENTARIOS = 1 - 1;

    private static final Integer DEFAULT_VISTAS = 1;
    private static final Integer UPDATED_VISTAS = 2;
    private static final Integer SMALLER_VISTAS = 1 - 1;

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

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

    private static final String ENTITY_API_URL = "/api/informacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/informacions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InformacionRepository informacionRepository;

    @Autowired
    private InformacionMapper informacionMapper;

    @Autowired
    private InformacionSearchRepository informacionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformacionMockMvc;

    private Informacion informacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Informacion createEntity(EntityManager em) {
        Informacion informacion = new Informacion()
            .comentarios(DEFAULT_COMENTARIOS)
            .vistas(DEFAULT_VISTAS)
            .rating(DEFAULT_RATING)
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
        return informacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Informacion createUpdatedEntity(EntityManager em) {
        Informacion informacion = new Informacion()
            .comentarios(UPDATED_COMENTARIOS)
            .vistas(UPDATED_VISTAS)
            .rating(UPDATED_RATING)
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
        return informacion;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        informacionSearchRepository.deleteAll();
        assertThat(informacionSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        informacion = createEntity(em);
    }

    @Test
    @Transactional
    void createInformacion() throws Exception {
        int databaseSizeBeforeCreate = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        // Create the Informacion
        InformacionDTO informacionDTO = informacionMapper.toDto(informacion);
        restInformacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(informacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Informacion testInformacion = informacionList.get(informacionList.size() - 1);
        assertThat(testInformacion.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testInformacion.getVistas()).isEqualTo(DEFAULT_VISTAS);
        assertThat(testInformacion.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testInformacion.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testInformacion.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testInformacion.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testInformacion.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testInformacion.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testInformacion.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testInformacion.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testInformacion.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testInformacion.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testInformacion.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void createInformacionWithExistingId() throws Exception {
        // Create the Informacion with an existing ID
        informacion.setId(1L);
        InformacionDTO informacionDTO = informacionMapper.toDto(informacion);

        int databaseSizeBeforeCreate = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(informacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllInformacions() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList
        restInformacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].vistas").value(hasItem(DEFAULT_VISTAS)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
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
    void getInformacion() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get the informacion
        restInformacionMockMvc
            .perform(get(ENTITY_API_URL_ID, informacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(informacion.getId().intValue()))
            .andExpect(jsonPath("$.comentarios").value(DEFAULT_COMENTARIOS))
            .andExpect(jsonPath("$.vistas").value(DEFAULT_VISTAS))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
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
    void getInformacionsByIdFiltering() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        Long id = informacion.getId();

        defaultInformacionShouldBeFound("id.equals=" + id);
        defaultInformacionShouldNotBeFound("id.notEquals=" + id);

        defaultInformacionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInformacionShouldNotBeFound("id.greaterThan=" + id);

        defaultInformacionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInformacionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInformacionsByComentariosIsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where comentarios equals to DEFAULT_COMENTARIOS
        defaultInformacionShouldBeFound("comentarios.equals=" + DEFAULT_COMENTARIOS);

        // Get all the informacionList where comentarios equals to UPDATED_COMENTARIOS
        defaultInformacionShouldNotBeFound("comentarios.equals=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllInformacionsByComentariosIsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where comentarios in DEFAULT_COMENTARIOS or UPDATED_COMENTARIOS
        defaultInformacionShouldBeFound("comentarios.in=" + DEFAULT_COMENTARIOS + "," + UPDATED_COMENTARIOS);

        // Get all the informacionList where comentarios equals to UPDATED_COMENTARIOS
        defaultInformacionShouldNotBeFound("comentarios.in=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllInformacionsByComentariosIsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where comentarios is not null
        defaultInformacionShouldBeFound("comentarios.specified=true");

        // Get all the informacionList where comentarios is null
        defaultInformacionShouldNotBeFound("comentarios.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByComentariosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where comentarios is greater than or equal to DEFAULT_COMENTARIOS
        defaultInformacionShouldBeFound("comentarios.greaterThanOrEqual=" + DEFAULT_COMENTARIOS);

        // Get all the informacionList where comentarios is greater than or equal to UPDATED_COMENTARIOS
        defaultInformacionShouldNotBeFound("comentarios.greaterThanOrEqual=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllInformacionsByComentariosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where comentarios is less than or equal to DEFAULT_COMENTARIOS
        defaultInformacionShouldBeFound("comentarios.lessThanOrEqual=" + DEFAULT_COMENTARIOS);

        // Get all the informacionList where comentarios is less than or equal to SMALLER_COMENTARIOS
        defaultInformacionShouldNotBeFound("comentarios.lessThanOrEqual=" + SMALLER_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllInformacionsByComentariosIsLessThanSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where comentarios is less than DEFAULT_COMENTARIOS
        defaultInformacionShouldNotBeFound("comentarios.lessThan=" + DEFAULT_COMENTARIOS);

        // Get all the informacionList where comentarios is less than UPDATED_COMENTARIOS
        defaultInformacionShouldBeFound("comentarios.lessThan=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllInformacionsByComentariosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where comentarios is greater than DEFAULT_COMENTARIOS
        defaultInformacionShouldNotBeFound("comentarios.greaterThan=" + DEFAULT_COMENTARIOS);

        // Get all the informacionList where comentarios is greater than SMALLER_COMENTARIOS
        defaultInformacionShouldBeFound("comentarios.greaterThan=" + SMALLER_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllInformacionsByVistasIsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where vistas equals to DEFAULT_VISTAS
        defaultInformacionShouldBeFound("vistas.equals=" + DEFAULT_VISTAS);

        // Get all the informacionList where vistas equals to UPDATED_VISTAS
        defaultInformacionShouldNotBeFound("vistas.equals=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllInformacionsByVistasIsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where vistas in DEFAULT_VISTAS or UPDATED_VISTAS
        defaultInformacionShouldBeFound("vistas.in=" + DEFAULT_VISTAS + "," + UPDATED_VISTAS);

        // Get all the informacionList where vistas equals to UPDATED_VISTAS
        defaultInformacionShouldNotBeFound("vistas.in=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllInformacionsByVistasIsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where vistas is not null
        defaultInformacionShouldBeFound("vistas.specified=true");

        // Get all the informacionList where vistas is null
        defaultInformacionShouldNotBeFound("vistas.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByVistasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where vistas is greater than or equal to DEFAULT_VISTAS
        defaultInformacionShouldBeFound("vistas.greaterThanOrEqual=" + DEFAULT_VISTAS);

        // Get all the informacionList where vistas is greater than or equal to UPDATED_VISTAS
        defaultInformacionShouldNotBeFound("vistas.greaterThanOrEqual=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllInformacionsByVistasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where vistas is less than or equal to DEFAULT_VISTAS
        defaultInformacionShouldBeFound("vistas.lessThanOrEqual=" + DEFAULT_VISTAS);

        // Get all the informacionList where vistas is less than or equal to SMALLER_VISTAS
        defaultInformacionShouldNotBeFound("vistas.lessThanOrEqual=" + SMALLER_VISTAS);
    }

    @Test
    @Transactional
    void getAllInformacionsByVistasIsLessThanSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where vistas is less than DEFAULT_VISTAS
        defaultInformacionShouldNotBeFound("vistas.lessThan=" + DEFAULT_VISTAS);

        // Get all the informacionList where vistas is less than UPDATED_VISTAS
        defaultInformacionShouldBeFound("vistas.lessThan=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllInformacionsByVistasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where vistas is greater than DEFAULT_VISTAS
        defaultInformacionShouldNotBeFound("vistas.greaterThan=" + DEFAULT_VISTAS);

        // Get all the informacionList where vistas is greater than SMALLER_VISTAS
        defaultInformacionShouldBeFound("vistas.greaterThan=" + SMALLER_VISTAS);
    }

    @Test
    @Transactional
    void getAllInformacionsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where rating equals to DEFAULT_RATING
        defaultInformacionShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the informacionList where rating equals to UPDATED_RATING
        defaultInformacionShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllInformacionsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultInformacionShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the informacionList where rating equals to UPDATED_RATING
        defaultInformacionShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllInformacionsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where rating is not null
        defaultInformacionShouldBeFound("rating.specified=true");

        // Get all the informacionList where rating is null
        defaultInformacionShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where rating is greater than or equal to DEFAULT_RATING
        defaultInformacionShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the informacionList where rating is greater than or equal to UPDATED_RATING
        defaultInformacionShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllInformacionsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where rating is less than or equal to DEFAULT_RATING
        defaultInformacionShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the informacionList where rating is less than or equal to SMALLER_RATING
        defaultInformacionShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllInformacionsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where rating is less than DEFAULT_RATING
        defaultInformacionShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the informacionList where rating is less than UPDATED_RATING
        defaultInformacionShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllInformacionsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where rating is greater than DEFAULT_RATING
        defaultInformacionShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the informacionList where rating is greater than SMALLER_RATING
        defaultInformacionShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra1IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra1 equals to DEFAULT_EXTRA_1
        defaultInformacionShouldBeFound("extra1.equals=" + DEFAULT_EXTRA_1);

        // Get all the informacionList where extra1 equals to UPDATED_EXTRA_1
        defaultInformacionShouldNotBeFound("extra1.equals=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra1IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra1 in DEFAULT_EXTRA_1 or UPDATED_EXTRA_1
        defaultInformacionShouldBeFound("extra1.in=" + DEFAULT_EXTRA_1 + "," + UPDATED_EXTRA_1);

        // Get all the informacionList where extra1 equals to UPDATED_EXTRA_1
        defaultInformacionShouldNotBeFound("extra1.in=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra1IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra1 is not null
        defaultInformacionShouldBeFound("extra1.specified=true");

        // Get all the informacionList where extra1 is null
        defaultInformacionShouldNotBeFound("extra1.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra1ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra1 contains DEFAULT_EXTRA_1
        defaultInformacionShouldBeFound("extra1.contains=" + DEFAULT_EXTRA_1);

        // Get all the informacionList where extra1 contains UPDATED_EXTRA_1
        defaultInformacionShouldNotBeFound("extra1.contains=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra1NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra1 does not contain DEFAULT_EXTRA_1
        defaultInformacionShouldNotBeFound("extra1.doesNotContain=" + DEFAULT_EXTRA_1);

        // Get all the informacionList where extra1 does not contain UPDATED_EXTRA_1
        defaultInformacionShouldBeFound("extra1.doesNotContain=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra2IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra2 equals to DEFAULT_EXTRA_2
        defaultInformacionShouldBeFound("extra2.equals=" + DEFAULT_EXTRA_2);

        // Get all the informacionList where extra2 equals to UPDATED_EXTRA_2
        defaultInformacionShouldNotBeFound("extra2.equals=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra2IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra2 in DEFAULT_EXTRA_2 or UPDATED_EXTRA_2
        defaultInformacionShouldBeFound("extra2.in=" + DEFAULT_EXTRA_2 + "," + UPDATED_EXTRA_2);

        // Get all the informacionList where extra2 equals to UPDATED_EXTRA_2
        defaultInformacionShouldNotBeFound("extra2.in=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra2IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra2 is not null
        defaultInformacionShouldBeFound("extra2.specified=true");

        // Get all the informacionList where extra2 is null
        defaultInformacionShouldNotBeFound("extra2.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra2ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra2 contains DEFAULT_EXTRA_2
        defaultInformacionShouldBeFound("extra2.contains=" + DEFAULT_EXTRA_2);

        // Get all the informacionList where extra2 contains UPDATED_EXTRA_2
        defaultInformacionShouldNotBeFound("extra2.contains=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra2NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra2 does not contain DEFAULT_EXTRA_2
        defaultInformacionShouldNotBeFound("extra2.doesNotContain=" + DEFAULT_EXTRA_2);

        // Get all the informacionList where extra2 does not contain UPDATED_EXTRA_2
        defaultInformacionShouldBeFound("extra2.doesNotContain=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra3IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra3 equals to DEFAULT_EXTRA_3
        defaultInformacionShouldBeFound("extra3.equals=" + DEFAULT_EXTRA_3);

        // Get all the informacionList where extra3 equals to UPDATED_EXTRA_3
        defaultInformacionShouldNotBeFound("extra3.equals=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra3IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra3 in DEFAULT_EXTRA_3 or UPDATED_EXTRA_3
        defaultInformacionShouldBeFound("extra3.in=" + DEFAULT_EXTRA_3 + "," + UPDATED_EXTRA_3);

        // Get all the informacionList where extra3 equals to UPDATED_EXTRA_3
        defaultInformacionShouldNotBeFound("extra3.in=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra3IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra3 is not null
        defaultInformacionShouldBeFound("extra3.specified=true");

        // Get all the informacionList where extra3 is null
        defaultInformacionShouldNotBeFound("extra3.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra3ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra3 contains DEFAULT_EXTRA_3
        defaultInformacionShouldBeFound("extra3.contains=" + DEFAULT_EXTRA_3);

        // Get all the informacionList where extra3 contains UPDATED_EXTRA_3
        defaultInformacionShouldNotBeFound("extra3.contains=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra3NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra3 does not contain DEFAULT_EXTRA_3
        defaultInformacionShouldNotBeFound("extra3.doesNotContain=" + DEFAULT_EXTRA_3);

        // Get all the informacionList where extra3 does not contain UPDATED_EXTRA_3
        defaultInformacionShouldBeFound("extra3.doesNotContain=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra4IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra4 equals to DEFAULT_EXTRA_4
        defaultInformacionShouldBeFound("extra4.equals=" + DEFAULT_EXTRA_4);

        // Get all the informacionList where extra4 equals to UPDATED_EXTRA_4
        defaultInformacionShouldNotBeFound("extra4.equals=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra4IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra4 in DEFAULT_EXTRA_4 or UPDATED_EXTRA_4
        defaultInformacionShouldBeFound("extra4.in=" + DEFAULT_EXTRA_4 + "," + UPDATED_EXTRA_4);

        // Get all the informacionList where extra4 equals to UPDATED_EXTRA_4
        defaultInformacionShouldNotBeFound("extra4.in=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra4IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra4 is not null
        defaultInformacionShouldBeFound("extra4.specified=true");

        // Get all the informacionList where extra4 is null
        defaultInformacionShouldNotBeFound("extra4.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra4ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra4 contains DEFAULT_EXTRA_4
        defaultInformacionShouldBeFound("extra4.contains=" + DEFAULT_EXTRA_4);

        // Get all the informacionList where extra4 contains UPDATED_EXTRA_4
        defaultInformacionShouldNotBeFound("extra4.contains=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra4NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra4 does not contain DEFAULT_EXTRA_4
        defaultInformacionShouldNotBeFound("extra4.doesNotContain=" + DEFAULT_EXTRA_4);

        // Get all the informacionList where extra4 does not contain UPDATED_EXTRA_4
        defaultInformacionShouldBeFound("extra4.doesNotContain=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra5IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra5 equals to DEFAULT_EXTRA_5
        defaultInformacionShouldBeFound("extra5.equals=" + DEFAULT_EXTRA_5);

        // Get all the informacionList where extra5 equals to UPDATED_EXTRA_5
        defaultInformacionShouldNotBeFound("extra5.equals=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra5IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra5 in DEFAULT_EXTRA_5 or UPDATED_EXTRA_5
        defaultInformacionShouldBeFound("extra5.in=" + DEFAULT_EXTRA_5 + "," + UPDATED_EXTRA_5);

        // Get all the informacionList where extra5 equals to UPDATED_EXTRA_5
        defaultInformacionShouldNotBeFound("extra5.in=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra5IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra5 is not null
        defaultInformacionShouldBeFound("extra5.specified=true");

        // Get all the informacionList where extra5 is null
        defaultInformacionShouldNotBeFound("extra5.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra5ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra5 contains DEFAULT_EXTRA_5
        defaultInformacionShouldBeFound("extra5.contains=" + DEFAULT_EXTRA_5);

        // Get all the informacionList where extra5 contains UPDATED_EXTRA_5
        defaultInformacionShouldNotBeFound("extra5.contains=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra5NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra5 does not contain DEFAULT_EXTRA_5
        defaultInformacionShouldNotBeFound("extra5.doesNotContain=" + DEFAULT_EXTRA_5);

        // Get all the informacionList where extra5 does not contain UPDATED_EXTRA_5
        defaultInformacionShouldBeFound("extra5.doesNotContain=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra6IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra6 equals to DEFAULT_EXTRA_6
        defaultInformacionShouldBeFound("extra6.equals=" + DEFAULT_EXTRA_6);

        // Get all the informacionList where extra6 equals to UPDATED_EXTRA_6
        defaultInformacionShouldNotBeFound("extra6.equals=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra6IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra6 in DEFAULT_EXTRA_6 or UPDATED_EXTRA_6
        defaultInformacionShouldBeFound("extra6.in=" + DEFAULT_EXTRA_6 + "," + UPDATED_EXTRA_6);

        // Get all the informacionList where extra6 equals to UPDATED_EXTRA_6
        defaultInformacionShouldNotBeFound("extra6.in=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra6IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra6 is not null
        defaultInformacionShouldBeFound("extra6.specified=true");

        // Get all the informacionList where extra6 is null
        defaultInformacionShouldNotBeFound("extra6.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra6ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra6 contains DEFAULT_EXTRA_6
        defaultInformacionShouldBeFound("extra6.contains=" + DEFAULT_EXTRA_6);

        // Get all the informacionList where extra6 contains UPDATED_EXTRA_6
        defaultInformacionShouldNotBeFound("extra6.contains=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra6NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra6 does not contain DEFAULT_EXTRA_6
        defaultInformacionShouldNotBeFound("extra6.doesNotContain=" + DEFAULT_EXTRA_6);

        // Get all the informacionList where extra6 does not contain UPDATED_EXTRA_6
        defaultInformacionShouldBeFound("extra6.doesNotContain=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra7IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra7 equals to DEFAULT_EXTRA_7
        defaultInformacionShouldBeFound("extra7.equals=" + DEFAULT_EXTRA_7);

        // Get all the informacionList where extra7 equals to UPDATED_EXTRA_7
        defaultInformacionShouldNotBeFound("extra7.equals=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra7IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra7 in DEFAULT_EXTRA_7 or UPDATED_EXTRA_7
        defaultInformacionShouldBeFound("extra7.in=" + DEFAULT_EXTRA_7 + "," + UPDATED_EXTRA_7);

        // Get all the informacionList where extra7 equals to UPDATED_EXTRA_7
        defaultInformacionShouldNotBeFound("extra7.in=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra7IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra7 is not null
        defaultInformacionShouldBeFound("extra7.specified=true");

        // Get all the informacionList where extra7 is null
        defaultInformacionShouldNotBeFound("extra7.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra7ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra7 contains DEFAULT_EXTRA_7
        defaultInformacionShouldBeFound("extra7.contains=" + DEFAULT_EXTRA_7);

        // Get all the informacionList where extra7 contains UPDATED_EXTRA_7
        defaultInformacionShouldNotBeFound("extra7.contains=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra7NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra7 does not contain DEFAULT_EXTRA_7
        defaultInformacionShouldNotBeFound("extra7.doesNotContain=" + DEFAULT_EXTRA_7);

        // Get all the informacionList where extra7 does not contain UPDATED_EXTRA_7
        defaultInformacionShouldBeFound("extra7.doesNotContain=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra8IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra8 equals to DEFAULT_EXTRA_8
        defaultInformacionShouldBeFound("extra8.equals=" + DEFAULT_EXTRA_8);

        // Get all the informacionList where extra8 equals to UPDATED_EXTRA_8
        defaultInformacionShouldNotBeFound("extra8.equals=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra8IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra8 in DEFAULT_EXTRA_8 or UPDATED_EXTRA_8
        defaultInformacionShouldBeFound("extra8.in=" + DEFAULT_EXTRA_8 + "," + UPDATED_EXTRA_8);

        // Get all the informacionList where extra8 equals to UPDATED_EXTRA_8
        defaultInformacionShouldNotBeFound("extra8.in=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra8IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra8 is not null
        defaultInformacionShouldBeFound("extra8.specified=true");

        // Get all the informacionList where extra8 is null
        defaultInformacionShouldNotBeFound("extra8.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra8ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra8 contains DEFAULT_EXTRA_8
        defaultInformacionShouldBeFound("extra8.contains=" + DEFAULT_EXTRA_8);

        // Get all the informacionList where extra8 contains UPDATED_EXTRA_8
        defaultInformacionShouldNotBeFound("extra8.contains=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra8NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra8 does not contain DEFAULT_EXTRA_8
        defaultInformacionShouldNotBeFound("extra8.doesNotContain=" + DEFAULT_EXTRA_8);

        // Get all the informacionList where extra8 does not contain UPDATED_EXTRA_8
        defaultInformacionShouldBeFound("extra8.doesNotContain=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra9IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra9 equals to DEFAULT_EXTRA_9
        defaultInformacionShouldBeFound("extra9.equals=" + DEFAULT_EXTRA_9);

        // Get all the informacionList where extra9 equals to UPDATED_EXTRA_9
        defaultInformacionShouldNotBeFound("extra9.equals=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra9IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra9 in DEFAULT_EXTRA_9 or UPDATED_EXTRA_9
        defaultInformacionShouldBeFound("extra9.in=" + DEFAULT_EXTRA_9 + "," + UPDATED_EXTRA_9);

        // Get all the informacionList where extra9 equals to UPDATED_EXTRA_9
        defaultInformacionShouldNotBeFound("extra9.in=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra9IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra9 is not null
        defaultInformacionShouldBeFound("extra9.specified=true");

        // Get all the informacionList where extra9 is null
        defaultInformacionShouldNotBeFound("extra9.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra9ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra9 contains DEFAULT_EXTRA_9
        defaultInformacionShouldBeFound("extra9.contains=" + DEFAULT_EXTRA_9);

        // Get all the informacionList where extra9 contains UPDATED_EXTRA_9
        defaultInformacionShouldNotBeFound("extra9.contains=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra9NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra9 does not contain DEFAULT_EXTRA_9
        defaultInformacionShouldNotBeFound("extra9.doesNotContain=" + DEFAULT_EXTRA_9);

        // Get all the informacionList where extra9 does not contain UPDATED_EXTRA_9
        defaultInformacionShouldBeFound("extra9.doesNotContain=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra10IsEqualToSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra10 equals to DEFAULT_EXTRA_10
        defaultInformacionShouldBeFound("extra10.equals=" + DEFAULT_EXTRA_10);

        // Get all the informacionList where extra10 equals to UPDATED_EXTRA_10
        defaultInformacionShouldNotBeFound("extra10.equals=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra10IsInShouldWork() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra10 in DEFAULT_EXTRA_10 or UPDATED_EXTRA_10
        defaultInformacionShouldBeFound("extra10.in=" + DEFAULT_EXTRA_10 + "," + UPDATED_EXTRA_10);

        // Get all the informacionList where extra10 equals to UPDATED_EXTRA_10
        defaultInformacionShouldNotBeFound("extra10.in=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra10IsNullOrNotNull() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra10 is not null
        defaultInformacionShouldBeFound("extra10.specified=true");

        // Get all the informacionList where extra10 is null
        defaultInformacionShouldNotBeFound("extra10.specified=false");
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra10ContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra10 contains DEFAULT_EXTRA_10
        defaultInformacionShouldBeFound("extra10.contains=" + DEFAULT_EXTRA_10);

        // Get all the informacionList where extra10 contains UPDATED_EXTRA_10
        defaultInformacionShouldNotBeFound("extra10.contains=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllInformacionsByExtra10NotContainsSomething() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        // Get all the informacionList where extra10 does not contain DEFAULT_EXTRA_10
        defaultInformacionShouldNotBeFound("extra10.doesNotContain=" + DEFAULT_EXTRA_10);

        // Get all the informacionList where extra10 does not contain UPDATED_EXTRA_10
        defaultInformacionShouldBeFound("extra10.doesNotContain=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllInformacionsByReportesIsEqualToSomething() throws Exception {
        Reportes reportes;
        if (TestUtil.findAll(em, Reportes.class).isEmpty()) {
            informacionRepository.saveAndFlush(informacion);
            reportes = ReportesResourceIT.createEntity(em);
        } else {
            reportes = TestUtil.findAll(em, Reportes.class).get(0);
        }
        em.persist(reportes);
        em.flush();
        informacion.setReportes(reportes);
        informacionRepository.saveAndFlush(informacion);
        Long reportesId = reportes.getId();

        // Get all the informacionList where reportes equals to reportesId
        defaultInformacionShouldBeFound("reportesId.equals=" + reportesId);

        // Get all the informacionList where reportes equals to (reportesId + 1)
        defaultInformacionShouldNotBeFound("reportesId.equals=" + (reportesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInformacionShouldBeFound(String filter) throws Exception {
        restInformacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].vistas").value(hasItem(DEFAULT_VISTAS)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
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
        restInformacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInformacionShouldNotBeFound(String filter) throws Exception {
        restInformacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInformacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInformacion() throws Exception {
        // Get the informacion
        restInformacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInformacion() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();
        informacionSearchRepository.save(informacion);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());

        // Update the informacion
        Informacion updatedInformacion = informacionRepository.findById(informacion.getId()).get();
        // Disconnect from session so that the updates on updatedInformacion are not directly saved in db
        em.detach(updatedInformacion);
        updatedInformacion
            .comentarios(UPDATED_COMENTARIOS)
            .vistas(UPDATED_VISTAS)
            .rating(UPDATED_RATING)
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
        InformacionDTO informacionDTO = informacionMapper.toDto(updatedInformacion);

        restInformacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        Informacion testInformacion = informacionList.get(informacionList.size() - 1);
        assertThat(testInformacion.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testInformacion.getVistas()).isEqualTo(UPDATED_VISTAS);
        assertThat(testInformacion.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testInformacion.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testInformacion.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testInformacion.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testInformacion.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testInformacion.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testInformacion.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testInformacion.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testInformacion.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testInformacion.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testInformacion.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Informacion> informacionSearchList = IterableUtils.toList(informacionSearchRepository.findAll());
                Informacion testInformacionSearch = informacionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testInformacionSearch.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
                assertThat(testInformacionSearch.getVistas()).isEqualTo(UPDATED_VISTAS);
                assertThat(testInformacionSearch.getRating()).isEqualTo(UPDATED_RATING);
                assertThat(testInformacionSearch.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
                assertThat(testInformacionSearch.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
                assertThat(testInformacionSearch.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
                assertThat(testInformacionSearch.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
                assertThat(testInformacionSearch.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
                assertThat(testInformacionSearch.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
                assertThat(testInformacionSearch.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
                assertThat(testInformacionSearch.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
                assertThat(testInformacionSearch.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
                assertThat(testInformacionSearch.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
            });
    }

    @Test
    @Transactional
    void putNonExistingInformacion() throws Exception {
        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        informacion.setId(count.incrementAndGet());

        // Create the Informacion
        InformacionDTO informacionDTO = informacionMapper.toDto(informacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchInformacion() throws Exception {
        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        informacion.setId(count.incrementAndGet());

        // Create the Informacion
        InformacionDTO informacionDTO = informacionMapper.toDto(informacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInformacion() throws Exception {
        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        informacion.setId(count.incrementAndGet());

        // Create the Informacion
        InformacionDTO informacionDTO = informacionMapper.toDto(informacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(informacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateInformacionWithPatch() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();

        // Update the informacion using partial update
        Informacion partialUpdatedInformacion = new Informacion();
        partialUpdatedInformacion.setId(informacion.getId());

        partialUpdatedInformacion
            .comentarios(UPDATED_COMENTARIOS)
            .vistas(UPDATED_VISTAS)
            .extra2(UPDATED_EXTRA_2)
            .extra4(UPDATED_EXTRA_4)
            .extra7(UPDATED_EXTRA_7)
            .extra8(UPDATED_EXTRA_8)
            .extra9(UPDATED_EXTRA_9)
            .extra10(UPDATED_EXTRA_10);

        restInformacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInformacion))
            )
            .andExpect(status().isOk());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        Informacion testInformacion = informacionList.get(informacionList.size() - 1);
        assertThat(testInformacion.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testInformacion.getVistas()).isEqualTo(UPDATED_VISTAS);
        assertThat(testInformacion.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testInformacion.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testInformacion.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testInformacion.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testInformacion.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testInformacion.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testInformacion.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testInformacion.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testInformacion.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testInformacion.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testInformacion.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void fullUpdateInformacionWithPatch() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);

        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();

        // Update the informacion using partial update
        Informacion partialUpdatedInformacion = new Informacion();
        partialUpdatedInformacion.setId(informacion.getId());

        partialUpdatedInformacion
            .comentarios(UPDATED_COMENTARIOS)
            .vistas(UPDATED_VISTAS)
            .rating(UPDATED_RATING)
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

        restInformacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInformacion))
            )
            .andExpect(status().isOk());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        Informacion testInformacion = informacionList.get(informacionList.size() - 1);
        assertThat(testInformacion.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testInformacion.getVistas()).isEqualTo(UPDATED_VISTAS);
        assertThat(testInformacion.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testInformacion.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testInformacion.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testInformacion.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testInformacion.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testInformacion.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testInformacion.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testInformacion.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testInformacion.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testInformacion.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testInformacion.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void patchNonExistingInformacion() throws Exception {
        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        informacion.setId(count.incrementAndGet());

        // Create the Informacion
        InformacionDTO informacionDTO = informacionMapper.toDto(informacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, informacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(informacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInformacion() throws Exception {
        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        informacion.setId(count.incrementAndGet());

        // Create the Informacion
        InformacionDTO informacionDTO = informacionMapper.toDto(informacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(informacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInformacion() throws Exception {
        int databaseSizeBeforeUpdate = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        informacion.setId(count.incrementAndGet());

        // Create the Informacion
        InformacionDTO informacionDTO = informacionMapper.toDto(informacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformacionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(informacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Informacion in the database
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteInformacion() throws Exception {
        // Initialize the database
        informacionRepository.saveAndFlush(informacion);
        informacionRepository.save(informacion);
        informacionSearchRepository.save(informacion);

        int databaseSizeBeforeDelete = informacionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the informacion
        restInformacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, informacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Informacion> informacionList = informacionRepository.findAll();
        assertThat(informacionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(informacionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchInformacion() throws Exception {
        // Initialize the database
        informacion = informacionRepository.saveAndFlush(informacion);
        informacionSearchRepository.save(informacion);

        // Search the informacion
        restInformacionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + informacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].vistas").value(hasItem(DEFAULT_VISTAS)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
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
