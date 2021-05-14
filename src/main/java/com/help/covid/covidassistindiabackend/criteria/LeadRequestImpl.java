package com.help.covid.covidassistindiabackend.criteria;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.help.covid.covidassistindiabackend.entity.LeadRequestEntity;
import com.help.covid.covidassistindiabackend.model.SearchTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Repository
public class LeadRequestImpl implements LeadRequestRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<LeadRequestEntity> searchByRequestFilters(SearchTerms searchTerms) {

        ZonedDateTime to = searchTerms.getDateRange().getTo();
        ZonedDateTime from = searchTerms.getDateRange().getFrom();
        List<String> statusList = searchTerms.getStatuses();
        List<String> servicesList = searchTerms.getServiceTypes();
        String state = searchTerms.getState();
        String district = searchTerms.getDistrict();

        Pageable pageable = PageRequest.of(searchTerms.getPageNumber(), searchTerms.getLimit());

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LeadRequestEntity> query = builder.createQuery(LeadRequestEntity.class);

        Root<LeadRequestEntity> requestsRootQuery = query.from(LeadRequestEntity.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (to != null && from != null) {
            predicateList.add(builder.between(requestsRootQuery.get("createdAt"), from, to));
        }

        if (isNotEmpty(servicesList)) {
            predicateList.add(requestsRootQuery.get("leadType").in(servicesList));
        }

        if (isNotEmpty(statusList)) {
            predicateList.add(requestsRootQuery.get("verifiedStatus").in(statusList));
        }

        if (state != null && state.length() > 0) {
            predicateList.add(builder
                    .function("jsonb_extract_path_text",
                            String.class,
                            requestsRootQuery.get("businessAddress"),
                            builder.literal("state"))
                    .in(state)
            );
        }

        if (district != null && district.length() > 0) {
            predicateList.add(builder
                    .function("jsonb_extract_path_text",
                            String.class,
                            requestsRootQuery.get("businessAddress"),
                            builder.literal("district"))
                    .in(district)
            );
        }

        Predicate finalPredicate = builder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        query.where(finalPredicate);
        query.orderBy(builder.desc(requestsRootQuery.get("createdAt")));
        query.select(requestsRootQuery);

        List<LeadRequestEntity> requests = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<LeadRequestEntity> requestsRootCount = countQuery.from(LeadRequestEntity.class);
        countQuery.select(builder.count(requestsRootCount)).where(builder.and(predicateList.toArray(new Predicate[predicateList.size()])));

        // Fetches the count of all requests as per given criteria
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        Page<LeadRequestEntity> paginatedResult = new PageImpl<>(requests, pageable, count);
        return paginatedResult;
    }
}
