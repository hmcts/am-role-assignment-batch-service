DELETE FROM role_assignment_history;

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('638e8e7a-7d7c-4027-9d53-ea4b1095eab1', '077dc12a-02ba-4238-87c3-803ca26b515f', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, false, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'CREATED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-24 17:35:08.326');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('638e8e7a-7d7c-4027-9d53-ea4b1095eab1', '077dc12a-02ba-4238-87c3-803ca26b515f', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, false, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'APPROVED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-24 17:35:08.523');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('638e8e7a-7d7c-4027-9d53-ea4b1095eab1', '077dc12a-02ba-4238-87c3-803ca26b515f', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, false, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'LIVE', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-24 17:35:08.634');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('333d2a84-9dfa-4bf0-be5e-bf748656acc5', '340f6af0-bb36-4a9d-a67d-2f65f22be0cf', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'CREATED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-24 17:35:42.272');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('333d2a84-9dfa-4bf0-be5e-bf748656acc5', '340f6af0-bb36-4a9d-a67d-2f65f22be0cf', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'APPROVED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-24 17:35:42.308');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('333d2a84-9dfa-4bf0-be5e-bf748656acc5', '340f6af0-bb36-4a9d-a67d-2f65f22be0cf', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'LIVE', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-24 17:35:42.343');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('cf89f230-0023-4bb6-b548-30da6a944172', 'e84480f9-26ce-4811-805a-9ffab72a9f78', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'CREATED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:30:40.986');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('cf89f230-0023-4bb6-b548-30da6a944172', 'e84480f9-26ce-4811-805a-9ffab72a9f78', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'APPROVED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:30:41.149');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('cf89f230-0023-4bb6-b548-30da6a944172', 'e84480f9-26ce-4811-805a-9ffab72a9f78', 'IDAM', '123e4567-e89b-42d3-a456-556642445613', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'LIVE', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:30:41.244');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('44276b66-11eb-42f5-a4dc-510fec18b0fb', 'b320bfdc-c5ff-4064-9bce-50224e0a3af8', 'IDAM', '123e4567-e89b-42d3-a456-556642445614', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'CREATED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:32:03.588');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('44276b66-11eb-42f5-a4dc-510fec18b0fb', 'b320bfdc-c5ff-4064-9bce-50224e0a3af8', 'IDAM', '123e4567-e89b-42d3-a456-556642445614', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'APPROVED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:32:03.661');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('44276b66-11eb-42f5-a4dc-510fec18b0fb', 'b320bfdc-c5ff-4064-9bce-50224e0a3af8', 'IDAM', '123e4567-e89b-42d3-a456-556642445614', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'LIVE', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:32:03.748');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('ee7254d3-749b-4ed7-aec1-93d3191f3f9f', 'ee1a0441-8126-48cc-b6c6-d44737cbd17d', 'IDAM', '123e4567-e89b-42d3-a456-556642445615', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'CREATED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:32:07.021');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('ee7254d3-749b-4ed7-aec1-93d3191f3f9f', 'ee1a0441-8126-48cc-b6c6-d44737cbd17d', 'IDAM', '123e4567-e89b-42d3-a456-556642445615', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'APPROVED', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:32:07.099');

INSERT INTO public.role_assignment_history
(id, request_id, actor_id_type, actor_id, role_type, role_name, classification, grant_type, role_category, read_only, begin_time, end_time, status, reference, process, "attributes", notes, log, status_sequence, created)
VALUES('ee7254d3-749b-4ed7-aec1-93d3191f3f9f', 'ee1a0441-8126-48cc-b6c6-d44737cbd17d', 'IDAM', '123e4567-e89b-42d3-a456-556642445615', 'ORGANISATION', 'judge', 'PUBLIC', 'STANDARD', NULL, true, '2021-01-01 12:00:00.000', '2022-01-01 11:00:00.000', 'LIVE', 'abc-3434243', 'SPECIFIC', '{"region": "north-east", "contractType": "SALARIED", "jurisdiction": "divorce"}', '[{"time": "2020-01-01T11:00", "userId": "003352d0-e699-48bc-b6f5-5810411e60ag", "comment": "Need Access to case number 1234567890123456 for a month"}, {"time": "2020-01-02T00:00", "userId": "52aa3810-af1f-11ea-b3de-0242ac130004", "comment": "Access granted for 6 months"}]', NULL, 0, '2020-06-25 12:32:08.180');
