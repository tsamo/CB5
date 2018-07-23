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
import java.text.DecimalFormat;
import java.util.InputMismatchException;

/**
 *
 * @author tsamo
 */
public class Worker extends Person{
    private double weekSalary;
    private int workHoursPerDay;
    private static final DecimalFormat df2 = new DecimalFormat(".##");
    private static int workersNum=0;
    
    
    public Worker(String firstName, String lastName) {
        super(firstName, lastName);
        workersNum++;
    }

    public double getWeekSalary() {
        return weekSalary;
    }

    public int getWorkHoursPerDay() {
        return workHoursPerDay;
    }

    public Worker(String firstName, String lastName, int workHoursPerDay) {
        super(firstName, lastName);
        setWorkHoursPerDay(workHoursPerDay);
        workersNum++;
    }
    
    public Worker(String firstName, String lastName, double weekSalary) {
        super(firstName, lastName);
        setWeekSalary(weekSalary);
        workersNum++;
    }
    
    public Worker(String firstName, String lastName, double weekSalary, int workHoursPerDay) {
        super(firstName, lastName);
        setWeekSalary(weekSalary);
        setWorkHoursPerDay(workHoursPerDay);
        workersNum++;
    }
    
    public double salaryPerHour(){
        return weekSalary/(workHoursPerDay*5);
    }
    
    @Override
    public String toString() {
        if(checkForWeekSalary(this)==false&&checkForWorkHoursPerDay(this)==false){
            return "First Name : {"+this.getFirstName()+"}\nLast Name : {"+this.getLastName()+"}\nWeek Salary : {Not available at the moment.}\nHours Per Day : {Not available at the moment.}\nSalary per hour : {Not available at the moment.}";
        }
        else if(checkForWeekSalary(this)==false){
            return "First Name : {"+this.getFirstName()+"}\nLast Name : {"+this.getLastName()+"}\nWeek Salary : {Not available at the moment.}\nHours Per Day : {"+workHoursPerDay+"}\nSalary per hour : {Not available at the moment.}";
        }
        else if(checkForWorkHoursPerDay(this)==false){
            return "First Name : {"+this.getFirstName()+"}\nLast Name : {"+this.getLastName()+"}\nWeek Salary : {"+df2.format(weekSalary)+"€}\nHours Per Day : {Not available at the moment.}\nSalary per hour : {Not available at the moment.}";
        }
        return "First Name : {"+this.getFirstName()+"}\nLast Name : {"+this.getLastName()+"}\nWeek Salary : {"+df2.format(weekSalary)+"€}\nHours Per Day : {"+workHoursPerDay+"}\nSalary per hour : {"+df2.format(this.salaryPerHour())+"€}";
    }
    
    @Override
    public void displayInfo(){
        System.out.println(this.toString()+"\n");
    }
    
    public Boolean checkForWorkHoursPerDay(Worker worker){
        return worker.workHoursPerDay != 0;
    }
    
    public Boolean checkForWeekSalary(Worker worker){
        return worker.weekSalary != 0;
    }
    
    public void setWeekSalary(double weekSalary) {
        while(weekSalary<=10){
            try{
                System.out.println("Please input, for Worker "+this.getFirstName()+" "+this.getLastName()+", a valid week salary, of more than 10€:");
                weekSalary=input.nextDouble();
            }
            catch(InputMismatchException e){
                e.printStackTrace();
                input.next();
            }           
        }
        this.weekSalary=weekSalary;
    }
    
    public void setWorkHoursPerDay(int workHoursPerDay) {
        while(workHoursPerDay<1||workHoursPerDay>12){
            try{
                System.out.println("Please input, for Worker "+this.getFirstName()+" "+this.getLastName()+", a valid number of working hours, between 1 and 12:");
                workHoursPerDay=input.nextInt();
            }
            catch(InputMismatchException e){
                e.printStackTrace();
                input.next();
            }
        }
        this.workHoursPerDay=workHoursPerDay;
    }

    public static int getWorkersNum() {
        return workersNum;
    }
    
    public static void numberOfWorkers(){
        System.out.println("The current number of workers is "+Worker.getWorkersNum());
    }
}
