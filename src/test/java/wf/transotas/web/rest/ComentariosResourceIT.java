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
import wf.transotas.domain.Comentarios;
import wf.transotas.domain.Reportes;
import wf.transotas.repository.ComentariosRepository;
import wf.transotas.repository.search.ComentariosSearchRepository;
import wf.transotas.service.criteria.ComentariosCriteria;
import wf.transotas.service.dto.ComentariosDTO;
import wf.transotas.service.mapper.ComentariosMapper;

/**
 * Integration tests for the {@link ComentariosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComentariosResourceIT {

    private static final String DEFAULT_USUARIO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/comentarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/comentarios";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComentariosRepository comentariosRepository;

    @Autowired
    private ComentariosMapper comentariosMapper;

    @Autowired
    private ComentariosSearchRepository comentariosSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComentariosMockMvc;

    private Comentarios comentarios;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comentarios createEntity(EntityManager em) {
        Comentarios comentarios = new Comentarios()
            .usuario(DEFAULT_USUARIO)
            .comentario(DEFAULT_COMENTARIO)
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
        return comentarios;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comentarios createUpdatedEntity(EntityManager em) {
        Comentarios comentarios = new Comentarios()
            .usuario(UPDATED_USUARIO)
            .comentario(UPDATED_COMENTARIO)
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
        return comentarios;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        comentariosSearchRepository.deleteAll();
        assertThat(comentariosSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        comentarios = createEntity(em);
    }

    @Test
    @Transactional
    void createComentarios() throws Exception {
        int databaseSizeBeforeCreate = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        // Create the Comentarios
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(comentarios);
        restComentariosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentariosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Comentarios testComentarios = comentariosList.get(comentariosList.size() - 1);
        assertThat(testComentarios.getUsuario()).isEqualTo(DEFAULT_USUARIO);
        assertThat(testComentarios.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testComentarios.getExtra1()).isEqualTo(DEFAULT_EXTRA_1);
        assertThat(testComentarios.getExtra2()).isEqualTo(DEFAULT_EXTRA_2);
        assertThat(testComentarios.getExtra3()).isEqualTo(DEFAULT_EXTRA_3);
        assertThat(testComentarios.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testComentarios.getExtra5()).isEqualTo(DEFAULT_EXTRA_5);
        assertThat(testComentarios.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testComentarios.getExtra7()).isEqualTo(DEFAULT_EXTRA_7);
        assertThat(testComentarios.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testComentarios.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testComentarios.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void createComentariosWithExistingId() throws Exception {
        // Create the Comentarios with an existing ID
        comentarios.setId(1L);
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(comentarios);

        int databaseSizeBeforeCreate = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restComentariosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllComentarios() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList
        restComentariosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comentarios.getId().intValue())))
            .andExpect(jsonPath("$.[*].usuario").value(hasItem(DEFAULT_USUARIO)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)))
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
    void getComentarios() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get the comentarios
        restComentariosMockMvc
            .perform(get(ENTITY_API_URL_ID, comentarios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comentarios.getId().intValue()))
            .andExpect(jsonPath("$.usuario").value(DEFAULT_USUARIO))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO))
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
    void getComentariosByIdFiltering() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        Long id = comentarios.getId();

        defaultComentariosShouldBeFound("id.equals=" + id);
        defaultComentariosShouldNotBeFound("id.notEquals=" + id);

        defaultComentariosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComentariosShouldNotBeFound("id.greaterThan=" + id);

        defaultComentariosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComentariosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComentariosByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where usuario equals to DEFAULT_USUARIO
        defaultComentariosShouldBeFound("usuario.equals=" + DEFAULT_USUARIO);

        // Get all the comentariosList where usuario equals to UPDATED_USUARIO
        defaultComentariosShouldNotBeFound("usuario.equals=" + UPDATED_USUARIO);
    }

    @Test
    @Transactional
    void getAllComentariosByUsuarioIsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where usuario in DEFAULT_USUARIO or UPDATED_USUARIO
        defaultComentariosShouldBeFound("usuario.in=" + DEFAULT_USUARIO + "," + UPDATED_USUARIO);

        // Get all the comentariosList where usuario equals to UPDATED_USUARIO
        defaultComentariosShouldNotBeFound("usuario.in=" + UPDATED_USUARIO);
    }

    @Test
    @Transactional
    void getAllComentariosByUsuarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where usuario is not null
        defaultComentariosShouldBeFound("usuario.specified=true");

        // Get all the comentariosList where usuario is null
        defaultComentariosShouldNotBeFound("usuario.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByUsuarioContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where usuario contains DEFAULT_USUARIO
        defaultComentariosShouldBeFound("usuario.contains=" + DEFAULT_USUARIO);

        // Get all the comentariosList where usuario contains UPDATED_USUARIO
        defaultComentariosShouldNotBeFound("usuario.contains=" + UPDATED_USUARIO);
    }

    @Test
    @Transactional
    void getAllComentariosByUsuarioNotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where usuario does not contain DEFAULT_USUARIO
        defaultComentariosShouldNotBeFound("usuario.doesNotContain=" + DEFAULT_USUARIO);

        // Get all the comentariosList where usuario does not contain UPDATED_USUARIO
        defaultComentariosShouldBeFound("usuario.doesNotContain=" + UPDATED_USUARIO);
    }

    @Test
    @Transactional
    void getAllComentariosByComentarioIsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where comentario equals to DEFAULT_COMENTARIO
        defaultComentariosShouldBeFound("comentario.equals=" + DEFAULT_COMENTARIO);

        // Get all the comentariosList where comentario equals to UPDATED_COMENTARIO
        defaultComentariosShouldNotBeFound("comentario.equals=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllComentariosByComentarioIsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where comentario in DEFAULT_COMENTARIO or UPDATED_COMENTARIO
        defaultComentariosShouldBeFound("comentario.in=" + DEFAULT_COMENTARIO + "," + UPDATED_COMENTARIO);

        // Get all the comentariosList where comentario equals to UPDATED_COMENTARIO
        defaultComentariosShouldNotBeFound("comentario.in=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllComentariosByComentarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where comentario is not null
        defaultComentariosShouldBeFound("comentario.specified=true");

        // Get all the comentariosList where comentario is null
        defaultComentariosShouldNotBeFound("comentario.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByComentarioContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where comentario contains DEFAULT_COMENTARIO
        defaultComentariosShouldBeFound("comentario.contains=" + DEFAULT_COMENTARIO);

        // Get all the comentariosList where comentario contains UPDATED_COMENTARIO
        defaultComentariosShouldNotBeFound("comentario.contains=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllComentariosByComentarioNotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where comentario does not contain DEFAULT_COMENTARIO
        defaultComentariosShouldNotBeFound("comentario.doesNotContain=" + DEFAULT_COMENTARIO);

        // Get all the comentariosList where comentario does not contain UPDATED_COMENTARIO
        defaultComentariosShouldBeFound("comentario.doesNotContain=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra1IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra1 equals to DEFAULT_EXTRA_1
        defaultComentariosShouldBeFound("extra1.equals=" + DEFAULT_EXTRA_1);

        // Get all the comentariosList where extra1 equals to UPDATED_EXTRA_1
        defaultComentariosShouldNotBeFound("extra1.equals=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra1IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra1 in DEFAULT_EXTRA_1 or UPDATED_EXTRA_1
        defaultComentariosShouldBeFound("extra1.in=" + DEFAULT_EXTRA_1 + "," + UPDATED_EXTRA_1);

        // Get all the comentariosList where extra1 equals to UPDATED_EXTRA_1
        defaultComentariosShouldNotBeFound("extra1.in=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra1IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra1 is not null
        defaultComentariosShouldBeFound("extra1.specified=true");

        // Get all the comentariosList where extra1 is null
        defaultComentariosShouldNotBeFound("extra1.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra1ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra1 contains DEFAULT_EXTRA_1
        defaultComentariosShouldBeFound("extra1.contains=" + DEFAULT_EXTRA_1);

        // Get all the comentariosList where extra1 contains UPDATED_EXTRA_1
        defaultComentariosShouldNotBeFound("extra1.contains=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra1NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra1 does not contain DEFAULT_EXTRA_1
        defaultComentariosShouldNotBeFound("extra1.doesNotContain=" + DEFAULT_EXTRA_1);

        // Get all the comentariosList where extra1 does not contain UPDATED_EXTRA_1
        defaultComentariosShouldBeFound("extra1.doesNotContain=" + UPDATED_EXTRA_1);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra2IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra2 equals to DEFAULT_EXTRA_2
        defaultComentariosShouldBeFound("extra2.equals=" + DEFAULT_EXTRA_2);

        // Get all the comentariosList where extra2 equals to UPDATED_EXTRA_2
        defaultComentariosShouldNotBeFound("extra2.equals=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra2IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra2 in DEFAULT_EXTRA_2 or UPDATED_EXTRA_2
        defaultComentariosShouldBeFound("extra2.in=" + DEFAULT_EXTRA_2 + "," + UPDATED_EXTRA_2);

        // Get all the comentariosList where extra2 equals to UPDATED_EXTRA_2
        defaultComentariosShouldNotBeFound("extra2.in=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra2IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra2 is not null
        defaultComentariosShouldBeFound("extra2.specified=true");

        // Get all the comentariosList where extra2 is null
        defaultComentariosShouldNotBeFound("extra2.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra2ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra2 contains DEFAULT_EXTRA_2
        defaultComentariosShouldBeFound("extra2.contains=" + DEFAULT_EXTRA_2);

        // Get all the comentariosList where extra2 contains UPDATED_EXTRA_2
        defaultComentariosShouldNotBeFound("extra2.contains=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra2NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra2 does not contain DEFAULT_EXTRA_2
        defaultComentariosShouldNotBeFound("extra2.doesNotContain=" + DEFAULT_EXTRA_2);

        // Get all the comentariosList where extra2 does not contain UPDATED_EXTRA_2
        defaultComentariosShouldBeFound("extra2.doesNotContain=" + UPDATED_EXTRA_2);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra3IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra3 equals to DEFAULT_EXTRA_3
        defaultComentariosShouldBeFound("extra3.equals=" + DEFAULT_EXTRA_3);

        // Get all the comentariosList where extra3 equals to UPDATED_EXTRA_3
        defaultComentariosShouldNotBeFound("extra3.equals=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra3IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra3 in DEFAULT_EXTRA_3 or UPDATED_EXTRA_3
        defaultComentariosShouldBeFound("extra3.in=" + DEFAULT_EXTRA_3 + "," + UPDATED_EXTRA_3);

        // Get all the comentariosList where extra3 equals to UPDATED_EXTRA_3
        defaultComentariosShouldNotBeFound("extra3.in=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra3IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra3 is not null
        defaultComentariosShouldBeFound("extra3.specified=true");

        // Get all the comentariosList where extra3 is null
        defaultComentariosShouldNotBeFound("extra3.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra3ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra3 contains DEFAULT_EXTRA_3
        defaultComentariosShouldBeFound("extra3.contains=" + DEFAULT_EXTRA_3);

        // Get all the comentariosList where extra3 contains UPDATED_EXTRA_3
        defaultComentariosShouldNotBeFound("extra3.contains=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra3NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra3 does not contain DEFAULT_EXTRA_3
        defaultComentariosShouldNotBeFound("extra3.doesNotContain=" + DEFAULT_EXTRA_3);

        // Get all the comentariosList where extra3 does not contain UPDATED_EXTRA_3
        defaultComentariosShouldBeFound("extra3.doesNotContain=" + UPDATED_EXTRA_3);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra4IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra4 equals to DEFAULT_EXTRA_4
        defaultComentariosShouldBeFound("extra4.equals=" + DEFAULT_EXTRA_4);

        // Get all the comentariosList where extra4 equals to UPDATED_EXTRA_4
        defaultComentariosShouldNotBeFound("extra4.equals=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra4IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra4 in DEFAULT_EXTRA_4 or UPDATED_EXTRA_4
        defaultComentariosShouldBeFound("extra4.in=" + DEFAULT_EXTRA_4 + "," + UPDATED_EXTRA_4);

        // Get all the comentariosList where extra4 equals to UPDATED_EXTRA_4
        defaultComentariosShouldNotBeFound("extra4.in=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra4IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra4 is not null
        defaultComentariosShouldBeFound("extra4.specified=true");

        // Get all the comentariosList where extra4 is null
        defaultComentariosShouldNotBeFound("extra4.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra4ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra4 contains DEFAULT_EXTRA_4
        defaultComentariosShouldBeFound("extra4.contains=" + DEFAULT_EXTRA_4);

        // Get all the comentariosList where extra4 contains UPDATED_EXTRA_4
        defaultComentariosShouldNotBeFound("extra4.contains=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra4NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra4 does not contain DEFAULT_EXTRA_4
        defaultComentariosShouldNotBeFound("extra4.doesNotContain=" + DEFAULT_EXTRA_4);

        // Get all the comentariosList where extra4 does not contain UPDATED_EXTRA_4
        defaultComentariosShouldBeFound("extra4.doesNotContain=" + UPDATED_EXTRA_4);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra5IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra5 equals to DEFAULT_EXTRA_5
        defaultComentariosShouldBeFound("extra5.equals=" + DEFAULT_EXTRA_5);

        // Get all the comentariosList where extra5 equals to UPDATED_EXTRA_5
        defaultComentariosShouldNotBeFound("extra5.equals=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra5IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra5 in DEFAULT_EXTRA_5 or UPDATED_EXTRA_5
        defaultComentariosShouldBeFound("extra5.in=" + DEFAULT_EXTRA_5 + "," + UPDATED_EXTRA_5);

        // Get all the comentariosList where extra5 equals to UPDATED_EXTRA_5
        defaultComentariosShouldNotBeFound("extra5.in=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra5IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra5 is not null
        defaultComentariosShouldBeFound("extra5.specified=true");

        // Get all the comentariosList where extra5 is null
        defaultComentariosShouldNotBeFound("extra5.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra5ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra5 contains DEFAULT_EXTRA_5
        defaultComentariosShouldBeFound("extra5.contains=" + DEFAULT_EXTRA_5);

        // Get all the comentariosList where extra5 contains UPDATED_EXTRA_5
        defaultComentariosShouldNotBeFound("extra5.contains=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra5NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra5 does not contain DEFAULT_EXTRA_5
        defaultComentariosShouldNotBeFound("extra5.doesNotContain=" + DEFAULT_EXTRA_5);

        // Get all the comentariosList where extra5 does not contain UPDATED_EXTRA_5
        defaultComentariosShouldBeFound("extra5.doesNotContain=" + UPDATED_EXTRA_5);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra6IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra6 equals to DEFAULT_EXTRA_6
        defaultComentariosShouldBeFound("extra6.equals=" + DEFAULT_EXTRA_6);

        // Get all the comentariosList where extra6 equals to UPDATED_EXTRA_6
        defaultComentariosShouldNotBeFound("extra6.equals=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra6IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra6 in DEFAULT_EXTRA_6 or UPDATED_EXTRA_6
        defaultComentariosShouldBeFound("extra6.in=" + DEFAULT_EXTRA_6 + "," + UPDATED_EXTRA_6);

        // Get all the comentariosList where extra6 equals to UPDATED_EXTRA_6
        defaultComentariosShouldNotBeFound("extra6.in=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra6IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra6 is not null
        defaultComentariosShouldBeFound("extra6.specified=true");

        // Get all the comentariosList where extra6 is null
        defaultComentariosShouldNotBeFound("extra6.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra6ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra6 contains DEFAULT_EXTRA_6
        defaultComentariosShouldBeFound("extra6.contains=" + DEFAULT_EXTRA_6);

        // Get all the comentariosList where extra6 contains UPDATED_EXTRA_6
        defaultComentariosShouldNotBeFound("extra6.contains=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra6NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra6 does not contain DEFAULT_EXTRA_6
        defaultComentariosShouldNotBeFound("extra6.doesNotContain=" + DEFAULT_EXTRA_6);

        // Get all the comentariosList where extra6 does not contain UPDATED_EXTRA_6
        defaultComentariosShouldBeFound("extra6.doesNotContain=" + UPDATED_EXTRA_6);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra7IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra7 equals to DEFAULT_EXTRA_7
        defaultComentariosShouldBeFound("extra7.equals=" + DEFAULT_EXTRA_7);

        // Get all the comentariosList where extra7 equals to UPDATED_EXTRA_7
        defaultComentariosShouldNotBeFound("extra7.equals=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra7IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra7 in DEFAULT_EXTRA_7 or UPDATED_EXTRA_7
        defaultComentariosShouldBeFound("extra7.in=" + DEFAULT_EXTRA_7 + "," + UPDATED_EXTRA_7);

        // Get all the comentariosList where extra7 equals to UPDATED_EXTRA_7
        defaultComentariosShouldNotBeFound("extra7.in=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra7IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra7 is not null
        defaultComentariosShouldBeFound("extra7.specified=true");

        // Get all the comentariosList where extra7 is null
        defaultComentariosShouldNotBeFound("extra7.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra7ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra7 contains DEFAULT_EXTRA_7
        defaultComentariosShouldBeFound("extra7.contains=" + DEFAULT_EXTRA_7);

        // Get all the comentariosList where extra7 contains UPDATED_EXTRA_7
        defaultComentariosShouldNotBeFound("extra7.contains=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra7NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra7 does not contain DEFAULT_EXTRA_7
        defaultComentariosShouldNotBeFound("extra7.doesNotContain=" + DEFAULT_EXTRA_7);

        // Get all the comentariosList where extra7 does not contain UPDATED_EXTRA_7
        defaultComentariosShouldBeFound("extra7.doesNotContain=" + UPDATED_EXTRA_7);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra8IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra8 equals to DEFAULT_EXTRA_8
        defaultComentariosShouldBeFound("extra8.equals=" + DEFAULT_EXTRA_8);

        // Get all the comentariosList where extra8 equals to UPDATED_EXTRA_8
        defaultComentariosShouldNotBeFound("extra8.equals=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra8IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra8 in DEFAULT_EXTRA_8 or UPDATED_EXTRA_8
        defaultComentariosShouldBeFound("extra8.in=" + DEFAULT_EXTRA_8 + "," + UPDATED_EXTRA_8);

        // Get all the comentariosList where extra8 equals to UPDATED_EXTRA_8
        defaultComentariosShouldNotBeFound("extra8.in=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra8IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra8 is not null
        defaultComentariosShouldBeFound("extra8.specified=true");

        // Get all the comentariosList where extra8 is null
        defaultComentariosShouldNotBeFound("extra8.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra8ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra8 contains DEFAULT_EXTRA_8
        defaultComentariosShouldBeFound("extra8.contains=" + DEFAULT_EXTRA_8);

        // Get all the comentariosList where extra8 contains UPDATED_EXTRA_8
        defaultComentariosShouldNotBeFound("extra8.contains=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra8NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra8 does not contain DEFAULT_EXTRA_8
        defaultComentariosShouldNotBeFound("extra8.doesNotContain=" + DEFAULT_EXTRA_8);

        // Get all the comentariosList where extra8 does not contain UPDATED_EXTRA_8
        defaultComentariosShouldBeFound("extra8.doesNotContain=" + UPDATED_EXTRA_8);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra9IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra9 equals to DEFAULT_EXTRA_9
        defaultComentariosShouldBeFound("extra9.equals=" + DEFAULT_EXTRA_9);

        // Get all the comentariosList where extra9 equals to UPDATED_EXTRA_9
        defaultComentariosShouldNotBeFound("extra9.equals=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra9IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra9 in DEFAULT_EXTRA_9 or UPDATED_EXTRA_9
        defaultComentariosShouldBeFound("extra9.in=" + DEFAULT_EXTRA_9 + "," + UPDATED_EXTRA_9);

        // Get all the comentariosList where extra9 equals to UPDATED_EXTRA_9
        defaultComentariosShouldNotBeFound("extra9.in=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra9IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra9 is not null
        defaultComentariosShouldBeFound("extra9.specified=true");

        // Get all the comentariosList where extra9 is null
        defaultComentariosShouldNotBeFound("extra9.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra9ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra9 contains DEFAULT_EXTRA_9
        defaultComentariosShouldBeFound("extra9.contains=" + DEFAULT_EXTRA_9);

        // Get all the comentariosList where extra9 contains UPDATED_EXTRA_9
        defaultComentariosShouldNotBeFound("extra9.contains=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra9NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra9 does not contain DEFAULT_EXTRA_9
        defaultComentariosShouldNotBeFound("extra9.doesNotContain=" + DEFAULT_EXTRA_9);

        // Get all the comentariosList where extra9 does not contain UPDATED_EXTRA_9
        defaultComentariosShouldBeFound("extra9.doesNotContain=" + UPDATED_EXTRA_9);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra10IsEqualToSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra10 equals to DEFAULT_EXTRA_10
        defaultComentariosShouldBeFound("extra10.equals=" + DEFAULT_EXTRA_10);

        // Get all the comentariosList where extra10 equals to UPDATED_EXTRA_10
        defaultComentariosShouldNotBeFound("extra10.equals=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra10IsInShouldWork() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra10 in DEFAULT_EXTRA_10 or UPDATED_EXTRA_10
        defaultComentariosShouldBeFound("extra10.in=" + DEFAULT_EXTRA_10 + "," + UPDATED_EXTRA_10);

        // Get all the comentariosList where extra10 equals to UPDATED_EXTRA_10
        defaultComentariosShouldNotBeFound("extra10.in=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra10IsNullOrNotNull() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra10 is not null
        defaultComentariosShouldBeFound("extra10.specified=true");

        // Get all the comentariosList where extra10 is null
        defaultComentariosShouldNotBeFound("extra10.specified=false");
    }

    @Test
    @Transactional
    void getAllComentariosByExtra10ContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra10 contains DEFAULT_EXTRA_10
        defaultComentariosShouldBeFound("extra10.contains=" + DEFAULT_EXTRA_10);

        // Get all the comentariosList where extra10 contains UPDATED_EXTRA_10
        defaultComentariosShouldNotBeFound("extra10.contains=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllComentariosByExtra10NotContainsSomething() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        // Get all the comentariosList where extra10 does not contain DEFAULT_EXTRA_10
        defaultComentariosShouldNotBeFound("extra10.doesNotContain=" + DEFAULT_EXTRA_10);

        // Get all the comentariosList where extra10 does not contain UPDATED_EXTRA_10
        defaultComentariosShouldBeFound("extra10.doesNotContain=" + UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void getAllComentariosByReportesIsEqualToSomething() throws Exception {
        Reportes reportes;
        if (TestUtil.findAll(em, Reportes.class).isEmpty()) {
            comentariosRepository.saveAndFlush(comentarios);
            reportes = ReportesResourceIT.createEntity(em);
        } else {
            reportes = TestUtil.findAll(em, Reportes.class).get(0);
        }
        em.persist(reportes);
        em.flush();
        comentarios.setReportes(reportes);
        comentariosRepository.saveAndFlush(comentarios);
        Long reportesId = reportes.getId();

        // Get all the comentariosList where reportes equals to reportesId
        defaultComentariosShouldBeFound("reportesId.equals=" + reportesId);

        // Get all the comentariosList where reportes equals to (reportesId + 1)
        defaultComentariosShouldNotBeFound("reportesId.equals=" + (reportesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComentariosShouldBeFound(String filter) throws Exception {
        restComentariosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comentarios.getId().intValue())))
            .andExpect(jsonPath("$.[*].usuario").value(hasItem(DEFAULT_USUARIO)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)))
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
        restComentariosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComentariosShouldNotBeFound(String filter) throws Exception {
        restComentariosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComentariosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComentarios() throws Exception {
        // Get the comentarios
        restComentariosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComentarios() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();
        comentariosSearchRepository.save(comentarios);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());

        // Update the comentarios
        Comentarios updatedComentarios = comentariosRepository.findById(comentarios.getId()).get();
        // Disconnect from session so that the updates on updatedComentarios are not directly saved in db
        em.detach(updatedComentarios);
        updatedComentarios
            .usuario(UPDATED_USUARIO)
            .comentario(UPDATED_COMENTARIO)
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
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(updatedComentarios);

        restComentariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comentariosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comentariosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        Comentarios testComentarios = comentariosList.get(comentariosList.size() - 1);
        assertThat(testComentarios.getUsuario()).isEqualTo(UPDATED_USUARIO);
        assertThat(testComentarios.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testComentarios.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testComentarios.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testComentarios.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testComentarios.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testComentarios.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testComentarios.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testComentarios.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testComentarios.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testComentarios.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testComentarios.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Comentarios> comentariosSearchList = IterableUtils.toList(comentariosSearchRepository.findAll());
                Comentarios testComentariosSearch = comentariosSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testComentariosSearch.getUsuario()).isEqualTo(UPDATED_USUARIO);
                assertThat(testComentariosSearch.getComentario()).isEqualTo(UPDATED_COMENTARIO);
                assertThat(testComentariosSearch.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
                assertThat(testComentariosSearch.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
                assertThat(testComentariosSearch.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
                assertThat(testComentariosSearch.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
                assertThat(testComentariosSearch.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
                assertThat(testComentariosSearch.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
                assertThat(testComentariosSearch.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
                assertThat(testComentariosSearch.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
                assertThat(testComentariosSearch.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
                assertThat(testComentariosSearch.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
            });
    }

    @Test
    @Transactional
    void putNonExistingComentarios() throws Exception {
        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        comentarios.setId(count.incrementAndGet());

        // Create the Comentarios
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(comentarios);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComentariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comentariosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comentariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchComentarios() throws Exception {
        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        comentarios.setId(count.incrementAndGet());

        // Create the Comentarios
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(comentarios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentariosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comentariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComentarios() throws Exception {
        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        comentarios.setId(count.incrementAndGet());

        // Create the Comentarios
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(comentarios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentariosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comentariosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateComentariosWithPatch() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();

        // Update the comentarios using partial update
        Comentarios partialUpdatedComentarios = new Comentarios();
        partialUpdatedComentarios.setId(comentarios.getId());

        partialUpdatedComentarios
            .extra1(UPDATED_EXTRA_1)
            .extra2(UPDATED_EXTRA_2)
            .extra3(UPDATED_EXTRA_3)
            .extra5(UPDATED_EXTRA_5)
            .extra7(UPDATED_EXTRA_7);

        restComentariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComentarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComentarios))
            )
            .andExpect(status().isOk());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        Comentarios testComentarios = comentariosList.get(comentariosList.size() - 1);
        assertThat(testComentarios.getUsuario()).isEqualTo(DEFAULT_USUARIO);
        assertThat(testComentarios.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testComentarios.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testComentarios.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testComentarios.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testComentarios.getExtra4()).isEqualTo(DEFAULT_EXTRA_4);
        assertThat(testComentarios.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testComentarios.getExtra6()).isEqualTo(DEFAULT_EXTRA_6);
        assertThat(testComentarios.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testComentarios.getExtra8()).isEqualTo(DEFAULT_EXTRA_8);
        assertThat(testComentarios.getExtra9()).isEqualTo(DEFAULT_EXTRA_9);
        assertThat(testComentarios.getExtra10()).isEqualTo(DEFAULT_EXTRA_10);
    }

    @Test
    @Transactional
    void fullUpdateComentariosWithPatch() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);

        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();

        // Update the comentarios using partial update
        Comentarios partialUpdatedComentarios = new Comentarios();
        partialUpdatedComentarios.setId(comentarios.getId());

        partialUpdatedComentarios
            .usuario(UPDATED_USUARIO)
            .comentario(UPDATED_COMENTARIO)
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

        restComentariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComentarios.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComentarios))
            )
            .andExpect(status().isOk());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        Comentarios testComentarios = comentariosList.get(comentariosList.size() - 1);
        assertThat(testComentarios.getUsuario()).isEqualTo(UPDATED_USUARIO);
        assertThat(testComentarios.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testComentarios.getExtra1()).isEqualTo(UPDATED_EXTRA_1);
        assertThat(testComentarios.getExtra2()).isEqualTo(UPDATED_EXTRA_2);
        assertThat(testComentarios.getExtra3()).isEqualTo(UPDATED_EXTRA_3);
        assertThat(testComentarios.getExtra4()).isEqualTo(UPDATED_EXTRA_4);
        assertThat(testComentarios.getExtra5()).isEqualTo(UPDATED_EXTRA_5);
        assertThat(testComentarios.getExtra6()).isEqualTo(UPDATED_EXTRA_6);
        assertThat(testComentarios.getExtra7()).isEqualTo(UPDATED_EXTRA_7);
        assertThat(testComentarios.getExtra8()).isEqualTo(UPDATED_EXTRA_8);
        assertThat(testComentarios.getExtra9()).isEqualTo(UPDATED_EXTRA_9);
        assertThat(testComentarios.getExtra10()).isEqualTo(UPDATED_EXTRA_10);
    }

    @Test
    @Transactional
    void patchNonExistingComentarios() throws Exception {
        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        comentarios.setId(count.incrementAndGet());

        // Create the Comentarios
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(comentarios);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComentariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comentariosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comentariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComentarios() throws Exception {
        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        comentarios.setId(count.incrementAndGet());

        // Create the Comentarios
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(comentarios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentariosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comentariosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComentarios() throws Exception {
        int databaseSizeBeforeUpdate = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        comentarios.setId(count.incrementAndGet());

        // Create the Comentarios
        ComentariosDTO comentariosDTO = comentariosMapper.toDto(comentarios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComentariosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(comentariosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comentarios in the database
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteComentarios() throws Exception {
        // Initialize the database
        comentariosRepository.saveAndFlush(comentarios);
        comentariosRepository.save(comentarios);
        comentariosSearchRepository.save(comentarios);

        int databaseSizeBeforeDelete = comentariosRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the comentarios
        restComentariosMockMvc
            .perform(delete(ENTITY_API_URL_ID, comentarios.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comentarios> comentariosList = comentariosRepository.findAll();
        assertThat(comentariosList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(comentariosSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchComentarios() throws Exception {
        // Initialize the database
        comentarios = comentariosRepository.saveAndFlush(comentarios);
        comentariosSearchRepository.save(comentarios);

        // Search the comentarios
        restComentariosMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + comentarios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comentarios.getId().intValue())))
            .andExpect(jsonPath("$.[*].usuario").value(hasItem(DEFAULT_USUARIO)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)))
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
