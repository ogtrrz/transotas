package wf.transotas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import wf.transotas.domain.Indice;
import wf.transotas.repository.IndiceRepository;
import wf.transotas.repository.search.IndiceSearchRepository;
import wf.transotas.service.criteria.IndiceCriteria;
import wf.transotas.service.dto.IndiceDTO;
import wf.transotas.service.mapper.IndiceMapper;

/**
 * Integration tests for the {@link IndiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndiceResourceIT {

    private static final String DEFAULT_IMG = "AAAAAAAAAA";
    private static final String UPDATED_IMG = "BBBBBBBBBB";

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_PAIS = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/indices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/indices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IndiceRepository indiceRepository;

    @Autowired
    private IndiceMapper indiceMapper;

    @Autowired
    private IndiceSearchRepository indiceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndiceMockMvc;

    private Indice indice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Indice createEntity(EntityManager em) {
        Indice indice = new Indice()
            .img(DEFAULT_IMG)
            .titulo(DEFAULT_TITULO)
            .url(DEFAULT_URL)
            .autor(DEFAULT_AUTOR)
            .fecha(DEFAULT_FECHA)
            .ciudad(DEFAULT_CIUDAD)
            .estado(DEFAULT_ESTADO)
            .pais(DEFAULT_PAIS)
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
        return indice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Indice createUpdatedEntity(EntityManager em) {
        Indice indice = new Indice()
            .img(UPDATED_IMG)
            .titulo(UPDATED_TITULO)
            .url(UPDATED_URL)
            .autor(UPDATED_AUTOR)
            .fecha(UPDATED_FECHA)
            .ciudad(UPDATED_CIUDAD)
            .estado(UPDATED_ESTADO)
            .pais(UPDATED_PAIS)
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
        return indice;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        indiceSearchRepository.deleteAll();
        assertThat(indiceSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        indice = createEntity(em);
    }

    @Test
    @Transactional
    void createIndice() throws Exception {
        int databaseSizeBeforeCreate = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        // Create the Indice
        IndiceDTO indiceDTO = indiceMapper.toDto(indice);
        restIndiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(indiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Indice testIndice = indiceList.get(indiceList.size() - 1);
        assertThat(testIndice.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testIndice.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testIndice.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testIndice.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testIndice.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testIndice.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testIndice.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testIndice.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testIndice.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testIndice.getVistas()).isEqualTo(DEFAULT_VISTAS);
        assertThat(testIndice.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testIndice.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testIndice.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testIndice.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testIndice.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testIndice.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testIndice.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testIndice.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testIndice.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testIndice.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testIndice.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void createIndiceWithExistingId() throws Exception {
        // Create the Indice with an existing ID
        indice.setId(1L);
        IndiceDTO indiceDTO = indiceMapper.toDto(indice);

        int databaseSizeBeforeCreate = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(indiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllIndices() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList
        restIndiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indice.getId().intValue())))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
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
    void getIndice() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get the indice
        restIndiceMockMvc
            .perform(get(ENTITY_API_URL_ID, indice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indice.getId().intValue()))
            .andExpect(jsonPath("$.img").value(DEFAULT_IMG))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.autor").value(DEFAULT_AUTOR))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS))
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
    void getIndicesByIdFiltering() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        Long id = indice.getId();

        defaultIndiceShouldBeFound("id.equals=" + id);
        defaultIndiceShouldNotBeFound("id.notEquals=" + id);

        defaultIndiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIndiceShouldNotBeFound("id.greaterThan=" + id);

        defaultIndiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIndiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndicesByImgIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where img equals to DEFAULT_IMG
        defaultIndiceShouldBeFound("img.equals=" + DEFAULT_IMG);

        // Get all the indiceList where img equals to UPDATED_IMG
        defaultIndiceShouldNotBeFound("img.equals=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllIndicesByImgIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where img in DEFAULT_IMG or UPDATED_IMG
        defaultIndiceShouldBeFound("img.in=" + DEFAULT_IMG + "," + UPDATED_IMG);

        // Get all the indiceList where img equals to UPDATED_IMG
        defaultIndiceShouldNotBeFound("img.in=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllIndicesByImgIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where img is not null
        defaultIndiceShouldBeFound("img.specified=true");

        // Get all the indiceList where img is null
        defaultIndiceShouldNotBeFound("img.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByImgContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where img contains DEFAULT_IMG
        defaultIndiceShouldBeFound("img.contains=" + DEFAULT_IMG);

        // Get all the indiceList where img contains UPDATED_IMG
        defaultIndiceShouldNotBeFound("img.contains=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllIndicesByImgNotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where img does not contain DEFAULT_IMG
        defaultIndiceShouldNotBeFound("img.doesNotContain=" + DEFAULT_IMG);

        // Get all the indiceList where img does not contain UPDATED_IMG
        defaultIndiceShouldBeFound("img.doesNotContain=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllIndicesByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where titulo equals to DEFAULT_TITULO
        defaultIndiceShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the indiceList where titulo equals to UPDATED_TITULO
        defaultIndiceShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllIndicesByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultIndiceShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the indiceList where titulo equals to UPDATED_TITULO
        defaultIndiceShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllIndicesByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where titulo is not null
        defaultIndiceShouldBeFound("titulo.specified=true");

        // Get all the indiceList where titulo is null
        defaultIndiceShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByTituloContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where titulo contains DEFAULT_TITULO
        defaultIndiceShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the indiceList where titulo contains UPDATED_TITULO
        defaultIndiceShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllIndicesByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where titulo does not contain DEFAULT_TITULO
        defaultIndiceShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the indiceList where titulo does not contain UPDATED_TITULO
        defaultIndiceShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllIndicesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where url equals to DEFAULT_URL
        defaultIndiceShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the indiceList where url equals to UPDATED_URL
        defaultIndiceShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllIndicesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where url in DEFAULT_URL or UPDATED_URL
        defaultIndiceShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the indiceList where url equals to UPDATED_URL
        defaultIndiceShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllIndicesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where url is not null
        defaultIndiceShouldBeFound("url.specified=true");

        // Get all the indiceList where url is null
        defaultIndiceShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByUrlContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where url contains DEFAULT_URL
        defaultIndiceShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the indiceList where url contains UPDATED_URL
        defaultIndiceShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllIndicesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where url does not contain DEFAULT_URL
        defaultIndiceShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the indiceList where url does not contain UPDATED_URL
        defaultIndiceShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllIndicesByAutorIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where autor equals to DEFAULT_AUTOR
        defaultIndiceShouldBeFound("autor.equals=" + DEFAULT_AUTOR);

        // Get all the indiceList where autor equals to UPDATED_AUTOR
        defaultIndiceShouldNotBeFound("autor.equals=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllIndicesByAutorIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where autor in DEFAULT_AUTOR or UPDATED_AUTOR
        defaultIndiceShouldBeFound("autor.in=" + DEFAULT_AUTOR + "," + UPDATED_AUTOR);

        // Get all the indiceList where autor equals to UPDATED_AUTOR
        defaultIndiceShouldNotBeFound("autor.in=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllIndicesByAutorIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where autor is not null
        defaultIndiceShouldBeFound("autor.specified=true");

        // Get all the indiceList where autor is null
        defaultIndiceShouldNotBeFound("autor.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByAutorContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where autor contains DEFAULT_AUTOR
        defaultIndiceShouldBeFound("autor.contains=" + DEFAULT_AUTOR);

        // Get all the indiceList where autor contains UPDATED_AUTOR
        defaultIndiceShouldNotBeFound("autor.contains=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllIndicesByAutorNotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where autor does not contain DEFAULT_AUTOR
        defaultIndiceShouldNotBeFound("autor.doesNotContain=" + DEFAULT_AUTOR);

        // Get all the indiceList where autor does not contain UPDATED_AUTOR
        defaultIndiceShouldBeFound("autor.doesNotContain=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllIndicesByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where fecha equals to DEFAULT_FECHA
        defaultIndiceShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the indiceList where fecha equals to UPDATED_FECHA
        defaultIndiceShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllIndicesByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultIndiceShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the indiceList where fecha equals to UPDATED_FECHA
        defaultIndiceShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllIndicesByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where fecha is not null
        defaultIndiceShouldBeFound("fecha.specified=true");

        // Get all the indiceList where fecha is null
        defaultIndiceShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByCiudadIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where ciudad equals to DEFAULT_CIUDAD
        defaultIndiceShouldBeFound("ciudad.equals=" + DEFAULT_CIUDAD);

        // Get all the indiceList where ciudad equals to UPDATED_CIUDAD
        defaultIndiceShouldNotBeFound("ciudad.equals=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllIndicesByCiudadIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where ciudad in DEFAULT_CIUDAD or UPDATED_CIUDAD
        defaultIndiceShouldBeFound("ciudad.in=" + DEFAULT_CIUDAD + "," + UPDATED_CIUDAD);

        // Get all the indiceList where ciudad equals to UPDATED_CIUDAD
        defaultIndiceShouldNotBeFound("ciudad.in=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllIndicesByCiudadIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where ciudad is not null
        defaultIndiceShouldBeFound("ciudad.specified=true");

        // Get all the indiceList where ciudad is null
        defaultIndiceShouldNotBeFound("ciudad.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByCiudadContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where ciudad contains DEFAULT_CIUDAD
        defaultIndiceShouldBeFound("ciudad.contains=" + DEFAULT_CIUDAD);

        // Get all the indiceList where ciudad contains UPDATED_CIUDAD
        defaultIndiceShouldNotBeFound("ciudad.contains=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllIndicesByCiudadNotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where ciudad does not contain DEFAULT_CIUDAD
        defaultIndiceShouldNotBeFound("ciudad.doesNotContain=" + DEFAULT_CIUDAD);

        // Get all the indiceList where ciudad does not contain UPDATED_CIUDAD
        defaultIndiceShouldBeFound("ciudad.doesNotContain=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllIndicesByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where estado equals to DEFAULT_ESTADO
        defaultIndiceShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the indiceList where estado equals to UPDATED_ESTADO
        defaultIndiceShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIndicesByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultIndiceShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the indiceList where estado equals to UPDATED_ESTADO
        defaultIndiceShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIndicesByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where estado is not null
        defaultIndiceShouldBeFound("estado.specified=true");

        // Get all the indiceList where estado is null
        defaultIndiceShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByEstadoContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where estado contains DEFAULT_ESTADO
        defaultIndiceShouldBeFound("estado.contains=" + DEFAULT_ESTADO);

        // Get all the indiceList where estado contains UPDATED_ESTADO
        defaultIndiceShouldNotBeFound("estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIndicesByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where estado does not contain DEFAULT_ESTADO
        defaultIndiceShouldNotBeFound("estado.doesNotContain=" + DEFAULT_ESTADO);

        // Get all the indiceList where estado does not contain UPDATED_ESTADO
        defaultIndiceShouldBeFound("estado.doesNotContain=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIndicesByPaisIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where pais equals to DEFAULT_PAIS
        defaultIndiceShouldBeFound("pais.equals=" + DEFAULT_PAIS);

        // Get all the indiceList where pais equals to UPDATED_PAIS
        defaultIndiceShouldNotBeFound("pais.equals=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllIndicesByPaisIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where pais in DEFAULT_PAIS or UPDATED_PAIS
        defaultIndiceShouldBeFound("pais.in=" + DEFAULT_PAIS + "," + UPDATED_PAIS);

        // Get all the indiceList where pais equals to UPDATED_PAIS
        defaultIndiceShouldNotBeFound("pais.in=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllIndicesByPaisIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where pais is not null
        defaultIndiceShouldBeFound("pais.specified=true");

        // Get all the indiceList where pais is null
        defaultIndiceShouldNotBeFound("pais.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByPaisContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where pais contains DEFAULT_PAIS
        defaultIndiceShouldBeFound("pais.contains=" + DEFAULT_PAIS);

        // Get all the indiceList where pais contains UPDATED_PAIS
        defaultIndiceShouldNotBeFound("pais.contains=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllIndicesByPaisNotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where pais does not contain DEFAULT_PAIS
        defaultIndiceShouldNotBeFound("pais.doesNotContain=" + DEFAULT_PAIS);

        // Get all the indiceList where pais does not contain UPDATED_PAIS
        defaultIndiceShouldBeFound("pais.doesNotContain=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllIndicesByComentariosIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where comentarios equals to DEFAULT_COMENTARIOS
        defaultIndiceShouldBeFound("comentarios.equals=" + DEFAULT_COMENTARIOS);

        // Get all the indiceList where comentarios equals to UPDATED_COMENTARIOS
        defaultIndiceShouldNotBeFound("comentarios.equals=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndicesByComentariosIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where comentarios in DEFAULT_COMENTARIOS or UPDATED_COMENTARIOS
        defaultIndiceShouldBeFound("comentarios.in=" + DEFAULT_COMENTARIOS + "," + UPDATED_COMENTARIOS);

        // Get all the indiceList where comentarios equals to UPDATED_COMENTARIOS
        defaultIndiceShouldNotBeFound("comentarios.in=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndicesByComentariosIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where comentarios is not null
        defaultIndiceShouldBeFound("comentarios.specified=true");

        // Get all the indiceList where comentarios is null
        defaultIndiceShouldNotBeFound("comentarios.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByComentariosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where comentarios is greater than or equal to DEFAULT_COMENTARIOS
        defaultIndiceShouldBeFound("comentarios.greaterThanOrEqual=" + DEFAULT_COMENTARIOS);

        // Get all the indiceList where comentarios is greater than or equal to UPDATED_COMENTARIOS
        defaultIndiceShouldNotBeFound("comentarios.greaterThanOrEqual=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndicesByComentariosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where comentarios is less than or equal to DEFAULT_COMENTARIOS
        defaultIndiceShouldBeFound("comentarios.lessThanOrEqual=" + DEFAULT_COMENTARIOS);

        // Get all the indiceList where comentarios is less than or equal to SMALLER_COMENTARIOS
        defaultIndiceShouldNotBeFound("comentarios.lessThanOrEqual=" + SMALLER_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndicesByComentariosIsLessThanSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where comentarios is less than DEFAULT_COMENTARIOS
        defaultIndiceShouldNotBeFound("comentarios.lessThan=" + DEFAULT_COMENTARIOS);

        // Get all the indiceList where comentarios is less than UPDATED_COMENTARIOS
        defaultIndiceShouldBeFound("comentarios.lessThan=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndicesByComentariosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where comentarios is greater than DEFAULT_COMENTARIOS
        defaultIndiceShouldNotBeFound("comentarios.greaterThan=" + DEFAULT_COMENTARIOS);

        // Get all the indiceList where comentarios is greater than SMALLER_COMENTARIOS
        defaultIndiceShouldBeFound("comentarios.greaterThan=" + SMALLER_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndicesByVistasIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where vistas equals to DEFAULT_VISTAS
        defaultIndiceShouldBeFound("vistas.equals=" + DEFAULT_VISTAS);

        // Get all the indiceList where vistas equals to UPDATED_VISTAS
        defaultIndiceShouldNotBeFound("vistas.equals=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndicesByVistasIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where vistas in DEFAULT_VISTAS or UPDATED_VISTAS
        defaultIndiceShouldBeFound("vistas.in=" + DEFAULT_VISTAS + "," + UPDATED_VISTAS);

        // Get all the indiceList where vistas equals to UPDATED_VISTAS
        defaultIndiceShouldNotBeFound("vistas.in=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndicesByVistasIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where vistas is not null
        defaultIndiceShouldBeFound("vistas.specified=true");

        // Get all the indiceList where vistas is null
        defaultIndiceShouldNotBeFound("vistas.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByVistasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where vistas is greater than or equal to DEFAULT_VISTAS
        defaultIndiceShouldBeFound("vistas.greaterThanOrEqual=" + DEFAULT_VISTAS);

        // Get all the indiceList where vistas is greater than or equal to UPDATED_VISTAS
        defaultIndiceShouldNotBeFound("vistas.greaterThanOrEqual=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndicesByVistasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where vistas is less than or equal to DEFAULT_VISTAS
        defaultIndiceShouldBeFound("vistas.lessThanOrEqual=" + DEFAULT_VISTAS);

        // Get all the indiceList where vistas is less than or equal to SMALLER_VISTAS
        defaultIndiceShouldNotBeFound("vistas.lessThanOrEqual=" + SMALLER_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndicesByVistasIsLessThanSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where vistas is less than DEFAULT_VISTAS
        defaultIndiceShouldNotBeFound("vistas.lessThan=" + DEFAULT_VISTAS);

        // Get all the indiceList where vistas is less than UPDATED_VISTAS
        defaultIndiceShouldBeFound("vistas.lessThan=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndicesByVistasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where vistas is greater than DEFAULT_VISTAS
        defaultIndiceShouldNotBeFound("vistas.greaterThan=" + DEFAULT_VISTAS);

        // Get all the indiceList where vistas is greater than SMALLER_VISTAS
        defaultIndiceShouldBeFound("vistas.greaterThan=" + SMALLER_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndicesByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where rating equals to DEFAULT_RATING
        defaultIndiceShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the indiceList where rating equals to UPDATED_RATING
        defaultIndiceShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllIndicesByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultIndiceShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the indiceList where rating equals to UPDATED_RATING
        defaultIndiceShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllIndicesByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where rating is not null
        defaultIndiceShouldBeFound("rating.specified=true");

        // Get all the indiceList where rating is null
        defaultIndiceShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where rating is greater than or equal to DEFAULT_RATING
        defaultIndiceShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the indiceList where rating is greater than or equal to UPDATED_RATING
        defaultIndiceShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllIndicesByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where rating is less than or equal to DEFAULT_RATING
        defaultIndiceShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the indiceList where rating is less than or equal to SMALLER_RATING
        defaultIndiceShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllIndicesByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where rating is less than DEFAULT_RATING
        defaultIndiceShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the indiceList where rating is less than UPDATED_RATING
        defaultIndiceShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllIndicesByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where rating is greater than DEFAULT_RATING
        defaultIndiceShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the indiceList where rating is greater than SMALLER_RATING
        defaultIndiceShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra1IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra1 equals to DEFAULT_EXTRA_1
        defaultIndiceShouldBeFound("extra1.equals=" + DEFAULT_EXTRA_1);

        // Get all the indiceList where extra1 equals to UPDATED_EXTRA_1
        defaultIndiceShouldNotBeFound("extra1.equals=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra1IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra1 in DEFAULT_EXTRA_1 or UPDATED_EXTRA_1
        defaultIndiceShouldBeFound("extra1.in=" + DEFAULT_EXTRA_1 + "," + UPDATED_EXTRA_1);

        // Get all the indiceList where extra1 equals to UPDATED_EXTRA_1
        defaultIndiceShouldNotBeFound("extra1.in=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra1IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra1 is not null
        defaultIndiceShouldBeFound("extra1.specified=true");

        // Get all the indiceList where extra1 is null
        defaultIndiceShouldNotBeFound("extra1.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra1ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra1 contains DEFAULT_EXTRA_1
        defaultIndiceShouldBeFound("extra1.contains=" + DEFAULT_EXTRA_1);

        // Get all the indiceList where extra1 contains UPDATED_EXTRA_1
        defaultIndiceShouldNotBeFound("extra1.contains=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra1NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra1 does not contain DEFAULT_EXTRA_1
        defaultIndiceShouldNotBeFound("extra1.doesNotContain=" + DEFAULT_EXTRA_1);

        // Get all the indiceList where extra1 does not contain UPDATED_EXTRA_1
        defaultIndiceShouldBeFound("extra1.doesNotContain=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra2IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra2 equals to DEFAULT_EXTRA_2
        defaultIndiceShouldBeFound("extra2.equals=" + DEFAULT_EXTRA_2);

        // Get all the indiceList where extra2 equals to UPDATED_EXTRA_2
        defaultIndiceShouldNotBeFound("extra2.equals=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra2IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra2 in DEFAULT_EXTRA_2 or UPDATED_EXTRA_2
        defaultIndiceShouldBeFound("extra2.in=" + DEFAULT_EXTRA_2 + "," + UPDATED_EXTRA_2);

        // Get all the indiceList where extra2 equals to UPDATED_EXTRA_2
        defaultIndiceShouldNotBeFound("extra2.in=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra2IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra2 is not null
        defaultIndiceShouldBeFound("extra2.specified=true");

        // Get all the indiceList where extra2 is null
        defaultIndiceShouldNotBeFound("extra2.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra2ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra2 contains DEFAULT_EXTRA_2
        defaultIndiceShouldBeFound("extra2.contains=" + DEFAULT_EXTRA_2);

        // Get all the indiceList where extra2 contains UPDATED_EXTRA_2
        defaultIndiceShouldNotBeFound("extra2.contains=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra2NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra2 does not contain DEFAULT_EXTRA_2
        defaultIndiceShouldNotBeFound("extra2.doesNotContain=" + DEFAULT_EXTRA_2);

        // Get all the indiceList where extra2 does not contain UPDATED_EXTRA_2
        defaultIndiceShouldBeFound("extra2.doesNotContain=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra3IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra3 equals to DEFAULT_EXTRA_3
        defaultIndiceShouldBeFound("extra3.equals=" + DEFAULT_EXTRA_3);

        // Get all the indiceList where extra3 equals to UPDATED_EXTRA_3
        defaultIndiceShouldNotBeFound("extra3.equals=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra3IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra3 in DEFAULT_EXTRA_3 or UPDATED_EXTRA_3
        defaultIndiceShouldBeFound("extra3.in=" + DEFAULT_EXTRA_3 + "," + UPDATED_EXTRA_3);

        // Get all the indiceList where extra3 equals to UPDATED_EXTRA_3
        defaultIndiceShouldNotBeFound("extra3.in=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra3IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra3 is not null
        defaultIndiceShouldBeFound("extra3.specified=true");

        // Get all the indiceList where extra3 is null
        defaultIndiceShouldNotBeFound("extra3.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra3ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra3 contains DEFAULT_EXTRA_3
        defaultIndiceShouldBeFound("extra3.contains=" + DEFAULT_EXTRA_3);

        // Get all the indiceList where extra3 contains UPDATED_EXTRA_3
        defaultIndiceShouldNotBeFound("extra3.contains=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra3NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra3 does not contain DEFAULT_EXTRA_3
        defaultIndiceShouldNotBeFound("extra3.doesNotContain=" + DEFAULT_EXTRA_3);

        // Get all the indiceList where extra3 does not contain UPDATED_EXTRA_3
        defaultIndiceShouldBeFound("extra3.doesNotContain=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra4IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra4 equals to DEFAULT_EXTRA_4
        defaultIndiceShouldBeFound("extra4.equals=" + DEFAULT_EXTRA_4);

        // Get all the indiceList where extra4 equals to UPDATED_EXTRA_4
        defaultIndiceShouldNotBeFound("extra4.equals=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra4IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra4 in DEFAULT_EXTRA_4 or UPDATED_EXTRA_4
        defaultIndiceShouldBeFound("extra4.in=" + DEFAULT_EXTRA_4 + "," + UPDATED_EXTRA_4);

        // Get all the indiceList where extra4 equals to UPDATED_EXTRA_4
        defaultIndiceShouldNotBeFound("extra4.in=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra4IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra4 is not null
        defaultIndiceShouldBeFound("extra4.specified=true");

        // Get all the indiceList where extra4 is null
        defaultIndiceShouldNotBeFound("extra4.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra4ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra4 contains DEFAULT_EXTRA_4
        defaultIndiceShouldBeFound("extra4.contains=" + DEFAULT_EXTRA_4);

        // Get all the indiceList where extra4 contains UPDATED_EXTRA_4
        defaultIndiceShouldNotBeFound("extra4.contains=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra4NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra4 does not contain DEFAULT_EXTRA_4
        defaultIndiceShouldNotBeFound("extra4.doesNotContain=" + DEFAULT_EXTRA_4);

        // Get all the indiceList where extra4 does not contain UPDATED_EXTRA_4
        defaultIndiceShouldBeFound("extra4.doesNotContain=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra5IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra5 equals to DEFAULT_EXTRA_5
        defaultIndiceShouldBeFound("extra5.equals=" + DEFAULT_EXTRA_5);

        // Get all the indiceList where extra5 equals to UPDATED_EXTRA_5
        defaultIndiceShouldNotBeFound("extra5.equals=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra5IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra5 in DEFAULT_EXTRA_5 or UPDATED_EXTRA_5
        defaultIndiceShouldBeFound("extra5.in=" + DEFAULT_EXTRA_5 + "," + UPDATED_EXTRA_5);

        // Get all the indiceList where extra5 equals to UPDATED_EXTRA_5
        defaultIndiceShouldNotBeFound("extra5.in=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra5IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra5 is not null
        defaultIndiceShouldBeFound("extra5.specified=true");

        // Get all the indiceList where extra5 is null
        defaultIndiceShouldNotBeFound("extra5.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra5ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra5 contains DEFAULT_EXTRA_5
        defaultIndiceShouldBeFound("extra5.contains=" + DEFAULT_EXTRA_5);

        // Get all the indiceList where extra5 contains UPDATED_EXTRA_5
        defaultIndiceShouldNotBeFound("extra5.contains=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra5NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra5 does not contain DEFAULT_EXTRA_5
        defaultIndiceShouldNotBeFound("extra5.doesNotContain=" + DEFAULT_EXTRA_5);

        // Get all the indiceList where extra5 does not contain UPDATED_EXTRA_5
        defaultIndiceShouldBeFound("extra5.doesNotContain=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra6IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra6 equals to DEFAULT_EXTRA_6
        defaultIndiceShouldBeFound("extra6.equals=" + DEFAULT_EXTRA_6);

        // Get all the indiceList where extra6 equals to UPDATED_EXTRA_6
        defaultIndiceShouldNotBeFound("extra6.equals=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra6IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra6 in DEFAULT_EXTRA_6 or UPDATED_EXTRA_6
        defaultIndiceShouldBeFound("extra6.in=" + DEFAULT_EXTRA_6 + "," + UPDATED_EXTRA_6);

        // Get all the indiceList where extra6 equals to UPDATED_EXTRA_6
        defaultIndiceShouldNotBeFound("extra6.in=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra6IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra6 is not null
        defaultIndiceShouldBeFound("extra6.specified=true");

        // Get all the indiceList where extra6 is null
        defaultIndiceShouldNotBeFound("extra6.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra6ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra6 contains DEFAULT_EXTRA_6
        defaultIndiceShouldBeFound("extra6.contains=" + DEFAULT_EXTRA_6);

        // Get all the indiceList where extra6 contains UPDATED_EXTRA_6
        defaultIndiceShouldNotBeFound("extra6.contains=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra6NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra6 does not contain DEFAULT_EXTRA_6
        defaultIndiceShouldNotBeFound("extra6.doesNotContain=" + DEFAULT_EXTRA_6);

        // Get all the indiceList where extra6 does not contain UPDATED_EXTRA_6
        defaultIndiceShouldBeFound("extra6.doesNotContain=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra7IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra7 equals to DEFAULT_EXTRA_7
        defaultIndiceShouldBeFound("extra7.equals=" + DEFAULT_EXTRA_7);

        // Get all the indiceList where extra7 equals to UPDATED_EXTRA_7
        defaultIndiceShouldNotBeFound("extra7.equals=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra7IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra7 in DEFAULT_EXTRA_7 or UPDATED_EXTRA_7
        defaultIndiceShouldBeFound("extra7.in=" + DEFAULT_EXTRA_7 + "," + UPDATED_EXTRA_7);

        // Get all the indiceList where extra7 equals to UPDATED_EXTRA_7
        defaultIndiceShouldNotBeFound("extra7.in=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra7IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra7 is not null
        defaultIndiceShouldBeFound("extra7.specified=true");

        // Get all the indiceList where extra7 is null
        defaultIndiceShouldNotBeFound("extra7.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra7ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra7 contains DEFAULT_EXTRA_7
        defaultIndiceShouldBeFound("extra7.contains=" + DEFAULT_EXTRA_7);

        // Get all the indiceList where extra7 contains UPDATED_EXTRA_7
        defaultIndiceShouldNotBeFound("extra7.contains=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra7NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra7 does not contain DEFAULT_EXTRA_7
        defaultIndiceShouldNotBeFound("extra7.doesNotContain=" + DEFAULT_EXTRA_7);

        // Get all the indiceList where extra7 does not contain UPDATED_EXTRA_7
        defaultIndiceShouldBeFound("extra7.doesNotContain=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra8IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra8 equals to DEFAULT_EXTRA_8
        defaultIndiceShouldBeFound("extra8.equals=" + DEFAULT_EXTRA_8);

        // Get all the indiceList where extra8 equals to UPDATED_EXTRA_8
        defaultIndiceShouldNotBeFound("extra8.equals=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra8IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra8 in DEFAULT_EXTRA_8 or UPDATED_EXTRA_8
        defaultIndiceShouldBeFound("extra8.in=" + DEFAULT_EXTRA_8 + "," + UPDATED_EXTRA_8);

        // Get all the indiceList where extra8 equals to UPDATED_EXTRA_8
        defaultIndiceShouldNotBeFound("extra8.in=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra8IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra8 is not null
        defaultIndiceShouldBeFound("extra8.specified=true");

        // Get all the indiceList where extra8 is null
        defaultIndiceShouldNotBeFound("extra8.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra8ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra8 contains DEFAULT_EXTRA_8
        defaultIndiceShouldBeFound("extra8.contains=" + DEFAULT_EXTRA_8);

        // Get all the indiceList where extra8 contains UPDATED_EXTRA_8
        defaultIndiceShouldNotBeFound("extra8.contains=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra8NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra8 does not contain DEFAULT_EXTRA_8
        defaultIndiceShouldNotBeFound("extra8.doesNotContain=" + DEFAULT_EXTRA_8);

        // Get all the indiceList where extra8 does not contain UPDATED_EXTRA_8
        defaultIndiceShouldBeFound("extra8.doesNotContain=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra9IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra9 equals to DEFAULT_EXTRA_9
        defaultIndiceShouldBeFound("extra9.equals=" + DEFAULT_EXTRA_9);

        // Get all the indiceList where extra9 equals to UPDATED_EXTRA_9
        defaultIndiceShouldNotBeFound("extra9.equals=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra9IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra9 in DEFAULT_EXTRA_9 or UPDATED_EXTRA_9
        defaultIndiceShouldBeFound("extra9.in=" + DEFAULT_EXTRA_9 + "," + UPDATED_EXTRA_9);

        // Get all the indiceList where extra9 equals to UPDATED_EXTRA_9
        defaultIndiceShouldNotBeFound("extra9.in=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra9IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra9 is not null
        defaultIndiceShouldBeFound("extra9.specified=true");

        // Get all the indiceList where extra9 is null
        defaultIndiceShouldNotBeFound("extra9.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra9ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra9 contains DEFAULT_EXTRA_9
        defaultIndiceShouldBeFound("extra9.contains=" + DEFAULT_EXTRA_9);

        // Get all the indiceList where extra9 contains UPDATED_EXTRA_9
        defaultIndiceShouldNotBeFound("extra9.contains=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra9NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra9 does not contain DEFAULT_EXTRA_9
        defaultIndiceShouldNotBeFound("extra9.doesNotContain=" + DEFAULT_EXTRA_9);

        // Get all the indiceList where extra9 does not contain UPDATED_EXTRA_9
        defaultIndiceShouldBeFound("extra9.doesNotContain=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra10IsEqualToSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra10 equals to DEFAULT_EXTRA_10
        defaultIndiceShouldBeFound("extra10.equals=" + DEFAULT_EXTRA_10);

        // Get all the indiceList where extra10 equals to UPDATED_EXTRA_10
        defaultIndiceShouldNotBeFound("extra10.equals=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra10IsInShouldWork() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra10 in DEFAULT_EXTRA_10 or UPDATED_EXTRA_10
        defaultIndiceShouldBeFound("extra10.in=" + DEFAULT_EXTRA_10 + "," + UPDATED_EXTRA_10);

        // Get all the indiceList where extra10 equals to UPDATED_EXTRA_10
        defaultIndiceShouldNotBeFound("extra10.in=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra10IsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra10 is not null
        defaultIndiceShouldBeFound("extra10.specified=true");

        // Get all the indiceList where extra10 is null
        defaultIndiceShouldNotBeFound("extra10.specified=false");
    }

    @Test
    @Transactional
    void getAllIndicesByExtra10ContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra10 contains DEFAULT_EXTRA_10
        defaultIndiceShouldBeFound("extra10.contains=" + DEFAULT_EXTRA_10);

        // Get all the indiceList where extra10 contains UPDATED_EXTRA_10
        defaultIndiceShouldNotBeFound("extra10.contains=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllIndicesByExtra10NotContainsSomething() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        // Get all the indiceList where extra10 does not contain DEFAULT_EXTRA_10
        defaultIndiceShouldNotBeFound("extra10.doesNotContain=" + DEFAULT_EXTRA_10);

        // Get all the indiceList where extra10 does not contain UPDATED_EXTRA_10
        defaultIndiceShouldBeFound("extra10.doesNotContain=" + UPDATED_EXTRA_10);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndiceShouldBeFound(String filter) throws Exception {
        restIndiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indice.getId().intValue())))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
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
        restIndiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndiceShouldNotBeFound(String filter) throws Exception {
        restIndiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndice() throws Exception {
        // Get the indice
        restIndiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndice() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();
        indiceSearchRepository.save(indice);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());

        // Update the indice
        Indice updatedIndice = indiceRepository.findById(indice.getId()).get();
        // Disconnect from session so that the updates on updatedIndice are not directly saved in db
        em.detach(updatedIndice);
        updatedIndice
            .img(UPDATED_IMG)
            .titulo(UPDATED_TITULO)
            .url(UPDATED_URL)
            .autor(UPDATED_AUTOR)
            .fecha(UPDATED_FECHA)
            .ciudad(UPDATED_CIUDAD)
            .estado(UPDATED_ESTADO)
            .pais(UPDATED_PAIS)
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
        IndiceDTO indiceDTO = indiceMapper.toDto(updatedIndice);

        restIndiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(indiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        Indice testIndice = indiceList.get(indiceList.size() - 1);
        assertThat(testIndice.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testIndice.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testIndice.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testIndice.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testIndice.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testIndice.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testIndice.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testIndice.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testIndice.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testIndice.getVistas()).isEqualTo(UPDATED_VISTAS);
        assertThat(testIndice.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testIndice.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testIndice.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testIndice.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testIndice.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testIndice.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testIndice.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testIndice.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testIndice.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testIndice.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testIndice.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Indice> indiceSearchList = IterableUtils.toList(indiceSearchRepository.findAll());
                Indice testIndiceSearch = indiceSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testIndiceSearch.getImg()).isEqualTo(UPDATED_IMG);
                assertThat(testIndiceSearch.getTitulo()).isEqualTo(UPDATED_TITULO);
                assertThat(testIndiceSearch.getUrl()).isEqualTo(UPDATED_URL);
                assertThat(testIndiceSearch.getAutor()).isEqualTo(UPDATED_AUTOR);
                assertThat(testIndiceSearch.getFecha()).isEqualTo(UPDATED_FECHA);
                assertThat(testIndiceSearch.getCiudad()).isEqualTo(UPDATED_CIUDAD);
                assertThat(testIndiceSearch.getEstado()).isEqualTo(UPDATED_ESTADO);
                assertThat(testIndiceSearch.getPais()).isEqualTo(UPDATED_PAIS);
                assertThat(testIndiceSearch.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
                assertThat(testIndiceSearch.getVistas()).isEqualTo(UPDATED_VISTAS);
                assertThat(testIndiceSearch.getRating()).isEqualTo(UPDATED_RATING);
                assertThat(testIndiceSearch.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
                assertThat(testIndiceSearch.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
                assertThat(testIndiceSearch.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
                assertThat(testIndiceSearch.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
                assertThat(testIndiceSearch.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
                assertThat(testIndiceSearch.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
                assertThat(testIndiceSearch.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
                assertThat(testIndiceSearch.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
                assertThat(testIndiceSearch.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
                assertThat(testIndiceSearch.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
            });
    }

    @Test
    @Transactional
    void putNonExistingIndice() throws Exception {
        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        indice.setId(count.incrementAndGet());

        // Create the Indice
        IndiceDTO indiceDTO = indiceMapper.toDto(indice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(indiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndice() throws Exception {
        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        indice.setId(count.incrementAndGet());

        // Create the Indice
        IndiceDTO indiceDTO = indiceMapper.toDto(indice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(indiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndice() throws Exception {
        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        indice.setId(count.incrementAndGet());

        // Create the Indice
        IndiceDTO indiceDTO = indiceMapper.toDto(indice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(indiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateIndiceWithPatch() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();

        // Update the indice using partial update
        Indice partialUpdatedIndice = new Indice();
        partialUpdatedIndice.setId(indice.getId());

        partialUpdatedIndice
            .url(UPDATED_URL)
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO)
            .extra1(UPDATED_EXTRA_1)
            .extra6(UPDATED_EXTRA_6)
            .extra8(UPDATED_EXTRA_8)
            .extra9(UPDATED_EXTRA_9);

        restIndiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndice))
            )
            .andExpect(status().isOk());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        Indice testIndice = indiceList.get(indiceList.size() - 1);
        assertThat(testIndice.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testIndice.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testIndice.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testIndice.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testIndice.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testIndice.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testIndice.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testIndice.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testIndice.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testIndice.getVistas()).isEqualTo(DEFAULT_VISTAS);
        assertThat(testIndice.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testIndice.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testIndice.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testIndice.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testIndice.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testIndice.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testIndice.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testIndice.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testIndice.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testIndice.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testIndice.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void fullUpdateIndiceWithPatch() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);

        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();

        // Update the indice using partial update
        Indice partialUpdatedIndice = new Indice();
        partialUpdatedIndice.setId(indice.getId());

        partialUpdatedIndice
            .img(UPDATED_IMG)
            .titulo(UPDATED_TITULO)
            .url(UPDATED_URL)
            .autor(UPDATED_AUTOR)
            .fecha(UPDATED_FECHA)
            .ciudad(UPDATED_CIUDAD)
            .estado(UPDATED_ESTADO)
            .pais(UPDATED_PAIS)
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

        restIndiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndice))
            )
            .andExpect(status().isOk());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        Indice testIndice = indiceList.get(indiceList.size() - 1);
        assertThat(testIndice.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testIndice.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testIndice.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testIndice.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testIndice.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testIndice.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testIndice.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testIndice.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testIndice.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testIndice.getVistas()).isEqualTo(UPDATED_VISTAS);
        assertThat(testIndice.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testIndice.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testIndice.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testIndice.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testIndice.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testIndice.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testIndice.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testIndice.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testIndice.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testIndice.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testIndice.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void patchNonExistingIndice() throws Exception {
        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        indice.setId(count.incrementAndGet());

        // Create the Indice
        IndiceDTO indiceDTO = indiceMapper.toDto(indice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(indiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndice() throws Exception {
        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        indice.setId(count.incrementAndGet());

        // Create the Indice
        IndiceDTO indiceDTO = indiceMapper.toDto(indice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(indiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndice() throws Exception {
        int databaseSizeBeforeUpdate = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        indice.setId(count.incrementAndGet());

        // Create the Indice
        IndiceDTO indiceDTO = indiceMapper.toDto(indice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(indiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Indice in the database
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteIndice() throws Exception {
        // Initialize the database
        indiceRepository.saveAndFlush(indice);
        indiceRepository.save(indice);
        indiceSearchRepository.save(indice);

        int databaseSizeBeforeDelete = indiceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the indice
        restIndiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, indice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Indice> indiceList = indiceRepository.findAll();
        assertThat(indiceList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchIndice() throws Exception {
        // Initialize the database
        indice = indiceRepository.saveAndFlush(indice);
        indiceSearchRepository.save(indice);

        // Search the indice
        restIndiceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + indice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indice.getId().intValue())))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].autor").value(hasItem(DEFAULT_AUTOR)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
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
