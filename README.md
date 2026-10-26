# Enterprise Doc Intel (PDF Chatbot)

I built this project to solve a common problem: reading long PDF documents. Instead of searching through a 50-page manual for one answer, I created a system where you can just upload the PDF and ask questions in plain English.

The core idea here is using RAG (Retrieval-Augmented Generation). Basically, the app doesn't just guess the answer; it finds the exact paragraph in your document and uses that as context to give a correct response.

## How it works
I kept the flow simple:
1. **Processing**: The app takes the PDF and breaks it into small chunks of text.
2. **Embeddings**: It converts these chunks into vectors (numerical representations of meaning) using an AI model.
3. **Storage**: These vectors are stored in a PostgreSQL database with the pgvector extension.
4. **Retrieval**: When you ask a question, the app finds the most similar text chunks from the DB and sends them to Google Gemini AI to generate the final answer.

## Tech Stack
- **Backend**: Java 17 and Spring Boot (for the API).
- **Database**: PostgreSQL + pgvector (for storing and searching embeddings).
- **AI**: Google Gemini API (for generating embeddings and answers).
- **Frontend**: Simple HTML, CSS, and JavaScript.
- **PDF Parsing**: Apache PDFBox.

## Getting it running
I have written all the setup steps in the [setup.md](./setup.md) file. If you have Java 17 and PostgreSQL installed, it should be easy to start.

## What I learned
This was a great way for me to understand how Vector Databases actually work. The most challenging part was getting the document chunking right so that the AI didn't lose the context of the paragraphs. I also learned how to handle CORS and port conflicts while connecting the frontend and backend.
