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
public abstract class Person {
    private String firstName;
    private String lastName;
    private static int entries=0;
    
    
    public Person(String firstName, String lastName) {
        entries++;
        setFirstName(firstName);
        setLastName(lastName);
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public abstract void displayInfo();
    
    public void setFirstName(String firstName) {
        
        while(firstName.length()<3){
            try{
                System.out.println("Please input, for entry number "+entries+", a valid first name with more than 2 characters:");
                firstName=input.next();
            }
            catch(InputMismatchException e){
                e.printStackTrace();
                input.next();
            }
        }
        this.firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
    }
    
    public void setLastName(String lastName) {
        while(lastName.length()<4){
            try{
                System.out.println("Please input, for entry number "+entries+", a valid last name with more than 3 characters:");
                lastName=input.next();
            }
            catch(InputMismatchException e){
                e.printStackTrace();
                input.next();
            }
        }
        this.lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
    }
    
}
