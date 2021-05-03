package com.help.covid.covidassistindiabackend.criteria;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.help.covid.covidassistindiabackend.entity.PatientAssistRequestEntity;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Repository
public class PatientAssistRequestImpl implements PatientAssistRequestRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<PatientAssistRequestEntity> searchByRequestFilters(SearchTerms searchTerms) {

        ZonedDateTime to = searchTerms.getDateRange().getTo();
        ZonedDateTime from = searchTerms.getDateRange().getFrom();
        List<String> statusList = searchTerms.getStatuses();
        List<String> servicesList = searchTerms.getServiceTypes();

        Pageable pageable = PageRequest.of(searchTerms.getPageNumber() - 1, searchTerms.getLimit());

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PatientAssistRequestEntity> query = builder.createQuery(PatientAssistRequestEntity.class);

        Root<PatientAssistRequestEntity> requestsRootQuery = query.from(PatientAssistRequestEntity.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (to != null && from != null) {
            predicateList.add(builder.between(requestsRootQuery.get("createdAt"), from, to));
        }

        if (isNotEmpty(servicesList)) {
            predicateList.add(requestsRootQuery.get("serviceRequested").in(servicesList));
        }

        if (isNotEmpty(statusList)) {
            predicateList.add(requestsRootQuery.get("currentStatus").in(statusList));
        }
        Predicate finalPredicate = builder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        query.where(finalPredicate);
        query.orderBy(builder.desc(requestsRootQuery.get("createdAt")));
        query.select(requestsRootQuery);

        List<PatientAssistRequestEntity> requests = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<PatientAssistRequestEntity> requestsRootCount = countQuery.from(PatientAssistRequestEntity.class);
        countQuery.select(builder.count(requestsRootCount)).where(builder.and(predicateList.toArray(new Predicate[predicateList.size()])));

        // Fetches the count of all requests as per given criteria
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        Page<PatientAssistRequestEntity> paginatedResult = new PageImpl<>(requests, pageable, count);
        return paginatedResult;
    }
}
