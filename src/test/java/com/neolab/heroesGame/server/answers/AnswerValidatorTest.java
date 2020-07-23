package com.neolab.heroesGame.server.answers;

import com.neolab.heroesGame.arena.Army;
import com.neolab.heroesGame.arena.BattleArena;
import com.neolab.heroesGame.arena.SquareCoordinate;
import com.neolab.heroesGame.enumerations.HeroActions;
import com.neolab.heroesGame.enumerations.HeroErrorCode;
import com.neolab.heroesGame.errors.HeroExceptions;
import com.neolab.heroesGame.heroes.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AnswerValidatorTest {

    @Test
    public void footmanTest() throws HeroExceptions {
        final BattleArena arena = getBatleArena();
        final SquareCoordinate activeHero = new SquareCoordinate(2, 1);
        final int playerId = 1;
        boolean isValidate;

        SquareCoordinate targetHero = new SquareCoordinate(2, 1);
        Answer answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(1, 1);
        answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(0, 1);
        answer = new Answer(activeHero, HeroActions.DEFENCE, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);
    }

    @Test(expected = HeroExceptions.class)
    public void wrongActionFootman1() throws HeroExceptions {
        final BattleArena arena = getBatleArena();
        final SquareCoordinate activeHero = new SquareCoordinate(2, 1);
        final int playerId = 1;
        final SquareCoordinate targetHero = new SquareCoordinate(0, 1);
        final Answer answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        AnswerValidator.isAnswerValidate(answer, arena);
    }

    @Test(expected = HeroExceptions.class)
    public void wrongActionFootman2() throws HeroExceptions {
        final BattleArena arena = getBatleArena();
        final SquareCoordinate activeHero = new SquareCoordinate(2, 1);
        final int playerId = 1;
        final SquareCoordinate targetHero = new SquareCoordinate(1, 1);
        final Answer answer = new Answer(activeHero, HeroActions.HEAL, targetHero, playerId);
        AnswerValidator.isAnswerValidate(answer, arena);
    }

    @Test
    public void archerTest() throws HeroExceptions {
        final BattleArena arena = getBatleArena();
        final SquareCoordinate activeHero = new SquareCoordinate(2, 0);
        final int playerId = 1;
        boolean isValidate;

        SquareCoordinate targetHero = new SquareCoordinate(2, 0);
        Answer answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(1, 1);
        answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(0, 1);
        answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(0, 0);
        answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(-1, -1);
        answer = new Answer(activeHero, HeroActions.DEFENCE, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);
    }

    @Test(expected = HeroExceptions.class)
    public void wrongActionArcher() throws HeroExceptions {
        final BattleArena arena = getBatleArena();
        final SquareCoordinate activeHero = new SquareCoordinate(2, 0);
        final int playerId = 1;
        final SquareCoordinate targetHero = new SquareCoordinate(1, 1);
        final Answer answer = new Answer(activeHero, HeroActions.HEAL, targetHero, playerId);
        AnswerValidator.isAnswerValidate(answer, arena);
    }

    @Test
    public void healerTest() throws HeroExceptions {
        final BattleArena arena = getBatleArena();
        final SquareCoordinate activeHero = new SquareCoordinate(0, 0);
        final int playerId = 1;
        boolean isValidate;

        SquareCoordinate targetHero = new SquareCoordinate(2, 0);
        Answer answer = new Answer(activeHero, HeroActions.HEAL, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(1, 1);
        answer = new Answer(activeHero, HeroActions.HEAL, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(0, 1);
        answer = new Answer(activeHero, HeroActions.HEAL, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(0, 0);
        answer = new Answer(activeHero, HeroActions.HEAL, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(-1, -1);
        answer = new Answer(activeHero, HeroActions.DEFENCE, targetHero, playerId);
        isValidate = AnswerValidator.isAnswerValidate(answer, arena);
        assertTrue(isValidate);

        targetHero = new SquareCoordinate(-1, -1);
        answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        try {
            AnswerValidator.isAnswerValidate(answer, arena);
            fail();
        } catch (final HeroExceptions ex) {
            assertEquals(HeroErrorCode.ERROR_UNIT_ATTACK, ex.getHeroErrorCode());
        }
    }

    @Test(expected = HeroExceptions.class)
    public void wrongActionHealer() throws HeroExceptions {
        final BattleArena arena = getBatleArena();
        final SquareCoordinate activeHero = new SquareCoordinate(0, 0);
        final int playerId = 1;
        final SquareCoordinate targetHero = new SquareCoordinate(1, 1);
        final Answer answer = new Answer(activeHero, HeroActions.ATTACK, targetHero, playerId);
        AnswerValidator.isAnswerValidate(answer, arena);
    }

    private BattleArena getBatleArena() throws HeroExceptions {
        final Map<Integer, Army> armies = new HashMap<>();
        armies.put(1, getArmy());
        armies.put(2, getArmy());
        return new BattleArena(armies);
    }

    private Army getArmy() throws HeroExceptions {
        final Map<SquareCoordinate, Hero> heroes = new HashMap<>();
        heroes.put(new SquareCoordinate(0, 0), Healer.createInstance());
        heroes.put(new SquareCoordinate(1, 0), Magician.createInstance());
        heroes.put(new SquareCoordinate(2, 0), Archer.createInstance());
        heroes.put(new SquareCoordinate(0, 1), WarlordFootman.createInstance());
        heroes.put(new SquareCoordinate(1, 1), Footman.createInstance());
        heroes.put(new SquareCoordinate(2, 1), Footman.createInstance());
        return new Army(heroes);
    }

}