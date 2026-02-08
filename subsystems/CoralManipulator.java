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
    AUTO,
    STOP;
  }

  /** Creates a new CoralManipulator. */
  public CoralManipulator() {
    this.state = ManipulatorState.AUTO;
  }

  // Cycle through each control state
  public void toggleControl() {
    switch (state) {
      case AUTO:
        state = ManipulatorState.FORCE;
        break;
      case FORCE:
        state = ManipulatorState.STOP;
        break;
      case STOP:
        state = ManipulatorState.AUTO;
        break;
    }
  }

  @Override
  public void periodic() {
    switch (state) {
      case AUTO:
        if (sensorRear.get()) {
          motorLeft.set(SPEED);
          motorRight.set(-SPEED);
        }

        if (sensorFront.get()) {
          motorLeft.set(0);
          motorRight.set(0);
        }
      case FORCE:
        if (!sensorRear.get() && !sensorFront.get()) {
          motorLeft.set(SPEED);
          motorRight.set(-SPEED);
        } else {
          motorLeft.set(0);
          motorRight.set(0);

          // Uncomment following line to reset to AUTO after detection stops
          // state = ManipulatorState.AUTO;
        }
      case STOP:
        motorLeft.set(0);
        motorRight.set(0);
    }
  }
}
