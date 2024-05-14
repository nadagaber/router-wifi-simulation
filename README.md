# router-wifi-simulation

Problem Description:

This project simulates a limited number of devices connected to a router's Wi-Fi using Java threading and semaphore. The router restricts the number of open connections and accepts new connections only when existing ones are released. Devices perform activities such as connecting, performing online tasks, and logging out, with random waiting times between actions.

<br>

Solution Design:

Classes:

  Router: Manages connections, occupies and releases them.
  
  Semaphore: Implements synchronization for managing connections.
  
  Device: Represents individual devices (threads) connecting to the router, performing activities, and disconnecting.
  
  Network: Contains the main method to input maximum connection limit (N) and total number of devices (TC), and device details.

  <br>
  
  
Program Output:
The program generates output logs in a file, simulating the execution order of device threads and their corresponding actions.
