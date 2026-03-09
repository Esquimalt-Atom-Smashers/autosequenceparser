# FTC Instant Auto (Prototype)

A Java-based architecture for FTC robots designed to parse text files into AUTONOMOUS commends (RoadRunner Action to be added) sequences and robot configurations. This system replaces hard-coded Android Studio Auto codes with a decoupled, registry-based system, allowing for rapid iteration and configuration without code redeploys.

## Key Features

- **Text-Based Configuration**: Manage robot constants, goal positions, and intake settings through simple `.txt` files.
- **Registry-Based Actions**: Decoupled "Mini Actions" (base commands) and "Big Actions" (composed sequences) managed via a central registry.
- **Validation & Dry Run**: A robust parser that validates syntax, types, and parameter counts during initialization, reporting errors via Telemetry to prevent OpMode crashes.
- **Hierarchical Loading**: Supports base configurations (e.g., `GeneralRobotSettings`) with autonomous-specific overrides.
- **Comment Support**: Clean text files using `//` comment style.

## Architecture

The project is divided into three main layers:

### 1. Definition Layer (examples in :pureJava/src/.../textfiles/)
- **MetaField<T>**: Interface for complex data types (e.g., `Pose2d`, `IntakeSetting`) that defines parameter types and identifiers.
- **MetaAction**: Interface for creating executable Actions from parameterized strings.
  - Mini Action: class for making registerable primitive actions (e.g., `GO.TO.POSE2D`).
  - Big Action: class for making registerable composite actions (e.g., `BLUE.LOAD.INTAKE`).
  - MetaActionRegistry: registry for both Mini Actions and Big Actions, through both within the code itself and parsing `pureJava/src/.../textfiles/MetaActionSettings`
 

### 2. Registry Layer
- **MetaFieldRegistry**: Maps field names (e.g., `RedGoalPose`) to specific data types and default values.
- **MetaActionRegistry**: A factory for both primitive Mini Actions and user-defined Big Actions.

### 3. Parser Engine
- **ConfigParser**: Handles file I/O, string cleaning, and strict type validation (e.g., ensuring boolean values aren't mistakenly parsed from numbers).
- **AutoParser**: Orchestrates the loading of the robot's state, scanning for `[ACTIVE]` autonomous routines and validating the required `Starting` fields.

## File Structure

- `pureJava/src/.../actions/`: Core action interfaces and the `ActionManager`.
- `pureJava/src/.../configs/`: Configuration parsing logic and type definitions.
- `pureJava/src/.../textfiles/`:
  - `GeneralRobotSettings`: Base robot constants.
  - `MetaActionSettings`: Definitions for composite "Big Actions".
  - `[ACTIVE]DemoAuto`: An example autonomous routine.

## Usage

### Defining a Big Action
In `MetaActionSettings`:
```
BLUE.LOAD.INTAKE={
    GO.TO.POSE2D(65,-20,270),
    GO.TO.POSE2D(65,-65,270)
}
```

### Creating an Autonomous Routine
Create a file named `[ACTIVE]MyRoutine`:
```
Title=My Epic Auto
Starting=RED.FAR
BLUE.LOAD.INTAKE
GO.TO.POSE2D(0,0,0)
```

### Running the System
Run `AutoRuntime.java` to:
1. Scan for active autonomous files.
2. Select your routine.
3. Validate all configurations and actions.
4. Execute the sequence upon user confirmation.

## Requirements
- Java 8+
- (Advised) Android Studio (Panda 2 version)
