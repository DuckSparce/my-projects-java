package simpleCalendarViewer;
import java.time.*;
import java.time.format.*;
public class Dispatcher {
	public static void main(String[] args) {
		try{
			LocalDate date = LocalDate.parse("2022-05-22");
//			System.out.println(date.getDayOfWeek());
			View.showMonth(date, true);
		}catch(DateTimeParseException dtpe) {
			System.out.println("Oops... Wrong date)");
		}
	}
}
class View {
	static void showMonth(LocalDate date, boolean english) {
		System.out.println(date.getYear() + " - " + date.getMonth());
		int neededDay = date.getDayOfMonth();
		if(english) {
			System.out.println("Mon Tue Wed Thu Fri Sat Sun");
		}else {
			System.out.println("Ïí  Âò  Ñð  ×ò  Ïò  Ñá  Íä");
		}
		int monthLength = date.lengthOfMonth();
		LocalDate dateToPrint = LocalDate.parse(date.format(
				DateTimeFormatter.ofPattern("yyyy'-'MM'-'01")));
		int dayOfMonth = 1;
		switch(dateToPrint.getDayOfWeek()) {
		case MONDAY : System.out.print(" " + dayOfMonth + "  "); break;
		case TUESDAY : System.out.print("     " + dayOfMonth + "  "); break;
		case WEDNESDAY : System.out.print("         " + dayOfMonth + "  "); break;
		case THURSDAY : System.out.print("             " + dayOfMonth + "  "); break;
		case FRIDAY : System.out.print("                 " + dayOfMonth + "  "); break;
		case SATURDAY : System.out.print("                     " + dayOfMonth + "  "); break;
		case SUNDAY : System.out.println("                         " + dayOfMonth); break;
		default : System.out.print("ERROR"); 
		}
		dateToPrint = dateToPrint.plusDays(1);
		for(dayOfMonth = 2; dayOfMonth <= monthLength; dayOfMonth++) {
			dateToPrint = dateToPrint.plusDays(1);
			if(dayOfMonth < 10) {
				System.out.print(" ");
			}
			if(dateToPrint.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
				if(dayOfMonth != neededDay) {
					System.out.println(dayOfMonth);
				}else {
					System.out.println(dayOfMonth + "*");
				}
				continue;
			}
			if(dayOfMonth != neededDay) {
				System.out.print(dayOfMonth + "  ");
			}else {
				System.out.print(dayOfMonth + "* ");
			}
		}
	}
}
