package com.neolab.heroesGame.client.ai.version.first;

import com.neolab.heroesGame.aditional.CommonFunction;
import com.neolab.heroesGame.arena.Army;
import com.neolab.heroesGame.arena.BattleArena;
import com.neolab.heroesGame.arena.SquareCoordinate;
import com.neolab.heroesGame.client.ai.Player;
import com.neolab.heroesGame.client.ai.PlayerBot;
import com.neolab.heroesGame.enumerations.GameEvent;
import com.neolab.heroesGame.enumerations.HeroActions;
import com.neolab.heroesGame.errors.HeroExceptions;
import com.neolab.heroesGame.heroes.Archer;
import com.neolab.heroesGame.heroes.Healer;
import com.neolab.heroesGame.heroes.Hero;
import com.neolab.heroesGame.heroes.Magician;
import com.neolab.heroesGame.server.answers.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SimpleBot extends Player {
    private static final String BOT_NAME = "Mazaev_v_1";
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerBot.class);
    private final long SEED = 5916;
    private final Random RANDOM = new Random(SEED);
    private final Map<Integer, SquareCoordinate> coordinateMap;
    private final SquareCoordinate coordinateDoesntMatters = new SquareCoordinate(-1, -1);

    public SimpleBot(final int id) {
        super(id, BOT_NAME);
        coordinateMap = createCoordinateMap();
    }

    private Map<Integer, SquareCoordinate> createCoordinateMap() {
        final Map<Integer, SquareCoordinate> coordinateMap = new HashMap<>();
        coordinateMap.put(0, new SquareCoordinate(0, 0));
        coordinateMap.put(1, new SquareCoordinate(1, 0));
        coordinateMap.put(2, new SquareCoordinate(2, 0));
        coordinateMap.put(3, new SquareCoordinate(0, 1));
        coordinateMap.put(4, new SquareCoordinate(1, 1));
        coordinateMap.put(5, new SquareCoordinate(2, 1));
        return coordinateMap;
    }

    @Override
    public Answer getAnswer(final BattleArena board) throws HeroExceptions {
        final long startTime = System.currentTimeMillis();
        final SimulationsTree tree = new SimulationsTree();
        for (int i = 0; ; i++) {
            if (System.currentTimeMillis() - startTime > 1000) {
                LOGGER.info("Количество симуляций за {}мс: {}", System.currentTimeMillis() - startTime, i);
                break;
            }
            final GameProcessor processor = new GameProcessor(getId(), board.getCopy());
            recursiveSimulation(processor, tree);
            tree.toRoot();
        }
        final GameProcessor processor = new GameProcessor(getId(), board);
        //LOGGER.info("Пошли выбирать ход");
        return answerFromTree(processor, tree);
    }

    @Override
    public String getStringArmyFirst(final int armySize) {
        final List<String> armies = CommonFunction.getAllAvailableArmiesCode(armySize);
        return armies.get(RANDOM.nextInt(armies.size()));
    }

    @Override
    public String getStringArmySecond(final int armySize, final Army army) {
        return getStringArmyFirst(armySize);
    }

    private int recursiveSimulation(final GameProcessor processor, final SimulationsTree tree) throws HeroExceptions {
        //LOGGER.trace(processor.getBoard().toString());
        //LOGGER.trace("Active: {}, waiting: {}", processor.getActivePlayerId(), processor.getWaitingPlayerId());
        final Map<Integer, Answer> actions = getAllAction(processor);
        //LOGGER.trace("Количество действий: {}", actions.size());
        final Map<Integer, Double> actionPriority = calculateActionPriority(actions, tree);
        final int actionNumber = chooseAction(actionPriority);
        processor.handleAnswer(actions.get(actionNumber));
        final GameEvent event = processor.matchOver();
        //LOGGER.trace(event.getDescription());

        if (event == GameEvent.NOTHING_HAPPEN) {
            if (!tree.downToChild(actionNumber)) {
                tree.createChild(actionNumber, actions.get(actionNumber));
                tree.downToChild(actionNumber);
                //LOGGER.trace("Создали узел с номером {}", actionNumber);
            }
            final int winner = recursiveSimulation(processor, tree);

            //LOGGER.trace("Winner: {}", winner);
            if (winner == -1) {
                tree.increase(GameEvent.GAME_END_WITH_A_TIE);
            } else if (winner == processor.getActivePlayerId()) {
                tree.increase(GameEvent.YOU_WIN_GAME);
            } else {
                tree.increase(GameEvent.YOU_LOSE_GAME);
            }
            tree.upToParent();
            return winner;

        } else if (event == GameEvent.GAME_END_WITH_A_TIE) {
            //LOGGER.trace("tie");
            tree.upToParent();
            return -1;
        } else if (event == GameEvent.YOU_WIN_GAME) {
            //LOGGER.trace("win");
            tree.upToParent();
            return processor.getActivePlayerId();
        }
        //LOGGER.trace("loose");
        tree.upToParent();
        return processor.getWaitingPlayerId();
    }

    private Map<Integer, Answer> getAllAction(final GameProcessor processor) {
        final Map<Integer, Answer> actions = new HashMap<>();
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            final List<Answer> answers = getHeroAction(processor, coordinateMap.get(i));
            for (final Answer answer : answers) {
                actions.put(counter++, answer);
                //LOGGER.trace("{}: {}", counter - 1, answer.toString());
            }
        }
        return actions;
    }

    private List<Answer> getHeroAction(final GameProcessor processor, final SquareCoordinate coordinate) {
        final Army currentArmy = processor.getActivePlayerArmy();
        final Army enemyArmy = processor.getWaitingPlayerArmy();
        final Integer activePlayerId = processor.getActivePlayerId();
        final Hero hero = currentArmy.getAvailableHeroes().get(coordinate);

        if (hero == null) {
            return Collections.emptyList();
        }

        final List<Answer> answers = new ArrayList<>();

        if (hero instanceof Magician) {
            answers.add(new Answer(coordinate, HeroActions.ATTACK, coordinateDoesntMatters, activePlayerId));

        } else if (hero instanceof Archer) {
            for (final SquareCoordinate enemyCoordinate : enemyArmy.getHeroes().keySet()) {
                answers.add(new Answer(coordinate, HeroActions.ATTACK, enemyCoordinate, activePlayerId));
            }

        } else if (hero instanceof Healer) {
            for (final SquareCoordinate alliesCoordinate : currentArmy.getHeroes().keySet()) {
                if (currentArmy.getHero(alliesCoordinate).get().isInjure()) {
                    answers.add(new Answer(coordinate, HeroActions.HEAL, alliesCoordinate, activePlayerId));
                }
            }

        } else {
            for (final SquareCoordinate enemyCoordinate : CommonFunction.getCorrectTargetForFootman(coordinate, enemyArmy)) {
                answers.add(new Answer(coordinate, HeroActions.ATTACK, enemyCoordinate, activePlayerId));
            }
        }
        answers.add(new Answer(coordinate, HeroActions.DEFENCE, coordinateDoesntMatters, activePlayerId));
        return answers;
    }

    private Map<Integer, Double> calculateActionPriority(final Map<Integer, Answer> actions,
                                                         final SimulationsTree tree) {
        final Map<Integer, Double> actionPriority = new HashMap<>();
        double priority = 0.0;
        for (int i = 0; i < actions.size(); i++) {
            priority += (0.5 + 0.2 * tree.calculatePointsForChild(i))
                    * (actions.get(i).getAction() == HeroActions.DEFENCE ? 0.5 : 1.5);
            actionPriority.put(i, priority);
        }
        return actionPriority;
    }

    private int chooseAction(final Map<Integer, Double> actionPriority) {
        final Double random = RANDOM.nextDouble() * actionPriority.get(actionPriority.size() - 1);
        //LOGGER.trace("RANDOM: {}", random);
        for (int i = 0; i < actionPriority.size(); i++) {
            if (actionPriority.get(i) > random) {
                return i;
            }
        }
        return 0;
    }

    private Answer answerFromTree(final GameProcessor processor, final SimulationsTree tree) {
        final Map<Integer, Answer> actions = getAllAction(processor);
        final List<Node> nodes = tree.getChildren();
        int counter = -1;
        for (int i = 0; i < nodes.size(); i++) {
            final Node temp = nodes.get(i);
            if (temp != null) {
                if (counter == -1 || nodes.get(i).getSimulationsCounter() > nodes.get(counter).getSimulationsCounter()) {
                    counter = i;
                }
                //LOGGER.trace("{}: {}", i, actions.get(i).toString());
                //LOGGER.trace("{}: {}", i, temp.getSimulationsCounter());
            }
        }
        //LOGGER.trace(actions.get(counter).toString());
        return actions.get(counter);
    }
}
