const API_BASE = 'http://localhost:8081/api/docs';

// Add listener to enable upload button and show filename when file is selected
document.addEventListener('DOMContentLoaded', () => {
    const fileInput = document.getElementById('fileInput');
    if (fileInput) {
        fileInput.addEventListener('change', function() {
            const btn = document.getElementById('uploadBtn');
            const display = document.getElementById('fileNameDisplay');

            if (this.files && this.files.length > 0) {
                btn.disabled = false;
                display.innerText = 'Selected: ' + this.files[0].name;
                display.classList.add('file-selected');
            } else {
                btn.disabled = true;
                display.innerText = 'No file selected';
                display.classList.remove('file-selected');
            }
        });
    }
});

async function uploadFile() {

    const fileInput = document.getElementById('fileInput');
    const status = document.getElementById('uploadStatus');
    const uploadBtn = document.getElementById('uploadBtn');

    if (!fileInput.files[0]) {
        alert('Please select a file first!');
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    status.innerText = 'Uploading and indexing... please wait...';
    uploadBtn.disabled = true;

    try {
        const response = await fetch(`${API_BASE}/upload`, {
            method: 'POST',
            body: formData
        });
        const result = await response.text();
        status.innerText = result;
    } catch (error) {
        status.innerText = 'Upload failed: ' + error.message;
    } finally {
        uploadBtn.disabled = false;
    }
}

async function askQuestion() {
    const input = document.getElementById('userInput');
    const chatWindow = document.getElementById('chatWindow');
    const question = input.value;

    if (!question) return;

    // Add user message to chat
    appendMessage('user', question);
    input.value = '';

    try {
        // Show a temporary "thinking" message
        const thinkingId = 'thinking-' + Date.now();
        appendMessage('bot', '...', thinkingId);

        const response = await fetch(`${API_BASE}/ask?q=${encodeURIComponent(question)}`);
        const result = await response.text();

        // Replace thinking message with actual result
        const thinkingMsg = document.getElementById(thinkingId);
        if (thinkingMsg) {
            thinkingMsg.querySelector('.message-content').innerText = result;
        } else {
            appendMessage('bot', result);
        }
    } catch (error) {
        appendMessage('bot', 'Error: ' + error.message);
    }
}

function appendMessage(sender, text, id = null) {
    const chatWindow = document.getElementById('chatWindow');

    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}`;
    if (id) messageDiv.id = id;

    const avatarDiv = document.createElement('div');
    avatarDiv.className = sender === 'bot' ? 'bot-avatar' : 'user-avatar';
    avatarDiv.innerHTML = sender === 'bot' ? '<i class="fas fa-robot"></i>' : '<i class="fas fa-user"></i>';

    const contentDiv = document.createElement('div');
    contentDiv.className = 'message-content';
    contentDiv.innerText = text;

    messageDiv.appendChild(avatarDiv);
    messageDiv.appendChild(contentDiv);

    chatWindow.appendChild(messageDiv);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}
