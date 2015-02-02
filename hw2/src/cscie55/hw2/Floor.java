package cscie55.hw2;
/**
 * Floor Object, representing a floor within a building 
 *
 *@author Shane Wiseman
 *@version 1.0.0 Submitted Sunday September 28,2014
**/

public class Floor {
    /** holds relationship to the building it belongs too **/ 
    private final Building building;
    /** what floor does this object relate too **/
    private final int      floorNumber;
    /** how many passengers are waiting for an Elevator on this floor **/
    public        int      waitingPassengers = 0;
    
    /** establishes relationship to the building **/
    public Floor(Building building, int floorNumber){
        
           this.building = building;
           this.floorNumber = floorNumber;
    }
    /** Returns the number of passengers on the floor who are waiting for the elevator. **/
    public int passengersWaiting(){
        return waitingPassengers;
    }

    /** Called when a passenger on the floor wants to wait for the elevator **/
    public void waitForElevator(){
        waitingPassengers++;
    }
    
}
