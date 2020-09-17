package view;

import db.EmployeeDB;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import models.Employee;
import util.HRManagerUtil;

public class View {

    private static final Scanner scanner = new Scanner(System.in);
    private static EmployeeDB db = new EmployeeDB();


    /**
     * Show the interface to add a employee to the db
     */
    public void showAddEmployee() {

        System.out.println("NEW USER\n\nplease enter prename:");
        String prename = scanner.nextLine();
        System.out.println("\nplease enter surname:");
        String surname = scanner.nextLine();
        Date birthdate = getDate("birthday");
        Date employmentDate = getDate("employment date");
        System.out.println("\nplease enter job description:");
        String jobDescription = scanner.nextLine();

        System.out.println("\nplease enter salary:");
        Double salary = scanner.nextDouble();
        Employee employee = new Employee(prename, surname, jobDescription, birthdate, salary, employmentDate);
        db.addEmployee(employee);
    }

    /**
     * Show the interface to edit a employee to the db
     */
    public void showEditEmployee() {
        String toEdit = "";
        Employee employee = selectEmployeeByUser();
        while (!toEdit.equals("X")) {
            System.out.println("please enter what you want to edit (x=exit, p=prename, su=surname, jd=jobDescription, bd=birthday,sa=salary, ed=employmentDate):");
            toEdit = scanner.nextLine();
            switch (toEdit.toLowerCase()) {
                case "x":
                    break;
                case "p":
                    employee.setPrename(scanner.nextLine());
                    break;
                case "su":
                    employee.setSurname(scanner.nextLine());
                    break;
                case "jd":
                    employee.setJobDescription(scanner.nextLine());
                    break;
                case "bd":
                    employee.setBirthdate(getDate("birthday"));
                    break;
                case "sa":
                    employee.setSalary(scanner.nextDouble());
                    break;
                case "ed":
                    employee.setEmploymentDate(getDate("employment day"));
                default:
                    System.out.println("please enter a valid input");
            }
        }
    }

    /**
     * Show the list of all employees from the db
     */
    public void showListEmployees() {
        outEmployees();
        System.out.println("average income: " + getAverageIncome());
        System.out.print("press enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the interface to delete a employee
     */
    public void showDeleteEmployee() {
        db.deleteEmployee(selectEmployeeByUser());
    }

    /**
     * Internal method to print out a employee
     *
     * @param employee to show
     */
    private void showEmployee(Employee employee) {
        System.out.println("id = " + outFormat(employee.getId(), 20) + "name = " + outFormat(new String(employee.getPrename() + " " + employee.getSurname()), 30) + "birthday = " + outFormat(HRManagerUtil.formatter.format(employee.getBirthdate()), 15) + "job description = " + outFormat(employee.getJobDescription(), 40) + "salary = " + outFormat(employee.getSalary() + "", 15) + "employment date = " + outFormat(HRManagerUtil.formatter.format(employee.getEmploymentDate()), 20));
    }

    private void outEmployees() {
        List<Employee> employeeList = db.getEmployeesBySallery();
        for (Employee employee : employeeList)
            showEmployee(employee);
    }

    private Employee selectEmployeeByUser() {
        Employee employee = null;
        while (employee == null) {
            String id = new String();
            outEmployees();
            System.out.println("\n\nenter valid id of User");
            id = scanner.nextLine();
            employee = selectEmployeeById(id);
        }
        return employee;
    }

    private Employee selectEmployeeById(String id) {
        for (Employee employee : db.getEmployees())
            if (employee.getId().equals(id))
                return employee;
        return null;
    }

    private Date getDate(String nameOfField) {
        Date date = null;
        while (date == null) {
            try {
                System.out.println("\nplease enter " + nameOfField + " (format dd.MM.yyyy):");
                date = HRManagerUtil.formatter.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.err.println("format not correct please retry");
            }
        }
        return date;
    }

    private Double getAverageIncome() {
        Double total = 0D;
        List<Employee> employeeList = db.getEmployees();
        for (Employee employee : employeeList)
            total += employee.getSalary();
        return total / employeeList.size();
    }

    private String outFormat(String in, Integer length) {
        return String.format("%-" + length + "s", in);
    }
}
