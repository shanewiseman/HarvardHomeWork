package cscie55.hw3;

public class Passenger {

    @SuppressWarnings("unused")
    private final        int ID;
    private final static int UNDEFINED_FLOOR = -1;
    private              int currentFloor = UNDEFINED_FLOOR;
    private              int destinedFloor = UNDEFINED_FLOOR;
    
    
    
    public Passenger(int id){
        currentFloor = Building.GROUND_FLOOR;
        ID = id;
    }
    
    public int currentFloor(){
        return currentFloor;
    }
    
    public int destinationFloor(){
        return destinedFloor;
    }
   
    /**
     * Passenger is signaling he is destined for a different floor
     * @param newDestinationFloor
     */
    public void waitForElevator(int newDestinationFloor){      
        floorCheck(newDestinationFloor);
        this.destinedFloor = newDestinationFloor;
        
    }
    
    /**
     * Called when passengers gets on Elevator
     */
    public void boardElevator(){
        currentFloor = UNDEFINED_FLOOR;
    }
    
    
    /**
     * Called when passenger arrives to destined floor
     */
    public void arrive(){
        currentFloor = destinedFloor;
        destinedFloor = UNDEFINED_FLOOR;
    }
    
    public String toString(){
        String string = 
                "PASSENGER: current: " + currentFloor + "  destined: " + destinedFloor;
        
        return string;
    }
    
    private boolean floorCheck(int floor){
        if( Building.GROUND_FLOOR <= floor && floor <= Building.FLOORS){
            return true;
        } else 
            throw new IllegalArgumentException("Floor is OutofBounds for Building");
    }
}
