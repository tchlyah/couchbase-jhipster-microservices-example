package com.couchbase.example.brewery.web.rest;

import com.couchbase.example.brewery.BreweryApp;

import com.couchbase.example.brewery.domain.Beer;
import com.couchbase.example.brewery.repository.BeerRepository;
import com.couchbase.example.brewery.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

    import static com.couchbase.example.brewery.web.rest.TestUtil.mockAuthentication;

import static com.couchbase.example.brewery.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BeerResource REST controller.
 *
 * @see BeerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BreweryApp.class)
public class BeerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STYLE = "AAAAAAAAAA";
    private static final String UPDATED_STYLE = "BBBBBBBBBB";

    private static final String DEFAULT_BREWERY = "AAAAAAAAAA";
    private static final String UPDATED_BREWERY = "BBBBBBBBBB";

    private static final Float DEFAULT_ABV = 1F;
    private static final Float UPDATED_ABV = 2F;

    private static final Integer DEFAULT_IBU = 1;
    private static final Integer UPDATED_IBU = 2;

    private static final Integer DEFAULT_SRM = 1;
    private static final Integer UPDATED_SRM = 2;

    private static final Integer DEFAULT_UPC = 1;
    private static final Integer UPDATED_UPC = 2;

    private static final LocalDate DEFAULT_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private BeerRepository beerRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restBeerMockMvc;

    private Beer beer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BeerResource beerResource = new BeerResource(beerRepository);
        this.restBeerMockMvc = MockMvcBuilders.standaloneSetup(beerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beer createEntity() {
        Beer beer = new Beer()
            .name(DEFAULT_NAME)
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .style(DEFAULT_STYLE)
            .brewery(DEFAULT_BREWERY)
            .abv(DEFAULT_ABV)
            .ibu(DEFAULT_IBU)
            .srm(DEFAULT_SRM)
            .upc(DEFAULT_UPC)
            .updated(DEFAULT_UPDATED);
        return beer;
    }

    @Before
    public void initTest() {
        mockAuthentication();
        beerRepository.deleteAll();
        beer = createEntity();
    }

    @Test
    public void createBeer() throws Exception {
        int databaseSizeBeforeCreate = beerRepository.findAll().size();

        // Create the Beer
        restBeerMockMvc.perform(post("/api/beers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beer)))
            .andExpect(status().isCreated());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeCreate + 1);
        Beer testBeer = beerList.get(beerList.size() - 1);
        assertThat(testBeer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBeer.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testBeer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBeer.getStyle()).isEqualTo(DEFAULT_STYLE);
        assertThat(testBeer.getBrewery()).isEqualTo(DEFAULT_BREWERY);
        assertThat(testBeer.getAbv()).isEqualTo(DEFAULT_ABV);
        assertThat(testBeer.getIbu()).isEqualTo(DEFAULT_IBU);
        assertThat(testBeer.getSrm()).isEqualTo(DEFAULT_SRM);
        assertThat(testBeer.getUpc()).isEqualTo(DEFAULT_UPC);
        assertThat(testBeer.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    public void createBeerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = beerRepository.findAll().size();

        // Create the Beer with an existing ID
        beer.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerMockMvc.perform(post("/api/beers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beer)))
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = beerRepository.findAll().size();
        // set the field null
        beer.setName(null);

        // Create the Beer, which fails.

        restBeerMockMvc.perform(post("/api/beers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beer)))
            .andExpect(status().isBadRequest());

        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = beerRepository.findAll().size();
        // set the field null
        beer.setCategory(null);

        // Create the Beer, which fails.

        restBeerMockMvc.perform(post("/api/beers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beer)))
            .andExpect(status().isBadRequest());

        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBeers() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        // Get all the beerList
        restBeerMockMvc.perform(get("/api/beers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beer.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].style").value(hasItem(DEFAULT_STYLE.toString())))
            .andExpect(jsonPath("$.[*].brewery").value(hasItem(DEFAULT_BREWERY.toString())))
            .andExpect(jsonPath("$.[*].abv").value(hasItem(DEFAULT_ABV.doubleValue())))
            .andExpect(jsonPath("$.[*].ibu").value(hasItem(DEFAULT_IBU)))
            .andExpect(jsonPath("$.[*].srm").value(hasItem(DEFAULT_SRM)))
            .andExpect(jsonPath("$.[*].upc").value(hasItem(DEFAULT_UPC)))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }
    

    @Test
    public void getBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        // Get the beer
        restBeerMockMvc.perform(get("/api/beers/{id}", beer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(beer.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.style").value(DEFAULT_STYLE.toString()))
            .andExpect(jsonPath("$.brewery").value(DEFAULT_BREWERY.toString()))
            .andExpect(jsonPath("$.abv").value(DEFAULT_ABV.doubleValue()))
            .andExpect(jsonPath("$.ibu").value(DEFAULT_IBU))
            .andExpect(jsonPath("$.srm").value(DEFAULT_SRM))
            .andExpect(jsonPath("$.upc").value(DEFAULT_UPC))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }
    @Test
    public void getNonExistingBeer() throws Exception {
        // Get the beer
        restBeerMockMvc.perform(get("/api/beers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Update the beer
        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        updatedBeer
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .style(UPDATED_STYLE)
            .brewery(UPDATED_BREWERY)
            .abv(UPDATED_ABV)
            .ibu(UPDATED_IBU)
            .srm(UPDATED_SRM)
            .upc(UPDATED_UPC)
            .updated(UPDATED_UPDATED);

        restBeerMockMvc.perform(put("/api/beers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBeer)))
            .andExpect(status().isOk());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
        Beer testBeer = beerList.get(beerList.size() - 1);
        assertThat(testBeer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBeer.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testBeer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBeer.getStyle()).isEqualTo(UPDATED_STYLE);
        assertThat(testBeer.getBrewery()).isEqualTo(UPDATED_BREWERY);
        assertThat(testBeer.getAbv()).isEqualTo(UPDATED_ABV);
        assertThat(testBeer.getIbu()).isEqualTo(UPDATED_IBU);
        assertThat(testBeer.getSrm()).isEqualTo(UPDATED_SRM);
        assertThat(testBeer.getUpc()).isEqualTo(UPDATED_UPC);
        assertThat(testBeer.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    public void updateNonExistingBeer() throws Exception {
        int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Create the Beer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restBeerMockMvc.perform(put("/api/beers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beer)))
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        int databaseSizeBeforeDelete = beerRepository.findAll().size();

        // Get the beer
        restBeerMockMvc.perform(delete("/api/beers/{id}", beer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Beer.class);
        Beer beer1 = new Beer();
        beer1.setId("id1");
        Beer beer2 = new Beer();
        beer2.setId(beer1.getId());
        assertThat(beer1).isEqualTo(beer2);
        beer2.setId("id2");
        assertThat(beer1).isNotEqualTo(beer2);
        beer1.setId(null);
        assertThat(beer1).isNotEqualTo(beer2);
    }
}
