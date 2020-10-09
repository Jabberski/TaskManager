package pl.coderslab;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class TaskManager {

    public static void main(String[] args) {
        String[][] tasks =loadFile();
        int i=0;
        while(i!=4){
            i=menu();
            switch (i){
                case 1:
                    add();
                    tasks =loadFile();
                    break;
                case 2:
                    remove(tasks);
                    tasks =loadFile();
                    break;
                case 3:
                    list(tasks);
                    break;
                case 4:
                    System.out.println(ConsoleColors.RED+"See you!");
                default:
            }
        }
    }


    public static String[][] loadFile(){
        Path path = Paths.get("tasks.csv");
        List<String> list = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(path)) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] tasks = list.toArray(new String[0]);
        String[][] tasksSeparated = new String[tasks.length][3];
        for (int i = 0; i < tasks.length; i++) {
            tasksSeparated[i]= tasks[i].split(",");
        }
        return tasksSeparated;
    }


    public static int menu(){
        Scanner scan = new Scanner(System.in);
        int option = 0;
        System.out.println(ConsoleColors.BLUE +"Please select an option: ");
        System.out.println("1. Add new task");
        System.out.println("2. Remove task");
        System.out.println("3. List of all tasks");
        System.out.println("4. Quit task manager");
        while(true){
            while (!scan.hasNextInt()) {
                scan.nextLine();
                System.out.println("Incorrect choice, please pick number from 1 to 4:");
            }
            option = scan.nextInt();
            if(option>0 && option <5) {
                break;
            }
            System.out.println("Incorrect choice, please pick number from 1 to 4:");
        }
        return option;
    }

    public  static  void add() {
        Scanner scan = new Scanner(System.in);
        try (FileWriter fileWriter = new FileWriter("tasks.csv", true)) {
            System.out.println("Please add task description: ");
            fileWriter.append(scan.nextLine()+", ");
            System.out.println("Please add task due date: ");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            boolean date = false;
            while(date == false) {
                try {
                    String dueDate = scan.nextLine();
                    dateFormat.parse(dueDate);
                    date=true;
                    fileWriter.append(dueDate+", ");
                } catch (ParseException ex) {
                    System.out.println("invalid date, use format YYYY-MM-DD");
                }
            }
            System.out.println("Is your task important? true/false: ");
            while (!scan.hasNextBoolean()) {
                scan.nextLine();
                System.out.println("Incorrect choice, please pick true or false:");
            }
            fileWriter.append(scan.next()+"\n");
            System.out.println(ConsoleColors.RED+"Task added");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void list(String[][] taskList){
        for (int i = 0; i < taskList.length; i++) {
            System.out.print((i+1)+". ");
            for (int j = 0; j <taskList[i].length-1 ; j++) {
                System.out.print(taskList[i][j]+", ");
            }
            System.out.print(taskList[i][taskList[i].length-1]+"\n");
        }
    }


    public static void remove(String[][] taskList){
        Scanner scan = new Scanner(System.in);
        System.out.println("Please select position of task to remove from the list: ");
        list(taskList);
        int toRemove=0;
        while(true){
            while (!scan.hasNextInt()) {
                scan.nextLine();
                System.out.println("Incorrect choice, please pick number from 1 to "+taskList.length+": ");
            }
            toRemove = scan.nextInt();
            if(toRemove>0 && toRemove <= taskList.length) {
                break;
            }
            System.out.println("Incorrect choice, please pick number from 1 to "+taskList.length+": ");
        }
        toRemove--;
        try (FileWriter fileWriter = new FileWriter("tasks.csv", false)) {
            for (int i = 0; i < taskList.length; i++) {
                if (i != toRemove) {
                    for (int j = 0; j < 2; j++) {
                        fileWriter.append(taskList[i][j]+", ");
                    }
                    fileWriter.append(taskList[i][2]+"\n");
                }
            }
            System.out.println(ConsoleColors.RED+"Task removed");
        }catch (IOException ex) {
            ex.getMessage();
        }

    }
}
