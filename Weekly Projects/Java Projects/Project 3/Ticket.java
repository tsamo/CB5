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

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author tsamo
 */
public class Ticket {
    private int id;
    private SimpleDateFormat sdf;
    private String date;
    private Player player;
    private static final Set<Integer> idNumbers = new LinkedHashSet<>();
    private Set<Integer> normalNumbers;
    private int jokerNumber;

    public Ticket(Player player, Set<Integer> normalNumbers, int jokerNumber) {
        this.player = player;
        this.normalNumbers = normalNumbers;
        this.jokerNumber = jokerNumber;
        setId();
        setSdf();
    }

    public void setId() {
        SecureRandom r = new SecureRandom();
        Integer next = r.nextInt(1000) + 1;

        if (idNumbers.contains(next)) {
            next = r.nextInt(1000) + 1;
            idNumbers.add(next);
        } else {
            idNumbers.add(next);
        }
        this.id = next;
    }

    public int getId() {
        return id;
    }

    public void setNumbersPlayed(Set<Integer> normalNumbersPlayed) {
        normalNumbers.addAll(normalNumbersPlayed);
    }

    public void setJoker(int jokerNumberPlayed) {
        jokerNumber = jokerNumberPlayed;
    }

    public void setSdf() {
        Date date1 = new Date();
        sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss");
        date = sdf.format(date1);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Integer> getNormalNumbers() {
        return normalNumbers;
    }

    public int getJokerNumber() {
        return jokerNumber;
    }

    @Override
    public String toString() {
        return "Ticket{id=" + id + ", date=" + date + ", player=" + player + ", normal numbers=" + normalNumbers + ", joker naumber=" + jokerNumber + '}';
    }
}
