-- Insert motorcycles
INSERT INTO motorcycles (manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
                         bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
                         throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
                         frame_design, front_suspension, rear_suspension, front_suspension_travel,
                         rear_suspension_travel, seat_height, front_brake, rear_brake, front_disc_diameter,
                         rear_disc_diameter, abs, wheels_type, front_tyre, rear_tyre, front_wheel_size, rear_wheel_size,
                         dash_display, riding_modes, traction_control, quick_shifter, cruise_control, lighting)
VALUES
    ('KTM'::motorcycle_manufacturer, '790 Adventure', '2019-2023', '/images/motorcycles/ktm-790-adventure.jpg', 'ADVENTURE'::motorcycle_category, '2-cylinder, 4-stroke, parallel twin',
     799, 95, 88, 88, 65.7, 12.7, 'Liquid cooled', 4.3, 'Euro5', 'Electronic fuel injection', 'Ride-by-Wire',
     'PASC slipper clutch', '6-speed', 189, 20, 1509, 233, 'Chromium-Molybdenum-Steel frame', 'WP APEX 43mm USD forks',
     'WP APEX monoshock', 200, 200, '830mm-850mm', 'Dual disc with 4-piston caliper', 'Single disc with 2-piston caliper',
     320, 260, TRUE, 'Spoked wheels with aluminum tubeless rims', '90/90', '150/70', 21, 18,
     '5" TFT display', 'Street, Offroad, Rain', TRUE, 'Up/Down Quickshifter+', TRUE, 'Full LED')
     ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
                         bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
                         throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
                         frame_design, front_suspension, rear_suspension, front_suspension_travel,
                         rear_suspension_travel, seat_height, front_brake, rear_brake, front_disc_diameter,
                         rear_disc_diameter, abs, wheels_type, front_tyre, rear_tyre, front_wheel_size, rear_wheel_size,
                         dash_display, riding_modes, traction_control, quick_shifter, cruise_control, lighting)
VALUES
    ('YAMAHA'::motorcycle_manufacturer, 'Tenere 700', '2019-2023', '/images/motorcycles/yamaha-tenere-700.jpeg', 'ADVENTURE'::motorcycle_category, '2-cylinder, 4-stroke, parallel twin',
     689, 73, 68, 80, 68.6, 11.5, 'Liquid cooled', 4.2, 'Euro5', 'Fuel Injection', 'Cable throttle',
     'Assist & Slipper clutch', '6-speed', 204, 16, 1590, 240, 'Steel double cradle frame',
     'KYB 43mm USD forks', 'KYB monoshock', 210, 200, '875mm', 'Dual disc with 4-piston caliper',
     'Single disc with 2-piston caliper', 282, 245, FALSE, 'Spoked wheels with tube-type rims', '90/90', '150/70',
     21, 18, 'LCD display', 'None', FALSE, 'None', FALSE, 'LED Headlight')
     ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
                         bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
                         throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
                         frame_design, front_suspension, rear_suspension, front_suspension_travel,
                         rear_suspension_travel, seat_height, front_brake, rear_brake, front_disc_diameter,
                         rear_disc_diameter, abs, wheels_type, front_tyre, rear_tyre, front_wheel_size, rear_wheel_size,
                         dash_display, riding_modes, traction_control, quick_shifter, cruise_control, lighting)
VALUES
    ('DUCATI'::motorcycle_manufacturer, 'DesertX', '2022-2024', '/images/motorcycles/ducati-desert-X.png', 'ADVENTURE'::motorcycle_category, '2-cylinder, 4-stroke, L-twin',
     937, 110, 92, 94, 67.5, 13.3, 'Liquid cooled', 5.0, 'Euro5', 'Electronic fuel injection', 'Ride-by-Wire',
     'Slipper clutch', '6-speed', 223, 21, 1608, 250, 'Tubular steel trellis frame', 'KYB 46mm fully adjustable USD forks',
     'KYB monoshock', 230, 220, '875mm', 'Dual disc with 4-piston Brembo caliper', 'Single disc with 2-piston caliper',
     320, 265, TRUE, 'Spoked wheels with aluminum tubeless rims', '90/90', '150/70', 21, 18,
     '5" TFT display', 'Sport, Touring, Urban, Enduro', TRUE, 'Up/Down Quickshifter', TRUE, 'Full LED with DRL')
     ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
                         bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
                         throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
                         frame_design, front_suspension, rear_suspension, front_suspension_travel,
                         rear_suspension_travel, seat_height, front_brake, rear_brake, front_disc_diameter,
                         rear_disc_diameter, abs, wheels_type, front_tyre, rear_tyre, front_wheel_size, rear_wheel_size,
                         dash_display, riding_modes, traction_control, quick_shifter, cruise_control, lighting)
