package client;

import java.util.LinkedList;
import java.util.Queue;

public class Command {
	static {
		LinkedList< Command > cmds = new LinkedList< Command >();
		for ( dir d : dir.values() ) {
			cmds.add( new Command( d ) );
		}

		for ( dir d1 : dir.values() ) {
			for ( dir d2 : dir.values() ) {
				if ( !Command.isOpposite( d1, d2 ) ) {
					cmds.add( new Command( type.Push, d1, d2 ) );
				}
			}
		}
		for ( dir d1 : dir.values() ) {
			for ( dir d2 : dir.values() ) {
				if ( d1 != d2 ) {
					cmds.add( new Command( type.Pull, d1, d2 ) );
				}
			}
		}

		every = cmds.toArray( new Command[0] );
	}

	public final static Command[] every;

	public static boolean isOpposite( dir d1, dir d2 ) {
		return d1.ordinal() + d2.ordinal() == 3;
	}

	// Order of enum important for determining opposites
	public static enum dir {
		N, W, E, S
	};
	
	public static enum type {
		Move, Push, Pull
	};

	public final type actType;
	public final dir dir1;
	public final dir dir2;

	public Command( dir d ) {
		actType = type.Move;
		dir1 = d;
		dir2 = null;
	}

	public Command( type t, dir d1, dir d2 ) {
		actType = t;
		dir1 = d1;
		dir2 = d2;
	}

	public String toString() {
		if ( actType == type.Move )
			return actType.toString() + "(" + dir1 + ")";

		return actType.toString() + "(" + dir1 + "," + dir2 + ")";
	}
	

	public String toActionString() {
		return "[" + this.toString() + "]";
	}
	
	public static Queue<Point> CommandsToPath(Point startPosition, LinkedList<Command> commands) {
		Queue<Point> path = new LinkedList<Point>();
		
		if(commands == null) {
			return path;
		}
		
		Point p = startPosition;
		for(Command cmd:commands ) {
			p = p.move(cmd.dir1);
			path.add(p);
		}
		
		
		return path;		
	}

}
