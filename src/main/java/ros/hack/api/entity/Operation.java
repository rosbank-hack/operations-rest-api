package ros.hack.api.entity;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@RedisHash("operation")
@Table(name = "operation", uniqueConstraints = @UniqueConstraint(columnNames = {"operation_id", "service"}))
@Accessors(chain = true)
public class Operation
        implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "operation_id", nullable = false)
    private UUID operationId;

    @Column(name = "service", nullable = false)
    private String service;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "status")
    private String status;

    @Column(name = "extended_status")
    private String extendedStatus;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "mcc")
    private String mcc;

    @Column(name = "extra")
    private String extra;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return new EqualsBuilder()
                .append(operationId, operation.operationId)
                .append(name, operation.name)
                .append(date, operation.date)
                .append(status, operation.status)
                .append(extendedStatus, operation.extendedStatus)
                .append(amount, operation.amount)
                .append(currency, operation.currency)
                .append(mcc, operation.mcc)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(operationId)
                .append(name)
                .append(date)
                .append(status)
                .append(extendedStatus)
                .append(amount)
                .append(currency)
                .append(mcc)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("operationId", operationId)
                .add("name", name)
                .add("date", date)
                .add("status", status)
                .add("extendedStatus", extendedStatus)
                .add("amount", amount)
                .add("currency", currency)
                .add("mcc", mcc)
                .toString();
    }
}
