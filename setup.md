# Setup and Execution Guide

This guide will walk you through the process of setting up and running the Enterprise Document Intelligence Platform.

## 📋 Prerequisites
- **Java 17** (JDK 17 or higher)
- **Maven** (for backend build)
- **PostgreSQL** (with `pgvector` installed)
- **Google AI API Key** (from Google AI Studio)

## ⚙️ Step-by-Step Installation

### 1. Database Configuration
1. Install PostgreSQL on your system.
2. Install the `pgvector` extension. On most systems, you can run:
   ```sql
   CREATE EXTENSION IF NOT EXISTS vector;
   ```
3. Create the database:
   ```sql
   CREATE DATABASE docintel;
   ```

### 2. Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd enterprise-doc-intel/backend
   ```
2. Open `src/main/resources/application.properties`.
3. Update the following values:
   - `spring.datasource.username`: Your PostgreSQL username (default: `postgres`).
   - `spring.datasource.password`: Your PostgreSQL password.
   - `google.ai.api.key`: Your Gemini API key.

4. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The server will start at `http://localhost:8080`.

### 3. Frontend Setup
1. No installation is required for the frontend.
2. Navigate to the `enterprise-doc-intel/frontend/` folder.
3. Open `index.html` in your preferred web browser.

## 🧪 Testing the Project

### Manual End-to-End Test
1. **Start the Backend**: Ensure the Spring Boot app is running.
2. **Open the Frontend**: Load `index.html` in the browser.
3. **Upload PDF**: 
   - Select a PDF document (e.g., a company handbook or technical manual).
   - Click **"Upload & Index"**.
   - Wait for the status message: *"Document uploaded and indexed successfully!"*.
4. **Query the AI**:
   - Type a specific question based on the content of the uploaded PDF in the chat box.
   - Click **"Send"**.
   - Verify that the response is relevant and based on the document context.

### Troubleshooting
- **CORS Error**: If the frontend cannot talk to the backend, ensure the `@CrossOrigin(origins = "*")` annotation is present in `DocumentController.java`.
- **DB Connection Error**: Verify that the PostgreSQL service is running and the credentials in `application.properties` are correct.
- **Vector Search Error**: Ensure the `vector` extension was successfully installed in the `docintel` database.
