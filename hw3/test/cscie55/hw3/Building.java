package cscie55.hw3;

public class Building {
    
    public final static int FLOORS       = 7;
    public final static int GROUND_FLOOR = 1;
    
    private Elevator elevator;
    private Floor [] floors = new Floor [Building.FLOORS + 1];
    
    
    public Building(){
        for( int i = Building.GROUND_FLOOR; i <= Building.FLOORS; i++){
            floors[i] = new Floor(this, i);
        }
        
        this.elevator = new Elevator(this); 
    }
    
    /**
     * No clue
     * @param passenger passenger to enter into ground floor, 
     * making he/she a resident on the ground floor
     */
    public void enter(Passenger passenger){
        floors[GROUND_FLOOR].enterGroundFloor(passenger);
    }
    
    public Elevator elevator(){
        return this.elevator;
        
    }
    public Floor floor(int floorNumber){
        return floors[ floorNumber ];
    }

}
