package cscie55.hw2;
/**
 * Building Object, representing a collection of floors and an elevator
 *
 *@author Shane Wiseman
 *@version 1.0.0 Submitted Sunday September 28,2014
**/

public class Building {
    
    /** Number of Floors In Building **/
    public final static int FLOORS       = 7;

    /** Bottom Floor Number **/
    public final static int GROUND_FLOOR = 1;
    
    /** Contains single Elevator object **/
    private Elevator elevator;

    /** static sized array for all Floor objects **/
    private Floor [] floors = new Floor [Building.FLOORS + 1];
    
    
    /** Initializes floors and Elevator **/
    public Building(){

        //init all of the floor objects
        for( int i = Building.GROUND_FLOOR; i <= Building.FLOORS; i++){
            floors[i] = new Floor(this, i);
        }
        
        this.elevator = new Elevator(this);
        
    }
    
    /** returns elevator belonging to this building **/
    public Elevator elevator(){
        return elevator;
        
    }

    /** returns floor object for a particular floor number **/
    public Floor floor(int floorNumber){
        return floors[ floorNumber ];
    }

}
