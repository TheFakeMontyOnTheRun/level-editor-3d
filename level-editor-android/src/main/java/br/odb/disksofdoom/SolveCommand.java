package br.odb.disksofdoom;

import java.util.LinkedList;

import br.odb.disksofdoom.DisksOfDoomMainApp.Disk;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserMetaCommandLineAction;

public class SolveCommand extends UserMetaCommandLineAction {
	
	class SolutionMove {
		
		public SolutionMove( int n, int p0, int p2, int p1) {
			problemSize = n;
			fromPole = p0;
			toPole = p2;
			usingPole = p1;
		}
		
		int problemSize;
		int fromPole;
		int toPole;
		int usingPole;
	}
	

	public SolveCommand( ConsoleApplication app ) {
		super( app );

	}
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	public void moveFrom( DisksOfDoomMainApp game, int n, LinkedList< Disk > from, int index0, LinkedList< Disk> to, int index1, LinkedList< Disk > using, int index2 ) {
		Disk d;
		
		System.out.println( "solving from n = " + n + " from " + index0 + " to " + index1 + " using " + index2  );

		if ( n == 1 ) {

			
			d = from.pop();
			System.out.println( "moving disk  = " + d.size + " from " + index0 + " to " + index1 );
			to.push( d );
			
			updateVisuals( game);
		} else {

			moveFrom( game, n - 1, from, index0, using, index2, to, index1 );

			d = from.pop();
			System.out.println( "moving disk  = " + d.size + " from " + index0 + " to " + index1 );
			to.push( d );
			updateVisuals( game);			
			
			moveFrom( game, n - 1, using, index2, to, index1, from, index0 );
		}
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		
		DisksOfDoomMainApp game = (DisksOfDoomMainApp) app;
		
		new NewGameCommand( game ).run(app, operand );
		
		
		int disks = Integer.parseInt( operand );
		
		if ( disks < 1 || disks > 10 ) {
			return;
		}
		
		updateVisuals( game );
		moveFrom( game, disks, game.pole[ 0 ], 0, game.pole[ 2 ], 2, game.pole[ 1 ], 1 );
//		LinkedList< SolutionMove > moves = new LinkedList< SolutionMove >();
//		
//		moves.push( new SolutionMove( 2, 0, 2, 1 ) );
//		
//		SolutionMove currentMove;
//		Disk disk;
//		
//		while ( !moves.isEmpty() ) {
//			
//			currentMove = moves.pop();	
//			
//			System.out.println( "solving from n = " + currentMove.problemSize + " from " + currentMove.fromPole + " to " + currentMove.toPole + " using " + currentMove.usingPole  );
//			
//			if ( currentMove.problemSize == 1 ) {
//				disk = game.pole[ currentMove.fromPole ].pop();
//				System.out.println( "moving disk  = " + disk.size + " from " + currentMove.fromPole + " to " + currentMove.toPole );
//				game.pole[ currentMove.toPole ].push( disk  );				
//			} else {
//				
//				moves.push( new SolutionMove( currentMove.problemSize - 1, currentMove.fromPole, currentMove.usingPole, currentMove.toPole ) );
//				
////				disk = game.pole[ currentMove.fromPole ].pop();
////				game.pole[ currentMove.toPole ].push( disk  );
//				
//				moves.push( new SolutionMove( currentMove.problemSize - 1, currentMove.fromPole,currentMove.toPole, currentMove.usingPole ) );
//				
//				moves.push( new SolutionMove( currentMove.problemSize - 1, currentMove.usingPole,currentMove.toPole, currentMove.fromPole ) );			
//			}
//		}
		
	}

	private void updateVisuals(DisksOfDoomMainApp game) {
		game.updateVisuals( game );
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "solve";
	}

}
