# JDBC Flight Manager (3NF & SQL Injection Defense)

## 1. Repository & Project Overview
A normalized (3NF) MySQL and Java JDBC application for managing airport flight arrivals, demonstrating batch updates, parameterized `INNER JOIN` queries, and defense against SQL Injection attacks.

## 2. Engineering Team Details
* **Role A (Student A - `SimpleFlightUpdater.java`):** [Amit Asaf - 207659087]
* **Role B (Student B - `AdvancedFlightDispatcher.java`):** [Oran Cohen - 324155845]

## 3. Database Architecture & Normalization (3NF)
The raw flat CSV dataset (`arrived_flights.csv`) violated 3NF due to transitive dependencies (`id -> flight_number -> from_city`). It was decomposed into two normalized tables:
* **`FlightRoutes` (Parent Table):** Stores unique `flight_number` (Primary Key) and `from_city`.
* **`Arrivals` (Child Table):** Stores `id` (Auto-Increment PK), `flight_number` (Foreign Key referencing `FlightRoutes(flight_number)` with `ON DELETE CASCADE`), `scheduled`, `delay_status`, and `terminal`.

```mermaid
erDiagram
    FlightRoutes ||--o{ Arrivals : "identifies route"
    FlightRoutes {
        VARCHAR(50) flight_number PK
        VARCHAR(100) from_city
    }
    Arrivals {
        INT id PK
        VARCHAR(50) flight_number FK
        VARCHAR(50) scheduled
        VARCHAR(50) delay_status
        VARCHAR(10) terminal
    }