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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Repository
@RequiredArgsConstructor
public class OperationRepositoryImpl
        implements OperationRepository {
    private static final long DEFAULT_LIMIT = 10;

    private final EntityManager entityManager;

    @Nonnull
    @Override
    @SuppressWarnings("all")
    public List<Operation> findAllOperations(@Nonnull OperationsRequest request) {
        JPAQuery query = new JPAQuery(entityManager);
        QOperation operation = QOperation.operation;

        Predicate predicate = buildPredicate(operation, request);

        query.from(operation)
                .where(operation.userId.eq(request.getUserId()))
                .where(predicate)
                .where(operation.id.goe(request.getLastItemId()))
                .orderBy(operation.id.desc())
                .limit(request.getItemsCount() > 0 ? request.getItemsCount() : DEFAULT_LIMIT);

        return query.fetch();
    }

    @Nonnull
    private Predicate buildPredicate(@Nonnull QOperation operation, @Nonnull OperationsRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (isNotEmpty(request.getSourceId())) {
            predicate.and(operation.sourceId.eq(request.getSourceId()));
        }
        if (isNotEmpty(request.getCategory())) {
            predicate.and(operation.mcc.eq(request.getCategory()));
        }
        return predicate;
    }
}
