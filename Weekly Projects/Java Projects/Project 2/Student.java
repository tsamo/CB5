/*
* The MIT License
*
* Copyright 2018 tsamo.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package c6Week3Project;

import static c6Week3Project.Main.input;
import java.util.InputMismatchException;

/**
 *
 * @author tsamo
 */
public class Student extends Person{
    private String facultyNumber;
    private static int studentsNum=0;
    
    public Student(String firstName, String lastName, String facultyNumber) {
        super(firstName, lastName);
        setFacultyNumber(facultyNumber);
        studentsNum++;
    }
    
    public Student(String firstName, String lastName) {
        super(firstName, lastName);
        studentsNum++;
    }
    
    public String getFacultyNumber() {
        return facultyNumber;
    }
    
    @Override
    public String toString() {
        if(checkForFacultyNumber(this)==false){
            return "First Name : {"+this.getFirstName()+"}\nLast Name : {"+this.getLastName()+"}\nFaculty Number : {Not available at the moment.}";
        }
        else{
            return "First Name : {"+this.getFirstName()+"}\nLast Name : {"+this.getLastName()+"}\nFaculty Number : {"+this.facultyNumber+"}";
        }
    }
    
    @Override
    public void displayInfo(){
        System.out.println(this.toString()+"\n");
    }
    
    public Boolean checkForFacultyNumber(Student student){
        return student.facultyNumber != null;
    }
    
    public void setFacultyNumber(String facultyNumber) {
        String pattern = "^((?=[A-Za-z0-9@])(?![_\\\\-]).)*$";
        while(facultyNumber.length()<5||facultyNumber.length()>10||!facultyNumber.matches(pattern)){
            try{
                System.out.println("Please input a valid faculty Number, for the Student "+this.getFirstName()+" "+this.getLastName()+", between 5 and 10 characters long, consisting only of digits and letters:");
                facultyNumber=input.next();
            }
            catch(InputMismatchException ex){
                ex.printStackTrace();
                input.next();
            }
        }
        this.facultyNumber=facultyNumber;
    }

    public static int getStudentsNum() {
        return studentsNum;
    }
    
    public static void numberOfStudents(){
        System.out.println("The current number of students is "+Student.getStudentsNum());
    }
}
