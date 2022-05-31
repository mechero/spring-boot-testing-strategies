package io.tpd.superheroes.controller;

import io.tpd.superheroes.domain.SuperHero;
import io.tpd.superheroes.exceptions.NonExistingHeroException;
import io.tpd.superheroes.repository.SuperHeroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * This class demonstrates how to test a controller using Spring Boot Test
 * (what makes it much closer to an Integration Test)
 *
 * @author moises.macero
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SuperHeroControllerSpringBootTest {

    @MockBean
    private SuperHeroRepository superHeroRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void canRetrieveByIdWhenExists() {
        // given
        given(superHeroRepository.getSuperHero(2))
                .willReturn(new SuperHero("Rob", "Mannon", "RobotMan"));

        // when
        ResponseEntity<SuperHero> superHeroResponse = restTemplate.getForEntity("/superheroes/2", SuperHero.class);

        // then
        assertThat(superHeroResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(superHeroResponse.getBody().equals(new SuperHero("Rob", "Mannon", "RobotMan")));
    }

    @Test
    void canRetrieveByIdWhenDoesNotExist() {
        // given
        given(superHeroRepository.getSuperHero(2))
                .willThrow(new NonExistingHeroException());

        // when
        ResponseEntity<SuperHero> superHeroResponse = restTemplate.getForEntity("/superheroes/2", SuperHero.class);

        // then
        assertThat(superHeroResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(superHeroResponse.getBody()).isNull();
    }

    @Test
    void canRetrieveByNameWhenExists() {
        // given
        given(superHeroRepository.getSuperHero("RobotMan"))
                .willReturn(Optional.of(new SuperHero("Rob", "Mannon", "RobotMan")));

        // when
        ResponseEntity<SuperHero> superHeroResponse = restTemplate
                .getForEntity("/superheroes/?name=RobotMan", SuperHero.class);

        // then
        assertThat(superHeroResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(superHeroResponse.getBody().equals(new SuperHero("Rob", "Mannon", "RobotMan")));
    }

    @Test
    void canRetrieveByNameWhenDoesNotExist() {
        // given
        given(superHeroRepository.getSuperHero("RobotMan"))
                .willReturn(Optional.empty());

        // when
        ResponseEntity<SuperHero> superHeroResponse = restTemplate
                .getForEntity("/superheroes/?name=RobotMan", SuperHero.class);

        // then
        assertThat(superHeroResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(superHeroResponse.getBody()).isNull();
    }

    @Test
    void canCreateANewSuperHero() {
        // when
        ResponseEntity<SuperHero> superHeroResponse = restTemplate.postForEntity("/superheroes/",
                new SuperHero("Rob", "Mannon", "RobotMan"), SuperHero.class);

        // then
        assertThat(superHeroResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void headerIsPresent() throws Exception {
        // when
        ResponseEntity<SuperHero> superHeroResponse = restTemplate.getForEntity("/superheroes/2", SuperHero.class);

        // then
        assertThat(superHeroResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(superHeroResponse.getHeaders().get("X-SUPERHERO-APP")).containsOnly("super-header");
    }

}
