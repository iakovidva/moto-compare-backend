DROP TYPE IF EXISTS motorcycle_manufacturer CASCADE;
DROP TYPE IF EXISTS motorcycle_category CASCADE;

CREATE TYPE motorcycle_manufacturer AS ENUM (
    'HONDA', 'YAMAHA', 'KAWASAKI', 'SUZUKI', 'HARLEY_DAVIDSON',
    'DUCATI', 'BMW', 'KTM', 'KOVE', 'TRIUMPH', 'ROYAL_ENFIELD',
    'APRILIA', 'MV_AGUSTA', 'INDIAN_MOTORCYCLE', 'MOTO_GUZZI',
    'BAJAJ', 'PIAGGIO', 'HYOSUNG', 'CFMOTO', 'BENELLI',
    'ZERO_MOTORCYCLES', 'HUSQVARNA', 'VICTORY', 'URAL', 'SYM',
    'KYMCO', 'BIMOTA', 'MORBIDELLI', 'LAMBRETTA', 'QJ_MOTOR', 'NORTON',
    'VOGE'
);

CREATE TYPE motorcycle_category AS ENUM (
    'ADVENTURE', 'NAKED', 'SUPERSPORT', 'CRUISER', 'TOURING',
    'DUAL_SPORT', 'SCOOTER', 'UNDERBONE', 'ENDURO', 'CAFE_RACER',
    'SCRAMBLER', 'SPORT_TOURING', 'MOTOCROSS', 'ELECTRIC', 'CUSTOM', 'SIDECAR'
);


CREATE TABLE IF NOT EXISTS motorcycles (
    id SERIAL PRIMARY KEY,
    manufacturer motorcycle_manufacturer NOT NULL,
    model VARCHAR(255) NOT NULL,
    year_range VARCHAR(50) NOT NULL,
    image TEXT NOT NULL,
    category motorcycle_category NOT NULL,

    -- Engine fields
    engine_design VARCHAR(255) NOT NULL,
    displacement INT NOT NULL,
    horse_power INT NOT NULL,
    torque INT NOT NULL,
    bore INT,
    stroke INT,
    compression_ratio DECIMAL(5,2),
    cooling_system VARCHAR(255),
    fuel_consumption DECIMAL(5,2) NOT NULL,
    emissions VARCHAR(255),
    fuel_system VARCHAR(255),
    throttle_control VARCHAR(255),
    clutch VARCHAR(255),
    transmission VARCHAR(50),

    -- Chassis
    weight INT NOT NULL,
    tank_capacity INT NOT NULL,
    wheelbase INT,
    ground_clearance INT,
    frame_design VARCHAR(255),
    front_suspension VARCHAR(255),
    rear_suspension VARCHAR(255),
    front_suspension_travel INT,
    rear_suspension_travel INT,
    seat_height VARCHAR(50),

    -- Wheels
    wheels_type VARCHAR(255),
    front_tyre VARCHAR(50),
    rear_tyre VARCHAR(50),
    front_wheel_size INT NOT NULL,
    rear_wheel_size INT NOT NULL,

    -- Brakes
    front_brake VARCHAR(255),
    rear_brake VARCHAR(255),
    front_disc_diameter INT,
    rear_disc_diameter INT,
    abs BOOLEAN,

    -- Electronics
    dash_display VARCHAR(255),
    riding_modes VARCHAR(255),
    traction_control BOOLEAN,
    quick_shifter VARCHAR(255),
    cruise_control BOOLEAN,
    lighting VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_motorcycle UNIQUE (manufacturer, model, displacement, year_range)
);

-- Many-to-Many relationship for similar motorcycles
CREATE TABLE IF NOT EXISTS similar_motorcycles (
    motorcycle_id INT NOT NULL,
    similar_motorcycle_id INT NOT NULL,
    PRIMARY KEY (motorcycle_id, similar_motorcycle_id),
    CONSTRAINT fk_motorcycle FOREIGN KEY (motorcycle_id) REFERENCES motorcycles(id) ON DELETE CASCADE,
    CONSTRAINT fk_similar_motorcycle FOREIGN KEY (similar_motorcycle_id) REFERENCES motorcycles(id) ON DELETE CASCADE,
    CONSTRAINT chk_not_self_referencing CHECK (motorcycle_id <> similar_motorcycle_id) -- Prevent self-referencing
);

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user UNIQUE (user_name, email)
);

CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    motorcycle_id BIGINT NOT NULL,
    user_id UUID NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_motorcycle FOREIGN KEY (motorcycle_id) REFERENCES motorcycles(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_review UNIQUE (motorcycle_id, user_id, created_at)
);

CREATE TABLE IF NOT EXISTS user_requests (
    id SERIAL PRIMARY KEY,
    new_motorcycle_request BOOLEAN NOT NULL,
    request_content jsonb,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_favorites (
    user_id UUID NOT NULL,
    motorcycle_id SERIAL NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_motorcycle UNIQUE (user_id, motorcycle_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_motorcycle FOREIGN KEY (motorcycle_id) REFERENCES motorcycles(id) ON DELETE CASCADE
);