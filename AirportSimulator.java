package taskForThreadsFinal;

// It's a simple airport simulator, where I tried to use threads. User adds planes and calls one of two methods from Controller(parallel or notParallel).
// They both do the same. Planes have some number of families(1-4 members, name from 2 letters("aa")) which would like to visit certain city, but
// exactly 100 members. Program separates them by buses(6-8 passengersCapacity) in the way that every family has their bus(members are not separated). 

import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;

public class Dispatcher {
	
	public static void main(String[] args) {

		Plane plane1 = new Plane(1, new ArrayList<Family>());
		plane1.setRandomFamilies("a`");
		Plane plane2 = new Plane(2, new ArrayList<Family>());
		plane2.setRandomFamilies(plane1.getFamilies().get(plane1.getFamilies().size() - 1).getName());
		Plane plane3 = new Plane(3, new ArrayList<Family>());
		plane3.setRandomFamilies(plane2.getFamilies().get(plane2.getFamilies().size() - 1).getName());
		System.out.println(plane1);
		System.out.println(plane2);
		System.out.println(plane3);

		ConcurrentHashMap<City, CopyOnWriteArrayList<Bus>> buses = 
				               AirportController.separateAllFamilies_Parallel(plane1, plane2, plane3);
//		ConcurrentHashMap<City, CopyOnWriteArrayList<Bus>> buses = 
//		                       AirportController.separateAllFamilies_NotParallel(plane1, plane2, plane3);

		System.out.println(buses);
	}
	
}

class AirportController {
	
	private static ConcurrentHashMap<City, CopyOnWriteArrayList<Bus>> busStation = new ConcurrentHashMap<>();

	private static void makeNewStation() {
		busStation.clear();
		busStation.put(City.KALUSH, new CopyOnWriteArrayList<Bus>());
		busStation.put(City.KOSIV, new CopyOnWriteArrayList<Bus>());
		busStation.put(City.GALYCH, new CopyOnWriteArrayList<Bus>());
		busStation.put(City.KOLOMIYA, new CopyOnWriteArrayList<Bus>());
	}

	public static ConcurrentHashMap<City, CopyOnWriteArrayList<Bus>> separateAllFamilies_Parallel(Plane... planes) {
		AirportController.makeNewStation();
		long startTimeNanos = System.nanoTime();
		ArrayList<Thread> threadsList = new ArrayList<>();
		for (Plane p : planes) {
			threadsList.add(new Thread(p, "" + p.getID()));
		}
		for (Thread temp : threadsList) {
			temp.start();
		}
		try {
			for (Thread temp : threadsList) {
				temp.join();
			}
		} catch (InterruptedException ie) {
			System.out.println("Error! Some thread was interrupted.");
		}

		System.out.println("Execution time : " + (System.nanoTime() - startTimeNanos) + " nanos.");
		return busStation;
	}

	public static ConcurrentHashMap<City, CopyOnWriteArrayList<Bus>> separateAllFamilies_NotParallel(Plane... planes) {
		AirportController.makeNewStation();
		long startTimeNanos = System.nanoTime();
		for (Plane p : planes) {
			System.out.println("Service of plane with ID " + p.getID() + " is started.");

			for (Family f : p.getFamilies()) {
				boardAFamily.accept(f);
			}
			p.freePassengers();

			System.out.println("Service of plane with ID " + p.getID() + " is finished.");
		}

		System.out.println(System.nanoTime() - startTimeNanos);
		return busStation;
	}

	public static Function<City, Bus> callNewBus = (city) -> {
		Bus newBus = new Bus(city, (int) (Math.random() * 3 + 6));
		busStation.get(city).add(newBus);
		return newBus;
	};

	public static Consumer<Family> boardAFamily = (f) -> {
		int members = f.getCount();
		for (Bus b : busStation.get(f.getTravelTo())) {
			synchronized (b) {
				if (b.getPassengersCount() + members - 1 < b.getPassengersCapacity()) {
					b.addPassengers(members);
					return;
				}
			}
		}
		callNewBus.apply(f.getTravelTo()).addPassengers(members);
	};

}

