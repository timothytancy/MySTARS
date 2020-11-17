package MySTARS;

/**
 * Create new {@link Course} object and fills in necessary information.
 * @author Bhargav
 * @version 1.0
 * @since 2020-11-1
 */
public final class AddCourseView extends View {

    /**
     * Required method from View.
     */
    public void print() {
        
        if (Database.CURRENT_ACCESS_LEVEL == AccessLevel.ADMIN) {
            addAdminCourse();
        } else if (Database.CURRENT_ACCESS_LEVEL == AccessLevel.STUDENT) {
            addStudentCourse();
        } else {
            System.out.println("Invalid AddCourse option.");
        }
    }

    /**
     * Prints prompts for the User to key in so that the relevant information for a new {@link Course} is keyed in. 
     */
    private void addAdminCourse() {
        
        clearScreen("Admin Main > Add Course");

        while (true) {
            System.out.print(String.format("%-50s: ", "Enter course code or Q to quit"));
            String courseCode = Helper.readLine();
            if (courseCode.equals("Q")) {
                break;
            }

            if (Helper.checkCourseCodeFormat(courseCode)) {
                if (!Database.COURSES.containsKey(courseCode)) {
                    try {
                        
                        String courseName;
                        while (true) {
                            System.out.print(String.format("%-50s: ", "Enter course name"));
                            courseName = Helper.readLine();
                            if (courseName.length() <= 50) {
                                break;
                            } else {
                                System.out.println("Course Name is too long!");
                            }
                        }

                        AU acadUnits;
                        while (true) {
                            try {
                                System.out.print(String.format("%-50s: ", "Enter no. of AUs"));
                                int au = Integer.parseInt(Helper.readLine());
                                if (au < 1 || au > 4) {
                                    throw new Exception();
                                }
                                acadUnits = AU.getAU(au);
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter valid number.");
                            }
                        }
    
                        System.out.print(String.format("%-50s: ", "Enter course description"));
                        String description = Helper.readLine();
    
                        Course course = new Course(courseCode, courseName, acadUnits, description);
    
                        int numIndices;
                        while (true) {
                            try {
                                System.out.print(String.format("%-50s: ", "Enter the number of indices (1 - 10)"));
                                numIndices = Integer.parseInt(Helper.readLine());
                                if (numIndices < 1 || numIndices > 10) {
                                    throw new Exception();
                                }
                                break;
                            } catch (Exception e) {
                                System.out.println("Please enter valid number.");
                            }
                        }

                        Helper.printSmallSpace();

                        course.addIndices(numIndices);
                        
                        Database.COURSES.put(courseCode, course);
                        Database.serialise(FileType.COURSES);
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                } else {
                    System.out.println("Course already exists!");
                }
            } else {
                System.out.println("Invalid course code format");
            }
        }
    }

    /**
     * Method to add the {@link User} to a {@link Course} of the User's choice.
     */
    private void addStudentCourse() {

        clearScreen("Student Main > Add Course");
        
        Student currentUser = (Student) Database.CURRENT_USER;
        while (true) {
            CourseManager.printCourseList(CourseStatus.NONE);
            System.out.print(String.format("%-50s: ", "Enter the course code or Q to quit"));
            String courseCode = Helper.readLine();
            if (courseCode.equals("Q")) {
                break;
            }

            Course course = Database.COURSES.get(courseCode);
            if (course != null) {
                Course studentCourse = currentUser.getCourse(courseCode);
                if (studentCourse != null){
                    if (studentCourse.getStatus() == CourseStatus.REGISTERED) {
                        System.out.println("You are already registered in this course!");
                        break;
                    } else if (studentCourse.getStatus() == CourseStatus.WAITLIST) {
                        System.out.println("You are already on the waitlist for this course!");
                        break;
                    }
                }

                Helper.printSmallSpace();

                CourseManager.printIndexList(course, true);
                System.out.print(String.format("%-50s: ", "Enter the course index or Q to quit"));
                String courseIndex = Helper.readLine();
                if (courseIndex.equals("Q")) {
                    break;
                }

                CourseIndex index = course.getIndex(courseIndex);
                if (index != null) {
                    Helper.printSmallSpace();
                    CourseManager.printLesson(index);
                    System.out.print(String.format("%-50s: ", "Add these lesson timings to your timetable? y/n"));
                    String answer = Helper.readLine();

                    if (answer.equals("Y")) {
                        try {
                            currentUser.addCourse(courseCode,courseIndex);
                            Database.serialise(FileType.USERS);
                            Database.serialise(FileType.COURSES);
                            System.out.println(courseCode + " has been added successfully.");
                            Helper.pause();
                            break;
                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                            Helper.pause();
                        }
                    } else {
                        Helper.printMediumSpace();
                    }
                } else {
                    System.out.println("The course index that you have entered is invalid!");
                    Helper.pause();
                }
            } else {
                System.out.println("The course code you entered is invalid!");
                Helper.pause();
            }
        }
    }
}
