<<<<<<< HEAD
This branch delivers Feature 1 of the Real-Time News Application.
It includes a fully working Java web app built with:

Servlet (Jakarta 5)

JSP

Maven (WAR packaging)

Tomcat 10

NewsAPI (Free “everything” endpoint)

The UI auto-refreshes, shows today’s news, and includes a clean, modern layout using plain CSS.

✨ Feature 1 – What’s Included
✔ Fetch Real-Time News

The servlet fetches news from NewsAPI using the /v2/everything endpoint.

✔ Auto-Refresh

The page refreshes automatically every 30 seconds to show fresh headlines.

✔ Today’s News Only

Backend filters news by today’s date using:

from=<today>&to=<today>


If today’s news is empty, it falls back to last 7 days.

✔ Clean Modern UI

Index page uses:

Card-based layout

Hover animations

Centered header

Responsive layout

Timestamp indicator

✔ Externalized API Key

config.properties stores the API key:
https://newsapi.org/

NEWS_API_KEY=<your_key>
NEWS_URL=https://newsapi.org/v2/everything?q=

✔ Error Handling

If no news found → clean fallback text
If API returns error → servlet does not crash

📂 Project Structure
src/main/java/com/vikas/news/NewsServlet.java
src/main/resources/config.properties
src/main/webapp/index.jsp
src/main/webapp/WEB-INF/web.xml
pom.xml

🛠 Installation & Setup (Ubuntu)
1. Install Java 17 and Maven
=======
## 📌 Requirements to Install

### **1. Install Java 17**

```bash
>>>>>>> feature-2
sudo apt update
sudo apt install openjdk-17-jdk -y
java -version

### **2. Install Maven**

```bash
sudo apt install maven -y
mvn -version

### **3. Install Tomcat 10 (Manual Install)**

```bash
cd /opt
sudo wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.30/bin/apache-tomcat-10.1.30.tar.gz
sudo tar -xzf apache-tomcat-10.1.30.tar.gz
sudo mv apache-tomcat-10.1.30 tomcat10
sudo chmod +x /opt/tomcat10/bin/*.sh
```

Start Tomcat:

```bash
sudo /opt/tomcat10/bin/startup.sh
```

Stop Tomcat:

```bash
sudo /opt/tomcat10/bin/shutdown.sh
```

---

## 📌 Build Instructions

Inside the project folder:

```bash
mvn clean package
```

This will generate the artifact:

```
target/news-app.war
```

---

## 📌 Deployment Instructions (Tomcat 10)

### **1. Remove old deployment**

```bash
sudo rm -rf /opt/tomcat10/webapps/news-app
sudo rm -f /opt/tomcat10/webapps/news-app.war
```

### **2. Copy new WAR**

```bash
sudo cp target/news-app.war /opt/tomcat10/webapps/
```

### **3. Restart Tomcat**

```bash
sudo /opt/tomcat10/bin/shutdown.sh
sudo /opt/tomcat10/bin/startup.sh
```

---

## 📌 Configure API Key

Edit the config file:

```
src/main/resources/config.properties
```

Add:

```
NEWS_API_KEY=YOUR_KEY
NEWS_URL=https://newsapi.org/v2/everything?
```

---

## 📌 Access Application

```
http://<server-ip>:8080/news-app/news
```

---

## ✔ Feature 2 Includes

* Search (AJAX)
* Categories (India, Sports, Tech, Business, etc.)
* Infinite scroll
* Trending tags
* Modern UI
* Dark mode
* News cards with images & descriptions

