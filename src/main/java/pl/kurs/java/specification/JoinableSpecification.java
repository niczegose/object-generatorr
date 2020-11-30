package pl.kurs.java.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

public interface JoinableSpecification<T> extends Specification<T> {


    @SuppressWarnings("unchecked")
    public default <K, Z> ListJoin<K, Z> joinList(From<?, K> from, ListAttribute<K, Z> attribute) {

        for (Join<K, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attribute.getName());

            if (sameName) {

                return (ListJoin<K, Z>) join; //TODO verify Z type it should be of Z after all its ListAttribute<K,Z>
            }
        }
        return from.join(attribute);
    }

    @SuppressWarnings("unchecked")
    public default <K, Z> SetJoin<K, Z> joinList(From<?, K> from, SetAttribute<K, Z> attribute, JoinType joinType) {

        for (Join<K, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attribute.getName());

            if (sameName && join.getJoinType().equals(joinType)) {
                return (SetJoin<K, Z>) join; //TODO verify Z type it should be of Z after all its ListAttribute<K,Z>
            }
        }
        return from.join(attribute, joinType);
    }

    @SuppressWarnings("unchecked")
    public default <K, Z> Join<K, Z> joinList(From<?, K> from, SingularAttribute<K, Z> attribute) {

        for (Join<K, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attribute.getName());

            if (sameName) {
                return (Join<K, Z>) join; //TODO verify Z type it should be of Z after all its ListAttribute<K,Z>
            }
        }
        return from.join(attribute);
    }
}