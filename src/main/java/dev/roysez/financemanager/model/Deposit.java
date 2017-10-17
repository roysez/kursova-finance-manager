package dev.roysez.financemanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Deposit implements Comparable<Deposit> {

        @Override
        public int compareTo(Deposit o) {
            return this.id-o.id;
        }

    private Integer id;

    private Long sum;

    private Long income;

    private Integer percentages;

    private DepositStatus depositStatus;

    private String description;

    private Integer term;

    private Integer monthPaid;

    public Long doCharge(){

        if(!depositStatus.equals(DepositStatus.COMPLETED)){
            Long profit = sum*percentages/100;
            income+=profit;
            monthPaid++;
            if(term.equals(monthPaid))
                depositStatus = DepositStatus.COMPLETED;

            return profit;
        } else throw new IllegalStateException("Deposit is already ended");
    }

    public boolean checkIfCompleted(){
        return depositStatus.equals(DepositStatus.COMPLETED);
    }

    public Deposit() {
        depositStatus = DepositStatus.IN_PROCESS;
        income = 0L;
        monthPaid = 0;

    }

    public enum DepositStatus {
        COMPLETED,
        IN_PROCESS;

        @Override
        public String toString() {
            return this.name().equals("COMPLETED")?"Completed":"In process";
        }
    }

}
