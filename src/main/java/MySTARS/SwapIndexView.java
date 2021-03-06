package MySTARS;

/**
 * Verifies a second {@link User} from {@link Database}.USERS and swaps the indices for both the Users.
 * @author Bhargav
 * @version 1.0
 * @since 2020-11-1
 */
public final class SwapIndexView extends View {
    
    /**
     * Required method from View. Prints prompts necessary to swap the indices of 2 Students.
     */
    public void print() {

        clearScreen("Student Main > Swap Index with Peer");

        while (true) {
            CourseManager.printCourseList(CourseStatus.REGISTERED, (Student) Database.CURRENT_USER);
            System.out.print(String.format("%-50s: ", "Enter the course code or Q to Quit"));
            String courseCode = Helper.readLine();
            if (courseCode.equals("Q")) {
                break;
            }
            Student currentUser = (Student) Database.CURRENT_USER;
            if (currentUser.getCourse(courseCode) != null) {
                Student secondUser = verifySecondUser(courseCode);
                if (secondUser == null) {
                    //User has entered "Q"
                    break;
                } else {
                    String currentUserIndexString = currentUser.getCourse(courseCode).getIndicesString()[0];
                    String secondUserIndexString = secondUser.getCourse(courseCode).getIndicesString()[0];
                    if (!currentUser.clashes(courseCode, secondUserIndexString) && !secondUser.clashes(courseCode, currentUserIndexString)) {
                        
                        CourseIndex tempIndex = currentUser.removeIndex(courseCode, currentUserIndexString);
                        secondUser.setIndex(courseCode, tempIndex);
                        tempIndex = secondUser.removeIndex(courseCode, secondUserIndexString);
                        currentUser.setIndex(courseCode, tempIndex);

                        Course course = Database.COURSES.get(courseCode);
                        CourseIndex currentUserIndex = course.getIndex(currentUserIndexString);
                        CourseIndex secondUserIndex = course.getIndex(secondUserIndexString);

                        Student tempStudent = currentUserIndex.removeStudent(currentUser.getUsername());
                        secondUserIndex.addStudent(tempStudent);
                        tempStudent = secondUserIndex.removeStudent(secondUser.getUsername());
                        currentUserIndex.addStudent(tempStudent);


                        Database.serialise(FileType.COURSES);
                        Database.serialise(FileType.USERS);
                        System.out.println("Swapped successfully.");
                        Helper.pause();
                        break;
                    } else {
                        System.out.println("There is a clash of index!");
                        Helper.pause();
                    }
                }
            } else {
                System.out.println("You are not registered in this course!");
                Helper.pause();
            }
        }
    }

    /**
     * For swapping index with a peer, this method checks if peer is a valid user that is registered for the relevant course.
     * @param courseCode The course code, e.g CZ2002
     * @return {@link Student} object for the second user
     */
    private Student verifySecondUser(String courseCode) {

        while (true) {
            System.out.print(String.format("%-50s: ", "Enter Second User's username or Q to quit"));
            String username = Helper.readLine();
            if (username.equals("Q")) {
                return null;
            }

            if (!Database.USERS.containsKey(username)) {
                System.out.println(username + " does not exist!");
                Helper.pause();
            } else {
                System.out.print(String.format("%-50s: ", "Enter password"));
                String password = Helper.getPasswordInput();
                User result = Database.USERS.get(username);
                if (result.checkPassword(password)) {
                    try {
                        Student secondUser = (Student) result;
                        Course course = secondUser.getCourse(courseCode);
                        if (course != null && course.getStatus() == CourseStatus.REGISTERED) {
                            return secondUser;
                        } else {
                            System.out.println("Second User is not registered for this course!");
                            Helper.pause();
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid user. Please enter again!");
                        Helper.pause();
                    }
                } else {
                    System.out.println("Invalid Password!");
                    Helper.pause();
                }
            }
        }
    }
}
