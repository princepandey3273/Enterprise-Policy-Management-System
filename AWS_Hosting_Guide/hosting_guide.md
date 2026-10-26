# ☁️ Hosting your DocIntel App on AWS (Free Tier Guide)

Since you are new to AWS, this guide is written simply. AWS can be scary because there are 100+ services, but for this project, we only need **one** main service: **EC2 (Elastic Compute Cloud)**.

Think of EC2 as a "computer in the cloud" that runs Linux. We will rent this computer for free (using the Free Tier), install Java and PostgreSQL on it, and run your app.

---

## 🛠️ The Big Picture (How it works)
**Your Laptop** $\rightarrow$ **Internet** $\rightarrow$ **AWS EC2 (Linux Server)** $\rightarrow$ **Your App (Backend + DB)**

---

## 🚀 Step-by-Step Guide

### Phase 1: Launching your Server (EC2)
1. **Log in to AWS Console** and search for **EC2**.
2. Click **"Launch Instance"**.
3. **Name**: Call it `DocIntel-Server`.
4. **OS (AMI)**: Choose **Ubuntu 22.04 LTS** (It is the most beginner-friendly Linux).
5. **Instance Type**: Choose `t2.micro` (This is the **Free Tier** one).
6. **Key Pair**: Click "Create new key pair". 
   - Name it `docintel-key`.
   - Download the `.pem` file and **SAVE IT**. You cannot download it again, and without it, you can't enter your server.
7. **Network Settings (Security Group)**: This is like a firewall. You must open these ports:
   - **SSH (22)**: To let you log in.
   - **Custom TCP (8081)**: To let the frontend talk to your backend.
   - **Custom TCP (5432)**: Only if you want to access the DB from your laptop (not recommended for security).
8. Click **Launch Instance**.

---

### Phase 2: Connecting to your Server
Open your terminal (or Git Bash) and run:
```bash
# Change permissions of your key file (crucial for Linux)
chmod 400 docintel-key.pem

# Connect to your server (Replace <Public-IP> with the IP from AWS Console)
ssh -i "docintel-key.pem" ubuntu@<Public-IP>
```

---

### Phase 3: Installing the Software
Once you are inside the black terminal of your AWS server, run these commands one by one:

**1. Update the system:**
```bash
sudo apt update && sudo apt upgrade -y
```

**2. Install Java 17:**
```bash
sudo apt install openjdk-17-jdk -y
```

**3. Install Maven:**
```bash
sudo apt install maven -y
```

**4. Install PostgreSQL & pgvector:**
```bash
sudo apt install postgresql postgresql-contrib -y

# Switch to postgres user to setup DB
sudo -i -u postgres
psql

# Inside the psql prompt, run:
CREATE DATABASE docintel;
\c docintel
CREATE EXTENSION IF NOT EXISTS vector;
\q
exit
```

---

### Phase 4: Moving your code to AWS
The easiest way is to push your code to GitHub and then "clone" it onto the server.

**On the AWS Server:**
```bash
git clone <your-github-repo-url>
cd enterprise-doc-intel/backend
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

---

## ❓ Important Questions & Answers

**Q: My backend is running on AWS, but the frontend on my laptop still says "Connection Error". Why?**
**A:** Because your `script.js` still says `localhost:8081`. "Localhost" means "this current machine". You must change `localhost` to your **AWS Public IP**.
*Change this in `script.js`:*
`const API_BASE = 'http://<YOUR-AWS-PUBLIC-IP>:8081/api/docs';`

**Q: If I close my terminal, will the backend stop?**
**A:** Yes. To keep it running forever, use a tool called `tmux` or `nohup`.
*Run it in background:*
`nohup mvn spring-boot:run &`

**Q: Will I be charged money?**
**A:** As long as you use `t2.micro` and stay within the first 12 months of your account, it is free. But **be careful**: if you create 5 different servers, you will be charged. Stick to one.

**Q: How do I handle the `application.properties` on AWS?**
**A:** Since we added it to `.gitignore`, it won't be on GitHub. You will need to create the file manually on the server:
`nano src/main/resources/application.properties`
(Paste your keys and DB password here, then press `Ctrl+O`, `Enter`, `Ctrl+X`).

---

## ⚠️ Summary Checklist
- [ ] Launch `t2.micro` Ubuntu Instance.
- [ ] Open Port 8081 in Security Group.
- [ ] Install JDK 17, Maven, and PostgreSQL.
- [ ] Install `pgvector` extension in the DB.
- [ ] Clone code $\rightarrow$ build $\rightarrow$ run.
- [ ] Update Frontend `API_BASE` to AWS Public IP.
