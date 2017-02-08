import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        triangleTest();

    }

    public static void triangleTest(){

        Scanner scan = new Scanner(System.in);
        int[] inputArray = new int[3];

        System.out.println("Welcome to triangle test");
        try{
            for(int i=0;i < 3; i++){
                System.out.println("Please input a number:");
                inputArray[i] = scan.nextInt();

            }
        }catch(ArrayIndexOutOfBoundsException|ArrayStoreException|InputMismatchException ex){
            System.out.println("ERROR: " + ex);
            System.out.println("--- RESTARTING ---");
            triangleTest();
        }
        compareInputs(inputArray);

    }

    public static void compareInputs(int[] inputArray){

        if(inputArray[0] == inputArray[1] && inputArray[0] == inputArray[2]){
            System.out.printf("The result is: An EQUILATERAL triangle");
        }else if(inputArray[0] == inputArray[1] || inputArray[0] == inputArray[2] || inputArray[1] == inputArray[2]){
            System.out.printf("The result is: An ISOSCELES triangle");
        }else{
            System.out.printf("The result is: A SCALENE triangle");
        }

    }

}
