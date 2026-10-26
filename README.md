# 🚀 Enterprise Doc Intel (PDF Chatbot)

Hi! This is my project where I tried to build an AI-powered system that can actually "read" your PDF documents and answer questions from them. Instead of just a normal chatbot, I used something called **RAG (Retrieval-Augmented Generation)** so that the AI doesn't give random answers but gives answers based on the document you upload.

## 🧐 What does this actually do?
Basically, you upload a PDF (like an employee handbook or a technical manual), and the app:
1. Breaks the PDF into small a pieces (chunks).
2. Converts those pieces into "numbers" (embeddings) using AI.
3. Stores these numbers in a special database called **PostgreSQL with pgvector**.
4. When you ask a question, it finds the most relevant piece of text from the DB and sends it to **Google Gemini AI** to get a proper answer.

## 🛠️ Tech I used
I wanted to keep it simple but powerful, so I used:
- **Backend**: Java 17 with Spring Boot (for the API and logic).
- **Database**: PostgreSQL + `pgvector` (this is the most important part for storing the vector embeddings).
- **AI**: Google Gemini API (to handle the embeddings and the final answering).
- **Frontend**: Simple HTML, CSS, and JavaScript (no heavy frameworks, just pure web).
- **PDF Tool**: Apache PDFBox to extract text from files.

## 📂 How it's organized
- `backend/`: All the Java code, controllers, and services.
- `frontend/`: The dashboard UI and the JS logic to talk to the backend.
- `setup.md`: I wrote a full guide here on how to run this on your own machine.

## 🚀 How to run it?
I have already written all the steps in the **[setup.md](./setup.md)** file. Just follow that and it should work. Make sure you have:
- Java 17 installed.
- PostgreSQL with the `vector` extension.
- A Google AI API key.

## 💡 What I learned from this
While building this, I learned how Vector Databases work and how the AI can be "grounded" with real data so it doesn't hallucinate. It was a bit challenging to get the port and database settings right, but it was a great learning experience!
