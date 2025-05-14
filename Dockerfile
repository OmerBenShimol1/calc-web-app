# ✅ Base: Jenkins LTS (Debian)
FROM jenkins/jenkins:lts

# 🔁 Switch to root to install system packages
USER root

# ✅ Install Java 17, Maven, Docker CLI
RUN apt-get update && \
    apt-get install -y \
        openjdk-17-jdk \
        maven \
        curl \
        gnupg \
        lsb-release \
        ca-certificates && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
    https://download.docker.com/linux/debian $(lsb_release -cs) stable" | \
    tee /etc/apt/sources.list.d/docker.list > /dev/null && \
    apt-get update && \
    apt-get install -y docker-ce-cli && \
    apt-get clean

# 📦 הוסף את Jenkins לקבוצת docker (חשוב אם אתה עובד עם docker.sock)
RUN usermod -aG docker jenkins

# 👤 חזור למשתמש Jenkins
USER jenkins
