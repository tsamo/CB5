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
package a6Week1Project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.nio.file.Files.probeContentType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tsamo
 */
public class Project1 {
    public static void main(String[] args) {
        
        try {
            
            Date date = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy h:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm:ss");
            String formattedDate1 = sdf1.format(date);
            String formattedDate2 = sdf2.format(date);
            
            if(args.length!=2){
                System.out.println("Please provide valid arguments");
                System.out.println("argument 1: path to text file");
                System.out.println("argument 2: command to apply(wc or find)");
            }
            else{
                
                File f =new File(args[0]);
                
                if(f.exists()){
                    String contentType = probeContentType(f.toPath());
                    if(contentType.equals("text/plain")){
                        System.out.println(formattedDate2+": File "+args[0]+" found!");
                        if(null == args[1]){
                            System.out.println("Argument "+args[1]+" is invalid. Only wc and find are supported");
                        }
                        else switch (args[1]) {
                            case "wc":
                            {
                                BufferedWriter bw;
                                try (FileWriter fw = new FileWriter("wordcount.txt", true)) {
                                    bw = new BufferedWriter(fw);
                                    try (PrintWriter out = new PrintWriter(bw)) {
                                        Scanner sc = new Scanner(new FileInputStream(f));
                                        int count=0;
                                        while(sc.hasNext()){
                                            sc.next();
                                            count++;
                                        }   out.printf("%-21s %-5s %-12s %d %n", formattedDate1, "wc", args[0], count);
                                        System.out.println(formattedDate2+": Total word count started");
                                        System.out.println(formattedDate2+": Word count finished. Counted "+count+" words");
                                    }
                                }       bw.close();
                                break;
                            }
                            case "find":
                            {
                                BufferedWriter bw;
                                try (FileWriter fw = new FileWriter("wordcount.txt", true)) {
                                    bw = new BufferedWriter(fw);
                                    String wts;
                                    try (PrintWriter out = new PrintWriter(bw)) {
                                        Scanner sc1 = new Scanner(new FileInputStream(f));
                                        try (Scanner sc2 = new Scanner(System.in)) {
                                            System.out.print("Enter the word you wish to search in the file: ");
                                            wts = sc2.nextLine();
                                        }
                                        int count=0;
                                        
                                        while(sc1.hasNext()){
                                            String next = sc1.next();
                                            Pattern p = Pattern.compile(wts);
                                            Matcher m = p.matcher(next);
                                            
                                            if(m.find()){
                                                count++;
                                            }
                                        }
                                        if(count!=0)
                                            out.printf("%-21s %-5s %-12s %-2s:%d %n", formattedDate1, "find", args[0], wts, count);
                                        System.out.println(formattedDate2+": Counting occurences of word "+wts);
                                        System.out.println(formattedDate2+": Count of word "+wts+" finished. Occurences found "+count);
                                    }
                                }       bw.close();
                                break;
                            }
                            default:
                                System.out.println("Argument "+args[1]+" is invalid. Only wc and find are supported");
                                break;
                        }
                    }
                    else{
                        System.out.println(args[0]+" is not a text file");
                    }
                }
                else{
                    System.out.println(args[0]+" is not a valid file path");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Project1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }
}