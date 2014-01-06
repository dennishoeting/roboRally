package prototyp.shared.util;

import java.io.Serializable;

/**
 * Enum für Himmelsrichtungen
 * 
 * @author Marcus
 * 
 */
public enum Direction implements Serializable {

	NORTH, EAST, SOUTH, WEST;

	public static Direction getDirection(final Direction direction,
			final RobotMovement robotMovement) {
		switch (robotMovement) {
		case TURN_LEFT:
			switch (direction) {
			case NORTH:
				return WEST;
			case EAST:
				return NORTH;
			case SOUTH:
				return EAST;
			case WEST:
				return SOUTH;
			}
		case TURN_RIGHT:
			switch (direction) {
			case NORTH:
				return EAST;
			case EAST:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
				return NORTH;
			}
		case TURN_AROUND:
			switch (direction) {
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
			}
		}
		return null;
	}

	public static RobotMovement getRobotMovement(final Direction in,
			final Direction out) {
		switch (in) {
		case NORTH:
			switch (out) {
			case EAST:
				return RobotMovement.TURN_LEFT;
			case WEST:
				return RobotMovement.TURN_RIGHT;
			}
			break;
		case EAST:
			switch (out) {
			case NORTH:
				return RobotMovement.TURN_RIGHT;
			case SOUTH:
				return RobotMovement.TURN_LEFT;
			}
			break;
		case SOUTH:
			switch (out) {
			case EAST:
				return RobotMovement.TURN_RIGHT;
			case WEST:
				return RobotMovement.TURN_LEFT;
			}
			break;
		default:
			switch (out) {
			case NORTH:
				return RobotMovement.TURN_LEFT;
			case SOUTH:
				return RobotMovement.TURN_RIGHT;
			}
		}

		return null;
	}

	public static int getRotationDegrees(final Direction direction) {
		switch (direction) {
		case NORTH:
			return 0;
		case EAST:
			return 90;
		case SOUTH:
			return 180;
		case WEST:
			return 270;
		}
		return -1;
	}
}
