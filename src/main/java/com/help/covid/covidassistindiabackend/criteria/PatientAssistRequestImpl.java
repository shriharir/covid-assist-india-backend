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
        String volunteerId = searchTerms.getVolunteerId();
        String state = searchTerms.getState();
        String district = searchTerms.getDistrict();

        Pageable pageable = PageRequest.of(searchTerms.getPageNumber(), searchTerms.getLimit());

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

        if (volunteerId != null) {
            predicateList.add(builder.equal(requestsRootQuery.get("volunteerId"), volunteerId));
        }

        if (state != null && state.length() > 0) {
            predicateList.add(builder
                    .function("jsonb_extract_path_text",
                            String.class,
                            requestsRootQuery.get("address"),
                            builder.literal("state"))
                    .in(state)
            );
        }

        if (district != null && district.length() > 0) {
            predicateList.add(builder
                    .function("jsonb_extract_path_text",
                            String.class,
                            requestsRootQuery.get("address"),
                            builder.literal("district"))
                    .in(district)
            );
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

    @Override
    public Long findDuplicateRequestCount(String srfId, String buNumber) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PatientAssistRequestEntity> query = builder.createQuery(PatientAssistRequestEntity.class);

        Root<PatientAssistRequestEntity> requestsRootQuery = query.from(PatientAssistRequestEntity.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (srfId != null) {
            predicateList.add(builder.equal(requestsRootQuery.get("srfId"), srfId));
        }

        if (buNumber != null) {
            predicateList.add(builder.equal(requestsRootQuery.get("buNumber"), buNumber));
        }

        Predicate finalPredicate = builder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        query.where(finalPredicate);
        query.select(requestsRootQuery);

        List<PatientAssistRequestEntity> requests = entityManager.createQuery(query)
                .getResultList();

        return Long.valueOf(requests.size());
    }
}