VALUES
    ('HONDA'::motorcycle_manufacturer, 'Africa Twin 1100', '2020-2024', '/images/motorcycles/honda-africa-twin-1100.jpg', 'ADVENTURE'::motorcycle_category, '2-cylinder, 4-stroke, parallel twin',
     1084, 101, 105, 92, 81.5, 10.1, 'Liquid cooled', 4.8, 'Euro5', 'PGM-FI electronic fuel injection', 'Ride-by-Wire',
     'Assist & Slipper clutch', '6-speed (DCT optional)', 226, 18.8, 1575, 250, 'Steel semi-double cradle frame',
     'Showa 45mm fully adjustable USD forks', 'Showa Pro-Link monoshock', 230, 220, '850mm-870mm',
     'Dual disc with 4-piston caliper', 'Single disc with 1-piston caliper', 310, 265, TRUE,
     'Spoked wheels with tube-type rims', '90/90', '150/70', 21, 18, '6.5" TFT touchscreen', 'Tour, Urban, Gravel, Off-road',
     TRUE, 'Up/Down Quickshifter (optional)', TRUE, 'LED Headlight with DRL')
     ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
                         bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
                         throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
                         frame_design, front_suspension, rear_suspension, front_suspension_travel,
                         rear_suspension_travel, seat_height, front_brake, rear_brake, front_disc_diameter,
                         rear_disc_diameter, abs, wheels_type, front_tyre, rear_tyre, front_wheel_size, rear_wheel_size,
                         dash_display, riding_modes, traction_control, quick_shifter, cruise_control, lighting)
VALUES
    ('KOVE'::motorcycle_manufacturer, '800X', '2023-2024', '/images/motorcycles/kove-800x.png', 'ADVENTURE'::motorcycle_category, '2-cylinder, 4-stroke, parallel twin',
     799, 94, 80, 88, 66.5, 12.5, 'Liquid cooled', 4.5, 'Euro5', 'Fuel Injection', 'Cable throttle',
     'Wet multi-plate clutch', '6-speed', 190, 22, 1550, 240, 'Steel trellis frame',
     'KYB 43mm USD forks', 'KYB monoshock', 200, 200, '850mm', 'Dual disc with 4-piston caliper',
     'Single disc with 2-piston caliper', 310, 260, TRUE, 'Spoked wheels with aluminum tubeless rims',
     '90/90', '150/70', 21, 18, 'LCD display', 'Offroad, Street', TRUE, 'None', FALSE, 'LED Headlight with DRL')
     ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
                         bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
                         throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
                         frame_design, front_suspension, rear_suspension, front_suspension_travel,
                         rear_suspension_travel, seat_height, front_brake, rear_brake, front_disc_diameter,
                         rear_disc_diameter, abs, wheels_type, front_tyre, rear_tyre, front_wheel_size, rear_wheel_size,
                         dash_display, riding_modes, traction_control, quick_shifter, cruise_control, lighting)
VALUES
    ('HONDA'::motorcycle_manufacturer, 'Transalp 750', '2024', '/images/motorcycles/honda-transalp-750.png', 'ADVENTURE'::motorcycle_category, '2-cylinder, 4-stroke, parallel twin',
     755, 90, 75, 87, 63.5, 11.0, 'Liquid cooled', 4.5, 'Euro5', 'PGM-FI electronic fuel injection', 'Ride-by-Wire',
     'Assist & Slipper clutch', '6-speed', 208, 16.9, 1560, 210, 'Steel diamond frame',
     'Showa 43mm inverted telescopic fork', 'Showa Pro-Link monoshock', 200, 190, '850mm',
     'Dual disc with 2-piston caliper', 'Single disc with 1-piston caliper', 310, 256, TRUE,
     'Spoked wheels (tubeless)', '90/90', '150/70', 21, 18, '5-inch TFT color display', 'Standard, Rain, Gravel, User',
     TRUE, 'Up/Down Quickshifter (optional)', TRUE, 'Full LED with DRL')
     ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
                         bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
                         throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
                         frame_design, front_suspension, rear_suspension, front_suspension_travel,
                         rear_suspension_travel, seat_height, front_brake, rear_brake, front_disc_diameter,
                         rear_disc_diameter, abs, wheels_type, front_tyre, rear_tyre, front_wheel_size, rear_wheel_size,
                         dash_display, riding_modes, traction_control, quick_shifter, cruise_control, lighting)
