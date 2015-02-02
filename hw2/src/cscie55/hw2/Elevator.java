package cscie55.hw2;
/**
 * Elevator Object, providing functionality similiar to an elevator
 *
 *@author Shane Wiseman
 *@version 1.0.0 Submitted Sunday September 28,2014
**/

public class Elevator {

    private final static int MAX_FLOOR    = Building.FLOORS;
    private final static int MIN_FLOOR    = Building.GROUND_FLOOR;
    private              int currentFloor = 1;
    private              int direction    = 1;
    private final        Building building; 
    
    public  final static int CAPACITY     = 10;

    private int [] destinedPassengers = 
            new int [Elevator.MAX_FLOOR + 1];

    public Elevator (Building building) {
        
        this.building = building;
        
    }

    /** HW2 Public Methods **/
    
    /** Return the number of passengers currently on the elevator **/
    public int passengers(){
        return currentCapacity(); 
    }

    /** returns the Elevator's current floor **/
    public int currentFloor(){
        return currentFloor;
    }
    
    /** HW1 Public Methods **/

    /** moves elevator and changes passenger data as necessary **/
    public void move(){
        
        currentFloor = currentFloor + ( 1 * this.direction);
        //if we're at the bottom or top after move, flip the bit
        if(Elevator.MIN_FLOOR == currentFloor 
                || currentFloor == Elevator.MAX_FLOOR)
            this.direction *= -1;
        
        if(destinedPassengers[ currentFloor ] > 0)
            elevatorStop( currentFloor, true );
        
        if(building.floor(currentFloor).passengersWaiting() > 0)
            elevatorStop(currentFloor, false);
        
    }
   
    /** attemps to board passenger onto the Elevator destined for a given floor **/ 
    public void boardPassenger(int dstFloor) throws ElevatorFullException {
        
        if( floorVerfication(dstFloor)){
            //if we were to add one, would we be at or less @ capacity      
            if( currentCapacity() <= ( Elevator.CAPACITY - 1 ) )
                destinedPassengers[dstFloor]++;
            else
                throw new ElevatorFullException();
            
        } else {
            System.out.println("WRONG FLOOR N00B");
        }
    
    }
    
    /** debug info **/
    public String toString(){
        return "CurrentFloor: " + currentFloor + " Passengers: " 
          + currentCapacity() ;
    }
    
    /** PRIVATE METHODS **/
    

    /** calculates current occupancy of Elevator **/
    private int currentCapacity(){
        int total = 0;
        
        for(int i = Elevator.MIN_FLOOR; i <= Elevator.MAX_FLOOR; i++){
            total += destinedPassengers[i];
        }
        
        return total;
    }
    
    /** Ensures floor is within the bounds of operation **/
    private Boolean floorVerfication(int floor){
        
        if(Elevator.MIN_FLOOR <= floor && floor <= Elevator.MAX_FLOOR)
            return true;
        else 
            return false;
    }
    
    /** peforms any required actions on a floor Elevator needs to stop at **/
    private void elevatorStop(int floor, Boolean unload){
        
        if(unload){
            destinedPassengers[ floor ] = 0;
        } else {
            
            while(building.floor(floor).passengersWaiting() > 0){
                try {
                    boardPassenger(1);
                } catch (ElevatorFullException e) {
                    break;
                }
                
                building.floor(floor).waitingPassengers--;
            }
                     
        }
    }
}
