package com.neolab.heroesGame.arena;

import com.neolab.heroesGame.errors.HeroExceptions;
import com.neolab.heroesGame.heroes.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ArmyTest {
    @Test
    public void ArmyMethodsTest() throws HeroExceptions {
        final Hero warlord = WarlordFootman.createInstance();
        final Hero footman = Footman.createInstance();
        final int defaultHpWarlord = warlord.getHp();
        final int defaultDamageWarlord = warlord.getDamage();
        final int defaultHp = footman.getHp();
        final int defaultDamage = footman.getDamage();

        final Map<SquareCoordinate, Hero> armyMap = new HashMap<>();
        final SquareCoordinate warlordCoord = new SquareCoordinate(0, 0);
        final SquareCoordinate footmanCoord = new SquareCoordinate(1, 1);
        armyMap.put(warlordCoord, warlord);
        armyMap.put(footmanCoord, footman);
        final Army army = new Army(armyMap);

        //someOneAct
        final Map<SquareCoordinate, Hero> map1 = new HashMap<>();
        final SquareCoordinate sq = new SquareCoordinate(1,1);
        map1.put(sq, WarlordVampire.createInstance());
        final Army army1 = new Army(map1);
        final Hero h = army1.getHero(sq).orElseThrow();
        army1.removeAvailableHeroById(h.getUnitId());
        assertFalse(army1.canSomeOneAct());
        assertTrue(army.canSomeOneAct());

        //improveTest
        assertNotEquals(defaultDamage, footman.getDamage());
        assertNotEquals(defaultHp, footman.getHp());
        assertNotEquals(defaultDamageWarlord, warlord.getDamage());
        assertNotEquals(defaultHpWarlord, warlord.getHp());

        //cancelImproveTest
        army.cancelImprove();
        assertEquals(defaultDamage, footman.getDamage());
        assertEquals(defaultHp, footman.getHp());
        assertEquals(defaultDamageWarlord, warlord.getDamage());
        assertEquals(defaultHpWarlord, warlord.getHp());

        //killHeroTest
        warlord.setHp(0);
        army.killHero(warlordCoord);
        assertEquals(army.getHeroes().size(), 1);
        assertEquals(army.getAvailableHeroes().size(), 1);
        footman.setHp(0);
        army.killHero(footmanCoord);
        assertEquals(army.getHeroes().size(), 0);
        assertEquals(army.getAvailableHeroes().size(), 0);
    }

    @Test(expected = HeroExceptions.class)
    public void findWarlordThrowsTest() throws HeroExceptions {
        final Map<SquareCoordinate, Hero> map = new HashMap<>();
        final Army army = new Army(map);
    }

    @Test(expected = HeroExceptions.class)
    public void findTwoWarlordThrowsTest() throws HeroExceptions {
        final Map<SquareCoordinate, Hero> map = new HashMap<>();
        map.put(new SquareCoordinate(1, 2), WarlordMagician.createInstance());
        map.put(new SquareCoordinate(0, 0), WarlordFootman.createInstance());

        final Army army = new Army(map);
    }
}
