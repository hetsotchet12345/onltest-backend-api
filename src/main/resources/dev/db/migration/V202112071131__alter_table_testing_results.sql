ALTER TABLE testing_results
    ADD created_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW();

ALTER TABLE testing_results
    ADD updated_date TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW();