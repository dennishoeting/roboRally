package prototyp.client.presenter.round;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import prototyp.shared.animation.Animate;
import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.round.PlayingBoard;
import prototyp.shared.round.Robot;
import prototyp.shared.round.RoundOption;
import prototyp.shared.util.Position;

/**
 * Interface, um den LogicManager mit dem WatcherManager abzugleichen
 * 
 * @author timo
 * @version 1.0
 */
public interface ManagerInterface {

	public void createBackupFieldMap();

	public List<List<Animate>> getAnimateReplayList();

	public Map<Position, Stack<Integer>> getBackupFieldStack();

	public PlayingBoard getPlayingBoard();

	public Map<Integer, Robot> getRobots();

	public RoundOption getRoundOption();

	public boolean isGameFinished();

	public void moveAllRobots(final Map<Integer, List<Programmingcard>> programmingcardsMap);

	public void setPlayingBoard(final PlayingBoard playingBoard);

	public void setRobotOnRestartField();

	public void setRobots(final Map<Integer, Robot> robots);

	public void setRoundOption(final RoundOption roundOption);

	public AnimationTimerAbstract getAnimationTimer();
}