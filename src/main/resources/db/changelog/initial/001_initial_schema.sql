-- liquibase formatted sql

-- changeset aleksey:001_create_users_table
-- precondition: not tableExists table_name=users
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
-- precondition: tableExists table_name=users
-- precondition: not tableExists table_name=user_roles
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            PRIMARY KEY (user_id, role),
                            CONSTRAINT fk_user_roles_user_id
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE
);

-- changeset aleksey:003_create_equipment_types_table
-- precondition: not tableExists table_name=equipment_types
CREATE TABLE equipment_types (
                                 id BIGSERIAL PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL UNIQUE,
                                 description TEXT,
                                 category VARCHAR(100) NOT NULL
);

-- changeset aleksey:004_create_products_table
-- precondition: tableExists table_name=users
-- precondition: tableExists table_name=equipment_types
-- precondition: not tableExists table_name=products
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
                          equipment_type_id BIGINT NOT NULL,
                          CONSTRAINT fk_products_owner
                              FOREIGN KEY (id_owner)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,
                          CONSTRAINT fk_products_equipment_type
                              FOREIGN KEY (equipment_type_id)
                                  REFERENCES equipment_types(id)
                                  ON DELETE RESTRICT
);

-- changeset aleksey:005_create_indexes
-- precondition: tableExists table_name=users
-- precondition: tableExists table_name=user_roles
-- precondition: tableExists table_name=products
-- precondition: tableExists table_name=equipment_types
-- precondition: not indexExists table_name=users indexName=idx_users_email
-- precondition: not indexExists table_name=user_roles indexName=idx_user_roles_user_id
-- precondition: not indexExists table_name=products indexName=idx_products_owner
-- precondition: not indexExists table_name=products indexName=idx_products_equipment_type
-- precondition: not indexExists table_name=equipment_types indexName=idx_equipment_types_category
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_products_owner ON products(id_owner);
CREATE INDEX idx_products_equipment_type ON products(equipment_type_id);
CREATE INDEX idx_equipment_types_category ON equipment_types(category);

-- changeset aleksey:006_insert_equipment_types
-- precondition: tableExists table_name=equipment_types
-- precondition: sqlCheck expectedResult:0
SELECT COUNT(*) FROM equipment_types WHERE name = 'Строительное оборудование';
INSERT INTO equipment_types (name, description, category) VALUES
                                                              ('Строительное оборудование', 'Оборудование для строительных работ', 'СТРОИТЕЛЬСТВО'),
                                                              ('Сельхозтехника', 'Техника для сельскохозяйственных работ', 'СЕЛЬСКОЕ_ХОЗЯЙСТВО'),
                                                              ('Электроинструмент', 'Электрические инструменты', 'ИНСТРУМЕНТЫ'),
                                                              ('Грузовой транспорт', 'Транспорт для перевозки грузов', 'ТРАНСПОРТ'),
                                                              ('Спецтехника', 'Специализированная техника', 'ТЕХНИКА'),
                                                              ('Офисная техника', 'Техника для офиса', 'ОФИС'),
                                                              ('Производственное оборудование', 'Оборудование для производственных нужд', 'ПРОИЗВОДСТВО'),
                                                              ('Измерительные приборы', 'Приборы для измерений и контроля', 'ИЗМЕРЕНИЯ');

-- changeset aleksey:007_insert_electronics_types
-- precondition: tableExists table_name=equipment_types
-- precondition: sqlCheck expectedResult:0
SELECT COUNT(*) FROM equipment_types WHERE category = 'ЭЛЕКТРОНИКА';
INSERT INTO equipment_types (name, description, category) VALUES
                                                              ('Смартфоны и телефоны', 'Мобильные телефоны и смартфоны', 'ЭЛЕКТРОНИКА'),
                                                              ('Ноутбуки и ультрабуки', 'Портативные компьютеры', 'ЭЛЕКТРОНИКА'),
                                                              ('Планшеты и электронные книги', 'Планшеты и устройства для чтения', 'ЭЛЕКТРОНИКА'),
                                                              ('Фото и видеокамеры', 'Цифровые фотоаппараты и видеокамеры', 'ЭЛЕКТРОНИКА'),
                                                              ('Аудиотехника', 'Наушники, колонки, усилители', 'ЭЛЕКТРОНИКА'),
                                                              ('Игровые консоли и аксессуары', 'Игровые приставки и оборудование', 'ЭЛЕКТРОНИКА'),
                                                              ('Телевизоры и проекторы', 'Телевизоры, мониторы, проекторы', 'ЭЛЕКТРОНИКА'),
                                                              ('Умные часы и фитнес-трекеры', 'Носимые устройства', 'ЭЛЕКТРОНИКА'),
                                                              ('Сетевое оборудование', 'Роутеры, коммутаторы, точки доступа', 'ЭЛЕКТРОНИКА'),
                                                              ('Компьютерные компоненты', 'Процессоры, видеокарты, память', 'ЭЛЕКТРОНИКА');

-- changeset aleksey:008_prepare_for_replication
-- precondition: tableExists table_name=users
-- precondition: tableExists table_name=user_roles
-- precondition: tableExists table_name=products
-- precondition: tableExists table_name=equipment_types
ALTER TABLE users REPLICA IDENTITY FULL;
ALTER TABLE user_roles REPLICA IDENTITY FULL;
ALTER TABLE products REPLICA IDENTITY FULL;
ALTER TABLE equipment_types REPLICA IDENTITY FULL;