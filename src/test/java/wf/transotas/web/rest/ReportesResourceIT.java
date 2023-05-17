package wf.transotas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import wf.transotas.domain.Caso;
import wf.transotas.domain.Categorys;
import wf.transotas.domain.Comentarios;
import wf.transotas.domain.Informacion;
import wf.transotas.domain.Reportes;
import wf.transotas.repository.ReportesRepository;
import wf.transotas.repository.search.ReportesSearchRepository;
import wf.transotas.service.ReportesService;
import wf.transotas.service.criteria.ReportesCriteria;
import wf.transotas.service.dto.ReportesDTO;
import wf.transotas.service.mapper.ReportesMapper;

/**
 * Integration tests for the {@link ReportesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReportesResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_CASO = "AAAAAAAAAA";
    private static final String UPDATED_CASO = "BBBBBBBBBB";

    private static final String DEFAULT_IMG = "AAAAAAAAAA";
    private static final String UPDATED_IMG = "BBBBBBBBBB";

    private static final String DEFAULT_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_TITULOIX = "AAAAAAAAAA";
    private static final String UPDATED_TITULOIX = "BBBBBBBBBB";

    private static final String DEFAULT_AUTORIX = "AAAAAAAAAA";
    private static final String UPDATED_AUTORIX = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHAIX = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHAIX = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_IMGIX = "AAAAAAAAAA";
    private static final String UPDATED_IMGIX = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_PAIS = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/reportes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/reportes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportesRepository reportesRepository;

    @Mock
    private ReportesRepository reportesRepositoryMock;

    @Autowired
    private ReportesMapper reportesMapper;

    @Mock
    private ReportesService reportesServiceMock;

    @Autowired
    private ReportesSearchRepository reportesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportesMockMvc;

    private Reportes reportes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reportes createEntity(EntityManager em) {
        Reportes reportes = new Reportes()
            .titulo(DEFAULT_TITULO)
            .caso(DEFAULT_CASO)
            .img(DEFAULT_IMG)
            .autor(DEFAULT_AUTOR)
            .tituloix(DEFAULT_TITULOIX)
            .autorix(DEFAULT_AUTORIX)
            .fechaix(DEFAULT_FECHAIX)
            .imgix(DEFAULT_IMGIX)
            .ciudad(DEFAULT_CIUDAD)
            .estado(DEFAULT_ESTADO)
            .pais(DEFAULT_PAIS)
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
        return reportes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reportes createUpdatedEntity(EntityManager em) {
        Reportes reportes = new Reportes()
            .titulo(UPDATED_TITULO)
            .caso(UPDATED_CASO)
            .img(UPDATED_IMG)
            .autor(UPDATED_AUTOR)
            .tituloix(UPDATED_TITULOIX)
            .autorix(UPDATED_AUTORIX)
            .fechaix(UPDATED_FECHAIX)
            .imgix(UPDATED_IMGIX)
            .ciudad(UPDATED_CIUDAD)
            .estado(UPDATED_ESTADO)
            .pais(UPDATED_PAIS)
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
        return reportes;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        reportesSearchRepository.deleteAll();
        assertThat(reportesSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        reportes = createEntity(em);
    }

    @Test
    @Transactional
    void createReportes() throws Exception {
        int databaseSizeBeforeCreate = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        // Create the Reportes
        ReportesDTO reportesDTO = reportesMapper.toDto(reportes);
        restReportesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportesDTO)))
            .andExpect(status().isCreated());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Reportes testReportes = reportesList.get(reportesList.size() - 1);
        assertThat(testReportes.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testReportes.getCaso()).isEqualTo(DEFAULT_CASO);
        assertThat(testReportes.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testReportes.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testReportes.getTituloix()).isEqualTo(DEFAULT_TITULOIX);
        assertThat(testReportes.getAutorix()).isEqualTo(DEFAULT_AUTORIX);
        assertThat(testReportes.getFechaix()).isEqualTo(DEFAULT_FECHAIX);
        assertThat(testReportes.getImgix()).isEqualTo(DEFAULT_IMGIX);
        assertThat(testReportes.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testReportes.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testReportes.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testReportes.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testReportes.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testReportes.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testReportes.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testReportes.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testReportes.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testReportes.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testReportes.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testReportes.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testReportes.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void createReportesWithExistingId() throws Exception {
        // Create the Reportes with an existing ID
        reportes.setId(1L);
        ReportesDTO reportesDTO = reportesMapper.toDto(reportes);

        int databaseSizeBeforeCreate = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllReportes() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList
        restReportesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportes.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].caso").value(hasItem(DEFAULT_CASO)))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG)))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].tituloix").value(hasItem(DEFAULT_TITULOIX)))
            .andExpect(jsonPath("$.[*].autorix").value(hasItem(DEFAULT_AUTORIX)))
            .andExpect(jsonPath("$.[*].fechaix").value(hasItem(DEFAULT_FECHAIX.toString())))
            .andExpect(jsonPath("$.[*].imgix").value(hasItem(DEFAULT_IMGIX)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
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

    @SuppressWarnings({ "unchecked" })
    void getAllReportesWithEagerRelationshipsIsEnabled() throws Exception {
        when(reportesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reportesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reportesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReportes() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get the reportes
        restReportesMockMvc
            .perform(get(ENTITY_API_URL_ID, reportes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportes.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.caso").value(DEFAULT_CASO))
            .andExpect(jsonPath("$.img").value(DEFAULT_IMG))
            .andExpect(jsonPath("$.autor").value(DEFAULT_AUTOR))
            .andExpect(jsonPath("$.tituloix").value(DEFAULT_TITULOIX))
            .andExpect(jsonPath("$.autorix").value(DEFAULT_AUTORIX))
            .andExpect(jsonPath("$.fechaix").value(DEFAULT_FECHAIX.toString()))
            .andExpect(jsonPath("$.imgix").value(DEFAULT_IMGIX))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS))
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
    void getReportesByIdFiltering() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        Long id = reportes.getId();

        defaultReportesShouldBeFound("id.equals=" + id);
        defaultReportesShouldNotBeFound("id.notEquals=" + id);

        defaultReportesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportesShouldNotBeFound("id.greaterThan=" + id);

        defaultReportesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportesByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where titulo equals to DEFAULT_TITULO
        defaultReportesShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the reportesList where titulo equals to UPDATED_TITULO
        defaultReportesShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllReportesByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultReportesShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the reportesList where titulo equals to UPDATED_TITULO
        defaultReportesShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllReportesByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where titulo is not null
        defaultReportesShouldBeFound("titulo.specified=true");

        // Get all the reportesList where titulo is null
        defaultReportesShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByTituloContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where titulo contains DEFAULT_TITULO
        defaultReportesShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the reportesList where titulo contains UPDATED_TITULO
        defaultReportesShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllReportesByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where titulo does not contain DEFAULT_TITULO
        defaultReportesShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the reportesList where titulo does not contain UPDATED_TITULO
        defaultReportesShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllReportesByCasoIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where caso equals to DEFAULT_CASO
        defaultReportesShouldBeFound("caso.equals=" + DEFAULT_CASO);

        // Get all the reportesList where caso equals to UPDATED_CASO
        defaultReportesShouldNotBeFound("caso.equals=" + UPDATED_CASO);
    }

    @Test
    @Transactional
    void getAllReportesByCasoIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where caso in DEFAULT_CASO or UPDATED_CASO
        defaultReportesShouldBeFound("caso.in=" + DEFAULT_CASO + "," + UPDATED_CASO);

        // Get all the reportesList where caso equals to UPDATED_CASO
        defaultReportesShouldNotBeFound("caso.in=" + UPDATED_CASO);
    }

    @Test
    @Transactional
    void getAllReportesByCasoIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where caso is not null
        defaultReportesShouldBeFound("caso.specified=true");

        // Get all the reportesList where caso is null
        defaultReportesShouldNotBeFound("caso.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByCasoContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where caso contains DEFAULT_CASO
        defaultReportesShouldBeFound("caso.contains=" + DEFAULT_CASO);

        // Get all the reportesList where caso contains UPDATED_CASO
        defaultReportesShouldNotBeFound("caso.contains=" + UPDATED_CASO);
    }

    @Test
    @Transactional
    void getAllReportesByCasoNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where caso does not contain DEFAULT_CASO
        defaultReportesShouldNotBeFound("caso.doesNotContain=" + DEFAULT_CASO);

        // Get all the reportesList where caso does not contain UPDATED_CASO
        defaultReportesShouldBeFound("caso.doesNotContain=" + UPDATED_CASO);
    }

    @Test
    @Transactional
    void getAllReportesByImgIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where img equals to DEFAULT_IMG
        defaultReportesShouldBeFound("img.equals=" + DEFAULT_IMG);

        // Get all the reportesList where img equals to UPDATED_IMG
        defaultReportesShouldNotBeFound("img.equals=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllReportesByImgIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where img in DEFAULT_IMG or UPDATED_IMG
        defaultReportesShouldBeFound("img.in=" + DEFAULT_IMG + "," + UPDATED_IMG);

        // Get all the reportesList where img equals to UPDATED_IMG
        defaultReportesShouldNotBeFound("img.in=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllReportesByImgIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where img is not null
        defaultReportesShouldBeFound("img.specified=true");

        // Get all the reportesList where img is null
        defaultReportesShouldNotBeFound("img.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByImgContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where img contains DEFAULT_IMG
        defaultReportesShouldBeFound("img.contains=" + DEFAULT_IMG);

        // Get all the reportesList where img contains UPDATED_IMG
        defaultReportesShouldNotBeFound("img.contains=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllReportesByImgNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where img does not contain DEFAULT_IMG
        defaultReportesShouldNotBeFound("img.doesNotContain=" + DEFAULT_IMG);

        // Get all the reportesList where img does not contain UPDATED_IMG
        defaultReportesShouldBeFound("img.doesNotContain=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllReportesByAutorIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autor equals to DEFAULT_AUTOR
        defaultReportesShouldBeFound("autor.equals=" + DEFAULT_AUTOR);

        // Get all the reportesList where autor equals to UPDATED_AUTOR
        defaultReportesShouldNotBeFound("autor.equals=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllReportesByAutorIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autor in DEFAULT_AUTOR or UPDATED_AUTOR
        defaultReportesShouldBeFound("autor.in=" + DEFAULT_AUTOR + "," + UPDATED_AUTOR);

        // Get all the reportesList where autor equals to UPDATED_AUTOR
        defaultReportesShouldNotBeFound("autor.in=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllReportesByAutorIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autor is not null
        defaultReportesShouldBeFound("autor.specified=true");

        // Get all the reportesList where autor is null
        defaultReportesShouldNotBeFound("autor.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByAutorContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autor contains DEFAULT_AUTOR
        defaultReportesShouldBeFound("autor.contains=" + DEFAULT_AUTOR);

        // Get all the reportesList where autor contains UPDATED_AUTOR
        defaultReportesShouldNotBeFound("autor.contains=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllReportesByAutorNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autor does not contain DEFAULT_AUTOR
        defaultReportesShouldNotBeFound("autor.doesNotContain=" + DEFAULT_AUTOR);

        // Get all the reportesList where autor does not contain UPDATED_AUTOR
        defaultReportesShouldBeFound("autor.doesNotContain=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllReportesByTituloixIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where tituloix equals to DEFAULT_TITULOIX
        defaultReportesShouldBeFound("tituloix.equals=" + DEFAULT_TITULOIX);

        // Get all the reportesList where tituloix equals to UPDATED_TITULOIX
        defaultReportesShouldNotBeFound("tituloix.equals=" + UPDATED_TITULOIX);
    }

    @Test
    @Transactional
    void getAllReportesByTituloixIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where tituloix in DEFAULT_TITULOIX or UPDATED_TITULOIX
        defaultReportesShouldBeFound("tituloix.in=" + DEFAULT_TITULOIX + "," + UPDATED_TITULOIX);

        // Get all the reportesList where tituloix equals to UPDATED_TITULOIX
        defaultReportesShouldNotBeFound("tituloix.in=" + UPDATED_TITULOIX);
    }

    @Test
    @Transactional
    void getAllReportesByTituloixIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where tituloix is not null
        defaultReportesShouldBeFound("tituloix.specified=true");

        // Get all the reportesList where tituloix is null
        defaultReportesShouldNotBeFound("tituloix.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByTituloixContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where tituloix contains DEFAULT_TITULOIX
        defaultReportesShouldBeFound("tituloix.contains=" + DEFAULT_TITULOIX);

        // Get all the reportesList where tituloix contains UPDATED_TITULOIX
        defaultReportesShouldNotBeFound("tituloix.contains=" + UPDATED_TITULOIX);
    }

    @Test
    @Transactional
    void getAllReportesByTituloixNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where tituloix does not contain DEFAULT_TITULOIX
        defaultReportesShouldNotBeFound("tituloix.doesNotContain=" + DEFAULT_TITULOIX);

        // Get all the reportesList where tituloix does not contain UPDATED_TITULOIX
        defaultReportesShouldBeFound("tituloix.doesNotContain=" + UPDATED_TITULOIX);
    }

    @Test
    @Transactional
    void getAllReportesByAutorixIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autorix equals to DEFAULT_AUTORIX
        defaultReportesShouldBeFound("autorix.equals=" + DEFAULT_AUTORIX);

        // Get all the reportesList where autorix equals to UPDATED_AUTORIX
        defaultReportesShouldNotBeFound("autorix.equals=" + UPDATED_AUTORIX);
    }

    @Test
    @Transactional
    void getAllReportesByAutorixIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autorix in DEFAULT_AUTORIX or UPDATED_AUTORIX
        defaultReportesShouldBeFound("autorix.in=" + DEFAULT_AUTORIX + "," + UPDATED_AUTORIX);

        // Get all the reportesList where autorix equals to UPDATED_AUTORIX
        defaultReportesShouldNotBeFound("autorix.in=" + UPDATED_AUTORIX);
    }

    @Test
    @Transactional
    void getAllReportesByAutorixIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autorix is not null
        defaultReportesShouldBeFound("autorix.specified=true");

        // Get all the reportesList where autorix is null
        defaultReportesShouldNotBeFound("autorix.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByAutorixContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autorix contains DEFAULT_AUTORIX
        defaultReportesShouldBeFound("autorix.contains=" + DEFAULT_AUTORIX);

        // Get all the reportesList where autorix contains UPDATED_AUTORIX
        defaultReportesShouldNotBeFound("autorix.contains=" + UPDATED_AUTORIX);
    }

    @Test
    @Transactional
    void getAllReportesByAutorixNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where autorix does not contain DEFAULT_AUTORIX
        defaultReportesShouldNotBeFound("autorix.doesNotContain=" + DEFAULT_AUTORIX);

        // Get all the reportesList where autorix does not contain UPDATED_AUTORIX
        defaultReportesShouldBeFound("autorix.doesNotContain=" + UPDATED_AUTORIX);
    }

    @Test
    @Transactional
    void getAllReportesByFechaixIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where fechaix equals to DEFAULT_FECHAIX
        defaultReportesShouldBeFound("fechaix.equals=" + DEFAULT_FECHAIX);

        // Get all the reportesList where fechaix equals to UPDATED_FECHAIX
        defaultReportesShouldNotBeFound("fechaix.equals=" + UPDATED_FECHAIX);
    }

    @Test
    @Transactional
    void getAllReportesByFechaixIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where fechaix in DEFAULT_FECHAIX or UPDATED_FECHAIX
        defaultReportesShouldBeFound("fechaix.in=" + DEFAULT_FECHAIX + "," + UPDATED_FECHAIX);

        // Get all the reportesList where fechaix equals to UPDATED_FECHAIX
        defaultReportesShouldNotBeFound("fechaix.in=" + UPDATED_FECHAIX);
    }

    @Test
    @Transactional
    void getAllReportesByFechaixIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where fechaix is not null
        defaultReportesShouldBeFound("fechaix.specified=true");

        // Get all the reportesList where fechaix is null
        defaultReportesShouldNotBeFound("fechaix.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByImgixIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where imgix equals to DEFAULT_IMGIX
        defaultReportesShouldBeFound("imgix.equals=" + DEFAULT_IMGIX);

        // Get all the reportesList where imgix equals to UPDATED_IMGIX
        defaultReportesShouldNotBeFound("imgix.equals=" + UPDATED_IMGIX);
    }

    @Test
    @Transactional
    void getAllReportesByImgixIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where imgix in DEFAULT_IMGIX or UPDATED_IMGIX
        defaultReportesShouldBeFound("imgix.in=" + DEFAULT_IMGIX + "," + UPDATED_IMGIX);

        // Get all the reportesList where imgix equals to UPDATED_IMGIX
        defaultReportesShouldNotBeFound("imgix.in=" + UPDATED_IMGIX);
    }

    @Test
    @Transactional
    void getAllReportesByImgixIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where imgix is not null
        defaultReportesShouldBeFound("imgix.specified=true");

        // Get all the reportesList where imgix is null
        defaultReportesShouldNotBeFound("imgix.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByImgixContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where imgix contains DEFAULT_IMGIX
        defaultReportesShouldBeFound("imgix.contains=" + DEFAULT_IMGIX);

        // Get all the reportesList where imgix contains UPDATED_IMGIX
        defaultReportesShouldNotBeFound("imgix.contains=" + UPDATED_IMGIX);
    }

    @Test
    @Transactional
    void getAllReportesByImgixNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where imgix does not contain DEFAULT_IMGIX
        defaultReportesShouldNotBeFound("imgix.doesNotContain=" + DEFAULT_IMGIX);

        // Get all the reportesList where imgix does not contain UPDATED_IMGIX
        defaultReportesShouldBeFound("imgix.doesNotContain=" + UPDATED_IMGIX);
    }

    @Test
    @Transactional
    void getAllReportesByCiudadIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where ciudad equals to DEFAULT_CIUDAD
        defaultReportesShouldBeFound("ciudad.equals=" + DEFAULT_CIUDAD);

        // Get all the reportesList where ciudad equals to UPDATED_CIUDAD
        defaultReportesShouldNotBeFound("ciudad.equals=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllReportesByCiudadIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where ciudad in DEFAULT_CIUDAD or UPDATED_CIUDAD
        defaultReportesShouldBeFound("ciudad.in=" + DEFAULT_CIUDAD + "," + UPDATED_CIUDAD);

        // Get all the reportesList where ciudad equals to UPDATED_CIUDAD
        defaultReportesShouldNotBeFound("ciudad.in=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllReportesByCiudadIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where ciudad is not null
        defaultReportesShouldBeFound("ciudad.specified=true");

        // Get all the reportesList where ciudad is null
        defaultReportesShouldNotBeFound("ciudad.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByCiudadContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where ciudad contains DEFAULT_CIUDAD
        defaultReportesShouldBeFound("ciudad.contains=" + DEFAULT_CIUDAD);

        // Get all the reportesList where ciudad contains UPDATED_CIUDAD
        defaultReportesShouldNotBeFound("ciudad.contains=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllReportesByCiudadNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where ciudad does not contain DEFAULT_CIUDAD
        defaultReportesShouldNotBeFound("ciudad.doesNotContain=" + DEFAULT_CIUDAD);

        // Get all the reportesList where ciudad does not contain UPDATED_CIUDAD
        defaultReportesShouldBeFound("ciudad.doesNotContain=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllReportesByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where estado equals to DEFAULT_ESTADO
        defaultReportesShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the reportesList where estado equals to UPDATED_ESTADO
        defaultReportesShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllReportesByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultReportesShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the reportesList where estado equals to UPDATED_ESTADO
        defaultReportesShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllReportesByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where estado is not null
        defaultReportesShouldBeFound("estado.specified=true");

        // Get all the reportesList where estado is null
        defaultReportesShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByEstadoContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where estado contains DEFAULT_ESTADO
        defaultReportesShouldBeFound("estado.contains=" + DEFAULT_ESTADO);

        // Get all the reportesList where estado contains UPDATED_ESTADO
        defaultReportesShouldNotBeFound("estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllReportesByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where estado does not contain DEFAULT_ESTADO
        defaultReportesShouldNotBeFound("estado.doesNotContain=" + DEFAULT_ESTADO);

        // Get all the reportesList where estado does not contain UPDATED_ESTADO
        defaultReportesShouldBeFound("estado.doesNotContain=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllReportesByPaisIsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where pais equals to DEFAULT_PAIS
        defaultReportesShouldBeFound("pais.equals=" + DEFAULT_PAIS);

        // Get all the reportesList where pais equals to UPDATED_PAIS
        defaultReportesShouldNotBeFound("pais.equals=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllReportesByPaisIsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where pais in DEFAULT_PAIS or UPDATED_PAIS
        defaultReportesShouldBeFound("pais.in=" + DEFAULT_PAIS + "," + UPDATED_PAIS);

        // Get all the reportesList where pais equals to UPDATED_PAIS
        defaultReportesShouldNotBeFound("pais.in=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllReportesByPaisIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where pais is not null
        defaultReportesShouldBeFound("pais.specified=true");

        // Get all the reportesList where pais is null
        defaultReportesShouldNotBeFound("pais.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByPaisContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where pais contains DEFAULT_PAIS
        defaultReportesShouldBeFound("pais.contains=" + DEFAULT_PAIS);

        // Get all the reportesList where pais contains UPDATED_PAIS
        defaultReportesShouldNotBeFound("pais.contains=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllReportesByPaisNotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where pais does not contain DEFAULT_PAIS
        defaultReportesShouldNotBeFound("pais.doesNotContain=" + DEFAULT_PAIS);

        // Get all the reportesList where pais does not contain UPDATED_PAIS
        defaultReportesShouldBeFound("pais.doesNotContain=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllReportesByExtra1IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra1 equals to DEFAULT_EXTRA_1
        defaultReportesShouldBeFound("extra1.equals=" + DEFAULT_EXTRA_1);

        // Get all the reportesList where extra1 equals to UPDATED_EXTRA_1
        defaultReportesShouldNotBeFound("extra1.equals=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllReportesByExtra1IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra1 in DEFAULT_EXTRA_1 or UPDATED_EXTRA_1
        defaultReportesShouldBeFound("extra1.in=" + DEFAULT_EXTRA_1 + "," + UPDATED_EXTRA_1);

        // Get all the reportesList where extra1 equals to UPDATED_EXTRA_1
        defaultReportesShouldNotBeFound("extra1.in=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllReportesByExtra1IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra1 is not null
        defaultReportesShouldBeFound("extra1.specified=true");

        // Get all the reportesList where extra1 is null
        defaultReportesShouldNotBeFound("extra1.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra1ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra1 contains DEFAULT_EXTRA_1
        defaultReportesShouldBeFound("extra1.contains=" + DEFAULT_EXTRA_1);

        // Get all the reportesList where extra1 contains UPDATED_EXTRA_1
        defaultReportesShouldNotBeFound("extra1.contains=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllReportesByExtra1NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra1 does not contain DEFAULT_EXTRA_1
        defaultReportesShouldNotBeFound("extra1.doesNotContain=" + DEFAULT_EXTRA_1);

        // Get all the reportesList where extra1 does not contain UPDATED_EXTRA_1
        defaultReportesShouldBeFound("extra1.doesNotContain=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllReportesByExtra2IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra2 equals to DEFAULT_EXTRA_2
        defaultReportesShouldBeFound("extra2.equals=" + DEFAULT_EXTRA_2);

        // Get all the reportesList where extra2 equals to UPDATED_EXTRA_2
        defaultReportesShouldNotBeFound("extra2.equals=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllReportesByExtra2IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra2 in DEFAULT_EXTRA_2 or UPDATED_EXTRA_2
        defaultReportesShouldBeFound("extra2.in=" + DEFAULT_EXTRA_2 + "," + UPDATED_EXTRA_2);

        // Get all the reportesList where extra2 equals to UPDATED_EXTRA_2
        defaultReportesShouldNotBeFound("extra2.in=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllReportesByExtra2IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra2 is not null
        defaultReportesShouldBeFound("extra2.specified=true");

        // Get all the reportesList where extra2 is null
        defaultReportesShouldNotBeFound("extra2.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra2ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra2 contains DEFAULT_EXTRA_2
        defaultReportesShouldBeFound("extra2.contains=" + DEFAULT_EXTRA_2);

        // Get all the reportesList where extra2 contains UPDATED_EXTRA_2
        defaultReportesShouldNotBeFound("extra2.contains=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllReportesByExtra2NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra2 does not contain DEFAULT_EXTRA_2
        defaultReportesShouldNotBeFound("extra2.doesNotContain=" + DEFAULT_EXTRA_2);

        // Get all the reportesList where extra2 does not contain UPDATED_EXTRA_2
        defaultReportesShouldBeFound("extra2.doesNotContain=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllReportesByExtra3IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra3 equals to DEFAULT_EXTRA_3
        defaultReportesShouldBeFound("extra3.equals=" + DEFAULT_EXTRA_3);

        // Get all the reportesList where extra3 equals to UPDATED_EXTRA_3
        defaultReportesShouldNotBeFound("extra3.equals=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllReportesByExtra3IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra3 in DEFAULT_EXTRA_3 or UPDATED_EXTRA_3
        defaultReportesShouldBeFound("extra3.in=" + DEFAULT_EXTRA_3 + "," + UPDATED_EXTRA_3);

        // Get all the reportesList where extra3 equals to UPDATED_EXTRA_3
        defaultReportesShouldNotBeFound("extra3.in=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllReportesByExtra3IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra3 is not null
        defaultReportesShouldBeFound("extra3.specified=true");

        // Get all the reportesList where extra3 is null
        defaultReportesShouldNotBeFound("extra3.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra3ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra3 contains DEFAULT_EXTRA_3
        defaultReportesShouldBeFound("extra3.contains=" + DEFAULT_EXTRA_3);

        // Get all the reportesList where extra3 contains UPDATED_EXTRA_3
        defaultReportesShouldNotBeFound("extra3.contains=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllReportesByExtra3NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra3 does not contain DEFAULT_EXTRA_3
        defaultReportesShouldNotBeFound("extra3.doesNotContain=" + DEFAULT_EXTRA_3);

        // Get all the reportesList where extra3 does not contain UPDATED_EXTRA_3
        defaultReportesShouldBeFound("extra3.doesNotContain=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllReportesByExtra4IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra4 equals to DEFAULT_EXTRA_4
        defaultReportesShouldBeFound("extra4.equals=" + DEFAULT_EXTRA_4);

        // Get all the reportesList where extra4 equals to UPDATED_EXTRA_4
        defaultReportesShouldNotBeFound("extra4.equals=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllReportesByExtra4IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra4 in DEFAULT_EXTRA_4 or UPDATED_EXTRA_4
        defaultReportesShouldBeFound("extra4.in=" + DEFAULT_EXTRA_4 + "," + UPDATED_EXTRA_4);

        // Get all the reportesList where extra4 equals to UPDATED_EXTRA_4
        defaultReportesShouldNotBeFound("extra4.in=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllReportesByExtra4IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra4 is not null
        defaultReportesShouldBeFound("extra4.specified=true");

        // Get all the reportesList where extra4 is null
        defaultReportesShouldNotBeFound("extra4.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra4ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra4 contains DEFAULT_EXTRA_4
        defaultReportesShouldBeFound("extra4.contains=" + DEFAULT_EXTRA_4);

        // Get all the reportesList where extra4 contains UPDATED_EXTRA_4
        defaultReportesShouldNotBeFound("extra4.contains=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllReportesByExtra4NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra4 does not contain DEFAULT_EXTRA_4
        defaultReportesShouldNotBeFound("extra4.doesNotContain=" + DEFAULT_EXTRA_4);

        // Get all the reportesList where extra4 does not contain UPDATED_EXTRA_4
        defaultReportesShouldBeFound("extra4.doesNotContain=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllReportesByExtra5IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra5 equals to DEFAULT_EXTRA_5
        defaultReportesShouldBeFound("extra5.equals=" + DEFAULT_EXTRA_5);

        // Get all the reportesList where extra5 equals to UPDATED_EXTRA_5
        defaultReportesShouldNotBeFound("extra5.equals=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllReportesByExtra5IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra5 in DEFAULT_EXTRA_5 or UPDATED_EXTRA_5
        defaultReportesShouldBeFound("extra5.in=" + DEFAULT_EXTRA_5 + "," + UPDATED_EXTRA_5);

        // Get all the reportesList where extra5 equals to UPDATED_EXTRA_5
        defaultReportesShouldNotBeFound("extra5.in=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllReportesByExtra5IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra5 is not null
        defaultReportesShouldBeFound("extra5.specified=true");

        // Get all the reportesList where extra5 is null
        defaultReportesShouldNotBeFound("extra5.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra5ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra5 contains DEFAULT_EXTRA_5
        defaultReportesShouldBeFound("extra5.contains=" + DEFAULT_EXTRA_5);

        // Get all the reportesList where extra5 contains UPDATED_EXTRA_5
        defaultReportesShouldNotBeFound("extra5.contains=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllReportesByExtra5NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra5 does not contain DEFAULT_EXTRA_5
        defaultReportesShouldNotBeFound("extra5.doesNotContain=" + DEFAULT_EXTRA_5);

        // Get all the reportesList where extra5 does not contain UPDATED_EXTRA_5
        defaultReportesShouldBeFound("extra5.doesNotContain=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllReportesByExtra6IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra6 equals to DEFAULT_EXTRA_6
        defaultReportesShouldBeFound("extra6.equals=" + DEFAULT_EXTRA_6);

        // Get all the reportesList where extra6 equals to UPDATED_EXTRA_6
        defaultReportesShouldNotBeFound("extra6.equals=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllReportesByExtra6IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra6 in DEFAULT_EXTRA_6 or UPDATED_EXTRA_6
        defaultReportesShouldBeFound("extra6.in=" + DEFAULT_EXTRA_6 + "," + UPDATED_EXTRA_6);

        // Get all the reportesList where extra6 equals to UPDATED_EXTRA_6
        defaultReportesShouldNotBeFound("extra6.in=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllReportesByExtra6IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra6 is not null
        defaultReportesShouldBeFound("extra6.specified=true");

        // Get all the reportesList where extra6 is null
        defaultReportesShouldNotBeFound("extra6.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra6ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra6 contains DEFAULT_EXTRA_6
        defaultReportesShouldBeFound("extra6.contains=" + DEFAULT_EXTRA_6);

        // Get all the reportesList where extra6 contains UPDATED_EXTRA_6
        defaultReportesShouldNotBeFound("extra6.contains=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllReportesByExtra6NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra6 does not contain DEFAULT_EXTRA_6
        defaultReportesShouldNotBeFound("extra6.doesNotContain=" + DEFAULT_EXTRA_6);

        // Get all the reportesList where extra6 does not contain UPDATED_EXTRA_6
        defaultReportesShouldBeFound("extra6.doesNotContain=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllReportesByExtra7IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra7 equals to DEFAULT_EXTRA_7
        defaultReportesShouldBeFound("extra7.equals=" + DEFAULT_EXTRA_7);

        // Get all the reportesList where extra7 equals to UPDATED_EXTRA_7
        defaultReportesShouldNotBeFound("extra7.equals=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllReportesByExtra7IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra7 in DEFAULT_EXTRA_7 or UPDATED_EXTRA_7
        defaultReportesShouldBeFound("extra7.in=" + DEFAULT_EXTRA_7 + "," + UPDATED_EXTRA_7);

        // Get all the reportesList where extra7 equals to UPDATED_EXTRA_7
        defaultReportesShouldNotBeFound("extra7.in=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllReportesByExtra7IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra7 is not null
        defaultReportesShouldBeFound("extra7.specified=true");

        // Get all the reportesList where extra7 is null
        defaultReportesShouldNotBeFound("extra7.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra7ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra7 contains DEFAULT_EXTRA_7
        defaultReportesShouldBeFound("extra7.contains=" + DEFAULT_EXTRA_7);

        // Get all the reportesList where extra7 contains UPDATED_EXTRA_7
        defaultReportesShouldNotBeFound("extra7.contains=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllReportesByExtra7NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra7 does not contain DEFAULT_EXTRA_7
        defaultReportesShouldNotBeFound("extra7.doesNotContain=" + DEFAULT_EXTRA_7);

        // Get all the reportesList where extra7 does not contain UPDATED_EXTRA_7
        defaultReportesShouldBeFound("extra7.doesNotContain=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllReportesByExtra8IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra8 equals to DEFAULT_EXTRA_8
        defaultReportesShouldBeFound("extra8.equals=" + DEFAULT_EXTRA_8);

        // Get all the reportesList where extra8 equals to UPDATED_EXTRA_8
        defaultReportesShouldNotBeFound("extra8.equals=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllReportesByExtra8IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra8 in DEFAULT_EXTRA_8 or UPDATED_EXTRA_8
        defaultReportesShouldBeFound("extra8.in=" + DEFAULT_EXTRA_8 + "," + UPDATED_EXTRA_8);

        // Get all the reportesList where extra8 equals to UPDATED_EXTRA_8
        defaultReportesShouldNotBeFound("extra8.in=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllReportesByExtra8IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra8 is not null
        defaultReportesShouldBeFound("extra8.specified=true");

        // Get all the reportesList where extra8 is null
        defaultReportesShouldNotBeFound("extra8.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra8ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra8 contains DEFAULT_EXTRA_8
        defaultReportesShouldBeFound("extra8.contains=" + DEFAULT_EXTRA_8);

        // Get all the reportesList where extra8 contains UPDATED_EXTRA_8
        defaultReportesShouldNotBeFound("extra8.contains=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllReportesByExtra8NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra8 does not contain DEFAULT_EXTRA_8
        defaultReportesShouldNotBeFound("extra8.doesNotContain=" + DEFAULT_EXTRA_8);

        // Get all the reportesList where extra8 does not contain UPDATED_EXTRA_8
        defaultReportesShouldBeFound("extra8.doesNotContain=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllReportesByExtra9IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra9 equals to DEFAULT_EXTRA_9
        defaultReportesShouldBeFound("extra9.equals=" + DEFAULT_EXTRA_9);

        // Get all the reportesList where extra9 equals to UPDATED_EXTRA_9
        defaultReportesShouldNotBeFound("extra9.equals=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllReportesByExtra9IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra9 in DEFAULT_EXTRA_9 or UPDATED_EXTRA_9
        defaultReportesShouldBeFound("extra9.in=" + DEFAULT_EXTRA_9 + "," + UPDATED_EXTRA_9);

        // Get all the reportesList where extra9 equals to UPDATED_EXTRA_9
        defaultReportesShouldNotBeFound("extra9.in=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllReportesByExtra9IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra9 is not null
        defaultReportesShouldBeFound("extra9.specified=true");

        // Get all the reportesList where extra9 is null
        defaultReportesShouldNotBeFound("extra9.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra9ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra9 contains DEFAULT_EXTRA_9
        defaultReportesShouldBeFound("extra9.contains=" + DEFAULT_EXTRA_9);

        // Get all the reportesList where extra9 contains UPDATED_EXTRA_9
        defaultReportesShouldNotBeFound("extra9.contains=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllReportesByExtra9NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra9 does not contain DEFAULT_EXTRA_9
        defaultReportesShouldNotBeFound("extra9.doesNotContain=" + DEFAULT_EXTRA_9);

        // Get all the reportesList where extra9 does not contain UPDATED_EXTRA_9
        defaultReportesShouldBeFound("extra9.doesNotContain=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllReportesByExtra10IsEqualToSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra10 equals to DEFAULT_EXTRA_10
        defaultReportesShouldBeFound("extra10.equals=" + DEFAULT_EXTRA_10);

        // Get all the reportesList where extra10 equals to UPDATED_EXTRA_10
        defaultReportesShouldNotBeFound("extra10.equals=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllReportesByExtra10IsInShouldWork() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra10 in DEFAULT_EXTRA_10 or UPDATED_EXTRA_10
        defaultReportesShouldBeFound("extra10.in=" + DEFAULT_EXTRA_10 + "," + UPDATED_EXTRA_10);

        // Get all the reportesList where extra10 equals to UPDATED_EXTRA_10
        defaultReportesShouldNotBeFound("extra10.in=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllReportesByExtra10IsNullOrNotNull() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra10 is not null
        defaultReportesShouldBeFound("extra10.specified=true");

        // Get all the reportesList where extra10 is null
        defaultReportesShouldNotBeFound("extra10.specified=false");
    }

    @Test
    @Transactional
    void getAllReportesByExtra10ContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra10 contains DEFAULT_EXTRA_10
        defaultReportesShouldBeFound("extra10.contains=" + DEFAULT_EXTRA_10);

        // Get all the reportesList where extra10 contains UPDATED_EXTRA_10
        defaultReportesShouldNotBeFound("extra10.contains=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllReportesByExtra10NotContainsSomething() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        // Get all the reportesList where extra10 does not contain DEFAULT_EXTRA_10
        defaultReportesShouldNotBeFound("extra10.doesNotContain=" + DEFAULT_EXTRA_10);

        // Get all the reportesList where extra10 does not contain UPDATED_EXTRA_10
        defaultReportesShouldBeFound("extra10.doesNotContain=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllReportesByInformacionIsEqualToSomething() throws Exception {
        Informacion informacion;
        if (TestUtil.findAll(em, Informacion.class).isEmpty()) {
            reportesRepository.saveAndFlush(reportes);
            informacion = InformacionResourceIT.createEntity(em);
        } else {
            informacion = TestUtil.findAll(em, Informacion.class).get(0);
        }
        em.persist(informacion);
        em.flush();
        reportes.setInformacion(informacion);
        reportesRepository.saveAndFlush(reportes);
        Long informacionId = informacion.getId();

        // Get all the reportesList where informacion equals to informacionId
        defaultReportesShouldBeFound("informacionId.equals=" + informacionId);

        // Get all the reportesList where informacion equals to (informacionId + 1)
        defaultReportesShouldNotBeFound("informacionId.equals=" + (informacionId + 1));
    }

    @Test
    @Transactional
    void getAllReportesByCasoIsEqualToSomething() throws Exception {
        Caso caso;
        if (TestUtil.findAll(em, Caso.class).isEmpty()) {
            reportesRepository.saveAndFlush(reportes);
            caso = CasoResourceIT.createEntity(em);
        } else {
            caso = TestUtil.findAll(em, Caso.class).get(0);
        }
        em.persist(caso);
        em.flush();
        reportes.setCaso(caso);
        reportesRepository.saveAndFlush(reportes);
        Long casoId = caso.getId();

        // Get all the reportesList where caso equals to casoId
        defaultReportesShouldBeFound("casoId.equals=" + casoId);

        // Get all the reportesList where caso equals to (casoId + 1)
        defaultReportesShouldNotBeFound("casoId.equals=" + (casoId + 1));
    }

    @Test
    @Transactional
    void getAllReportesByCategorysIsEqualToSomething() throws Exception {
        Categorys categorys;
        if (TestUtil.findAll(em, Categorys.class).isEmpty()) {
            reportesRepository.saveAndFlush(reportes);
            categorys = CategorysResourceIT.createEntity(em);
        } else {
            categorys = TestUtil.findAll(em, Categorys.class).get(0);
        }
        em.persist(categorys);
        em.flush();
        reportes.addCategorys(categorys);
        reportesRepository.saveAndFlush(reportes);
        Long categorysId = categorys.getId();

        // Get all the reportesList where categorys equals to categorysId
        defaultReportesShouldBeFound("categorysId.equals=" + categorysId);

        // Get all the reportesList where categorys equals to (categorysId + 1)
        defaultReportesShouldNotBeFound("categorysId.equals=" + (categorysId + 1));
    }

    @Test
    @Transactional
    void getAllReportesByComentariosIsEqualToSomething() throws Exception {
        Comentarios comentarios;
        if (TestUtil.findAll(em, Comentarios.class).isEmpty()) {
            reportesRepository.saveAndFlush(reportes);
            comentarios = ComentariosResourceIT.createEntity(em);
        } else {
            comentarios = TestUtil.findAll(em, Comentarios.class).get(0);
        }
        em.persist(comentarios);
        em.flush();
        reportes.addComentarios(comentarios);
        reportesRepository.saveAndFlush(reportes);
        Long comentariosId = comentarios.getId();

        // Get all the reportesList where comentarios equals to comentariosId
        defaultReportesShouldBeFound("comentariosId.equals=" + comentariosId);

        // Get all the reportesList where comentarios equals to (comentariosId + 1)
        defaultReportesShouldNotBeFound("comentariosId.equals=" + (comentariosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportesShouldBeFound(String filter) throws Exception {
        restReportesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportes.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].caso").value(hasItem(DEFAULT_CASO)))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG)))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].tituloix").value(hasItem(DEFAULT_TITULOIX)))
            .andExpect(jsonPath("$.[*].autorix").value(hasItem(DEFAULT_AUTORIX)))
            .andExpect(jsonPath("$.[*].fechaix").value(hasItem(DEFAULT_FECHAIX.toString())))
            .andExpect(jsonPath("$.[*].imgix").value(hasItem(DEFAULT_IMGIX)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
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
        restReportesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportesShouldNotBeFound(String filter) throws Exception {
        restReportesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportes() throws Exception {
        // Get the reportes
        restReportesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportes() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();
        reportesSearchRepository.save(reportes);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());

        // Update the reportes
        Reportes updatedReportes = reportesRepository.findById(reportes.getId()).get();
        // Disconnect from session so that the updates on updatedReportes are not directly saved in db
        em.detach(updatedReportes);
        updatedReportes
            .titulo(UPDATED_TITULO)
            .caso(UPDATED_CASO)
            .img(UPDATED_IMG)
            .autor(UPDATED_AUTOR)
            .tituloix(UPDATED_TITULOIX)
            .autorix(UPDATED_AUTORIX)
            .fechaix(UPDATED_FECHAIX)
            .imgix(UPDATED_IMGIX)
            .ciudad(UPDATED_CIUDAD)
            .estado(UPDATED_ESTADO)
            .pais(UPDATED_PAIS)
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
        ReportesDTO reportesDTO = reportesMapper.toDto(updatedReportes);

        restReportesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        Reportes testReportes = reportesList.get(reportesList.size() - 1);
        assertThat(testReportes.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testReportes.getCaso()).isEqualTo(UPDATED_CASO);
        assertThat(testReportes.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testReportes.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testReportes.getTituloix()).isEqualTo(UPDATED_TITULOIX);
        assertThat(testReportes.getAutorix()).isEqualTo(UPDATED_AUTORIX);
        assertThat(testReportes.getFechaix()).isEqualTo(UPDATED_FECHAIX);
        assertThat(testReportes.getImgix()).isEqualTo(UPDATED_IMGIX);
        assertThat(testReportes.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testReportes.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testReportes.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testReportes.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testReportes.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testReportes.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testReportes.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testReportes.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testReportes.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testReportes.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testReportes.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testReportes.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testReportes.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Reportes> reportesSearchList = IterableUtils.toList(reportesSearchRepository.findAll());
                Reportes testReportesSearch = reportesSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testReportesSearch.getTitulo()).isEqualTo(UPDATED_TITULO);
                assertThat(testReportesSearch.getCaso()).isEqualTo(UPDATED_CASO);
                assertThat(testReportesSearch.getImg()).isEqualTo(UPDATED_IMG);
                assertThat(testReportesSearch.getAutor()).isEqualTo(UPDATED_AUTOR);
                assertThat(testReportesSearch.getTituloix()).isEqualTo(UPDATED_TITULOIX);
                assertThat(testReportesSearch.getAutorix()).isEqualTo(UPDATED_AUTORIX);
                assertThat(testReportesSearch.getFechaix()).isEqualTo(UPDATED_FECHAIX);
                assertThat(testReportesSearch.getImgix()).isEqualTo(UPDATED_IMGIX);
                assertThat(testReportesSearch.getCiudad()).isEqualTo(UPDATED_CIUDAD);
                assertThat(testReportesSearch.getEstado()).isEqualTo(UPDATED_ESTADO);
                assertThat(testReportesSearch.getPais()).isEqualTo(UPDATED_PAIS);
                assertThat(testReportesSearch.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
                assertThat(testReportesSearch.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
                assertThat(testReportesSearch.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
                assertThat(testReportesSearch.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
                assertThat(testReportesSearch.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
                assertThat(testReportesSearch.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
                assertThat(testReportesSearch.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
                assertThat(testReportesSearch.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
                assertThat(testReportesSearch.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
                assertThat(testReportesSearch.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
            });
    }

    @Test
    @Transactional
    void putNonExistingReportes() throws Exception {
        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        reportes.setId(count.incrementAndGet());

        // Create the Reportes
        ReportesDTO reportesDTO = reportesMapper.toDto(reportes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportes() throws Exception {
        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        reportes.setId(count.incrementAndGet());

        // Create the Reportes
        ReportesDTO reportesDTO = reportesMapper.toDto(reportes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportes() throws Exception {
        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        reportes.setId(count.incrementAndGet());

        // Create the Reportes
        ReportesDTO reportesDTO = reportesMapper.toDto(reportes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateReportesWithPatch() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();

        // Update the reportes using partial update
        Reportes partialUpdatedReportes = new Reportes();
        partialUpdatedReportes.setId(reportes.getId());

        partialUpdatedReportes
            .titulo(UPDATED_TITULO)
            .autor(UPDATED_AUTOR)
            .tituloix(UPDATED_TITULOIX)
            .imgix(UPDATED_IMGIX)
            .estado(UPDATED_ESTADO)
            .extra2(UPDATED_EXTRA_2)
            .extra3(UPDATED_EXTRA_3)
            .extra4(UPDATED_EXTRA_4)
            .extra5(UPDATED_EXTRA_5)
            .extra9(UPDATED_EXTRA_9)
            .extra10(UPDATED_EXTRA_10);

        restReportesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportes))
            )
            .andExpect(status().isOk());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        Reportes testReportes = reportesList.get(reportesList.size() - 1);
        assertThat(testReportes.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testReportes.getCaso()).isEqualTo(DEFAULT_CASO);
        assertThat(testReportes.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testReportes.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testReportes.getTituloix()).isEqualTo(UPDATED_TITULOIX);
        assertThat(testReportes.getAutorix()).isEqualTo(DEFAULT_AUTORIX);
        assertThat(testReportes.getFechaix()).isEqualTo(DEFAULT_FECHAIX);
        assertThat(testReportes.getImgix()).isEqualTo(UPDATED_IMGIX);
        assertThat(testReportes.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testReportes.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testReportes.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testReportes.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testReportes.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testReportes.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testReportes.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testReportes.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testReportes.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testReportes.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testReportes.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testReportes.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testReportes.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void fullUpdateReportesWithPatch() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);

        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();

        // Update the reportes using partial update
        Reportes partialUpdatedReportes = new Reportes();
        partialUpdatedReportes.setId(reportes.getId());

        partialUpdatedReportes
            .titulo(UPDATED_TITULO)
            .caso(UPDATED_CASO)
            .img(UPDATED_IMG)
            .autor(UPDATED_AUTOR)
            .tituloix(UPDATED_TITULOIX)
            .autorix(UPDATED_AUTORIX)
            .fechaix(UPDATED_FECHAIX)
            .imgix(UPDATED_IMGIX)
            .ciudad(UPDATED_CIUDAD)
            .estado(UPDATED_ESTADO)
            .pais(UPDATED_PAIS)
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

        restReportesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportes))
            )
            .andExpect(status().isOk());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        Reportes testReportes = reportesList.get(reportesList.size() - 1);
        assertThat(testReportes.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testReportes.getCaso()).isEqualTo(UPDATED_CASO);
        assertThat(testReportes.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testReportes.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testReportes.getTituloix()).isEqualTo(UPDATED_TITULOIX);
        assertThat(testReportes.getAutorix()).isEqualTo(UPDATED_AUTORIX);
        assertThat(testReportes.getFechaix()).isEqualTo(UPDATED_FECHAIX);
        assertThat(testReportes.getImgix()).isEqualTo(UPDATED_IMGIX);
        assertThat(testReportes.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testReportes.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testReportes.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testReportes.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testReportes.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testReportes.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testReportes.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testReportes.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testReportes.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testReportes.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testReportes.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testReportes.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testReportes.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void patchNonExistingReportes() throws Exception {
        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        reportes.setId(count.incrementAndGet());

        // Create the Reportes
        ReportesDTO reportesDTO = reportesMapper.toDto(reportes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportes() throws Exception {
        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        reportes.setId(count.incrementAndGet());

        // Create the Reportes
        ReportesDTO reportesDTO = reportesMapper.toDto(reportes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportes() throws Exception {
        int databaseSizeBeforeUpdate = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        reportes.setId(count.incrementAndGet());

        // Create the Reportes
        ReportesDTO reportesDTO = reportesMapper.toDto(reportes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reportesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reportes in the database
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteReportes() throws Exception {
        // Initialize the database
        reportesRepository.saveAndFlush(reportes);
        reportesRepository.save(reportes);
        reportesSearchRepository.save(reportes);

        int databaseSizeBeforeDelete = reportesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the reportes
        restReportesMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reportes> reportesList = reportesRepository.findAll();
        assertThat(reportesList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reportesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchReportes() throws Exception {
        // Initialize the database
        reportes = reportesRepository.saveAndFlush(reportes);
        reportesSearchRepository.save(reportes);

        // Search the reportes
        restReportesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportes.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].caso").value(hasItem(DEFAULT_CASO)))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG)))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].tituloix").value(hasItem(DEFAULT_TITULOIX)))
            .andExpect(jsonPath("$.[*].autorix").value(hasItem(DEFAULT_AUTORIX)))
            .andExpect(jsonPath("$.[*].fechaix").value(hasItem(DEFAULT_FECHAIX.toString())))
            .andExpect(jsonPath("$.[*].imgix").value(hasItem(DEFAULT_IMGIX)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
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
