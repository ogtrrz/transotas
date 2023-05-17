package wf.transotas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import wf.transotas.domain.Categorys;
import wf.transotas.domain.Reportes;
import wf.transotas.repository.CategorysRepository;
import wf.transotas.repository.search.CategorysSearchRepository;
import wf.transotas.service.CategorysService;
import wf.transotas.service.criteria.CategorysCriteria;
import wf.transotas.service.dto.CategorysDTO;
import wf.transotas.service.mapper.CategorysMapper;

/**
 * Integration tests for the {@link CategorysResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CategorysResourceIT {

    private static final String DEFAULT_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIA = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/categorys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/categorys";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategorysRepository categorysRepository;

    @Mock
    private CategorysRepository categorysRepositoryMock;

    @Autowired
    private CategorysMapper categorysMapper;

    @Mock
    private CategorysService categorysServiceMock;

    @Autowired
    private CategorysSearchRepository categorysSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorysMockMvc;

    private Categorys categorys;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorys createEntity(EntityManager em) {
        Categorys categorys = new Categorys()
            .categoria(DEFAULT_CATEGORIA)
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
        return categorys;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorys createUpdatedEntity(EntityManager em) {
        Categorys categorys = new Categorys()
            .categoria(UPDATED_CATEGORIA)
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
        return categorys;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        categorysSearchRepository.deleteAll();
        assertThat(categorysSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        categorys = createEntity(em);
    }

    @Test
    @Transactional
    void createCategorys() throws Exception {
        int databaseSizeBeforeCreate = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        // Create the Categorys
        CategorysDTO categorysDTO = categorysMapper.toDto(categorys);
        restCategorysMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorysDTO)))
            .andExpect(status().isCreated());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Categorys testCategorys = categorysList.get(categorysList.size() - 1);
        assertThat(testCategorys.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);
        assertThat(testCategorys.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testCategorys.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testCategorys.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testCategorys.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testCategorys.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testCategorys.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testCategorys.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testCategorys.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testCategorys.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testCategorys.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void createCategorysWithExistingId() throws Exception {
        // Create the Categorys with an existing ID
        categorys.setId(1L);
        CategorysDTO categorysDTO = categorysMapper.toDto(categorys);

        int databaseSizeBeforeCreate = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorysMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorysDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCategorys() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList
        restCategorysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorys.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)))
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
    void getAllCategorysWithEagerRelationshipsIsEnabled() throws Exception {
        when(categorysServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCategorysMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(categorysServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCategorysWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(categorysServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCategorysMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(categorysRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCategorys() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get the categorys
        restCategorysMockMvc
            .perform(get(ENTITY_API_URL_ID, categorys.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorys.getId().intValue()))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA))
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
    void getCategorysByIdFiltering() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        Long id = categorys.getId();

        defaultCategorysShouldBeFound("id.equals=" + id);
        defaultCategorysShouldNotBeFound("id.notEquals=" + id);

        defaultCategorysShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategorysShouldNotBeFound("id.greaterThan=" + id);

        defaultCategorysShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategorysShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategorysByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where categoria equals to DEFAULT_CATEGORIA
        defaultCategorysShouldBeFound("categoria.equals=" + DEFAULT_CATEGORIA);

        // Get all the categorysList where categoria equals to UPDATED_CATEGORIA
        defaultCategorysShouldNotBeFound("categoria.equals=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategorysByCategoriaIsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where categoria in DEFAULT_CATEGORIA or UPDATED_CATEGORIA
        defaultCategorysShouldBeFound("categoria.in=" + DEFAULT_CATEGORIA + "," + UPDATED_CATEGORIA);

        // Get all the categorysList where categoria equals to UPDATED_CATEGORIA
        defaultCategorysShouldNotBeFound("categoria.in=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategorysByCategoriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where categoria is not null
        defaultCategorysShouldBeFound("categoria.specified=true");

        // Get all the categorysList where categoria is null
        defaultCategorysShouldNotBeFound("categoria.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByCategoriaContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where categoria contains DEFAULT_CATEGORIA
        defaultCategorysShouldBeFound("categoria.contains=" + DEFAULT_CATEGORIA);

        // Get all the categorysList where categoria contains UPDATED_CATEGORIA
        defaultCategorysShouldNotBeFound("categoria.contains=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategorysByCategoriaNotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where categoria does not contain DEFAULT_CATEGORIA
        defaultCategorysShouldNotBeFound("categoria.doesNotContain=" + DEFAULT_CATEGORIA);

        // Get all the categorysList where categoria does not contain UPDATED_CATEGORIA
        defaultCategorysShouldBeFound("categoria.doesNotContain=" + UPDATED_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra1IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra1 equals to DEFAULT_EXTRA_1
        defaultCategorysShouldBeFound("extra1.equals=" + DEFAULT_EXTRA_1);

        // Get all the categorysList where extra1 equals to UPDATED_EXTRA_1
        defaultCategorysShouldNotBeFound("extra1.equals=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra1IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra1 in DEFAULT_EXTRA_1 or UPDATED_EXTRA_1
        defaultCategorysShouldBeFound("extra1.in=" + DEFAULT_EXTRA_1 + "," + UPDATED_EXTRA_1);

        // Get all the categorysList where extra1 equals to UPDATED_EXTRA_1
        defaultCategorysShouldNotBeFound("extra1.in=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra1IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra1 is not null
        defaultCategorysShouldBeFound("extra1.specified=true");

        // Get all the categorysList where extra1 is null
        defaultCategorysShouldNotBeFound("extra1.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra1ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra1 contains DEFAULT_EXTRA_1
        defaultCategorysShouldBeFound("extra1.contains=" + DEFAULT_EXTRA_1);

        // Get all the categorysList where extra1 contains UPDATED_EXTRA_1
        defaultCategorysShouldNotBeFound("extra1.contains=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra1NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra1 does not contain DEFAULT_EXTRA_1
        defaultCategorysShouldNotBeFound("extra1.doesNotContain=" + DEFAULT_EXTRA_1);

        // Get all the categorysList where extra1 does not contain UPDATED_EXTRA_1
        defaultCategorysShouldBeFound("extra1.doesNotContain=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra2IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra2 equals to DEFAULT_EXTRA_2
        defaultCategorysShouldBeFound("extra2.equals=" + DEFAULT_EXTRA_2);

        // Get all the categorysList where extra2 equals to UPDATED_EXTRA_2
        defaultCategorysShouldNotBeFound("extra2.equals=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra2IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra2 in DEFAULT_EXTRA_2 or UPDATED_EXTRA_2
        defaultCategorysShouldBeFound("extra2.in=" + DEFAULT_EXTRA_2 + "," + UPDATED_EXTRA_2);

        // Get all the categorysList where extra2 equals to UPDATED_EXTRA_2
        defaultCategorysShouldNotBeFound("extra2.in=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra2IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra2 is not null
        defaultCategorysShouldBeFound("extra2.specified=true");

        // Get all the categorysList where extra2 is null
        defaultCategorysShouldNotBeFound("extra2.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra2ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra2 contains DEFAULT_EXTRA_2
        defaultCategorysShouldBeFound("extra2.contains=" + DEFAULT_EXTRA_2);

        // Get all the categorysList where extra2 contains UPDATED_EXTRA_2
        defaultCategorysShouldNotBeFound("extra2.contains=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra2NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra2 does not contain DEFAULT_EXTRA_2
        defaultCategorysShouldNotBeFound("extra2.doesNotContain=" + DEFAULT_EXTRA_2);

        // Get all the categorysList where extra2 does not contain UPDATED_EXTRA_2
        defaultCategorysShouldBeFound("extra2.doesNotContain=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra3IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra3 equals to DEFAULT_EXTRA_3
        defaultCategorysShouldBeFound("extra3.equals=" + DEFAULT_EXTRA_3);

        // Get all the categorysList where extra3 equals to UPDATED_EXTRA_3
        defaultCategorysShouldNotBeFound("extra3.equals=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra3IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra3 in DEFAULT_EXTRA_3 or UPDATED_EXTRA_3
        defaultCategorysShouldBeFound("extra3.in=" + DEFAULT_EXTRA_3 + "," + UPDATED_EXTRA_3);

        // Get all the categorysList where extra3 equals to UPDATED_EXTRA_3
        defaultCategorysShouldNotBeFound("extra3.in=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra3IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra3 is not null
        defaultCategorysShouldBeFound("extra3.specified=true");

        // Get all the categorysList where extra3 is null
        defaultCategorysShouldNotBeFound("extra3.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra3ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra3 contains DEFAULT_EXTRA_3
        defaultCategorysShouldBeFound("extra3.contains=" + DEFAULT_EXTRA_3);

        // Get all the categorysList where extra3 contains UPDATED_EXTRA_3
        defaultCategorysShouldNotBeFound("extra3.contains=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra3NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra3 does not contain DEFAULT_EXTRA_3
        defaultCategorysShouldNotBeFound("extra3.doesNotContain=" + DEFAULT_EXTRA_3);

        // Get all the categorysList where extra3 does not contain UPDATED_EXTRA_3
        defaultCategorysShouldBeFound("extra3.doesNotContain=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra4IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra4 equals to DEFAULT_EXTRA_4
        defaultCategorysShouldBeFound("extra4.equals=" + DEFAULT_EXTRA_4);

        // Get all the categorysList where extra4 equals to UPDATED_EXTRA_4
        defaultCategorysShouldNotBeFound("extra4.equals=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra4IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra4 in DEFAULT_EXTRA_4 or UPDATED_EXTRA_4
        defaultCategorysShouldBeFound("extra4.in=" + DEFAULT_EXTRA_4 + "," + UPDATED_EXTRA_4);

        // Get all the categorysList where extra4 equals to UPDATED_EXTRA_4
        defaultCategorysShouldNotBeFound("extra4.in=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra4IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra4 is not null
        defaultCategorysShouldBeFound("extra4.specified=true");

        // Get all the categorysList where extra4 is null
        defaultCategorysShouldNotBeFound("extra4.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra4ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra4 contains DEFAULT_EXTRA_4
        defaultCategorysShouldBeFound("extra4.contains=" + DEFAULT_EXTRA_4);

        // Get all the categorysList where extra4 contains UPDATED_EXTRA_4
        defaultCategorysShouldNotBeFound("extra4.contains=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra4NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra4 does not contain DEFAULT_EXTRA_4
        defaultCategorysShouldNotBeFound("extra4.doesNotContain=" + DEFAULT_EXTRA_4);

        // Get all the categorysList where extra4 does not contain UPDATED_EXTRA_4
        defaultCategorysShouldBeFound("extra4.doesNotContain=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra5IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra5 equals to DEFAULT_EXTRA_5
        defaultCategorysShouldBeFound("extra5.equals=" + DEFAULT_EXTRA_5);

        // Get all the categorysList where extra5 equals to UPDATED_EXTRA_5
        defaultCategorysShouldNotBeFound("extra5.equals=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra5IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra5 in DEFAULT_EXTRA_5 or UPDATED_EXTRA_5
        defaultCategorysShouldBeFound("extra5.in=" + DEFAULT_EXTRA_5 + "," + UPDATED_EXTRA_5);

        // Get all the categorysList where extra5 equals to UPDATED_EXTRA_5
        defaultCategorysShouldNotBeFound("extra5.in=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra5IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra5 is not null
        defaultCategorysShouldBeFound("extra5.specified=true");

        // Get all the categorysList where extra5 is null
        defaultCategorysShouldNotBeFound("extra5.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra5ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra5 contains DEFAULT_EXTRA_5
        defaultCategorysShouldBeFound("extra5.contains=" + DEFAULT_EXTRA_5);

        // Get all the categorysList where extra5 contains UPDATED_EXTRA_5
        defaultCategorysShouldNotBeFound("extra5.contains=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra5NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra5 does not contain DEFAULT_EXTRA_5
        defaultCategorysShouldNotBeFound("extra5.doesNotContain=" + DEFAULT_EXTRA_5);

        // Get all the categorysList where extra5 does not contain UPDATED_EXTRA_5
        defaultCategorysShouldBeFound("extra5.doesNotContain=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra6IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra6 equals to DEFAULT_EXTRA_6
        defaultCategorysShouldBeFound("extra6.equals=" + DEFAULT_EXTRA_6);

        // Get all the categorysList where extra6 equals to UPDATED_EXTRA_6
        defaultCategorysShouldNotBeFound("extra6.equals=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra6IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra6 in DEFAULT_EXTRA_6 or UPDATED_EXTRA_6
        defaultCategorysShouldBeFound("extra6.in=" + DEFAULT_EXTRA_6 + "," + UPDATED_EXTRA_6);

        // Get all the categorysList where extra6 equals to UPDATED_EXTRA_6
        defaultCategorysShouldNotBeFound("extra6.in=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra6IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra6 is not null
        defaultCategorysShouldBeFound("extra6.specified=true");

        // Get all the categorysList where extra6 is null
        defaultCategorysShouldNotBeFound("extra6.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra6ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra6 contains DEFAULT_EXTRA_6
        defaultCategorysShouldBeFound("extra6.contains=" + DEFAULT_EXTRA_6);

        // Get all the categorysList where extra6 contains UPDATED_EXTRA_6
        defaultCategorysShouldNotBeFound("extra6.contains=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra6NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra6 does not contain DEFAULT_EXTRA_6
        defaultCategorysShouldNotBeFound("extra6.doesNotContain=" + DEFAULT_EXTRA_6);

        // Get all the categorysList where extra6 does not contain UPDATED_EXTRA_6
        defaultCategorysShouldBeFound("extra6.doesNotContain=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra7IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra7 equals to DEFAULT_EXTRA_7
        defaultCategorysShouldBeFound("extra7.equals=" + DEFAULT_EXTRA_7);

        // Get all the categorysList where extra7 equals to UPDATED_EXTRA_7
        defaultCategorysShouldNotBeFound("extra7.equals=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra7IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra7 in DEFAULT_EXTRA_7 or UPDATED_EXTRA_7
        defaultCategorysShouldBeFound("extra7.in=" + DEFAULT_EXTRA_7 + "," + UPDATED_EXTRA_7);

        // Get all the categorysList where extra7 equals to UPDATED_EXTRA_7
        defaultCategorysShouldNotBeFound("extra7.in=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra7IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra7 is not null
        defaultCategorysShouldBeFound("extra7.specified=true");

        // Get all the categorysList where extra7 is null
        defaultCategorysShouldNotBeFound("extra7.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra7ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra7 contains DEFAULT_EXTRA_7
        defaultCategorysShouldBeFound("extra7.contains=" + DEFAULT_EXTRA_7);

        // Get all the categorysList where extra7 contains UPDATED_EXTRA_7
        defaultCategorysShouldNotBeFound("extra7.contains=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra7NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra7 does not contain DEFAULT_EXTRA_7
        defaultCategorysShouldNotBeFound("extra7.doesNotContain=" + DEFAULT_EXTRA_7);

        // Get all the categorysList where extra7 does not contain UPDATED_EXTRA_7
        defaultCategorysShouldBeFound("extra7.doesNotContain=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra8IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra8 equals to DEFAULT_EXTRA_8
        defaultCategorysShouldBeFound("extra8.equals=" + DEFAULT_EXTRA_8);

        // Get all the categorysList where extra8 equals to UPDATED_EXTRA_8
        defaultCategorysShouldNotBeFound("extra8.equals=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra8IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra8 in DEFAULT_EXTRA_8 or UPDATED_EXTRA_8
        defaultCategorysShouldBeFound("extra8.in=" + DEFAULT_EXTRA_8 + "," + UPDATED_EXTRA_8);

        // Get all the categorysList where extra8 equals to UPDATED_EXTRA_8
        defaultCategorysShouldNotBeFound("extra8.in=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra8IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra8 is not null
        defaultCategorysShouldBeFound("extra8.specified=true");

        // Get all the categorysList where extra8 is null
        defaultCategorysShouldNotBeFound("extra8.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra8ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra8 contains DEFAULT_EXTRA_8
        defaultCategorysShouldBeFound("extra8.contains=" + DEFAULT_EXTRA_8);

        // Get all the categorysList where extra8 contains UPDATED_EXTRA_8
        defaultCategorysShouldNotBeFound("extra8.contains=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra8NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra8 does not contain DEFAULT_EXTRA_8
        defaultCategorysShouldNotBeFound("extra8.doesNotContain=" + DEFAULT_EXTRA_8);

        // Get all the categorysList where extra8 does not contain UPDATED_EXTRA_8
        defaultCategorysShouldBeFound("extra8.doesNotContain=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra9IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra9 equals to DEFAULT_EXTRA_9
        defaultCategorysShouldBeFound("extra9.equals=" + DEFAULT_EXTRA_9);

        // Get all the categorysList where extra9 equals to UPDATED_EXTRA_9
        defaultCategorysShouldNotBeFound("extra9.equals=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra9IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra9 in DEFAULT_EXTRA_9 or UPDATED_EXTRA_9
        defaultCategorysShouldBeFound("extra9.in=" + DEFAULT_EXTRA_9 + "," + UPDATED_EXTRA_9);

        // Get all the categorysList where extra9 equals to UPDATED_EXTRA_9
        defaultCategorysShouldNotBeFound("extra9.in=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra9IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra9 is not null
        defaultCategorysShouldBeFound("extra9.specified=true");

        // Get all the categorysList where extra9 is null
        defaultCategorysShouldNotBeFound("extra9.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra9ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra9 contains DEFAULT_EXTRA_9
        defaultCategorysShouldBeFound("extra9.contains=" + DEFAULT_EXTRA_9);

        // Get all the categorysList where extra9 contains UPDATED_EXTRA_9
        defaultCategorysShouldNotBeFound("extra9.contains=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra9NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra9 does not contain DEFAULT_EXTRA_9
        defaultCategorysShouldNotBeFound("extra9.doesNotContain=" + DEFAULT_EXTRA_9);

        // Get all the categorysList where extra9 does not contain UPDATED_EXTRA_9
        defaultCategorysShouldBeFound("extra9.doesNotContain=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra10IsEqualToSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra10 equals to DEFAULT_EXTRA_10
        defaultCategorysShouldBeFound("extra10.equals=" + DEFAULT_EXTRA_10);

        // Get all the categorysList where extra10 equals to UPDATED_EXTRA_10
        defaultCategorysShouldNotBeFound("extra10.equals=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra10IsInShouldWork() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra10 in DEFAULT_EXTRA_10 or UPDATED_EXTRA_10
        defaultCategorysShouldBeFound("extra10.in=" + DEFAULT_EXTRA_10 + "," + UPDATED_EXTRA_10);

        // Get all the categorysList where extra10 equals to UPDATED_EXTRA_10
        defaultCategorysShouldNotBeFound("extra10.in=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra10IsNullOrNotNull() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra10 is not null
        defaultCategorysShouldBeFound("extra10.specified=true");

        // Get all the categorysList where extra10 is null
        defaultCategorysShouldNotBeFound("extra10.specified=false");
    }

    @Test
    @Transactional
    void getAllCategorysByExtra10ContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra10 contains DEFAULT_EXTRA_10
        defaultCategorysShouldBeFound("extra10.contains=" + DEFAULT_EXTRA_10);

        // Get all the categorysList where extra10 contains UPDATED_EXTRA_10
        defaultCategorysShouldNotBeFound("extra10.contains=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllCategorysByExtra10NotContainsSomething() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        // Get all the categorysList where extra10 does not contain DEFAULT_EXTRA_10
        defaultCategorysShouldNotBeFound("extra10.doesNotContain=" + DEFAULT_EXTRA_10);

        // Get all the categorysList where extra10 does not contain UPDATED_EXTRA_10
        defaultCategorysShouldBeFound("extra10.doesNotContain=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllCategorysByReportesIsEqualToSomething() throws Exception {
        Reportes reportes;
        if (TestUtil.findAll(em, Reportes.class).isEmpty()) {
            categorysRepository.saveAndFlush(categorys);
            reportes = ReportesResourceIT.createEntity(em);
        } else {
            reportes = TestUtil.findAll(em, Reportes.class).get(0);
        }
        em.persist(reportes);
        em.flush();
        categorys.addReportes(reportes);
        categorysRepository.saveAndFlush(categorys);
        Long reportesId = reportes.getId();

        // Get all the categorysList where reportes equals to reportesId
        defaultCategorysShouldBeFound("reportesId.equals=" + reportesId);

        // Get all the categorysList where reportes equals to (reportesId + 1)
        defaultCategorysShouldNotBeFound("reportesId.equals=" + (reportesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategorysShouldBeFound(String filter) throws Exception {
        restCategorysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorys.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)))
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
        restCategorysMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategorysShouldNotBeFound(String filter) throws Exception {
        restCategorysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategorysMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategorys() throws Exception {
        // Get the categorys
        restCategorysMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategorys() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();
        categorysSearchRepository.save(categorys);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());

        // Update the categorys
        Categorys updatedCategorys = categorysRepository.findById(categorys.getId()).get();
        // Disconnect from session so that the updates on updatedCategorys are not directly saved in db
        em.detach(updatedCategorys);
        updatedCategorys
            .categoria(UPDATED_CATEGORIA)
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
        CategorysDTO categorysDTO = categorysMapper.toDto(updatedCategorys);

        restCategorysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorysDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorysDTO))
            )
            .andExpect(status().isOk());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        Categorys testCategorys = categorysList.get(categorysList.size() - 1);
        assertThat(testCategorys.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testCategorys.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testCategorys.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testCategorys.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testCategorys.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testCategorys.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCategorys.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testCategorys.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testCategorys.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testCategorys.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testCategorys.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Categorys> categorysSearchList = IterableUtils.toList(categorysSearchRepository.findAll());
                Categorys testCategorysSearch = categorysSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCategorysSearch.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
                assertThat(testCategorysSearch.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
                assertThat(testCategorysSearch.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
                assertThat(testCategorysSearch.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
                assertThat(testCategorysSearch.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
                assertThat(testCategorysSearch.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
                assertThat(testCategorysSearch.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
                assertThat(testCategorysSearch.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
                assertThat(testCategorysSearch.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
                assertThat(testCategorysSearch.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
                assertThat(testCategorysSearch.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
            });
    }

    @Test
    @Transactional
    void putNonExistingCategorys() throws Exception {
        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        categorys.setId(count.incrementAndGet());

        // Create the Categorys
        CategorysDTO categorysDTO = categorysMapper.toDto(categorys);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorysDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategorys() throws Exception {
        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        categorys.setId(count.incrementAndGet());

        // Create the Categorys
        CategorysDTO categorysDTO = categorysMapper.toDto(categorys);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategorys() throws Exception {
        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        categorys.setId(count.incrementAndGet());

        // Create the Categorys
        CategorysDTO categorysDTO = categorysMapper.toDto(categorys);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorysMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorysDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCategorysWithPatch() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();

        // Update the categorys using partial update
        Categorys partialUpdatedCategorys = new Categorys();
        partialUpdatedCategorys.setId(categorys.getId());

        partialUpdatedCategorys.extra2(UPDATED_EXTRA_2).extra5(UPDATED_EXTRA_5).extra6(UPDATED_EXTRA_6).extra9(UPDATED_EXTRA_9);

        restCategorysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorys.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorys))
            )
            .andExpect(status().isOk());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        Categorys testCategorys = categorysList.get(categorysList.size() - 1);
        assertThat(testCategorys.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);
        assertThat(testCategorys.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testCategorys.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testCategorys.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testCategorys.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testCategorys.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCategorys.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testCategorys.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testCategorys.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testCategorys.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testCategorys.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void fullUpdateCategorysWithPatch() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);

        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();

        // Update the categorys using partial update
        Categorys partialUpdatedCategorys = new Categorys();
        partialUpdatedCategorys.setId(categorys.getId());

        partialUpdatedCategorys
            .categoria(UPDATED_CATEGORIA)
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

        restCategorysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorys.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorys))
            )
            .andExpect(status().isOk());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        Categorys testCategorys = categorysList.get(categorysList.size() - 1);
        assertThat(testCategorys.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testCategorys.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testCategorys.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testCategorys.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testCategorys.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testCategorys.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testCategorys.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testCategorys.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testCategorys.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testCategorys.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testCategorys.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void patchNonExistingCategorys() throws Exception {
        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        categorys.setId(count.incrementAndGet());

        // Create the Categorys
        CategorysDTO categorysDTO = categorysMapper.toDto(categorys);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categorysDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategorys() throws Exception {
        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        categorys.setId(count.incrementAndGet());

        // Create the Categorys
        CategorysDTO categorysDTO = categorysMapper.toDto(categorys);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategorys() throws Exception {
        int databaseSizeBeforeUpdate = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        categorys.setId(count.incrementAndGet());

        // Create the Categorys
        CategorysDTO categorysDTO = categorysMapper.toDto(categorys);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorysMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categorysDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categorys in the database
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCategorys() throws Exception {
        // Initialize the database
        categorysRepository.saveAndFlush(categorys);
        categorysRepository.save(categorys);
        categorysSearchRepository.save(categorys);

        int databaseSizeBeforeDelete = categorysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the categorys
        restCategorysMockMvc
            .perform(delete(ENTITY_API_URL_ID, categorys.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categorys> categorysList = categorysRepository.findAll();
        assertThat(categorysList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(categorysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCategorys() throws Exception {
        // Initialize the database
        categorys = categorysRepository.saveAndFlush(categorys);
        categorysSearchRepository.save(categorys);

        // Search the categorys
        restCategorysMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + categorys.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorys.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)))
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
