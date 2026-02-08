// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class Climber extends SubsystemBase {
  private final TalonFX motor = new TalonFX(0);
  private ClimberDirection direction;
  private ClimberPreset preset;
  private double target;

  // Up & down are relative to x-axis; up = positive y
  public enum ClimberDirection {
    ROTATE_UP,
    ROTATE_DOWN,
    STOP;
  }

  // Placeholder enum (or can be converted to array) of preset motor values
  public enum ClimberPreset {
    QUARTER_DOWN(-0.25),
    EIGHTH_DOWN(-0.125),
    ZERO(0),
    EIGHTH_UP(0.125),
    QUARTER_UP(0.25);

    private double rotations;

    ClimberPreset(double rotations) {
      this.rotations = rotations;
    }

    public double getRotations() {
      return rotations;
    }
  }

  private static final double SPEED = 0.25;

  /** Creates a new Climber. */
  public Climber() {
    this.target = 0;
  }

  // Cycles the preset; subject to change
  public void cyclePreset() {
    switch (preset) {
      case QUARTER_DOWN:
        preset = ClimberPreset.EIGHTH_DOWN;
        motor.set(preset.getRotations());
        break;
      case EIGHTH_DOWN:
        preset = ClimberPreset.ZERO;
        motor.set(preset.getRotations());
        break;
      case ZERO:
        preset = ClimberPreset.EIGHTH_UP;
        motor.set(preset.getRotations());
        break;
      case EIGHTH_UP:
        preset = ClimberPreset.QUARTER_UP;
        motor.set(preset.getRotations());
        break;
      case QUARTER_UP:
        preset = ClimberPreset.QUARTER_DOWN;
        motor.set(preset.getRotations());
        break;

    }
  }

  public void setTarget(double target) {
    this.target = target;

    if (target < getPosition()) {
      direction = ClimberDirection.ROTATE_DOWN;
      motor.set(-SPEED);
    } else if (target > getPosition()) {
      direction = ClimberDirection.ROTATE_UP;
      motor.set(SPEED);
    }
  }

  public void movePosition(double diff) {
    setTarget(getPosition() + diff);
  }

  public double getPosition() {
    return motor.getPosition().getValueAsDouble();
  }

  @Override
  public void periodic() {
    switch (direction) {
      case ROTATE_DOWN:
        if (getPosition() <= target) {
          motor.set(0);
          direction = ClimberDirection.STOP;
        }
        break;
      case ROTATE_UP:
        if (getPosition() >= target) {
          motor.set(0);
          direction = ClimberDirection.STOP;
        }
        break;
      case STOP:
        motor.set(0); // Optional; motor should already be stopped before this point
        break;
    }
  }
}
