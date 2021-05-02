create EXTENSION IF NOT EXISTS "uuid-ossp";

create TABLE IF NOT EXISTS patient_assist_requests
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1(),
    srf_id varchar(255),
    bu_number varchar(255),
    covid_test_result varchar(255),
    is_vaccination_taken varchar(255),
    patient_details jsonb,
    care_taker_details jsonb,
    address jsonb,
    hospital_details jsonb,
    service_requested varchar(255),
    description jsonb,
    request_status jsonb,
    created_at timestamp,
    last_modified_at timestamp
 );

create TABLE IF NOT EXISTS volunteer_details
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1(),
    firstName varchar(255),
    lastName varchar(255),
    email varchar(255),
    primary_mobile varchar(255),
    alternate_mobile varchar(255),
    age varchar(255),
    address jsonb,
    id_proof_number varchar(255),
    created_at timestamp,
    last_modified_at timestamp
);