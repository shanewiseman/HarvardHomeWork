package cscie55.hw2test;

import cscie55.hw2.Building;
import cscie55.hw2.Elevator;
import cscie55.hw2.ElevatorFullException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class HW2ElevatorSimulationTest {
    // Don't board any passengers. Just check that the elevator moves up and down correctly.
    @Test
    public void elevatorMotion()
    {
        Building building = new Building();
        Elevator elevator = building.elevator();
        int expectedFloorNumber = 1;
        // Go to the top
        while (expectedFloorNumber < Building.FLOORS) {
            checkElevator(elevator, expectedFloorNumber, 0);
            elevator.move();
            expectedFloorNumber++;
            checkElevator(elevator, expectedFloorNumber, 0);
        }
        assertEquals(Building.FLOORS, expectedFloorNumber);
        // Go back to the bottom
        while (expectedFloorNumber > 1) {
            checkElevator(elevator, expectedFloorNumber, 0);
            elevator.move();
            expectedFloorNumber--;
            checkElevator(elevator, expectedFloorNumber, 0);
        }
        assertEquals(1, expectedFloorNumber);
    }

    // Check that passengers get on and off correctly.
    @Test
    public void disembark() throws ElevatorFullException
    {
        Building building = new Building();
        Elevator elevator = building.elevator();
        checkElevator(elevator, 1, 0);
        // Add passengers and disembark them on the way up
        elevator.boardPassenger(3);
        elevator.boardPassenger(4);
        elevator.boardPassenger(4);
        elevator.boardPassenger(6);
        elevator.boardPassenger(6);
        elevator.boardPassenger(6);
        checkElevator(elevator, 1, 6);
        elevator.move();
        checkElevator(elevator, 2, 6);
        elevator.move();
        checkElevator(elevator, 3, 5);
        elevator.move();
        checkElevator(elevator, 4, 3);
        elevator.move();
        checkElevator(elevator, 5, 3);
        elevator.move();
        checkElevator(elevator, 6, 0);
        elevator.move();
        checkElevator(elevator, 7, 0);
    }

    // Check that passengers on higher floors can call and board the elevator, and then
    // disembark on the ground floor.
    @Test
    public void call()
    {
        Building building = new Building();
        Elevator elevator = building.elevator();
        building.floor(3).waitForElevator();
        building.floor(3).waitForElevator();
        building.floor(6).waitForElevator();
        building.floor(6).waitForElevator();
        building.floor(6).waitForElevator();
        checkElevator(elevator, 1, 0);
        elevator.move();
        checkElevator(elevator, 2, 0);
        elevator.move();
        checkElevator(elevator, 3, 2);
        elevator.move();
        checkElevator(elevator, 4, 2);
        elevator.move();
        checkElevator(elevator, 5, 2);
        elevator.move();
        checkElevator(elevator, 6, 5);
        elevator.move();
        checkElevator(elevator, 7, 5);
        elevator.move();
        checkElevator(elevator, 6, 5);
        elevator.move();
        checkElevator(elevator, 5, 5);
        elevator.move();
        checkElevator(elevator, 4, 5);
        elevator.move();
        checkElevator(elevator, 3, 5);
        elevator.move();
        checkElevator(elevator, 2, 5);
        elevator.move();
        checkElevator(elevator, 1, 0);
    }

    // Check handling of a full elevator.
    @Test
    public void elevatorFull()
    {
        Building building = new Building();
        Elevator elevator = building.elevator();
        // Have enough people waiting on the 4th floor to exceed elevator capacity by 50%
        int waiting = (int) (Elevator.CAPACITY * 1.5);
        for (int i = 0; i < waiting; i++) {
            building.floor(4).waitForElevator();
        }
        // Move to 4, checking state
        elevator.move();
        checkElevator(elevator, 2, 0);
        elevator.move();
        checkElevator(elevator, 3, 0);
        elevator.move();
        // Should have filled the elevator, leaving people on 4
        checkElevator(elevator, 4, Elevator.CAPACITY);
        assertEquals(waiting - Elevator.CAPACITY, building.floor(4).passengersWaiting());
        // Get to the ground floor
        while (elevator.currentFloor() != 1) {
            elevator.move();
            if (elevator.currentFloor() == 1) {
                checkElevator(elevator, elevator.currentFloor(), 0);
            } else {
                checkElevator(elevator, elevator.currentFloor(), Elevator.CAPACITY);
            }
        }
        // Go back to 4
        while (elevator.currentFloor() != 4) {
            elevator.move();
            if (elevator.currentFloor() == 4) {
                // Check to see that the remaining passengers boarded
                checkElevator(elevator, elevator.currentFloor(), waiting - Elevator.CAPACITY);
                assertEquals(0, building.floor(4).passengersWaiting());
            } else {
                checkElevator(elevator, elevator.currentFloor(), 0);
            }
        }
    }

    private void checkElevator(Elevator elevator, int floorNumber, int passengers)
    {
        assertEquals(floorNumber, elevator.currentFloor());
        assertEquals(passengers, elevator.passengers());
    }
}
