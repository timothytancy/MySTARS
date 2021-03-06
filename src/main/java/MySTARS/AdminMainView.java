package MySTARS;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * The main menu for the Student side of the Application.
 * @author Bhargav
 * @version 1.0
 * @since 2020-11-1
 */
public final class AdminMainView extends View {


    /**
     * Prints menu options for the User to select from. Required method from {@link View}.
     */
    public void print() {
        
        int choice;
        do {
            try {

                clearScreen("Admin Main");

                System.out.println("What would you like to do?");
                System.out.println("1: Add course to MySTARS"); 
                System.out.println("2: Update course in MySTARS");
                System.out.println("3: Check Course Vacanices");
                System.out.println("4: Print Students list"); 
                System.out.println("5: Change student's entry timing to MySTARS"); 
                System.out.println("6: Add new user to MySTARS");
                System.out.println("7: Change Password");
                System.out.println("8: Logout");
                System.out.print(String.format("%-50s: ", "Choice"));
                choice = Integer.parseInt(Helper.readLine());

                switch (choice) { 

                    case 1:
                        AddCourseView addCourseView = new AddCourseView();
                        addCourseView.print();
                        break;
                    case 2:
                        UpdateCourseView updateCourseView = new UpdateCourseView();
                        updateCourseView.print();
                        break;
                    case 3:
                        VacanciesView vacanciesView = new VacanciesView();
                        vacanciesView.print();
                        break;
                    case 4:
                        PrintStudentListView printStudentListView = new PrintStudentListView();
                        printStudentListView.print();
                        break;
                    case 5:
                        changeEntryTiming();
                        break;
                    case 6:
                        AddUserView addUserView = new AddUserView();
                        addUserView.print();
                        break;
                    case 7:
                        changePassword();
                        break;
                    case 8:
                        LogoutView logoutView = new LogoutView();
                        logoutView.print();
                        return;
                    default:
                        System.out.println("Please enter valid option.");
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        } while (true);
    }

    /**
     * Changes MySTARS access start and end dates and times.
     */
    private void changeEntryTiming() {
        
        System.out.println("Changing entry timing for Students");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime startTime, endTime;

        while (true) {
            while (true) {
                try {
                    System.out.print("Enter the start date and time in the format (dd/MM/yyyy HH:mm:ss): ");
                    startTime = formatter.parseDateTime(Helper.readLine());
                    if (startTime.isAfter(DateTime.now().minusYears(1))) {
                        break;
                    } else {
                        System.out.println("Please enter a valid date and time");
                    }
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
            while (true) {
                try {
                    System.out.print("Enter the end date and time in the format (dd/MM/yyyy HH:mm:ss): ");
                    endTime = formatter.parseDateTime(Helper.readLine());
                    if (endTime.isBefore(DateTime.now().plusYears(1))) {
                        break;
                    } else {
                        System.out.println("Please enter a valid date and time");
                    }
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
            try {
                LoginView.setLoginTime(startTime, endTime);
                System.out.println("Entry time updated succesfully.");
                Helper.pause();
                break;
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                Helper.pause();
            }
        }
    }

    /**
     * Changes the Password for the {@link Database#CURRENT_USER}.
     */
    private void changePassword() {

        Database.CURRENT_USER.changePassword();
        Helper.pause();
    }
}