VALUES
    ('DUCATI'::motorcycle_manufacturer, 'Scrambler Desert Sled', '2020', '/images/motorcycles/ducati-scrambler-desert-sled.webp', 'SCRAMBLER'::motorcycle_category, '2-cylinder, 4-stroke, L-twin',
     803, 73, 67, 88, 66, 11.1, 'Air-cooled', 5.2, 'Euro5', 'Electronic fuel injection', 'Cable throttle',
     'Wet multiplate clutch', '6-speed', 207, 13.5, 1505, 220, 'Tubular steel trellis frame',
     'KYB 46mm upside-down forks', 'KYB rear shock', 200, 200, '860mm',
     'Single disc with 4-piston Brembo caliper', 'Single disc with 1-piston caliper', 330, 245, TRUE,
     'Spoked wheels (tubeless)', '120/70', '180/55', 19, 17, 'LCD display', 'None', FALSE, 'None', FALSE, 'LED headlight')
     ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
                         bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
                         throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
                         frame_design, front_suspension, rear_suspension, front_suspension_travel,
                         rear_suspension_travel, seat_height, front_brake, rear_brake, front_disc_diameter,
                         rear_disc_diameter, abs, wheels_type, front_tyre, rear_tyre, front_wheel_size, rear_wheel_size,
                         dash_display, riding_modes, traction_control, quick_shifter, cruise_control, lighting)
