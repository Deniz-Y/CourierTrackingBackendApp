# 🚚 Courier Tracking Backend Application

This project is a **Spring Boot RESTful web application** designed for tracking courier locations and logging their entries into Migros stores based on proximity. It serves as a case study and proof-of-concept deployment on **AWS EC2** and uses **AWS RDS PostgreSQL** for data persistence.

## 📌 Case Study Requirements

- Record courier entry to a store if within **100 meters**.
- Prevent re-entries within **1 minute** for the same store.
- Track total distance traveled by a courier.
- Use at least **2 design patterns**.
- Provide a simple way to test the application.

## 🧠 Technologies Used

- Java 17
- Spring Boot 3
- Spring Web, Spring Data JPA, Validation
- PostgreSQL (via AWS RDS)
- Lombok
- Maven
- AWS EC2, AWS RDS
- PM2 (for background service management)
- GitHub
- Postman
- DBeaver
  

## 🧱 Design Patterns

- **Strategy Pattern**: Used for distance calculation (Haversine formula).
- **Builder Pattern**: Used in entity and DTO conversions.

## 🏛️ Architecture

```
+-------------+       HTTPS       +--------------------+       JDBC        +------------------+
|             |  ─────────────▶   |                    |  ─────────────▶   |                  |
|   Client    |                   |   Spring Boot API  |                   |     PostgreSQL   |
| (Postman)   |   REST API Calls  |   (EC2 Instance)   |   Database Conn. |      (RDS)       |
|             |  ◀─────────────   |                    |  ◀─────────────   |                  |
+-------------+     JSON Resp.    +--------------------+                   +------------------+
```


## 📌 API Endpoints
Base Url: # http://13.48.149.150:8080

- Courier Endpoints

| Method | Endpoint             | Description                      |
| ------ | -------------------- | -------------------------------- |
| POST   | `/api/couriers`      | Create a new courier             |
| GET    | `/api/couriers/{id}` | Retrieve a courier by ID         |
| GET    | `/api/couriers`      | Get all couriers                 |
| PUT    | `/api/couriers/{id}` | Update courier information by ID |
| DELETE | `/api/couriers/{id}` | Delete a courier by ID           |

- Courier Location Endpoints
  
| Method | Endpoint                                            | Description                                                 |
| ------ | --------------------------------------------------- | ----------------------------------------------------------- |
| POST   | `/api/courier-locations`                            | Add a new courier location (latitude/longitude + timestamp) |
| GET    | `/api/courier-locations/{courierId}/total-distance` | Get total distance traveled by the courier in meters        |

- Store Endpoints

| Method | Endpoint           | Description                |
| ------ | ------------------ | -------------------------- |
| POST   | `/api/stores`      | Add a new store            |
| GET    | `/api/stores`      | Get all registered stores  |
| GET    | `/api/stores/{id}` | Get store details by ID    |
| PUT    | `/api/stores/{id}` | Update store details by ID |
| DELETE | `/api/stores/{id}` | Delete store by ID         |

- Store Entrance Log Endpoints

| Method | Endpoint                                       | Description                              |
| ------ | ---------------------------------------------- | ---------------------------------------- |
| GET    | `/api/store-entrance-logs`                     | Retrieve all store entrance logs         |
| GET    | `/api/store-entrance-logs/courier/{courierId}` | Get entrance logs for a specific courier |
| GET    | `/api/store-entrance-logs/store/{storeId}`     | Get entrance logs for a specific store   |


🧪 Testing the Application

You can test the RESTful API using Postman or any HTTP client by sending requests.


🚀 Example Requests

# Create a courier:

POST /api/couriers 

Body:

{
  "name": "Deniz Yilmaz",
  "courierNumber": "CR-1234"
}

# Add courier location:

POST /api/courier-locations

Body:

{
  "courierId": 1,
  "latitude": 40.9923307,
  "longitude": 29.1244229,
  "timestamp": "2025-06-30T12:00:00"
}

# Add a store:

POST /api/stores

Body:
{
  "name": "Ataşehir MMM Migros",
  "lat": 40.9923307,
  "lng": 29.1244229
}











