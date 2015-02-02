package cscie55.hw3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Elevator {

    private int currentFloor= 1;
    private int direction = 1;
    private Map<Integer, ArrayList<Passenger>> occupants = new HashMap<Integer,ArrayList<Passenger>>();
    
 
    private final Building building; 
    
    public  final static int CAPACITY = 10;
    
    /**
     * Constructor
     * @param building building in which Elevator belongs too
     */
    public Elevator (Building building) {
        
        this.building = building;
        
        // each floor has it's own list for Passenger storage
        for(int i = Building.GROUND_FLOOR; i <= Building.FLOORS; i++){
            occupants.put(i, new ArrayList<Passenger>());
        }
        
    }
    
    /** HW3 Public Methods **/
    
    /**
     * Determines direction of Elevator
     * @return true: going up, false: going down
     */
    public boolean goingUp(){
        return (this.direction == 1);
    }
    /**
     * Determines direction of Elevator
     * @return ture: going down, false: going up
     */
    public boolean goingDown(){
        return (this.direction == -1);
    }
    
    /** HW2 Public Methods **/
    
    /**
     * 
     * @return Return the number of passengers currently on the elevator.
     */
    public Set<Passenger>  passengers(){
        return currentOccupancy(); 
    }
    
    /**
     * 
     * @return Floor number of current Elevator Position
     */
    public int currentFloor(){
        return this.currentFloor;
    }
    
    /** HW1 Public Methods **/
    
    
    /**
     *  moves elevator and calls routines to manage passenger collections
     */
    public void move(){
        
        /*
         * Note to self, elevator does NOT take into account passengers arriving 
         * while elevator is currently at floor. Before moving to a different floor,
         * should check to board additional passengers? 
         */
        
        //actually move the elevator
        currentFloor = currentFloor + this.direction;
        
        //if we're at the bottom or top after move, flip the bit for next move
        if(Building.GROUND_FLOOR == this.currentFloor 
                || this.currentFloor == Building.FLOORS )
            this.direction *= -1;

        //just grabbing the object so i don't have to use the absolute path
        Floor floorObj = this.building.floor(currentFloor);
        
        //unload Passengers
        if(this.occupants.get(currentFloor).size() > 0)
            this.elevatorStop( currentFloor, true, this.occupants.get(currentFloor) );
 
        //board Passengers going up
        if( this.goingUp()
                && floorObj.ascendingPassengers.size() > 0)
                
            this.elevatorStop(currentFloor, false, floorObj.ascendingPassengers );
        
        //board Passengers going down
        else if( this.goingDown()
                && floorObj.decendingPassengers.size() > 0)
            
            this.elevatorStop(currentFloor, false, floorObj.decendingPassengers);
  
    }
        

    /** PRIVATE METHODS **/

    /**
     * constructs Set of total passenger occupying Elevator
     * @return
     */
    private Set<Passenger> currentOccupancy(){
       
        Set<Passenger> returnSet = new HashSet<Passenger>();

        for(int floor : occupants.keySet() ){
            for(Passenger passenger : this.occupants.get(floor)){
                returnSet.add(passenger);
            }
        }
        
        return returnSet;
    }
        
    /**
     * Performs all required logic on Package collections
     * @param floor current position of elevator
     * @param unloading are we unloading passengers for this stop?
     * @param passengers Collection of passengers to use during passenger transition
     */
    private void elevatorStop(int floor, boolean isUnloading, Collection<Passenger> passengers){
        
        if(isUnloading){
            
            //unload passengers onto floor
            for( Passenger passenger : this.occupants.get(floor)){
                passenger.arrive();
                this.building.floor(floor).residentPassengers.add(passenger);
            }
            
            //remove passengers from elevator collection by simple erasing previous list
            this.occupants.put(floor, new ArrayList<Passenger>() );
            
            
        } else {
            
            Collection<Passenger> justBoarded = new ArrayList<Passenger>();
            
            try{
                for( Passenger passenger :  passengers){
                    
                    if(this.currentOccupancy().size() == Elevator.CAPACITY)
                        throw new ElevatorFullException();
                    
                    passenger.boardElevator();
                    this.occupants.get( passenger.destinationFloor() ).add(passenger);
                    justBoarded.add(passenger);
                   
                }
            } catch (ElevatorFullException e){ }
        
            //need to remove the passengers from the passed collection as, they were just added to
            // elevators collection, can't remove items as I iterate.. shame
            for(Passenger passenger : justBoarded){
                passengers.remove(passenger);
            }
            justBoarded = null;
            
        }
    }
}
