# Hotel Management System

A robust, production-ready Spring Boot application built with Java 17 and PostgreSQL. This system manages room bookings, restaurant orders, payments, and features an integrated AI chatbot for guest assistance.

## Features

- **Booking Engine**: Automated availability checks and price calculations based on stay duration.
- **Order Management**: Full menu item and ordering system for room service and restaurant operations.
- **Payment Processing**: Integrated payment tracking with support for multiple methods (Card, Cash, Online).
- **AI Guest Assistant**: Powered by Groq AI (Llama 3.1) via the `ChatService`.
- **Clean Architecture**: Standard package structure, constructor-based dependency injection, and Lombok-free implementation.

## Tech Stack

- **Backend**: Spring Boot 3.4.2, Spring Data JPA, Spring Validation
- **Database**: PostgreSQL
- **AI**: Groq Cloud API
- **Build Tool**: Maven

## Setup Instructions

1. **Database Configuration**:
   Update `src/main/resources/application.properties` with your PostgreSQL credentials.
   
2. **AI Integration**:
   Ensure the `groq.api.key` property is set in your configuration.

3. **Run Application**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

---
*Developed as a high-performance, maintainable enterprise solution.*