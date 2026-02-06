// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralManipulator extends SubsystemBase {
  private final TalonFX motorLeft = new TalonFX(0);
  private final TalonFX motorRight = new TalonFX(1);
  private final DigitalInput sensorRear = new DigitalInput(2);
  private final DigitalInput sensorFront = new DigitalInput(3);
  private ManipulatorState state;

  private static final double SPEED = 0.25;

  private enum ManipulatorState {
    FORCE,
    AUTO;
  }

  /** Creates a new CoralManipulator. */
  public CoralManipulator() {
    this.state = ManipulatorState.AUTO;

    // TODO: Invert a motor (manually place negative values for the right motor)
  }

  public void toggleControl() {
    if (state == ManipulatorState.FORCE) {
      state = ManipulatorState.AUTO;
    } else {
      state = ManipulatorState.FORCE;
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (state == ManipulatorState.FORCE) {
      if (!sensorRear.get() && !sensorFront.get()) {
        motorLeft.set(SPEED);
        motorRight.set(-SPEED);
      } else {
        motorLeft.set(0);
        motorRight.set(0);
      }
    } else {
      if (sensorRear.get()) {
        motorLeft.set(SPEED);
        motorRight.set(-SPEED);
      }

      if (sensorFront.get()) {
        motorLeft.set(0);
        motorRight.set(0);
      }
    }
  }
}
