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
import wf.transotas.domain.IndiceOriginal;
import wf.transotas.repository.IndiceOriginalRepository;
import wf.transotas.repository.search.IndiceOriginalSearchRepository;
import wf.transotas.service.criteria.IndiceOriginalCriteria;
import wf.transotas.service.dto.IndiceOriginalDTO;
import wf.transotas.service.mapper.IndiceOriginalMapper;

/**
 * Integration tests for the {@link IndiceOriginalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndiceOriginalResourceIT {

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

    private static final String ENTITY_API_URL = "/api/indice-originals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/indice-originals";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IndiceOriginalRepository indiceOriginalRepository;

    @Autowired
    private IndiceOriginalMapper indiceOriginalMapper;

    @Autowired
    private IndiceOriginalSearchRepository indiceOriginalSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndiceOriginalMockMvc;

    private IndiceOriginal indiceOriginal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndiceOriginal createEntity(EntityManager em) {
        IndiceOriginal indiceOriginal = new IndiceOriginal()
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
            .rating(DEFAULT_RATING);
        return indiceOriginal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndiceOriginal createUpdatedEntity(EntityManager em) {
        IndiceOriginal indiceOriginal = new IndiceOriginal()
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
            .rating(UPDATED_RATING);
        return indiceOriginal;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        indiceOriginalSearchRepository.deleteAll();
        assertThat(indiceOriginalSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        indiceOriginal = createEntity(em);
    }

    @Test
    @Transactional
    void createIndiceOriginal() throws Exception {
        int databaseSizeBeforeCreate = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        // Create the IndiceOriginal
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(indiceOriginal);
        restIndiceOriginalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        IndiceOriginal testIndiceOriginal = indiceOriginalList.get(indiceOriginalList.size() - 1);
        assertThat(testIndiceOriginal.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testIndiceOriginal.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testIndiceOriginal.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testIndiceOriginal.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testIndiceOriginal.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testIndiceOriginal.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testIndiceOriginal.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testIndiceOriginal.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testIndiceOriginal.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testIndiceOriginal.getVistas()).isEqualTo(DEFAULT_VISTAS);
        assertThat(testIndiceOriginal.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    void createIndiceOriginalWithExistingId() throws Exception {
        // Create the IndiceOriginal with an existing ID
        indiceOriginal.setId(1L);
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(indiceOriginal);

        int databaseSizeBeforeCreate = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndiceOriginalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllIndiceOriginals() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList
        restIndiceOriginalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indiceOriginal.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }

    @Test
    @Transactional
    void getIndiceOriginal() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get the indiceOriginal
        restIndiceOriginalMockMvc
            .perform(get(ENTITY_API_URL_ID, indiceOriginal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indiceOriginal.getId().intValue()))
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
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING));
    }

    @Test
    @Transactional
    void getIndiceOriginalsByIdFiltering() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        Long id = indiceOriginal.getId();

        defaultIndiceOriginalShouldBeFound("id.equals=" + id);
        defaultIndiceOriginalShouldNotBeFound("id.notEquals=" + id);

        defaultIndiceOriginalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIndiceOriginalShouldNotBeFound("id.greaterThan=" + id);

        defaultIndiceOriginalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIndiceOriginalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByImgIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where img equals to DEFAULT_IMG
        defaultIndiceOriginalShouldBeFound("img.equals=" + DEFAULT_IMG);

        // Get all the indiceOriginalList where img equals to UPDATED_IMG
        defaultIndiceOriginalShouldNotBeFound("img.equals=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByImgIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where img in DEFAULT_IMG or UPDATED_IMG
        defaultIndiceOriginalShouldBeFound("img.in=" + DEFAULT_IMG + "," + UPDATED_IMG);

        // Get all the indiceOriginalList where img equals to UPDATED_IMG
        defaultIndiceOriginalShouldNotBeFound("img.in=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByImgIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where img is not null
        defaultIndiceOriginalShouldBeFound("img.specified=true");

        // Get all the indiceOriginalList where img is null
        defaultIndiceOriginalShouldNotBeFound("img.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByImgContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where img contains DEFAULT_IMG
        defaultIndiceOriginalShouldBeFound("img.contains=" + DEFAULT_IMG);

        // Get all the indiceOriginalList where img contains UPDATED_IMG
        defaultIndiceOriginalShouldNotBeFound("img.contains=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByImgNotContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where img does not contain DEFAULT_IMG
        defaultIndiceOriginalShouldNotBeFound("img.doesNotContain=" + DEFAULT_IMG);

        // Get all the indiceOriginalList where img does not contain UPDATED_IMG
        defaultIndiceOriginalShouldBeFound("img.doesNotContain=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where titulo equals to DEFAULT_TITULO
        defaultIndiceOriginalShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the indiceOriginalList where titulo equals to UPDATED_TITULO
        defaultIndiceOriginalShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultIndiceOriginalShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the indiceOriginalList where titulo equals to UPDATED_TITULO
        defaultIndiceOriginalShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where titulo is not null
        defaultIndiceOriginalShouldBeFound("titulo.specified=true");

        // Get all the indiceOriginalList where titulo is null
        defaultIndiceOriginalShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByTituloContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where titulo contains DEFAULT_TITULO
        defaultIndiceOriginalShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the indiceOriginalList where titulo contains UPDATED_TITULO
        defaultIndiceOriginalShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where titulo does not contain DEFAULT_TITULO
        defaultIndiceOriginalShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the indiceOriginalList where titulo does not contain UPDATED_TITULO
        defaultIndiceOriginalShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where url equals to DEFAULT_URL
        defaultIndiceOriginalShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the indiceOriginalList where url equals to UPDATED_URL
        defaultIndiceOriginalShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where url in DEFAULT_URL or UPDATED_URL
        defaultIndiceOriginalShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the indiceOriginalList where url equals to UPDATED_URL
        defaultIndiceOriginalShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where url is not null
        defaultIndiceOriginalShouldBeFound("url.specified=true");

        // Get all the indiceOriginalList where url is null
        defaultIndiceOriginalShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByUrlContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where url contains DEFAULT_URL
        defaultIndiceOriginalShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the indiceOriginalList where url contains UPDATED_URL
        defaultIndiceOriginalShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where url does not contain DEFAULT_URL
        defaultIndiceOriginalShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the indiceOriginalList where url does not contain UPDATED_URL
        defaultIndiceOriginalShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByAutorIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where autor equals to DEFAULT_AUTOR
        defaultIndiceOriginalShouldBeFound("autor.equals=" + DEFAULT_AUTOR);

        // Get all the indiceOriginalList where autor equals to UPDATED_AUTOR
        defaultIndiceOriginalShouldNotBeFound("autor.equals=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByAutorIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where autor in DEFAULT_AUTOR or UPDATED_AUTOR
        defaultIndiceOriginalShouldBeFound("autor.in=" + DEFAULT_AUTOR + "," + UPDATED_AUTOR);

        // Get all the indiceOriginalList where autor equals to UPDATED_AUTOR
        defaultIndiceOriginalShouldNotBeFound("autor.in=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByAutorIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where autor is not null
        defaultIndiceOriginalShouldBeFound("autor.specified=true");

        // Get all the indiceOriginalList where autor is null
        defaultIndiceOriginalShouldNotBeFound("autor.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByAutorContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where autor contains DEFAULT_AUTOR
        defaultIndiceOriginalShouldBeFound("autor.contains=" + DEFAULT_AUTOR);

        // Get all the indiceOriginalList where autor contains UPDATED_AUTOR
        defaultIndiceOriginalShouldNotBeFound("autor.contains=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByAutorNotContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where autor does not contain DEFAULT_AUTOR
        defaultIndiceOriginalShouldNotBeFound("autor.doesNotContain=" + DEFAULT_AUTOR);

        // Get all the indiceOriginalList where autor does not contain UPDATED_AUTOR
        defaultIndiceOriginalShouldBeFound("autor.doesNotContain=" + UPDATED_AUTOR);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where fecha equals to DEFAULT_FECHA
        defaultIndiceOriginalShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the indiceOriginalList where fecha equals to UPDATED_FECHA
        defaultIndiceOriginalShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultIndiceOriginalShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the indiceOriginalList where fecha equals to UPDATED_FECHA
        defaultIndiceOriginalShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where fecha is not null
        defaultIndiceOriginalShouldBeFound("fecha.specified=true");

        // Get all the indiceOriginalList where fecha is null
        defaultIndiceOriginalShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByCiudadIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where ciudad equals to DEFAULT_CIUDAD
        defaultIndiceOriginalShouldBeFound("ciudad.equals=" + DEFAULT_CIUDAD);

        // Get all the indiceOriginalList where ciudad equals to UPDATED_CIUDAD
        defaultIndiceOriginalShouldNotBeFound("ciudad.equals=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByCiudadIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where ciudad in DEFAULT_CIUDAD or UPDATED_CIUDAD
        defaultIndiceOriginalShouldBeFound("ciudad.in=" + DEFAULT_CIUDAD + "," + UPDATED_CIUDAD);

        // Get all the indiceOriginalList where ciudad equals to UPDATED_CIUDAD
        defaultIndiceOriginalShouldNotBeFound("ciudad.in=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByCiudadIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where ciudad is not null
        defaultIndiceOriginalShouldBeFound("ciudad.specified=true");

        // Get all the indiceOriginalList where ciudad is null
        defaultIndiceOriginalShouldNotBeFound("ciudad.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByCiudadContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where ciudad contains DEFAULT_CIUDAD
        defaultIndiceOriginalShouldBeFound("ciudad.contains=" + DEFAULT_CIUDAD);

        // Get all the indiceOriginalList where ciudad contains UPDATED_CIUDAD
        defaultIndiceOriginalShouldNotBeFound("ciudad.contains=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByCiudadNotContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where ciudad does not contain DEFAULT_CIUDAD
        defaultIndiceOriginalShouldNotBeFound("ciudad.doesNotContain=" + DEFAULT_CIUDAD);

        // Get all the indiceOriginalList where ciudad does not contain UPDATED_CIUDAD
        defaultIndiceOriginalShouldBeFound("ciudad.doesNotContain=" + UPDATED_CIUDAD);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where estado equals to DEFAULT_ESTADO
        defaultIndiceOriginalShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the indiceOriginalList where estado equals to UPDATED_ESTADO
        defaultIndiceOriginalShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultIndiceOriginalShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the indiceOriginalList where estado equals to UPDATED_ESTADO
        defaultIndiceOriginalShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where estado is not null
        defaultIndiceOriginalShouldBeFound("estado.specified=true");

        // Get all the indiceOriginalList where estado is null
        defaultIndiceOriginalShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByEstadoContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where estado contains DEFAULT_ESTADO
        defaultIndiceOriginalShouldBeFound("estado.contains=" + DEFAULT_ESTADO);

        // Get all the indiceOriginalList where estado contains UPDATED_ESTADO
        defaultIndiceOriginalShouldNotBeFound("estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where estado does not contain DEFAULT_ESTADO
        defaultIndiceOriginalShouldNotBeFound("estado.doesNotContain=" + DEFAULT_ESTADO);

        // Get all the indiceOriginalList where estado does not contain UPDATED_ESTADO
        defaultIndiceOriginalShouldBeFound("estado.doesNotContain=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByPaisIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where pais equals to DEFAULT_PAIS
        defaultIndiceOriginalShouldBeFound("pais.equals=" + DEFAULT_PAIS);

        // Get all the indiceOriginalList where pais equals to UPDATED_PAIS
        defaultIndiceOriginalShouldNotBeFound("pais.equals=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByPaisIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where pais in DEFAULT_PAIS or UPDATED_PAIS
        defaultIndiceOriginalShouldBeFound("pais.in=" + DEFAULT_PAIS + "," + UPDATED_PAIS);

        // Get all the indiceOriginalList where pais equals to UPDATED_PAIS
        defaultIndiceOriginalShouldNotBeFound("pais.in=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByPaisIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where pais is not null
        defaultIndiceOriginalShouldBeFound("pais.specified=true");

        // Get all the indiceOriginalList where pais is null
        defaultIndiceOriginalShouldNotBeFound("pais.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByPaisContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where pais contains DEFAULT_PAIS
        defaultIndiceOriginalShouldBeFound("pais.contains=" + DEFAULT_PAIS);

        // Get all the indiceOriginalList where pais contains UPDATED_PAIS
        defaultIndiceOriginalShouldNotBeFound("pais.contains=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByPaisNotContainsSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where pais does not contain DEFAULT_PAIS
        defaultIndiceOriginalShouldNotBeFound("pais.doesNotContain=" + DEFAULT_PAIS);

        // Get all the indiceOriginalList where pais does not contain UPDATED_PAIS
        defaultIndiceOriginalShouldBeFound("pais.doesNotContain=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByComentariosIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where comentarios equals to DEFAULT_COMENTARIOS
        defaultIndiceOriginalShouldBeFound("comentarios.equals=" + DEFAULT_COMENTARIOS);

        // Get all the indiceOriginalList where comentarios equals to UPDATED_COMENTARIOS
        defaultIndiceOriginalShouldNotBeFound("comentarios.equals=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByComentariosIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where comentarios in DEFAULT_COMENTARIOS or UPDATED_COMENTARIOS
        defaultIndiceOriginalShouldBeFound("comentarios.in=" + DEFAULT_COMENTARIOS + "," + UPDATED_COMENTARIOS);

        // Get all the indiceOriginalList where comentarios equals to UPDATED_COMENTARIOS
        defaultIndiceOriginalShouldNotBeFound("comentarios.in=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByComentariosIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where comentarios is not null
        defaultIndiceOriginalShouldBeFound("comentarios.specified=true");

        // Get all the indiceOriginalList where comentarios is null
        defaultIndiceOriginalShouldNotBeFound("comentarios.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByComentariosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where comentarios is greater than or equal to DEFAULT_COMENTARIOS
        defaultIndiceOriginalShouldBeFound("comentarios.greaterThanOrEqual=" + DEFAULT_COMENTARIOS);

        // Get all the indiceOriginalList where comentarios is greater than or equal to UPDATED_COMENTARIOS
        defaultIndiceOriginalShouldNotBeFound("comentarios.greaterThanOrEqual=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByComentariosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where comentarios is less than or equal to DEFAULT_COMENTARIOS
        defaultIndiceOriginalShouldBeFound("comentarios.lessThanOrEqual=" + DEFAULT_COMENTARIOS);

        // Get all the indiceOriginalList where comentarios is less than or equal to SMALLER_COMENTARIOS
        defaultIndiceOriginalShouldNotBeFound("comentarios.lessThanOrEqual=" + SMALLER_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByComentariosIsLessThanSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where comentarios is less than DEFAULT_COMENTARIOS
        defaultIndiceOriginalShouldNotBeFound("comentarios.lessThan=" + DEFAULT_COMENTARIOS);

        // Get all the indiceOriginalList where comentarios is less than UPDATED_COMENTARIOS
        defaultIndiceOriginalShouldBeFound("comentarios.lessThan=" + UPDATED_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByComentariosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where comentarios is greater than DEFAULT_COMENTARIOS
        defaultIndiceOriginalShouldNotBeFound("comentarios.greaterThan=" + DEFAULT_COMENTARIOS);

        // Get all the indiceOriginalList where comentarios is greater than SMALLER_COMENTARIOS
        defaultIndiceOriginalShouldBeFound("comentarios.greaterThan=" + SMALLER_COMENTARIOS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByVistasIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where vistas equals to DEFAULT_VISTAS
        defaultIndiceOriginalShouldBeFound("vistas.equals=" + DEFAULT_VISTAS);

        // Get all the indiceOriginalList where vistas equals to UPDATED_VISTAS
        defaultIndiceOriginalShouldNotBeFound("vistas.equals=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByVistasIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where vistas in DEFAULT_VISTAS or UPDATED_VISTAS
        defaultIndiceOriginalShouldBeFound("vistas.in=" + DEFAULT_VISTAS + "," + UPDATED_VISTAS);

        // Get all the indiceOriginalList where vistas equals to UPDATED_VISTAS
        defaultIndiceOriginalShouldNotBeFound("vistas.in=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByVistasIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where vistas is not null
        defaultIndiceOriginalShouldBeFound("vistas.specified=true");

        // Get all the indiceOriginalList where vistas is null
        defaultIndiceOriginalShouldNotBeFound("vistas.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByVistasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where vistas is greater than or equal to DEFAULT_VISTAS
        defaultIndiceOriginalShouldBeFound("vistas.greaterThanOrEqual=" + DEFAULT_VISTAS);

        // Get all the indiceOriginalList where vistas is greater than or equal to UPDATED_VISTAS
        defaultIndiceOriginalShouldNotBeFound("vistas.greaterThanOrEqual=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByVistasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where vistas is less than or equal to DEFAULT_VISTAS
        defaultIndiceOriginalShouldBeFound("vistas.lessThanOrEqual=" + DEFAULT_VISTAS);

        // Get all the indiceOriginalList where vistas is less than or equal to SMALLER_VISTAS
        defaultIndiceOriginalShouldNotBeFound("vistas.lessThanOrEqual=" + SMALLER_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByVistasIsLessThanSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where vistas is less than DEFAULT_VISTAS
        defaultIndiceOriginalShouldNotBeFound("vistas.lessThan=" + DEFAULT_VISTAS);

        // Get all the indiceOriginalList where vistas is less than UPDATED_VISTAS
        defaultIndiceOriginalShouldBeFound("vistas.lessThan=" + UPDATED_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByVistasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where vistas is greater than DEFAULT_VISTAS
        defaultIndiceOriginalShouldNotBeFound("vistas.greaterThan=" + DEFAULT_VISTAS);

        // Get all the indiceOriginalList where vistas is greater than SMALLER_VISTAS
        defaultIndiceOriginalShouldBeFound("vistas.greaterThan=" + SMALLER_VISTAS);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where rating equals to DEFAULT_RATING
        defaultIndiceOriginalShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the indiceOriginalList where rating equals to UPDATED_RATING
        defaultIndiceOriginalShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultIndiceOriginalShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the indiceOriginalList where rating equals to UPDATED_RATING
        defaultIndiceOriginalShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where rating is not null
        defaultIndiceOriginalShouldBeFound("rating.specified=true");

        // Get all the indiceOriginalList where rating is null
        defaultIndiceOriginalShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where rating is greater than or equal to DEFAULT_RATING
        defaultIndiceOriginalShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the indiceOriginalList where rating is greater than or equal to UPDATED_RATING
        defaultIndiceOriginalShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where rating is less than or equal to DEFAULT_RATING
        defaultIndiceOriginalShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the indiceOriginalList where rating is less than or equal to SMALLER_RATING
        defaultIndiceOriginalShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where rating is less than DEFAULT_RATING
        defaultIndiceOriginalShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the indiceOriginalList where rating is less than UPDATED_RATING
        defaultIndiceOriginalShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllIndiceOriginalsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        // Get all the indiceOriginalList where rating is greater than DEFAULT_RATING
        defaultIndiceOriginalShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the indiceOriginalList where rating is greater than SMALLER_RATING
        defaultIndiceOriginalShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndiceOriginalShouldBeFound(String filter) throws Exception {
        restIndiceOriginalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indiceOriginal.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));

        // Check, that the count call also returns 1
        restIndiceOriginalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndiceOriginalShouldNotBeFound(String filter) throws Exception {
        restIndiceOriginalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndiceOriginalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndiceOriginal() throws Exception {
        // Get the indiceOriginal
        restIndiceOriginalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndiceOriginal() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();
        indiceOriginalSearchRepository.save(indiceOriginal);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());

        // Update the indiceOriginal
        IndiceOriginal updatedIndiceOriginal = indiceOriginalRepository.findById(indiceOriginal.getId()).get();
        // Disconnect from session so that the updates on updatedIndiceOriginal are not directly saved in db
        em.detach(updatedIndiceOriginal);
        updatedIndiceOriginal
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
            .rating(UPDATED_RATING);
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(updatedIndiceOriginal);

        restIndiceOriginalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indiceOriginalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isOk());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        IndiceOriginal testIndiceOriginal = indiceOriginalList.get(indiceOriginalList.size() - 1);
        assertThat(testIndiceOriginal.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testIndiceOriginal.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testIndiceOriginal.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testIndiceOriginal.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testIndiceOriginal.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testIndiceOriginal.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testIndiceOriginal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testIndiceOriginal.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testIndiceOriginal.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testIndiceOriginal.getVistas()).isEqualTo(UPDATED_VISTAS);
        assertThat(testIndiceOriginal.getRating()).isEqualTo(UPDATED_RATING);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<IndiceOriginal> indiceOriginalSearchList = IterableUtils.toList(indiceOriginalSearchRepository.findAll());
                IndiceOriginal testIndiceOriginalSearch = indiceOriginalSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testIndiceOriginalSearch.getImg()).isEqualTo(UPDATED_IMG);
                assertThat(testIndiceOriginalSearch.getTitulo()).isEqualTo(UPDATED_TITULO);
                assertThat(testIndiceOriginalSearch.getUrl()).isEqualTo(UPDATED_URL);
                assertThat(testIndiceOriginalSearch.getAutor()).isEqualTo(UPDATED_AUTOR);
                assertThat(testIndiceOriginalSearch.getFecha()).isEqualTo(UPDATED_FECHA);
                assertThat(testIndiceOriginalSearch.getCiudad()).isEqualTo(UPDATED_CIUDAD);
                assertThat(testIndiceOriginalSearch.getEstado()).isEqualTo(UPDATED_ESTADO);
                assertThat(testIndiceOriginalSearch.getPais()).isEqualTo(UPDATED_PAIS);
                assertThat(testIndiceOriginalSearch.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
                assertThat(testIndiceOriginalSearch.getVistas()).isEqualTo(UPDATED_VISTAS);
                assertThat(testIndiceOriginalSearch.getRating()).isEqualTo(UPDATED_RATING);
            });
    }

    @Test
    @Transactional
    void putNonExistingIndiceOriginal() throws Exception {
        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        indiceOriginal.setId(count.incrementAndGet());

        // Create the IndiceOriginal
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(indiceOriginal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndiceOriginalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indiceOriginalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndiceOriginal() throws Exception {
        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        indiceOriginal.setId(count.incrementAndGet());

        // Create the IndiceOriginal
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(indiceOriginal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndiceOriginalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndiceOriginal() throws Exception {
        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        indiceOriginal.setId(count.incrementAndGet());

        // Create the IndiceOriginal
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(indiceOriginal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndiceOriginalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateIndiceOriginalWithPatch() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();

        // Update the indiceOriginal using partial update
        IndiceOriginal partialUpdatedIndiceOriginal = new IndiceOriginal();
        partialUpdatedIndiceOriginal.setId(indiceOriginal.getId());

        partialUpdatedIndiceOriginal
            .titulo(UPDATED_TITULO)
            .fecha(UPDATED_FECHA)
            .estado(UPDATED_ESTADO)
            .comentarios(UPDATED_COMENTARIOS)
            .rating(UPDATED_RATING);

        restIndiceOriginalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndiceOriginal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndiceOriginal))
            )
            .andExpect(status().isOk());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        IndiceOriginal testIndiceOriginal = indiceOriginalList.get(indiceOriginalList.size() - 1);
        assertThat(testIndiceOriginal.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testIndiceOriginal.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testIndiceOriginal.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testIndiceOriginal.getAutor()).isEqualTo(DEFAULT_AUTOR);
        assertThat(testIndiceOriginal.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testIndiceOriginal.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testIndiceOriginal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testIndiceOriginal.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testIndiceOriginal.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testIndiceOriginal.getVistas()).isEqualTo(DEFAULT_VISTAS);
        assertThat(testIndiceOriginal.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    void fullUpdateIndiceOriginalWithPatch() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);

        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();

        // Update the indiceOriginal using partial update
        IndiceOriginal partialUpdatedIndiceOriginal = new IndiceOriginal();
        partialUpdatedIndiceOriginal.setId(indiceOriginal.getId());

        partialUpdatedIndiceOriginal
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
            .rating(UPDATED_RATING);

        restIndiceOriginalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndiceOriginal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndiceOriginal))
            )
            .andExpect(status().isOk());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        IndiceOriginal testIndiceOriginal = indiceOriginalList.get(indiceOriginalList.size() - 1);
        assertThat(testIndiceOriginal.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testIndiceOriginal.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testIndiceOriginal.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testIndiceOriginal.getAutor()).isEqualTo(UPDATED_AUTOR);
        assertThat(testIndiceOriginal.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testIndiceOriginal.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testIndiceOriginal.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testIndiceOriginal.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testIndiceOriginal.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testIndiceOriginal.getVistas()).isEqualTo(UPDATED_VISTAS);
        assertThat(testIndiceOriginal.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    void patchNonExistingIndiceOriginal() throws Exception {
        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        indiceOriginal.setId(count.incrementAndGet());

        // Create the IndiceOriginal
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(indiceOriginal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndiceOriginalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indiceOriginalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndiceOriginal() throws Exception {
        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        indiceOriginal.setId(count.incrementAndGet());

        // Create the IndiceOriginal
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(indiceOriginal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndiceOriginalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndiceOriginal() throws Exception {
        int databaseSizeBeforeUpdate = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        indiceOriginal.setId(count.incrementAndGet());

        // Create the IndiceOriginal
        IndiceOriginalDTO indiceOriginalDTO = indiceOriginalMapper.toDto(indiceOriginal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndiceOriginalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(indiceOriginalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndiceOriginal in the database
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteIndiceOriginal() throws Exception {
        // Initialize the database
        indiceOriginalRepository.saveAndFlush(indiceOriginal);
        indiceOriginalRepository.save(indiceOriginal);
        indiceOriginalSearchRepository.save(indiceOriginal);

        int databaseSizeBeforeDelete = indiceOriginalRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the indiceOriginal
        restIndiceOriginalMockMvc
            .perform(delete(ENTITY_API_URL_ID, indiceOriginal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IndiceOriginal> indiceOriginalList = indiceOriginalRepository.findAll();
        assertThat(indiceOriginalList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(indiceOriginalSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchIndiceOriginal() throws Exception {
        // Initialize the database
        indiceOriginal = indiceOriginalRepository.saveAndFlush(indiceOriginal);
        indiceOriginalSearchRepository.save(indiceOriginal);

        // Search the indiceOriginal
        restIndiceOriginalMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + indiceOriginal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indiceOriginal.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }
}
