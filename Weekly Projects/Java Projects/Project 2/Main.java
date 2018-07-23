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

import java.util.Scanner;

/**
 *
 * @author tsamo
 */
public class Main {
    public static final Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        Student student1=new Student("Giannis", "antetokoumpo");
        Student student2=new Student("Sophia", "Vempo", "GR20183669");
        Student student3=new Student("Qi", "Li", "GR!1799788");
        
        Worker worker1=new Worker("Katerina", "Papadopoulou");
        Worker worker2=new Worker("Dimitris", "Kastrinos", 12);
        Worker worker3=new Worker("Areti", "Leloglou", 200.0);
        Worker worker4=new Worker("Panagiwtis", "Kwstopoulos", 40.0, 8);
        Worker worker5=new Worker("Bo", "Cox", 10.0, 20);
        
        student1.displayInfo();
        student2.displayInfo();
        student3.displayInfo();

        worker1.displayInfo();
        worker2.displayInfo();
        worker3.displayInfo();
        worker4.displayInfo();
        worker5.displayInfo();
        
        Worker.numberOfWorkers();
        input.close();
    }
}
