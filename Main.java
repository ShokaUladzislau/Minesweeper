package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        char[][] array = new char[11][11];
        char[][] marks = new char[11][11];
        boolean[][] printField = new boolean[11][11];
        int markCount = 0;
        int n = 0;
        boolean minesSet = false;

        initialize(array, marks, printField);

        System.out.println("How many mines do you want on the field?");
        n = Integer.parseInt(scanner.nextLine());
        printEmptyField();


        while (true) {
            System.out.println("Set/unset mines marks or claim a cell as free: ");
            String[] input = scanner.nextLine().split(" ");
            if(input.length != 3) {
                System.out.println("bad input");
                continue;
            }
            int a = Integer.parseInt(input[0]);
            int b = Integer.parseInt(input[1]);

            if (a > 9 || a < 1 || b > 9 || b < 1) {
                System.out.println("Indexes out of range");
                continue;
            }

            if (input[2].equals("free")) {

                if(!minesSet) {
                    setMines(array, n, a, b);
                    minesSet = true;
                }
                updatePrintFields(array, printField, marks, a, b);
            }

            if (input[2].equals("mine")) {
                if (marks[b][a] == '*') {
                    marks[b][a] = '.';
                    markCount--;
                    printField[b][a] = false;
                }
                else {
                    marks[b][a] = '*';
                    markCount++;
                    printField[b][a] = true;
                }
            }

            printField(array, marks, printField, false);
            if ((markCount == n && match(array, marks)) || cleared(array, printField)) {
                System.out.println("Congratulations! You found all mines!");
                break;
            }
        }


    }

    public static boolean cleared(char[][] array, boolean[][] printField) {

        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[i].length; j++) {
                if (array[i][j] == '.' && !printField[i][j] )
                    return false;
            }
        }
        return true;
    }

    public static void initialize(char[][] array, char[][] marks, boolean[][] printField) {

        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[i].length; j++) {
                array[i][j] = '.';
                marks[i][j] = '.';
                printField[i][j] = false;
            }
        }
    }

    public static void updatePrintFields(char[][] array, boolean[][] printField, char[][] marks, int a, int b) {

        if (printField[b][a]) {
            if (marks[b][a] == '*' && (array[b][a] == '.' || (array[b][a] >= '0' && array[b][a] <= '9'))) {
                marks[b][a] = '.';
            }
            else
                return;
        }

        if (array[b][a] == 'X') {
            printField(array, new char[11][11], printField, true);
            System.out.println("You stepped on a mine and failed!");
            System.exit(0);
        }
        else {
            printField[b][a] = true;
            if (array[b][a] >= '1' && array[b][a] <= '8')
                return;
            else {
                if (b < 9)
                    updatePrintFields(array, printField, marks, a, b + 1);
                if (a < 9)
                    updatePrintFields(array, printField, marks, a + 1, b);
                if (a < 9 && b < 9)
                    updatePrintFields(array, printField, marks, a + 1, b + 1);
                if (b > 1)
                    updatePrintFields(array, printField,  marks, a, b - 1);
                if (a > 1)
                    updatePrintFields(array, printField,  marks, a - 1, b);
                if (a > 1 && b > 1)
                    updatePrintFields(array, printField,  marks, a - 1, b - 1);
                if (a > 1 && b < 9)
                    updatePrintFields(array, printField,  marks, a - 1, b + 1);
                if (a < 9 && b > 1)
                    updatePrintFields(array, printField,  marks, a + 1, b - 1);
            }
        }

    }

    public static void printField(char[][] array, char[][] marks, boolean[][] printFields, boolean dead){

        System.out.println(" |123456789|\n-|---------|");
        for(int i = 1; i < 10; i++) {
            System.out.print(i + "|");
            for(int j = 1; j < 10; j++) {
                if(!printFields[i][j]) {
                    if(dead && array[i][j] == 'X') { //bombs when dead
                        System.out.print('X');
                    }
                    else
                        System.out.print('.'); //hidden fields
                }
                else if (marks[i][j] == '*')
                    System.out.print('*');      //marks
                else if (array[i][j] == '.')
                    System.out.print('/');      //slashes
                else                            //numbers
                    System.out.print(array[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public static void printEmptyField(){

        System.out.println(" |123456789|\n-|---------|");
        for(int i = 1; i < 10; i++) {
            System.out.print(i + "|");
            for(int j = 1; j < 10; j++) {
                System.out.print('.');
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public static boolean match(char[][] array, char[][] mines) {

        for(int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                if (array[i][j] == 'X' && mines[i][j] != '*')
                    return false;
            }
        }
        return true;
    }


    public static void setMines(char[][] array, int n, int c, int d) {

        Random random = new Random();

        while(n > 0) {
            int a = random.nextInt(9) + 1;
            int b = random.nextInt(9) + 1;
            if(array[a][b] != 'X' && !(a == d && b == c)) {
                array[a][b] = 'X';
                n--;
            }
        }

        for(int i = 1; i < 10; i++) {
            for(int j = 1; j < 10; j++) {
                if (array[i][j] == 'X')
                    continue;
                int count = 0;
                count += array[i - 1][j] == 'X' ? 1 : 0;
                count += array[i + 1][j] == 'X' ? 1 : 0;
                count += array[i][j - 1] == 'X' ? 1 : 0;
                count += array[i][j + 1] == 'X' ? 1 : 0;
                count += array[i - 1][j + 1] == 'X' ? 1 : 0;
                count += array[i - 1][j - 1] == 'X' ? 1 : 0;
                count += array[i + 1][j - 1] == 'X' ? 1 : 0;
                count += array[i + 1][j + 1] == 'X' ? 1 : 0;

                if(count > 0)
                    array[i][j] = (char) ('0' + count);
            }
        }
    }
}