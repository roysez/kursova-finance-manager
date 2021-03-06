package dev.roysez.financemanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Transaction implements Comparable<Transaction> {

    private Integer id;
    private TransactionType trType;
    private Long sum;
    private Date date;
    private String description;
    private Category category;

    public Transaction() {

    }

    @Override
    public int compareTo(Transaction o) {
        return this.id - o.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (trType != that.trType) return false;
        if (sum != null ? !sum.equals(that.sum) : that.sum != null) return false;
        return (description != null ? description.equals(that.description) :
                that.description == null) && (category != null ? category.equals(that.category) :
                that.category == null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (trType != null ? trType.hashCode() : 0);
        result = 31 * result + (sum != null ? sum.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", trType=" + trType +
                ", sum=" + sum +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }


    public enum TransactionType {
        TRANSACTION_EXPENSE,
        TRANSACTION_INCOME;


        @Override
        public String toString() {
            return this.name().equals("TRANSACTION_EXPENSE") ? "Expense" : "Income";
        }
    }
}
