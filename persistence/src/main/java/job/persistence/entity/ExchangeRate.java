package job.persistence.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String currencyCode;
    @Column(nullable = false)
    private double rate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRate exchangeRate)) return false;
        return currencyCode.equalsIgnoreCase(exchangeRate.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode.toUpperCase());
    }
}
