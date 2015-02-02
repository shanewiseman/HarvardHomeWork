package cscie55.hw1.elevatortest;

import cscie55.hw1.elevator.Elevator;

public class ElevatorTest {
	
	public static void main (String[] args ){
		
		Elevator mainElevator = new Elevator(); 

		//board passengers going to floor 3
		for ( int i = 1; i <= 2; i++ )
			mainElevator.boardPassenger(3);

		//board passengers going to floor 5
		for ( int i = 1; i <= 1; i++)
			mainElevator.boardPassenger(5);

		//have elevator go all the way up
			System.out.println( mainElevator.toString() );
			mainElevator.move();
			System.out.println( mainElevator.toString() );

			mainElevator.move();
			System.out.println( mainElevator.toString() );

			mainElevator.move();
			System.out.println( mainElevator.toString() );
	}

}
