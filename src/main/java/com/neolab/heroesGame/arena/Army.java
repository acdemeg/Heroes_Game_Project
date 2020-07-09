package com.neolab.heroesGame.arena;

import com.neolab.heroesGame.heroes.Hero;
import com.neolab.heroesGame.heroes.IWarlord;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Army {
    private final Map<SquareCoordinate, Hero> heroes;
    private final IWarlord warlord;
    private Map<SquareCoordinate, Hero> availableHero;

    public Army(Map<SquareCoordinate, Hero> heroes, IWarlord warlord, SquareCoordinate warlordPos) {
        this.heroes = heroes;
        this.warlord = warlord;
        this.heroes.put(warlordPos, (Hero) warlord);
        setAvailableHeroes();
        improveAllies();
    }

    public Map<SquareCoordinate, Hero> getHeroes() {
        return heroes;
    }

    public Map<SquareCoordinate, Hero> getAvailableHero() {
        return availableHero;
    }

    public Optional<Hero> getHero(SquareCoordinate coord) {
        return Optional.of(heroes.get(coord));
    }

    public void setAvailableHeroes() {
        this.availableHero = new HashMap<>(heroes);
    }

    public void killHero(Hero hero) {
        removeAvailableHero(hero);
        heroes.values().removeIf(value -> value.equals(hero));
    }

    public void removeAvailableHero(Hero hero) {
        availableHero.values().removeIf(value -> value.equals(hero));
    }

    public boolean isWarlordAlive() {
        Optional<IWarlord> warlord = getWarlord();
        return warlord.isPresent();
    }

    public void improveAllies() {
        Optional<IWarlord> warlord = getWarlord();
        warlord.ifPresent(iWarlord -> heroes.values()
                .forEach(h -> improve(h, iWarlord.getImproveCoefficient())));
    }

    public Optional<IWarlord> getWarlord() {
        return Optional.of(this.warlord);
    }

    private void improve(Hero hero, float improveCoeff) {
        int value = hero.getHpMax() + Math.round((float) hero.getHpMax() * improveCoeff);
        hero.setHpMax(value);
        value = hero.getDamageDefault() + Math.round((float) hero.getDamageDefault() * improveCoeff);
        hero.setDamage(value);
        float armor = (1.0f + improveCoeff) * hero.getArmorDefault();
        hero.setArmor(armor);
    }

    public void cancelImprove() {
        heroes.values().forEach(this::cancel);
    }

    private void cancel(Hero hero) {
        hero.setArmor(hero.getArmorDefault());
        hero.setHpMax(hero.getHpDefault());
        hero.setDamage(hero.getDamageDefault());
    }
}