VALUES
    ('YAMAHA'::motorcycle_manufacturer, 'Tracer 900 GT', '2023', '/images/motorcycles/yamaha-tracer-900-gt.png', 'SPORT_TOURING'::motorcycle_category, '3-cylinder, 4-stroke, inline triple',
     890, 119, 93, 78, 59.1, 11.5, 'Liquid cooled', 4.8, 'Euro5', 'Fuel Injection', 'Ride-by-Wire',
     'Assist & Slipper clutch', '6-speed', 220, 18, 1500, 135, 'Aluminum die-cast frame',
     'KYB 41mm inverted forks', 'KYB monoshock', 130, 142, '850mm-865mm',
     'Dual disc with 4-piston caliper', 'Single disc with 1-piston caliper', 298, 245, TRUE,
     'Cast aluminum wheels', '120/70', '180/55', 17, 17, 'TFT color display', 'Standard, Rain, Sport, Touring',
     TRUE, 'Up/Down Quickshifter', TRUE, 'LED Headlight with cornering lights')
     ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (
    manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
    bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
    throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
    frame_design, front_suspension, rear_suspension, front_suspension_travel, rear_suspension_travel,
    seat_height, front_brake, rear_brake, front_disc_diameter, rear_disc_diameter, abs, wheels_type,
    front_tyre, rear_tyre, front_wheel_size, rear_wheel_size, dash_display, riding_modes,
    traction_control, quick_shifter, cruise_control, lighting
) VALUES (
    'CFMOTO'::motorcycle_manufacturer, '450MT', '2024', '/images/motorcycles/cfmoto-450-mt.png', 'ADVENTURE'::motorcycle_category,
    '2-cylinder, 4-stroke, parallel twin', 449, 44, 42, 72, 55.2, 11.5, 'Liquid cooled', 3.8, 'Euro5',
    'Electronic fuel injection', 'Ride-by-Wire', 'Assist & Slipper clutch', '6-speed', 175, 17.5, 1510, 200,
    'Steel trellis frame', 'KYB 41mm inverted forks', 'KYB monoshock', 180, 180, '820mm',
    'Single disc with 4-piston J.Juan caliper', 'Single disc with 1-piston caliper', 320, 240, TRUE,
    'Spoked wheels (tubeless)', '90/90', '140/80', 19, 17, '5-inch TFT display', 'Rain, Street, Off-road',
    TRUE, 'None', FALSE, 'LED with DRL'
) ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (
    manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
    bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
    throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
    frame_design, front_suspension, rear_suspension, front_suspension_travel, rear_suspension_travel,
    seat_height, front_brake, rear_brake, front_disc_diameter, rear_disc_diameter, abs, wheels_type,
    front_tyre, rear_tyre, front_wheel_size, rear_wheel_size, dash_display, riding_modes,
    traction_control, quick_shifter, cruise_control, lighting
) VALUES (
    'HONDA'::motorcycle_manufacturer, 'NX500', '2025', '/images/motorcycles/honda-nx500.webp', 'ADVENTURE'::motorcycle_category,
    '2-cylinder, 4-stroke, parallel twin', 471, 47, 43, 67, 66.8, 10.7, 'Liquid cooled', 3.5, 'Euro5+',
    'PGM-FI electronic fuel injection', 'Cable throttle', 'Assist & Slipper clutch', '6-speed', 196, 17.1, 1445, 180,
    'Steel diamond frame', 'Showa 41mm SFF-BP inverted fork', 'Showa Pro-Link monoshock', 150, 150, '830mm',
    'Single disc with 2-piston caliper', 'Single disc with 1-piston caliper', 296, 240, TRUE,
    'Cast aluminum wheels', '120/70', '160/60', 17, 17, '5-inch LCD display', 'Standard, Rain, Gravel',
    FALSE, 'None', FALSE, 'LED headlight'
) ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (
    manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
    bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
    throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
    frame_design, front_suspension, rear_suspension, front_suspension_travel, rear_suspension_travel,
    seat_height, front_brake, rear_brake, front_disc_diameter, rear_disc_diameter, abs, wheels_type,
    front_tyre, rear_tyre, front_wheel_size, rear_wheel_size, dash_display, riding_modes,
    traction_control, quick_shifter, cruise_control, lighting
) VALUES (
    'SUZUKI'::motorcycle_manufacturer, 'V-Strom 800DE', '2024', '/images/motorcycles/suzuki-vstrom-800-de.jpg', 'ADVENTURE'::motorcycle_category,
    '2-cylinder, 4-stroke, parallel twin', 776, 83, 78, 84, 70, 12.8, 'Liquid cooled', 4.3, 'Euro5',
    'Electronic fuel injection', 'Ride-by-Wire', 'Assist & Slipper clutch', '6-speed', 230, 20, 1575, 220,
    'Steel twin-spar frame', 'Showa 43mm inverted forks (fully adjustable)', 'Showa monoshock', 220, 220, '855mm',
    'Dual disc with 4-piston Brembo caliper', 'Single disc with 1-piston caliper', 310, 260, TRUE,
    'Spoked wheels (tubeless)', '90/90', '150/70', 21, 17, '5-inch TFT display', 'Active, Basic, Comfort, Off-road',
    TRUE, 'Up/Down Quickshifter (optional)', TRUE, 'LED with cornering lights'
) ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (
    manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
    bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
    throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
    frame_design, front_suspension, rear_suspension, front_suspension_travel, rear_suspension_travel,
    seat_height, front_brake, rear_brake, front_disc_diameter, rear_disc_diameter, abs, wheels_type,
    front_tyre, rear_tyre, front_wheel_size, rear_wheel_size, dash_display, riding_modes,
    traction_control, quick_shifter, cruise_control, lighting
) VALUES (
    'VOGE'::motorcycle_manufacturer, '525DSX', '2023', '/images/motorcycles/voge-525dsx.jpg', 'ADVENTURE'::motorcycle_category,
    '2-cylinder, 4-stroke, parallel twin', 494, 47, 43, 68, 68, 11.5, 'Liquid cooled', 4.0, 'Euro5',
    'Electronic fuel injection', 'Ride-by-Wire', 'Wet multi-plate clutch', '6-speed', 205, 18, 1480, 210,
    'Steel trellis frame', 'KYB 41mm inverted forks', 'KYB monoshock', 180, 180, '830mm',
    'Single disc with 2-piston caliper', 'Single disc with 1-piston caliper', 298, 240, TRUE,
    'Spoked wheels (tubeless)', '110/80', '150/70', 19, 17, 'TFT color display', 'Road, Off-road',
    TRUE, 'None', FALSE, 'LED headlight'
) ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (
    manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
    bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
    throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
    frame_design, front_suspension, rear_suspension, front_suspension_travel, rear_suspension_travel,
    seat_height, front_brake, rear_brake, front_disc_diameter, rear_disc_diameter, abs, wheels_type,
    front_tyre, rear_tyre, front_wheel_size, rear_wheel_size, dash_display, riding_modes,
    traction_control, quick_shifter, cruise_control, lighting
) VALUES (
    'YAMAHA'::motorcycle_manufacturer, 'MT-03', '2023', '/images/motorcycles/yamaha-mt-03.jpg', 'NAKED'::motorcycle_category,
    '1-cylinder, 4-stroke, single', 321, 42, 30, 68, 44, 11.2, 'Liquid cooled', 3.6, 'Euro5',
    'Fuel Injection', 'Cable throttle', 'Wet multi-plate clutch', '6-speed', 168, 14, 1380, 160,
    'Steel diamond frame', 'KYB 37mm USD forks', 'KYB monoshock', 130, 125, '780mm',
    'Single disc with 2-piston caliper', 'Single disc with 1-piston caliper', 298, 220, TRUE,
    'Cast aluminum wheels', '110/70', '140/70', 17, 17, 'LCD display', 'None',
    FALSE, 'None', FALSE, 'LED headlight'
) ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (
    manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
    bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
    throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
    frame_design, front_suspension, rear_suspension, front_suspension_travel, rear_suspension_travel,
    seat_height, front_brake, rear_brake, front_disc_diameter, rear_disc_diameter, abs, wheels_type,
    front_tyre, rear_tyre, front_wheel_size, rear_wheel_size, dash_display, riding_modes,
    traction_control, quick_shifter, cruise_control, lighting
) VALUES (
    'BMW'::motorcycle_manufacturer, 'R 1300 GS Adventure', '2024', '/images/motorcycles/bmw-gs-1300-adventure.webp', 'ADVENTURE'::motorcycle_category,
    '2-cylinder, 4-stroke, boxer twin', 1300, 143, 149, 106, 73, 13.3, 'Liquid/oil-cooled', 5.2, 'Euro5+',
    'Electronic fuel injection', 'Ride-by-Wire', 'Wet multi-plate clutch', '6-speed', 268, 30, 1518, 250,
    'Steel bridge frame', 'Electronic Dynamic ESA', 'Electronic Dynamic ESA', 250, 250, '850mm-890mm',
    'Dual disc with 4-piston radial caliper', 'Single disc with 2-piston caliper', 305, 285, TRUE,
    'Spoked wheels (tubeless)', '120/70', '170/60', 19, 17, '6.5" TFT display', 'Rain, Road, Dynamic, Enduro Pro',
    TRUE, 'Up/Down Quickshifter', TRUE, 'Full LED with adaptive headlight'
) ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (
    manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
    bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
    throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
    frame_design, front_suspension, rear_suspension, front_suspension_travel, rear_suspension_travel,
    seat_height, front_brake, rear_brake, front_disc_diameter, rear_disc_diameter, abs, wheels_type,
    front_tyre, rear_tyre, front_wheel_size, rear_wheel_size, dash_display, riding_modes,
    traction_control, quick_shifter, cruise_control, lighting
) VALUES (
    'TRIUMPH'::motorcycle_manufacturer, 'Tiger Sport 660', '2023', '/images/motorcycles/tiger-660-sport.jpeg', 'SPORT_TOURING'::motorcycle_category,
    '3-cylinder, 4-stroke, inline triple', 660, 81, 64, 74, 51, 11.95, 'Liquid cooled', 4.3, 'Euro5',
    'Electronic fuel injection', 'Ride-by-Wire', 'Wet multi-plate clutch', '6-speed', 206, 17, 1418, 150,
    'Steel perimeter frame', 'Showa 41mm USD forks', 'Showa monoshock', 150, 150, '835mm',
    'Dual disc with 2-piston caliper', 'Single disc with 1-piston caliper', 310, 255, TRUE,
    'Cast aluminum wheels', '120/70', '180/55', 17, 17, 'TFT display', 'Road, Rain, Sport',
    TRUE, 'Up/Down Quickshifter', TRUE, 'LED with DRL'
) ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;

