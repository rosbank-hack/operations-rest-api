package ros.hack.api.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ros.hack.api.entity.Operation;
import ros.hack.api.entity.QOperation;
import ros.hack.api.model.OperationsRequest;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OperationRepositoryImpl
        implements OperationRepository {

    private final EntityManager entityManager;

    @Nonnull
    @Override
    public List<Operation> findAllOperations(@Nonnull OperationsRequest request) {
        JPAQuery query = new JPAQuery(entityManager);
        QOperation operation = QOperation.operation;

        //Predicate predicate = buildPredicate(operation, request);

        query.from(operation)
               // .where(predicate)
                .orderBy(operation.id.desc());

        return query.fetch();
    }

    @Nonnull
    private Predicate buildPredicate(@Nonnull QOperation operation, @Nonnull OperationsRequest request) {
        return new BooleanBuilder(operation.isNotNull())
                .and(operation.userId.eq(request.getUserId()))
                //.and(operation.date.between(request.getDateFrom(), request.getDateTill()))
                .and(operation.sourceId.containsIgnoreCase(request.getSourceId()));
    }
}
