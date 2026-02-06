// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;


public class Climber extends SubsystemBase {
  private final TalonFX motor = new TalonFX(0);
  private CommandXboxController controller;
  private int direction;
  private double target;

  private static final double SPEED = 0.25;

  /** Creates a new Climber. */
  public Climber() {
    this.controller = null;
    this.target = this.direction = 0;
  }

  public void setController(CommandXboxController controller) {
    this.controller = controller;
  }

  public void setTarget(double target) {
    this.target = target;

    if (target < getPosition()) {
      direction = -1;
      motor.set(-SPEED);
    } else if (target > getPosition()) {
      direction = 1;
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
    if (direction < 0) {
      if (getPosition() <= target) {
        motor.set(0);
        direction = 0;
      }
    } else if (direction > 0) {
      if (getPosition() >= target) {
        motor.set(0);
        direction = 0;
      }
    }
  }
}
