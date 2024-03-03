# SD - Distributed Computing for Scalable Function Execution

## Table of Contents

1. [Overview](#overview)
2. [Key Features](#key-features)
3. [Dependencies](#dependencies)
4. [Cloning the Repository](#cloning-the-repository)
5. [Compiling and Running](#compiling-and-running)
6. [Group's Report](#groups-report)
7. [Conclusion](#conclusion)
8. [Developed by](#developed-by)

## Overview

The project for the subject Sistemas Distribuídos (Distributed Systems) serves as an exploration into the practical implementation of cloud computing services with a focus on Function-as-a-Service (FaaS) functionality. This endeavor is conducted as part of the First Semester, Third Year curriculum in Software Engineering at the University of Minho.

In this project, we aim to design and develop a cloud computing service capable of executing user-defined functions in a distributed environment. The core functionality revolves around enabling clients to submit task code for execution on remote servers, with an emphasis on efficient resource utilization and scalability.

For more information about this project, including detailed implementation and analysis, please refer to the [Project Report](docs/SD.pdf).

## Key Features

- `Function-as-a-Service (FaaS) Capability:` The system allows users to submit task code to be executed as functions on remote servers, abstracting away infrastructure management and enabling rapid development and deployment of applications.

- `User Authentication and Registration:` Users are required to authenticate themselves before interacting with the service, ensuring security and access control.

- `Resource Management:` The system efficiently manages available resources, particularly memory, to ensure optimal utilization and prevent resource exhaustion.

- `Task Execution:` Clients can submit task code along with memory requirements, and the system executes these tasks on available servers, returning results or error messages upon completion.

- `Status Monitoring:` Users can query the current status of the service, including available memory and the number of pending tasks in the queue, providing insights into system performance and utilization.

- `Advanced Task Submission:` Clients can submit multiple task requests without waiting for previous responses, improving system responsiveness and user experience.

- `Task Execution Order:` The system guarantees a fair and efficient order of task execution, preventing tasks from waiting indefinitely in the queue.

- `Distributed Implementation:` The system is designed to be distributed, with a central server managing task queues and multiple worker servers responsible for task execution. This architecture enhances scalability and fault tolerance.

- `Communication Protocol:` The system utilizes a custom binary communication protocol over TCP sockets to ensure efficient and reliable communication between client and server components.

- `Client Library and User Interface:` The system provides a Java client library and user interface for interacting with the service, enabling seamless integration into client applications and facilitating testing and demonstration.

## Dependencies

If you're using a Java-friendly IDE like IntelliJ IDEA, there are no external dependencies to worry about. However, if you're working with a different IDE or manually compiling the project, you may need to resolve dependencies manually.

## Cloning the Repository

To clone the repository, run the following command in your terminal:

```
$ git clone https://github.com/JoaoCoelho2003/SD.git
```

Once cloned, navigate to the repository directory using the cd command:

```
$ cd SD
```

## Compiling and Running

To ensure the program functions as intended, follow these steps:

1. Navigate to the `src` folder in your terminal or command prompt.

2. To set up the main server:
   - Go to the `Servidor` folder.
   - Compile and run the `Server.java` file.

3. For the client:
   - Go to the `Cliente` folder.
   - Compile and run the `Client.java` file.

4. For the worker server:
   - Go to the `Trabalhador` folder.
   - Compile and run the `WorkerServer.java` file.
   - Ensure to add an argument in your IDE representing the memory available for the worker. This argument should be a single digit representing the memory in MB. For example, to allocate 4000 MB, simply add `4000` as the argument.


## Group's Report

The group has prepared a comprehensive report detailing every aspect of the project, including design decisions, implementation details, system architecture, and more. This report serves as a comprehensive guide to understanding the project's development process and the rationale behind various choices made by the group.

For a detailed exploration of the project, including in-depth explanations of each component and their interactions, please refer to the [Group's Report](reports/relatorioSD_Grupo39.pdf).


## Conclusion

We believe that delving into this project in the realm of distributed systems and cloud computing has been both enlightening and fulfilling for you. Throughout this endeavor, we've ventured into the intricacies of object-oriented programming, threading, network communication, and more, gaining valuable insights into the complexities of building scalable and robust systems.

If you have any inquiries, recommendations, or feedback regarding our project, please don't hesitate to reach out. Your insights are invaluable in our continuous pursuit of excellence. Happy coding!

## Developed by

**A100596** João Coelho

**A100692** José Rodrigues

**A100750** Duarte Araújo

**A100754** Rafael Peixoto



