package com.help.covid.covidassistindiabackend.entity;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.help.covid.covidassistindiabackend.generic.GenericEntity;
import com.help.covid.covidassistindiabackend.generic.JsonType;
import com.help.covid.covidassistindiabackend.mapper.VolunteerMapper;
import com.help.covid.covidassistindiabackend.model.Address;
import com.help.covid.covidassistindiabackend.model.Volunteer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import static com.help.covid.covidassistindiabackend.generic.JsonType.JSON_TYPE;

@Getter
@Setter
@Entity
@Table(name = "volunteer_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs({
        @TypeDef(name = "JsonType", typeClass = JsonType.class)
})
@Slf4j
public class VolunteerEntity implements GenericEntity<Volunteer> {

    @Id
    @Column(name = "id")
    public String volunteerId;

    public String firstName;
    public String lastName;
    public String email;
    public String primaryMobile;
    public String alternateMobile;
    public String age;

    @Type(type = JSON_TYPE)
    public Address address;

    public String idProofNumber;
    public ZonedDateTime createdAt;
    public ZonedDateTime lastModifiedAt;

    @Override
    public Volunteer toDomain() {
        return VolunteerMapper.INSTANCE.toDomain(this);
    }

    @SneakyThrows
    @PrePersist
    public void updateRequiredFields() {
        log.info("In Pre Persist Method for request");
        createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        log.info("In Pre Update Method");
        lastModifiedAt = ZonedDateTime.now();
    }
}
