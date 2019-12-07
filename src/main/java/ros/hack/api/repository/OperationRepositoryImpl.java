package ros.hack.api.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ros.hack.api.entity.Operation;
import ros.hack.api.entity.QOperation;
import ros.hack.api.enums.PagingDirection;
import ros.hack.api.model.OperationsRequest;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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
    public List<Operation> findAll(@Nonnull OperationsRequest request) {
        final JPAQuery query = new JPAQuery(entityManager);
        final QOperation operation = QOperation.operation;

        final Predicate filterPredicate = buildFilterPredicate(operation, request);
        final Predicate directionPredicate = buildDirectionPredicate(operation, request);

        query.from(operation)
                .where(operation.userId.eq(request.getUserId()))
                .where(filterPredicate)
                .where(directionPredicate)
                .where(operation.ready.eq(true))
                .orderBy(operation.id.desc())
                .limit(request.getItemsCount() > 0 ? request.getItemsCount() : DEFAULT_LIMIT);

        return query.fetch();
    }

    @Nonnull
    @Override
    @SuppressWarnings("all")
    public Optional<Operation> findById(@Nonnull Long id) {
        JPAQuery query = new JPAQuery(entityManager);
        QOperation operation = QOperation.operation;

        query.from(operation)
                .where(operation.id.eq(id))
                .where(operation.ready.eq(true))
                .limit(1);

        return Optional.ofNullable((Operation) query.fetchFirst());
    }

    @Nonnull
    private Predicate buildFilterPredicate(@Nonnull QOperation operation, @Nonnull OperationsRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (isNotEmpty(request.getStatus())) {
            predicate.and(operation.status.eq(request.getStatus()));
        }
        if (isNotEmpty(request.getExtendedStatus())) {
            predicate.and(operation.extendedStatus.eq(request.getExtendedStatus()));
        }
        if (isNotEmpty(request.getSourceId())) {
            predicate.and(operation.sourceId.eq(request.getSourceId()));
        }
        if (isNotEmpty(request.getCategory())) {
            predicate.and(operation.mcc.eq(request.getCategory()));
        }
        return predicate;
    }

    @Nonnull
    private Predicate buildDirectionPredicate(@Nonnull QOperation operation, @Nonnull OperationsRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (request.getPagingDirection() == PagingDirection.UP) {
            predicate.and(operation.id.lt(request.getLastItemId()));
        } else {
            predicate.and(operation.id.gt(request.getLastItemId()));
        }
        return predicate;
    }
}
