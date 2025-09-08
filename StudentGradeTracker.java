import java.util.ArrayList;  // Import is required

public class StudentGradeTracker {

    // Inner Student class
    static class Student {
        String name;
        ArrayList<Integer> marks;

        Student(String name) {
            this.name = name;
            this.marks = new ArrayList<Integer>();  // no wildcards
        }

        void addMark(int mark) {
            marks.add(mark);
        }

        int getTotal() {
            int total = 0;
            for (int m : marks) total += m;
            return total;
        }

        double getAverage() {
            if (marks.size() == 0) return 0;
            return (double) getTotal() / marks.size();
        }

        int getHighest() {
            int max = marks.get(0);
            for (int m : marks) if (m > max) max = m;
            return max;
        }

        int getLowest() {
            int min = marks.get(0);
            for (int m : marks) if (m < min) min = m;
            return min;
        }

        String getReport() {
            return "Name: " + name +
                   "\nMarks: " + marks +
                   "\nTotal: " + getTotal() +
                   "\nAverage: " + getAverage() +
                   "\nHighest: " + getHighest() +
                   "\nLowest: " + getLowest();
        }
    }

    // Program entry point
    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<Student>(); // ✅ Correct

        Student s1 = new Student("Alice");
        s1.addMark(85);
        s1.addMark(90);
        s1.addMark(78);

        Student s2 = new Student("Bob");
        s2.addMark(70);
        s2.addMark(65);
        s2.addMark(80);

        students.add(s1);
        students.add(s2);

        for (Student s : students) {
            System.out.println(s.getReport() + "\n");
        }
    }
}
