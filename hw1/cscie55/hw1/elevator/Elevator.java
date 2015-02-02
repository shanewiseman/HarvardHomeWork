package cscie55.hw1.elevator;
/*

Student        : Shane Wiseman
Class          : CSCIE55 Java For Distributed Computing
Submitted Date : 

Class: Elevator
	Public Methods:
		Elevator       () 
		move           ()
		boardPassenger ( int )
		toString       ()
	
	Private Methods:
		floorCheck  ( int )
		shouldStop  ( int )
		floorArrival( int)
*/


public class Elevator {

	public int        currentFloor = 1; //current flow elevator resides
	public final int  maxFloor     = 7; // highest floor elevator can reach
	public final int  minFloor     = 1; // lowest floor elevator can reach
	public int        direction    = 1; // direction indication -- -1 down, 0 stable, 1 up

	//elevator memory, [ floor ][ destined passengers  ]
	//							[ delivered passengers ]
	//							[ stop flag            ]

	private int [][] elevatorMemory = new int [ ( this.maxFloor + 1 ) ][3]; 

//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	//class constructor
	public Elevator () {
		
		for ( int floor = this.minFloor; floor <= this.maxFloor; floor +=1 ){
			this.elevatorMemory[ floor ][0] = 0;
			this.elevatorMemory[ floor ][1] = 0;
			this.elevatorMemory[ floor ][2] = 0;
		}
	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	//event method signaling time to move
	public void move () {

		//move ends on either a floor stop or going up and then reaching bottom	

		for( int floor = ( this.currentFloor + 1 ); 
				( this.direction == 1 || floor > this.minFloor ); 
					floor = floor + ( this.direction * 1 ) ){

			this.currentFloor = floor;

      //if we've reached th top or bottom, flip the bit
			if( (floor == this.minFloor && this.direction == -1) || 
						(floor == this.maxFloor && this.direction == 1) )
					this.direction = (this.direction * -1);

      //should we stopping here
			if( this.shouldStop( floor ) == true ){
				floorArrival( floor );

				return;
			}
		}
	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	//event method indicating passengers are boarding
	public void boardPassenger ( int floor ){

		if( this.floorCheck( floor ) == true ){
			this.elevatorMemory[ floor ][0] += 1;
			this.elevatorMemory[ floor ][2] = 1;

		} else {
			throw new RuntimeException("Floor Out of Range! min: " +
				this.minFloor + " max: " + this.maxFloor  );
		}

	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	//print debugging info from class
	public String toString (){

		String out = "";
    //loop through all of the floors and print their status
		for ( int floor = this.minFloor; floor <= this.maxFloor; floor +=1 ){

			int numPassengers = this.elevatorMemory[ floor ][0];

			out = out + "Floor " + floor + ": " + numPassengers;
			if( numPassengers == 1 )
				out = out + " passenger\n";
			else
				out = out + " passengers\n";
		}

		return out;
	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	private boolean floorCheck ( int floor ){

    //if the floor number passed is legit we'll accept it
		if( floor >= this.minFloor && floor <= this.maxFloor )
			return true;
		else
			return false;
	}

//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	private boolean shouldStop ( int floor ){
  
    // we should stop if we've reached a floor that has people destined for it
    // or, the floor flag has been set
		if( this.elevatorMemory[ floor ][0] > 0 || this.elevatorMemory[ floor ][2] == 1 )
			return true;
		else
			return false;
	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################
	private void floorArrival ( int floor ){

		int numPassengers = elevatorMemory[ floor ][0];

		this.elevatorMemory[ floor ][2] = 0;

		elevatorMemory[ floor ][0] = elevatorMemory[ floor ][0] - numPassengers;
		elevatorMemory[ floor ][1] = elevatorMemory[ floor ][1] + numPassengers;

	}
//##############################################################################
//------------------------------------------------------------------------------
//##############################################################################

//END LINE 152
}
