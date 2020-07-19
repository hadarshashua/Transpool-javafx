package controller.feedback;

import java.util.ArrayList;
import java.util.List;

public class Feedbacks
{
    private int numOfRates, sumOfRates;
    double avgRate;
    List<String> feedbacks;

    public Feedbacks()
    {
        this.avgRate = 0;
        this.numOfRates = 0;
        this.sumOfRates = 0;
        this.feedbacks = new ArrayList<>();
    }

    public void setAvgRate(double avgRate) {
        this.avgRate = avgRate;
    }

    public void setNumOfRates(int numOfRates) {
        this.numOfRates = numOfRates;
    }

    public void setSumOfRates(int symOfRates) {
        this.sumOfRates = symOfRates;
    }

    public double getAvgRate() {
        return avgRate;
    }

    public int getNumOfRates() {
        return numOfRates;
    }

    public int getSumOfRates() {
        return sumOfRates;
    }

    public List<String> getFeedbacks() {
        return feedbacks;
    }

    public void addNewFeedback(String newFeedback)
    {
        this.feedbacks.add(newFeedback);
    }
}
