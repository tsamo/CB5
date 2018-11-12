/*
 *
 *  The MIT License
 *
 *  Copyright 2018 tsamo.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 * /
 */

import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author tsamo
 */
public class Player {
    private int afm;
    private String iban;
    private String firstName;
    private String lastName;
    private Set<Ticket> ticketSet = new LinkedHashSet<>();

    public Player(int afm, String iban, String firstName, String lastName) {
        this.afm = afm;
        this.iban = iban;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Set<Ticket> getTicketSet() {
        return ticketSet;
    }

    public int getAfm() {
        return afm;
    }

    public String getIban() {
        return iban;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void play() {
        try {
            Scanner input = new Scanner(System.in, "UTF-8");
            Set<Integer> normalNumbersPlayed = new LinkedHashSet<>();

            int jokerNumberPlayed;
            int temp;
            System.out.println("The 5 numbers must be in the range 1-45, while the joker number in the range 1-20.");

            for (int j = 1; j <= 5; j++) {
                System.out.println("Please input number " + j + ":>");
                temp = input.nextInt();

                while (temp < 1 || temp > 45) {
                    System.out.println("Error.");
                    System.out.println("Please input a valid number between 1 and 45 :>");
                    temp = input.nextInt();
                }

                if (normalNumbersPlayed.contains(temp)) {
                    System.out.println("You have already input this number");
                    System.out.println("Please input a valid number between 1 and 45 :>");
                    temp = input.nextInt();
                    normalNumbersPlayed.add(temp);
                } else {
                    normalNumbersPlayed.add(temp);
                }
            }

            System.out.println("Please input the joker number :>");
            temp = input.nextInt();

            while (temp < 1 || temp > 20) {
                System.out.println("Error.");
                System.out.println("Please input a valid joker number between 1 and 20 :>");
                temp = input.nextInt();
            }

            jokerNumberPlayed = temp;

            Ticket ticket = new Ticket(this, normalNumbersPlayed, jokerNumberPlayed);
            ticketSet.add(ticket);
        } catch (InputMismatchException e) {
            System.out.println("The input was not valid. Try again.");
        }
    }

    public void checkIfWon() {
        Draw joker = new Draw();
        joker.draw();
        int ticketnum = 0;

        for (Ticket t : this.getTicketSet()) {
            ticketnum++;
            t.getNormalNumbers().retainAll(joker.getNormalNumbersResults());
            if (t.getJokerNumber() == joker.getJokerResult() && t.getNormalNumbers().size() == 5) {
                System.out.println("For ticket number:" + ticketnum);
                System.out.println("You have won!! You got them all!!");
            } else if (t.getNormalNumbers().size() == 4 && t.getJokerNumber() != joker.getJokerResult()) {
                System.out.println("For ticket number:" + ticketnum);
                System.out.println("You have won!! You got 4 of the 5 numbers correct!!");
            } else {
                System.out.println("For ticket number:" + ticketnum);
                System.out.println("You have not won.");
            }
        }

    }
}
