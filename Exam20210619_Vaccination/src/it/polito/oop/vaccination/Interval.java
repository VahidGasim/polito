package it.polito.oop.vaccination;

public class Interval {
    private int bir;
    private int iki;

    public Interval(int i, int j) {
        bir = i;
        iki = j;
    }

    public boolean notIn(int i) {
    	return bir<=i && iki>i;
    }

    public int getLower() {
    	return bir;
    }
	
    public String toString() {
        if(iki>100)
            return  "["+bir+",+)"; 

          return  "["+bir+","+iki+")"; 
    }

}
