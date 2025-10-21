-- liquibase formatted sql

-- changeset aleksey:001_create_users_table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255),
                       provider VARCHAR(50) NOT NULL,
                       deals_as_owner INTEGER DEFAULT 0,
                       deals_as_customer INTEGER DEFAULT 0
);

-- changeset aleksey:002_create_user_roles_table
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            PRIMARY KEY (user_id, role),
                            CONSTRAINT fk_user_roles_user_id
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE
);

-- changeset aleksey:003_create_products_table
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          description TEXT,
                          name VARCHAR(255) NOT NULL,
                          rental_price DECIMAL(10,2) NOT NULL,
                          quantity INTEGER NOT NULL DEFAULT 1,
                          min_rental_period INTEGER DEFAULT 1,
                          max_rental_period INTEGER,
                          image VARCHAR,
                          id_owner BIGINT NOT NULL,
                          CONSTRAINT fk_products_owner
                              FOREIGN KEY (id_owner)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);

-- changeset aleksey:004_create_indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_products_owner ON products(id_owner);

-- changeset aleksey:005_prepare_for_replication
ALTER TABLE users REPLICA IDENTITY FULL;
ALTER TABLE user_roles REPLICA IDENTITY FULL;
ALTER TABLE products REPLICA IDENTITY FULL;