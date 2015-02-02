package cscie55.hw3test;

import cscie55.hw3.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HW3ElevatorSimulationTest {
    // Don't board any passengers. Just check that the elevator moves up and down correctly.
    @Test
    public void elevatorMotion()
    {
        Building building = new Building();
        Elevator elevator = building.elevator();
        int expectedFloorNumber = 1;
        // Go to the top
        while (expectedFloorNumber < Building.FLOORS) {
            checkElevator(elevator, expectedFloorNumber);
            elevator.move();
            expectedFloorNumber++;
            checkElevator(elevator, expectedFloorNumber);
        }
        assertEquals(Building.FLOORS, expectedFloorNumber);
        // Go back to the bottom
        while (expectedFloorNumber > 1) {
            checkElevator(elevator, expectedFloorNumber);
            elevator.move();
            expectedFloorNumber--;
            checkElevator(elevator, expectedFloorNumber);
        }
        assertEquals(1, expectedFloorNumber);
    }

    // Check that passengers get on and off correctly.
    @Test
    public void disembark() throws ElevatorFullException
    {
        Building building = new Building();
        // There are six passengers.
        Passenger p1 = new Passenger(1);
        Passenger p2 = new Passenger(2);
        Passenger p3 = new Passenger(3);
        Passenger p4 = new Passenger(4);
        Passenger p5 = new Passenger(5);
        Passenger p6 = new Passenger(6);
        // They enter the building (and become resident on the ground floor)
        building.enter(p1);
        building.enter(p2);
        building.enter(p3);
        building.enter(p4);
        building.enter(p5);
        building.enter(p6);
        Floor groundFloor = building.floor(1);
        assertTrue(groundFloor.isResident(p1));
        assertTrue(groundFloor.isResident(p2));
        assertTrue(groundFloor.isResident(p3));
        assertTrue(groundFloor.isResident(p4));
        assertTrue(groundFloor.isResident(p5));
        assertTrue(groundFloor.isResident(p6));
        Elevator elevator = building.elevator();
        // Everyone wants to go up, to various floors.
        groundFloor.waitForElevator(p1, 3);
        groundFloor.waitForElevator(p2, 4);
        groundFloor.waitForElevator(p3, 4);
        groundFloor.waitForElevator(p4, 6);
        groundFloor.waitForElevator(p5, 6);
        groundFloor.waitForElevator(p6, 6);
        // No one is on the elevator yet
        checkElevator(elevator, 1);
        // The elevator goes up to the top and then down. It should stop on the ground floor and
        // board everyone.
        roundTrip(elevator);
        checkElevator(elevator, 1, p1, p2, p3, p4, p5, p6);
        elevator.move();
        checkElevator(elevator, 2, p1, p2, p3, p4, p5, p6);
        // p1 wanted to get off on 3
        elevator.move();
        checkElevator(elevator, 3, p2, p3, p4, p5, p6);
        // p2 and p3 wanted to get off on 4
        elevator.move();
        checkElevator(elevator, 4, p4, p5, p6);
        elevator.move();
        checkElevator(elevator, 5, p4, p5, p6);
        // Remaining passengers get off on 6
        elevator.move();
        checkElevator(elevator, 6);
        elevator.move();
        checkElevator(elevator, 7);
        // Check that everyone is where they should be
        assertTrue(building.floor(3).isResident(p1));
        assertTrue(building.floor(4).isResident(p2));
        assertTrue(building.floor(4).isResident(p3));
        assertTrue(building.floor(6).isResident(p4));
        assertTrue(building.floor(6).isResident(p5));
        assertTrue(building.floor(6).isResident(p6));
    }

    // Check that passengers on higher floors can call and board the elevator, and then
    // disembark on the ground floor.
    @Test
    public void call() throws ElevatorFullException
    {
        Building building = new Building();
        // There are five passengers
        Passenger p1 = new Passenger(1);
        Passenger p2 = new Passenger(2);
        Passenger p3 = new Passenger(3);
        Passenger p4 = new Passenger(4);
        Passenger p5 = new Passenger(5);
        // They enter the building (and become resident on the ground floor)
        building.enter(p1);
        building.enter(p2);
        building.enter(p3);
        building.enter(p4);
        building.enter(p5);
        Floor groundFloor = building.floor(1);
        assertTrue(groundFloor.isResident(p1));
        assertTrue(groundFloor.isResident(p2));
        assertTrue(groundFloor.isResident(p3));
        assertTrue(groundFloor.isResident(p4));
        assertTrue(groundFloor.isResident(p5));
        Elevator elevator = building.elevator();
        // p1, p2 go to floor 3; p3, p4, p5 go to 6.
        groundFloor.waitForElevator(p1, 3);
        groundFloor.waitForElevator(p2, 3);
        groundFloor.waitForElevator(p3, 6);
        groundFloor.waitForElevator(p4, 6);
        groundFloor.waitForElevator(p5, 6);
        roundTrip(elevator); // Now they enter the elevator
        roundTrip(elevator); // Get them to their floors
        assertTrue(building.floor(3).isResident(p1));
        assertTrue(building.floor(3).isResident(p2));
        assertTrue(building.floor(6).isResident(p3));
        assertTrue(building.floor(6).isResident(p4));
        assertTrue(building.floor(6).isResident(p5));
        // p1 wants to go up (3 -> 5)
        // p2 wants to go down (3 -> 1)
        // p3 wants to go down (6 -> 1)
        building.floor(3).waitForElevator(p1, 5);
        building.floor(3).waitForElevator(p2, 1);
        building.floor(6).waitForElevator(p3, 1);
        // The passengers moving are no longer resident (they are waiting for the elevator)
        assertTrue(!building.floor(3).isResident(p1));
        assertTrue(!building.floor(3).isResident(p2));
        assertTrue(!building.floor(6).isResident(p3));
        assertTrue(building.floor(6).isResident(p4));
        assertTrue(building.floor(6).isResident(p5));
        // Start moving up
        elevator.move();
        checkElevator(elevator, 2);
        elevator.move();
        checkElevator(elevator, 3, p1); // p2 wants to go down but elevator is going up
        elevator.move();
        checkElevator(elevator, 4, p1);
        elevator.move();
        checkElevator(elevator, 5);
        elevator.move();
        checkElevator(elevator, 6); // p3 doesn't board, wants to go down
        elevator.move();
        checkElevator(elevator, 7);
        elevator.move();
        checkElevator(elevator, 6, p3);
        elevator.move();
        checkElevator(elevator, 5, p3);
        elevator.move();
        checkElevator(elevator, 4, p3);
        elevator.move();
        checkElevator(elevator, 3, p2, p3);
        elevator.move();
        checkElevator(elevator, 2, p2, p3);
        elevator.move();
        checkElevator(elevator, 1);
        assertTrue(building.floor(5).isResident(p1));
        assertTrue(building.floor(1).isResident(p2));
        assertTrue(building.floor(1).isResident(p3));
        assertTrue(building.floor(6).isResident(p4));
        assertTrue(building.floor(6).isResident(p5));
    }

    // Check handling of a full elevator.
    @Test
    public void elevatorFull() throws ElevatorFullException
    {
        Building building = new Building();
        // Create 15 passengers. (Capacity is 10, so not everyone will fit).
        // They all want to go to 4.
        Floor groundFloor = building.floor(1);
        assertEquals(10, Elevator.CAPACITY);
        final int PASSENGERS = 15;
        Passenger[] p = new Passenger[PASSENGERS];
        for (int id = 0; id < PASSENGERS; id++) {
            p[id] = new Passenger(id);
            building.enter(p[id]);
            groundFloor.waitForElevator(p[id], 4);
        }
        // Load to elevator capacity
        Elevator elevator = building.elevator();
        roundTrip(elevator); // Passengers board after elevator GOES to first floor.
                             // Starting on the ground floor won't do it.
        checkElevator(elevator, 1, p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8], p[9]);
        // After a round trip (1 -> 7 -> 1), the first passengers who boarded should be on 4,
        // and the elevator should have the remaining passengers.
        roundTrip(elevator);
        Floor floor4 = building.floor(4);
        checkElevator(elevator, 1, p[10], p[11], p[12], p[13], p[14]);
        assertTrue(floor4.isResident(p[0]));
        assertTrue(floor4.isResident(p[1]));
        assertTrue(floor4.isResident(p[2]));
        assertTrue(floor4.isResident(p[3]));
        assertTrue(floor4.isResident(p[4]));
        assertTrue(floor4.isResident(p[5]));
        assertTrue(floor4.isResident(p[6]));
        assertTrue(floor4.isResident(p[7]));
        assertTrue(floor4.isResident(p[8]));
        assertTrue(floor4.isResident(p[9]));
        // After one more round trip, everyone should be on 4.
        roundTrip(elevator);
        checkElevator(elevator, 1);
        assertTrue(floor4.isResident(p[0]));
        assertTrue(floor4.isResident(p[1]));
        assertTrue(floor4.isResident(p[2]));
        assertTrue(floor4.isResident(p[3]));
        assertTrue(floor4.isResident(p[4]));
        assertTrue(floor4.isResident(p[5]));
        assertTrue(floor4.isResident(p[6]));
        assertTrue(floor4.isResident(p[7]));
        assertTrue(floor4.isResident(p[8]));
        assertTrue(floor4.isResident(p[9]));
        assertTrue(floor4.isResident(p[10]));
        assertTrue(floor4.isResident(p[11]));
        assertTrue(floor4.isResident(p[12]));
        assertTrue(floor4.isResident(p[13]));
        assertTrue(floor4.isResident(p[14]));
    }

    private void roundTrip(Elevator elevator)
    {
        assert elevator.currentFloor() == 1;
        while (elevator.currentFloor() < Building.FLOORS) {
            elevator.move();
        }
        while (elevator.currentFloor() > 1) {
            elevator.move();
        }
    }

    private void checkElevator(Elevator elevator, int floorNumber, Passenger ... expectedPassengers)
    {
        assertEquals(floorNumber, elevator.currentFloor());
        assertEquals(new HashSet<Passenger>(Arrays.asList(expectedPassengers)),
                     elevator.passengers());
    }
}
