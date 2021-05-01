create EXTENSION IF NOT EXISTS "uuid-ossp";

create TABLE IF NOT EXISTS patient_assist_requests
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1(),
    srf_id VARCHAR(255),
    bu_number VARCHAR(255),
    covid_test_result VARCHAR(255),
    is_vaccination_taken boolean,
    patient_details jsonb,
    care_taker_details jsonb,
    address jsonb,
    hospital_details jsonb,
    service_requested VARCHAR(255),
    description jsonb,
    request_status jsonb,
    created_at timestamp,
    last_modified_at timestamp
 );