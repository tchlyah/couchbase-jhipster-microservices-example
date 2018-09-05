package com.couchbase.example.brewery.web.rest;

import com.couchbase.example.brewery.BreweryApp;

import com.couchbase.example.brewery.domain.Brewery;
import com.couchbase.example.brewery.repository.BreweryRepository;
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
 * Test class for the BreweryResource REST controller.
 *
 * @see BreweryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BreweryApp.class)
public class BreweryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "4 ";
    private static final String UPDATED_PHONE = "45";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private BreweryRepository breweryRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restBreweryMockMvc;

    private Brewery brewery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BreweryResource breweryResource = new BreweryResource(breweryRepository);
        this.restBreweryMockMvc = MockMvcBuilders.standaloneSetup(breweryResource)
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
    public static Brewery createEntity() {
        Brewery brewery = new Brewery()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .code(DEFAULT_CODE)
            .country(DEFAULT_COUNTRY)
            .phone(DEFAULT_PHONE)
            .state(DEFAULT_STATE)
            .website(DEFAULT_WEBSITE)
            .updated(DEFAULT_UPDATED);
        return brewery;
    }

    @Before
    public void initTest() {
        mockAuthentication();
        breweryRepository.deleteAll();
        brewery = createEntity();
    }

    @Test
    public void createBrewery() throws Exception {
        int databaseSizeBeforeCreate = breweryRepository.findAll().size();

        // Create the Brewery
        restBreweryMockMvc.perform(post("/api/breweries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brewery)))
            .andExpect(status().isCreated());

        // Validate the Brewery in the database
        List<Brewery> breweryList = breweryRepository.findAll();
        assertThat(breweryList).hasSize(databaseSizeBeforeCreate + 1);
        Brewery testBrewery = breweryList.get(breweryList.size() - 1);
        assertThat(testBrewery.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBrewery.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBrewery.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBrewery.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBrewery.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBrewery.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testBrewery.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testBrewery.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBrewery.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testBrewery.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    public void createBreweryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = breweryRepository.findAll().size();

        // Create the Brewery with an existing ID
        brewery.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restBreweryMockMvc.perform(post("/api/breweries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brewery)))
            .andExpect(status().isBadRequest());

        // Validate the Brewery in the database
        List<Brewery> breweryList = breweryRepository.findAll();
        assertThat(breweryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = breweryRepository.findAll().size();
        // set the field null
        brewery.setName(null);

        // Create the Brewery, which fails.

        restBreweryMockMvc.perform(post("/api/breweries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brewery)))
            .andExpect(status().isBadRequest());

        List<Brewery> breweryList = breweryRepository.findAll();
        assertThat(breweryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = breweryRepository.findAll().size();
        // set the field null
        brewery.setAddress(null);

        // Create the Brewery, which fails.

        restBreweryMockMvc.perform(post("/api/breweries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brewery)))
            .andExpect(status().isBadRequest());

        List<Brewery> breweryList = breweryRepository.findAll();
        assertThat(breweryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBreweries() throws Exception {
        // Initialize the database
        breweryRepository.save(brewery);

        // Get all the breweryList
        restBreweryMockMvc.perform(get("/api/breweries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brewery.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }
    

    @Test
    public void getBrewery() throws Exception {
        // Initialize the database
        breweryRepository.save(brewery);

        // Get the brewery
        restBreweryMockMvc.perform(get("/api/breweries/{id}", brewery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(brewery.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }
    @Test
    public void getNonExistingBrewery() throws Exception {
        // Get the brewery
        restBreweryMockMvc.perform(get("/api/breweries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBrewery() throws Exception {
        // Initialize the database
        breweryRepository.save(brewery);

        int databaseSizeBeforeUpdate = breweryRepository.findAll().size();

        // Update the brewery
        Brewery updatedBrewery = breweryRepository.findById(brewery.getId()).get();
        updatedBrewery
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .code(UPDATED_CODE)
            .country(UPDATED_COUNTRY)
            .phone(UPDATED_PHONE)
            .state(UPDATED_STATE)
            .website(UPDATED_WEBSITE)
            .updated(UPDATED_UPDATED);

        restBreweryMockMvc.perform(put("/api/breweries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBrewery)))
            .andExpect(status().isOk());

        // Validate the Brewery in the database
        List<Brewery> breweryList = breweryRepository.findAll();
        assertThat(breweryList).hasSize(databaseSizeBeforeUpdate);
        Brewery testBrewery = breweryList.get(breweryList.size() - 1);
        assertThat(testBrewery.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrewery.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBrewery.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBrewery.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBrewery.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBrewery.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testBrewery.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testBrewery.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBrewery.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testBrewery.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    public void updateNonExistingBrewery() throws Exception {
        int databaseSizeBeforeUpdate = breweryRepository.findAll().size();

        // Create the Brewery

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restBreweryMockMvc.perform(put("/api/breweries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brewery)))
            .andExpect(status().isBadRequest());

        // Validate the Brewery in the database
        List<Brewery> breweryList = breweryRepository.findAll();
        assertThat(breweryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBrewery() throws Exception {
        // Initialize the database
        breweryRepository.save(brewery);

        int databaseSizeBeforeDelete = breweryRepository.findAll().size();

        // Get the brewery
        restBreweryMockMvc.perform(delete("/api/breweries/{id}", brewery.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Brewery> breweryList = breweryRepository.findAll();
        assertThat(breweryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Brewery.class);
        Brewery brewery1 = new Brewery();
        brewery1.setId("id1");
        Brewery brewery2 = new Brewery();
        brewery2.setId(brewery1.getId());
        assertThat(brewery1).isEqualTo(brewery2);
        brewery2.setId("id2");
        assertThat(brewery1).isNotEqualTo(brewery2);
        brewery1.setId(null);
        assertThat(brewery1).isNotEqualTo(brewery2);
    }
}
