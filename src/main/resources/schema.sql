DROP TABLE IF EXISTS  "user";

CREATE TABLE "user"(
    uid uuid DEFAULT gen_random_uuid(),
    first_name VARCHAR(25),
    last_name VARCHAR(25)
);
