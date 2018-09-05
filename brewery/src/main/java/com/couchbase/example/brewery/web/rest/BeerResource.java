package com.couchbase.example.brewery.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.couchbase.example.brewery.domain.Beer;
import com.couchbase.example.brewery.repository.BeerRepository;
import com.couchbase.example.brewery.web.rest.errors.BadRequestAlertException;
import com.couchbase.example.brewery.web.rest.util.HeaderUtil;
import com.couchbase.example.brewery.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Beer.
 */
@RestController
@RequestMapping("/api")
public class BeerResource {

    private final Logger log = LoggerFactory.getLogger(BeerResource.class);

    private static final String ENTITY_NAME = "beer";

    private final BeerRepository beerRepository;

    public BeerResource(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    /**
     * POST  /beers : Create a new beer.
     *
     * @param beer the beer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new beer, or with status 400 (Bad Request) if the beer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/beers")
    @Timed
    public ResponseEntity<Beer> createBeer(@Valid @RequestBody Beer beer) throws URISyntaxException {
        log.debug("REST request to save Beer : {}", beer);
        if (beer.getId() != null) {
            throw new BadRequestAlertException("A new beer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Beer result = beerRepository.save(beer);
        return ResponseEntity.created(new URI("/api/beers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /beers : Updates an existing beer.
     *
     * @param beer the beer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated beer,
     * or with status 400 (Bad Request) if the beer is not valid,
     * or with status 500 (Internal Server Error) if the beer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/beers")
    @Timed
    public ResponseEntity<Beer> updateBeer(@Valid @RequestBody Beer beer) throws URISyntaxException {
        log.debug("REST request to update Beer : {}", beer);
        if (beer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Beer result = beerRepository.save(beer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, beer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /beers : get all the beers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of beers in body
     */
    @GetMapping("/beers")
    @Timed
    public ResponseEntity<List<Beer>> getAllBeers(Pageable pageable) {
        log.debug("REST request to get a page of Beers");
        Page<Beer> page = beerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/beers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /beers/:id : get the "id" beer.
     *
     * @param id the id of the beer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the beer, or with status 404 (Not Found)
     */
    @GetMapping("/beers/{id}")
    @Timed
    public ResponseEntity<Beer> getBeer(@PathVariable String id) {
        log.debug("REST request to get Beer : {}", id);
        Optional<Beer> beer = beerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(beer);
    }

    /**
     * DELETE  /beers/:id : delete the "id" beer.
     *
     * @param id the id of the beer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/beers/{id}")
    @Timed
    public ResponseEntity<Void> deleteBeer(@PathVariable String id) {
        log.debug("REST request to delete Beer : {}", id);

        beerRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
