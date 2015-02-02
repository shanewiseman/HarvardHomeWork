package cscie55.hw1.elevator;
/*

Student        : Shane Wiseman
Class          : CSCIE55 Java For Distributed Computing
Submitted Date : 

Class: Elevator
	Public Methods:
		Elevator () 
		move ()
		boardPassenger ( int )
		toString ()
	
	Private Methods:
		floorCheck( int )
		shouldStop( int )
*/


public class Elevator {

	public int        currentFloor = 1; //current flow elevator resides
	public final int  maxFloor     = 7; // highest floor elevator can reach
	public final int  minFloor     = 1; // lowest floor elevator can reach
	public int        direction    = 1; // direction indication -- -1 down, 0 stable, 1 up

	//elevator memory, [ floor ][ destined passengers  ]
	//							[ delivered passengers ]

	private int [][] elevatorMemory = new int[ maxFloor ][2]; 

//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	//clas constructor
	public Elevator () {
		
		for ( int floor = this.minFloor; floor <= this.maxFloor; floor +=1 ){
			this.elevatorMemory[ floor ][0] = 0;
			this.elevatorMemory[ floor ][1] = 0;
			System.out.println("HERE");
		}
		
		//System.out.println( this.toString() );
	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	//event method signaling time to move
	public void move () {

		this.currentFloor = this.currentFloor + ( this.direction * 1 );

		if( this.shouldStop( this.currentFloor ) == true )
			floorArrival( this.currentFloor );

		if( this.currentFloor == this.minFloor || this.currentFloor == this.maxFloor)
			this.direction = ( this.direction * -1 );// flip the bit

	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	//event method indicating passengers are boarding
	public void boardPassenger ( int floor ){

		if( this.floorCheck( floor ) == true ){
			this.elevatorMemory[ floor ][0] += 1;

		} else {
			throw new RuntimeException("Flooor out of Range! min: " +
				this.minFloor + " max: " + this.maxFloor  );
		}

	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	//print debugging info from class
	public String toString (){

		String out = "";

		for ( int floor = this.minFloor; floor <= this.maxFloor; floor +=1 ){
			out = out + "Floor " + floor + ": " + 
				this.elevatorMemory[ floor ][0] + " passengers\n"; 
		}

		return out;
	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	private boolean floorCheck ( int floor ){

		if( floor >= this.minFloor && floor <= this.maxFloor ){
			return true;

		} else {
			return false;
		}
	}

//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	private boolean shouldStop ( int floor ){

		if( this.elevatorMemory[ floor ][0] > 0 ){
			return true;

		} else {
			return false;
		}
	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	private void floorArrival ( int floor ){

		int numPassengers = elevatorMemory[ floor ][0];
		elevatorMemory[ floor ][0] = elevatorMemory[ floor ][0] - numPassengers;
		elevatorMemory[ floor ][1] = elevatorMemory[ floor ][1] + numPassengers;
		
	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################







}
