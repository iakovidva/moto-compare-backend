package com.vaiak.moto_compare.models;

import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "motorcycles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Motorcycle {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "manufacturer", nullable = false)
  private Manufacturer manufacturer;

  @Column(name = "model", nullable = false)
  private String model;

  @Column(name = "year_range", nullable = false)
  private String yearRange;

  @Column(name = "image", nullable = false)
  private String image;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private Category category;

  // Engine fields
  @Column(name = "engine_design", nullable = false)
  private String
      engineDesign; // 2-cylinder, 4-stroke, parallel twin or 4 stroke, parallel twin cylinder DOHC,

  // 5 valve per cylinder

  @Column(name = "displacement", nullable = false)
  private int displacement; // cc

  @Column(name = "horse_power", nullable = false)
  private int horsePower; // hp

  @Column(name = "torque", nullable = false)
  private int torque; // Nm

  @Column(name = "bore")
  private int bore; // mm

  @Column(name = "stroke")
  private int stroke; // mm

  @Column(name = "compression_ratio")
  private double compressionRatio; // eg 12.5 -> 12.5:1

  @Column(name = "cooling_system")
  private String coolingSystem; // eg. liquid cooled

  @Column(name = "fuel_consumption", nullable = false)
  private double fuelConsumption; // l/100km -> eg. 4.3l/100km

  @Column(name = "emissions")
  private String emissions; // can be CO2: 100 g/km, or just Euro5

  @Column(name = "fuel_system")
  private String fuelSystem; // injection, carburetor

  @Column(name = "throttle_control")
  private String throttleControl; // Cable throttle or Ride-by-Wire

  @Column(name = "clutch")
  private String clutch; // Cable operated PASC slipper clutch

  @Column(name = "transmission")
  private String transmission; // 6-speed, 5-speed, automatic

  // Chassis
  @Column(name = "weight", nullable = false)
  private int weight; // kg

  @Column(name = "tank_capacity", nullable = false)
  private int tankCapacity; // liters

  @Column(name = "wheelbase")
  private int wheelbase; // eg. 1509mm

  @Column(name = "ground_clearance")
  private int groundClearance; // eg. 233mm

  @Column(name = "frame_design")
  private String
      frameDesign; // e.g. Chromium-Molybdenum-Steel frame using the engine as stressed element,

  // powder coated

  @Column(name = "front_suspension")
  private String frontSuspension; // eg.KYB 43mm USD forks, fully adjustable

  @Column(name = "rear_suspension")
  private String rearSuspension; // KYB mono shock, adjustable preload & rebound

  @Column(name = "front_suspension_travel")
  private int frontSuspensionTravel; // 230 mm

  @Column(name = "rear_suspension_travel")
  private int rearSuspensionTravel; // 210 mm

  @Column(name = "seat_height")
  private String seatHeight; // minimum and maximum set up, like 840mm-860mm. 2 values

  // Wheels
  @Column(name = "wheels_type")
  private String wheelsType; // Spoked wheels with aluminium tubeless rims

  @Column(name = "front_tyre")
  private String frontTyre; // 90/90;

  @Column(name = "rear_tyre")
  private String rearTyre; // 150/70

  @Column(name = "front_wheel_size", nullable = false)
  private int frontWheelSize; // 21""

  @Column(name = "rear_wheel_size", nullable = false)
  private int rearWheelSize; // 18""

  // Brakes
  @Column(name = "front_brake")
  private String frontBrake;

  @Column(name = "rear_brake")
  private String rearBrake;

  @Column(name = "front_disc_diameter")
  private int frontDiscDiameter;

  @Column(name = "rear_disc_diameter")
  private int rearDiscDiameter;

  @Column(name = "abs")
  private boolean abs;

  // Electronics
  @Column(name = "dash_display")
  private String dashDisplay;

  @Column(name = "riding_modes")
  private String ridingModes;

  @Column(name = "traction_control")
  private boolean tractionControl;

  @Column(name = "quick_shifter")
  private String quickShifter;

  @Column(name = "cruise_control")
  private boolean cruiseControl;

  @Column(name = "lighting")
  private String lighting;

  @ManyToMany
  @JoinTable(
      name = "similar_motorcycles",
      joinColumns = @JoinColumn(name = "motorcycle_id"),
      inverseJoinColumns = @JoinColumn(name = "similar_motorcycle_id"),
      uniqueConstraints =
          @UniqueConstraint(columnNames = {"motorcycle_id", "similar_motorcycle_id"}))
  private Set<Motorcycle> similarMotorcycles;

  @OneToMany(mappedBy = "motorcycle", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews;

}
