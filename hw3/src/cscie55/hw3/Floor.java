package cscie55.hw3;

import java.util.ArrayList;
import java.util.Collection;

public class Floor {
    
    @SuppressWarnings("unused")
    private final Building building;
    private final int      floorNumber;
    
    Collection<Passenger> residentPassengers = new ArrayList<Passenger>();
    Collection<Passenger> ascendingPassengers = new ArrayList<Passenger>();
    Collection<Passenger> decendingPassengers = new ArrayList<Passenger>();
    
    Floor(Building building, int floorNumber){
        
           this.building = building;
           this.floorNumber = floorNumber;
    }

    
    public boolean isResident(Passenger passenger){
        return ( residentPassengers.contains(passenger) );
    }
    
    /**
     * associates passenger with ground floor
     * @param passenger
     */
    public void enterGroundFloor(Passenger passenger){
        //only a floor object who is the ground floor should execute this
        if(floorNumber == Building.GROUND_FLOOR)
            residentPassengers.add(passenger);
        else
            throw new RuntimeException("Method can only be called on the ground Floor Object");
        
    }
    
    /**
     * Called when a passenger on the floor wants to wait for the elevator
     * @param passenger Passenger signaling he is waiting to board
     * @param destinationFloor where is said Passenger going
     */
    public void waitForElevator(Passenger passenger, int destinationFloor){
       
        //to check ton ensure it's a valid floor
        floorCheck(destinationFloor);
       
        //Initiate the passenger's wait function
        passenger.waitForElevator(destinationFloor);
        
        //is he/she going up or down
        if(destinationFloor < floorNumber)
            decendingPassengers.add(passenger);
        
        else if(destinationFloor > floorNumber)
            ascendingPassengers.add(passenger);
        
        else 
            throw new IllegalArgumentException(
                    "Passenger Cannot Be Destined for the Floor he/she resides on"
            );
        
        //remove passenger as a resident, he/she is leaving to another floor
        residentPassengers.remove(passenger);
    }
    

    /**
     * Ensuring passenger is going to a allowable floor
     * @param floor
     * @return
     */
    private boolean floorCheck(int floor){
        if( Building.GROUND_FLOOR <= floor && floor <= Building.FLOORS){
            return true;
        } else 
            throw new IllegalArgumentException("Floor is OutofBounds for Building");
    }
}