class Plane implements Runnable {
	
	private int id;                              // 1, 2, or 3
	private ArrayList<Family> families;          // exactly 100 members

	Plane(int id, ArrayList<Family> families) {
		this.id = id;
		this.families = families;
	}

	Plane() {}

	public int getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public ArrayList<Family> getFamilies() {
		return this.families;
	}

	public void freePassengers() {
		this.families.clear();
	}

	public void setFamilies(ArrayList<Family> families) {
		this.families = families;
	}

	public void setRandomFamilies(String lastUsedFamilyName) {
		if (lastUsedFamilyName.length() != 2) {
			System.out.println("Wrong family name set for plane with id " + this.id);
			return;
		}
		int allMembers = 0;
		int familyMembers = 0;
		char firstChar = lastUsedFamilyName.charAt(0);
		char secondChar = lastUsedFamilyName.charAt(1);
		String name = null;
		while (allMembers < 97) {
			if (secondChar != 'z') {
				secondChar += 1;
			} else {
				firstChar += 1;
				secondChar = 'a';
			}
			familyMembers = (int) (Math.random() * 4 + 1);
			name = Character.toString(firstChar) + Character.toString(secondChar);
			this.families.add(new Family(name, City.getRandom(), familyMembers));
			allMembers += familyMembers;
		}
		if (secondChar != 'z') {
			secondChar += 1;
		} else {
			firstChar += 1;
			secondChar = 'a';
		}
		name = Character.toString(firstChar) + Character.toString(secondChar);
		this.families.add(new Family(name, City.getRandom(), 100 - allMembers));
	}

	@Override
	public String toString() {
		return "Plane(ID) " + this.id + " : " + this.families;
	}

	@Override
	public void run() {
		System.out.println("Service of plane with ID " + this.id + " is started.");

		for (Family f : this.families) {
			AirportController.boardAFamily.accept(f);
		}
		this.freePassengers();

		System.out.println("Service of plane with ID " + this.id + " is finished.");
	}
	
}

class Bus {
	
	private City driveTo;                        // Enum - Kalush Kosiv Galych Kolomiya
	private int passengersCapacity;              // from 6 to 8
	private int passengersCount;

	Bus(City driveTo, int passengersCapacity) {
		this.driveTo = driveTo;
		this.passengersCapacity = passengersCapacity;
	}

	Bus() {
	}

	public City getDriveTo() {
		return this.driveTo;
	}

	public void setDriveTo(City driveTo) {
		this.driveTo = driveTo;
	}

	public int getPassengersCapacity() {
		return this.passengersCapacity;
	}

	public int getPassengersCount() {
		return this.passengersCount;
	}

	public void addPassengers(int numberOfPassengers) {
		this.passengersCount += numberOfPassengers;
	}

	@Override
	public String toString() {
		return this.driveTo + " : " + this.passengersCount + " from " + this.passengersCapacity;
	}
	
}

class Family {
	
	private String name;                         // "aa", "ab", ... ,"zz"
	private City travelTo;                       // Enum - Kalush Kosiv Galych Kolomiya
	private int count;                           // from 1 to 4
	
	Family(String name, City travelTo, int count) {
		this.name = name;
		this.travelTo = travelTo;
		this.count = count;
	}
	
	Family() {}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public City getTravelTo() {
		return this.travelTo;
	}

	public void setTravelTo(City travelTo) {
		this.travelTo = travelTo;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return this.name + " : " + this.travelTo + " " + this.count;
	}
	
}

enum City {
	
	KALUSH, KOSIV, GALYCH, KOLOMIYA;

	private static final ArrayList<City> CITIES = new ArrayList<>(Arrays.asList(City.values()));
	private static final int SIZE = CITIES.size();
	private static final Random R = new Random();

	public static City getRandom() {
		return CITIES.get(R.nextInt(SIZE));
	}
	
}
