package taskForFinal;
import java.util.*;
public class Dispatcher {
	public static void main(String[] args) {
		// StudyYear is begun by default, but you can end/begin it with methods
		// For Objects : getters and setters
		// For Lists : getters, setters and adders(adds new object to the end)
		System.out.println(StudyYear.isStudyYearEnded);
		
		final Student S0 = new Student("Mary", new ArrayList<Integer>(Arrays.asList(10, 2, 7, 8, 11, 9)));
		final Student S1 = new Student("John", new ArrayList<Integer>(Arrays.asList(9, 4, 6, 7, 3)));
		final Student S2 = new Student("Nick", new ArrayList<Integer>(Arrays.asList(10, 9, 9, 11)));
		final Student S3 = new Student("Billie", new ArrayList<Integer>(Arrays.asList(12, 11, 7, 10, 7, 8, 9)));
		final Student S4 = new Student("Micheal", new ArrayList<Integer>(Arrays.asList(6, 5, 7, 8, 10, 11, 4)));
		
		final Group G = new Group(new ArrayList<Student>(Arrays.asList(S0, S1, S2, S3, S4)));
		
		System.out.print(G);
		// Here you can change everything
		G.setStudent(3, new Student("Ann", new ArrayList<Integer>(Arrays.asList(10, 10, 3, 9, 5, 12, 3))));
		System.out.println(G.getStudent(3));
		G.getStudent(1).addMark(11);
		System.out.println(G.getStudent(1));
/*		G.setList(new ArrayList<Student>(Arrays.asList(
				new Student("Gary", new ArrayList<Integer>(Arrays.asList(12, 11, 7, 10, 7, 8, 9))),
				new Student("Steph", new ArrayList<Integer>(Arrays.asList(9, 4, 6, 7, 3))),
				new Student("Mary", new ArrayList<Integer>(Arrays.asList(9, 8, 4, 7, 3, 9))))));
		System.out.print(G);
*/		
		////////// END OF THE YEAR //////////
		
		G.setList(Collections.unmodifiableList(G.getStudents()));
		StudyYear.endStudyYear();
		System.out.println(StudyYear.isStudyYearEnded);
		// But now you can't
		G.setList(new ArrayList<Student>(Arrays.asList(
				new Student("Gary", new ArrayList<Integer>(Arrays.asList(12, 11, 7, 10, 7, 8, 9))),
				new Student("Steph", new ArrayList<Integer>(Arrays.asList(9, 4, 6, 7, 3))) )));
		G.setStudent(2, new Student("Max", new ArrayList<Integer>(Arrays.asList(2, 8, 12, 11, 10, 7, 8))));
		G.getStudent(2).setMark(1, 1);
	}
}
class Student{
	private String name;
	private ArrayList<Integer> marks;
	Student(String name, ArrayList<Integer> marks){
		this.name = name;
		this.marks = marks;
	}
	
	public String toString() {
		return this.name + " : " + marks;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String newName) {
		if(!StudyYear.isStudyYearEnded) {
			System.out.println(this.name + "'s new name is " + newName);
			this.name = newName;
		}else {
			System.out.println("Have no access.");
		}
	}
	
	public ArrayList<Integer> getMarks() {
		return this.marks;
	}
	public Integer getMark(int index) {
		return this.marks.get(index);
	}
	public void addMark(int newMark) {
		if(!StudyYear.isStudyYearEnded) {
			System.out.println(this.name + " got a new mark: " + newMark + ".");
			this.marks.add(newMark);
		}else {
			System.out.println("Have no access.");
		}
	}
	final void setMark(int index, int newMark) {
		if(!StudyYear.isStudyYearEnded) {
			System.out.println(this.name + "'s mark on " + (index + 1) + " place has been replaced by " + newMark);
			this.marks.set(index, newMark);
		}else {
			System.out.println("Have no access.");
		}
	}
	
}
class Group{
	private List<Student> students;
	Group(List<Student> students){
		this.students = students;
	}
	public String toString() {
		String s = "";
		for(Student temp : this.students){
			s += temp.toString() + System.lineSeparator();
		}
		return s;
	}
	final public List<Student> getStudents() {
		return students;
	}
	final public Student getStudent(int index) {
		return students.get(index);
	}
	final void addStudent(Student newStudent) {
		if(!StudyYear.isStudyYearEnded) {
			System.out.println(newStudent.getName() + " is a new student.");
			this.students.add(newStudent);
		}else {
			System.out.println("Have no access.");
		}
	}
	final void setStudent(int index, Student student) {
		if(!StudyYear.isStudyYearEnded) {
			System.out.println(student.getName() + " has replaced " + students.get(index).getName() + ".");
			this.students.set(index, student);
		}else {
			System.out.println("Have no access.");
		}
	}
	final void setList(List<Student> list) {
		if(!StudyYear.isStudyYearEnded) {
			System.out.println("Group changed to another.");
			this.students = list;
		}else {
			System.out.println("Have no access.");
		}
	}
}
class StudyYear{
	static boolean isStudyYearEnded = false;
	static void beginStudyYear() {
		System.out.println("StudyYear has begun!");
		StudyYear.isStudyYearEnded = false;
	}
	static void endStudyYear() {
		System.out.println("StudyYear has ended!");
		StudyYear.isStudyYearEnded = true;
	}
}