INSERT INTO motorcycles (
    manufacturer, model, year_range, image, category, engine_design, displacement, horse_power, torque,
    bore, stroke, compression_ratio, cooling_system, fuel_consumption, emissions, fuel_system,
    throttle_control, clutch, transmission, weight, tank_capacity, wheelbase, ground_clearance,
    frame_design, front_suspension, rear_suspension, front_suspension_travel, rear_suspension_travel,
    seat_height, front_brake, rear_brake, front_disc_diameter, rear_disc_diameter, abs, wheels_type,
    front_tyre, rear_tyre, front_wheel_size, rear_wheel_size, dash_display, riding_modes,
    traction_control, quick_shifter, cruise_control, lighting
) VALUES (
    'APRILIA'::motorcycle_manufacturer, 'Tuareg 660', '2023', '/images/motorcycles/aprillia-tuareg-660.png', 'ADVENTURE'::motorcycle_category,
    '2-cylinder, 4-stroke, parallel twin', 659, 80, 70, 81, 63.9, 13.5, 'Liquid cooled', 4.5, 'Euro5',
    'Electronic fuel injection', 'Ride-by-Wire', 'Wet multi-plate clutch', '6-speed', 204, 18, 1525, 240,
    'Steel trellis frame', 'Kayaba 43mm USD forks (fully adjustable)', 'Kayaba monoshock', 240, 240, '860mm',
    'Dual disc with 4-piston Brembo caliper', 'Single disc with 1-piston caliper', 300, 260, TRUE,
    'Spoked wheels (tubeless)', '90/90', '150/70', 21, 18, '5" TFT display', 'Urban, Explore, Off-road',
    TRUE, 'Up/Down Quickshifter', TRUE, 'Full LED'
) ON CONFLICT (manufacturer, model, displacement, year_range) DO NOTHING;
-- Insert similar motorcycles relationships
INSERT INTO similar_motorcycles (motorcycle_id, similar_motorcycle_id) VALUES
    ((SELECT id FROM motorcycles WHERE model = '790 Adventure'), (SELECT id FROM motorcycles WHERE model = 'Tenere 700')),
    ((SELECT id FROM motorcycles WHERE model = '800X'), (SELECT id FROM motorcycles WHERE model = 'Tenere 700')),
    ((SELECT id FROM motorcycles WHERE model = '790 Adventure'), (SELECT id FROM motorcycles WHERE model = '800X'))
    ON CONFLICT (motorcycle_id, similar_motorcycle_id) DO NOTHING;


