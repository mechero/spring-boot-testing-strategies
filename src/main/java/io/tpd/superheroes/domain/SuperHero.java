package io.tpd.superheroes.domain;

public final class SuperHero {

    private String firstName;
    private String lastName;
    private String heroName;

    // Empty constructor is needed for Jackson to recreate the object from JSON
    public SuperHero() {
    }

    public SuperHero(String firstName, String lastName, String heroName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.heroName = heroName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHeroName() {
        return heroName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuperHero superHero = (SuperHero) o;

        if (firstName != null ? !firstName.equals(superHero.firstName) : superHero.firstName != null) return false;
        if (lastName != null ? !lastName.equals(superHero.lastName) : superHero.lastName != null) return false;
        return heroName != null ? heroName.equals(superHero.heroName) : superHero.heroName == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (heroName != null ? heroName.hashCode() : 0);
        return result;
    }
}
