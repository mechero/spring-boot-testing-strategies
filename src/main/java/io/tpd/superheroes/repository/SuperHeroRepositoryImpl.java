package io.tpd.superheroes.repository;

import io.tpd.superheroes.domain.SuperHero;
import io.tpd.superheroes.exceptions.NonExistingHeroException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple, In-memory implementation of SuperHero Repository. It comes with some predefined data.
 *
 * @author moises.macero
 */
@Component
class SuperHeroRepositoryImpl implements SuperHeroRepository {

    private List<SuperHero> superHeroList;

    public SuperHeroRepositoryImpl() {
        superHeroList = new ArrayList<>();
        superHeroList.add(new SuperHero("Jean", "Grey", "Phoenix"));
        superHeroList.add(new SuperHero("Bruce", "Wayne", "Batman"));
        superHeroList.add(new SuperHero("Susan", "Storm", "Invisible woman"));
        superHeroList.add(new SuperHero("Peter", "Parker", "Spiderman"));
    }

    @Override
    public SuperHero getSuperHero(int id) {
        if (id > superHeroList.size()) throw new NonExistingHeroException();
        return superHeroList.get(id - 1);
    }

    @Override
    public Optional<SuperHero> getSuperHero(String heroName) {
        return superHeroList.stream().filter(h -> h.getHeroName().equals(heroName)).findAny();
    }

    @Override
    public void saveSuperHero(SuperHero superHero) {
        superHeroList.add(superHero);
    }
}
