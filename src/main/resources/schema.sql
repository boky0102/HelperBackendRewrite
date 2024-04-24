DROP TABLE IF EXISTS  "user";

CREATE TABLE "user"(
    uid uuid DEFAULT gen_random_uuid(),
    user_name VARCHAR(25),
    first_name VARCHAR(25),
    last_name VARCHAR(25),
    password VARCHAR(255),
    created_at DATE,
    aid integer
);
