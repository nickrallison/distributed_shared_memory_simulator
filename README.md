# Distributed Shared Memory (DSM) Simulator

## Overview
This project simulates a Distributed Shared Memory (DSM) system, which allows multiple processors to share memory across a distributed environment. The system implements a token-based synchronization mechanism to ensure consistency and mutual exclusion among processors. The simulator is implemented in Java and provides a framework for testing and analyzing the behavior of distributed memory systems.

## Features
- **Token-Based Synchronization**: Implements a token ring mechanism to manage access to shared memory, ensuring mutual exclusion and consistency.
- **Broadcast System**: Facilitates communication between processors using a broadcast system to propagate memory updates.
- **Multiple Tokens Support**: Supports both single and multiple tokens in the token ring for enhanced concurrency.
- **Verbosity Levels**: Provides configurable verbosity levels (NONE, LOW, MEDIUM, HIGH) for detailed logging and debugging.
- **Simulation Framework**: Includes a simulation framework to test and analyze the behavior of the DSM system under different configurations.

## Project Structure
The project is organized into several key classes and files:
- **BroadcastSystem.java**: Manages the broadcast system for communication between processors.
- **BroadcastAgent.java**: Handles message broadcasting and receiving for individual processors.
- **DSM.java**: Implements the Distributed Shared Memory logic, including memory operations and synchronization.
- **LocalMemory.java**: Represents the local memory of each processor.
- **Processor.java**: Simulates the behavior of a processor, including critical section entry and exit.
- **TokenRing.java**: Manages the token ring mechanism for synchronization.
- **TokenRingAgent.java**: Represents an agent in the token ring, responsible for token acquisition and release.
- **Token.java**: Represents a token used in the token ring mechanism.
- **Message.java**: Defines the structure of messages exchanged between processors.
- **Main.java**: The entry point for the simulator, responsible for initializing the system and starting the simulation.

## Key Classes
- **Main.java**: Initializes the system, creates processors, and starts the simulation.
- **Processor.java**: Simulates processor behavior, including critical section management and token handling.
- **DSM.java**: Manages shared memory operations and synchronization.
- **TokenRing.java**: Implements the token ring mechanism for mutual exclusion.
- **BroadcastSystem.java**: Facilitates communication between processors.

## Usage
To run the simulation, follow these steps:
1. **Set Configuration Parameters**:
   - Modify the `Main.java` file to configure the number of processors, token ring activation, verbosity level, and other parameters.
   ```java
   int n = 10; // Number of processors
   boolean tokenRingActive = true; // Activate token ring
   boolean multipleTokens = true; // Allow multiple tokens
   Verbosity verbosity = Verbosity.HIGH; // Set verbosity level
   double messageDelay = 200; // Message delay in milliseconds
   double csDelay = 20; // Critical section delay in milliseconds
   ```

2. **Run the Simulation**:
   - Compile and run the `Main.java` file to start the simulation.
   ```bash
   javac Main.java
   java Main
   ```

3. **Observe Output**:
   - The simulation will output detailed logs based on the configured verbosity level, showing processor activities, token acquisition, and memory updates.

## Testing
The project includes built-in logging and verbosity levels to validate the correctness of the DSM system. You can adjust the verbosity level in `Main.java` to observe detailed logs for debugging and analysis.

## Acknowledgments
- This project was developed as part of a course assignment for CPSC 457.
- Special thanks to the teaching staff for their guidance and support.