--securepassword123   ->  $2a$10$hqfdKPGTrN2rRsniOhsPB.kXEUHvk/I6PPAzfZrkufgmo3Et9NNLa
--adminpass456        ->  $2a$10$d3inlVMCUtx9HELRIK/6..JzBTVFEJx/TfjkJeglF2BZNSIRzdyf6
--mypassword789       ->  $2a$10$jsNv9cMJscL23yXJaxLF/OncVvjo/03EYBGBu..fkgzJrbW7UaDW6ads
INSERT INTO users (id, user_name, email, password, role)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'john_doe', 'john.doe@example.com', '$2a$10$hqfdKPGTrN2rRsniOhsPB.kXEUHvk/I6PPAzfZrkufgmo3Et9NNLa', 'USER'),
    ('660f9511-f40c-42a1-b18d-550a9a660bfa', 'admin_user', 'admin@example.com', '$2a$10$d3inlVMCUtx9HELRIK/6..JzBTVFEJx/TfjkJeglF2BZNSIRzdyf6', 'ADMIN'),
    ('770c8302-c44d-49b3-b0d9-661b9b771acb', 'jane_doe', 'jane.doe@example.com', '$2a$10$jsNv9cMJscL23yXJaxLF/OncVvjo/03EYBGBu..fkgzJrbW7UaDW6', 'USER')
        ON CONFLICT (user_name, email) DO NOTHING;

INSERT INTO reviews (motorcycle_id, user_id, rating, comment, created_at)
VALUES
    (1, '550e8400-e29b-41d4-a716-446655440000', 5, 'Amazing ride, very smooth and powerful!', '2025-03-15 10:00:00'),
    (2, '660f9511-f40c-42a1-b18d-550a9a660bfa', 4, 'Great bike, but a bit heavy.', '2025-03-14 12:30:00')
    ON CONFLICT (motorcycle_id, user_id, created_at) DO NOTHING;

INSERT INTO category_statistics (category, counter, created_at, updated_at) VALUES
    ('ADVENTURE', 13, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('SCRAMBLER', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('SPORT_TOURING', 2, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('NAKED', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00')
ON CONFLICT (category) DO UPDATE SET counter = EXCLUDED.counter, updated_at = EXCLUDED.updated_at;

INSERT INTO manufacturer_statistics (manufacturer, counter, created_at, updated_at) VALUES
    ('HONDA', 3, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('YAMAHA', 3, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('DUCATI', 2, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('KTM', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('KOVE', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('CFMOTO', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('SUZUKI', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('VOGE', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('BMW', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('TRIUMPH', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00'),
    ('APRILIA', 1, '2025-07-01 10:00:00', '2025-07-01 10:00:00')
ON CONFLICT (manufacturer) DO UPDATE SET counter = EXCLUDED.counter, updated_at = EXCLUDED.updated_at;