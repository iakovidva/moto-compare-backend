DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'motorcycle_manufacturer') THEN
        CREATE TYPE motorcycle_manufacturer AS ENUM (
            'HONDA', 'YAMAHA', 'KAWASAKI', 'SUZUKI', 'HARLEY_DAVIDSON',
            'DUCATI', 'BMW', 'KTM', 'KOVE', 'TRIUMPH', 'ROYAL_ENFIELD',
            'APRILIA', 'MV_AGUSTA', 'INDIAN_MOTORCYCLE', 'MOTO_GUZZI',
            'BAJAJ', 'PIAGGIO', 'HYOSUNG', 'CFMOTO', 'BENELLI',
            'ZERO_MOTORCYCLES', 'HUSQVARNA', 'VICTORY', 'URAL', 'SYM',
            'KYMCO', 'BIMOTA', 'MORBIDELLI', 'LAMBRETTA', 'QJ_MOTOR', 'NORTON',
            'VOGE'
        );
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'motorcycle_category') THEN
        CREATE TYPE motorcycle_category AS ENUM (
            'ADVENTURE', 'NAKED', 'SUPERSPORT', 'CRUISER', 'TOURING',
            'DUAL_SPORT', 'SCOOTER', 'UNDERBONE', 'ENDURO', 'CAFE_RACER',
            'SCRAMBLER', 'SPORT_TOURING', 'MOTOCROSS', 'ELECTRIC', 'CUSTOM', 'SIDECAR'
        );
    END IF;
END
$$;