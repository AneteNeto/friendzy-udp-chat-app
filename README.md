<!-- Banner -->
<p align="center">
  <img src="/src/assets/FrienDzy5.png" alt="Friendzy — UDP Client/Server (Java, MVC)" width="600" heigh="200">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17+-red?logo=openjdk" />
  <img src="https://img.shields.io/badge/Protocol-UDP-blue?logo=opnsense" />
  <img src="https://img.shields.io/badge/Architecture-MVC-8A2BE2" />
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-green" /></a>
</p>

---

# Friendzy — UDP Client/Server (Java, MVC)

**Friendzy** is a **client/server chat application** built in **Java** using **UDP** communication, a **custom application-layer protocol**, and a clean **MVC architecture** with a **Swing interface**.  
The project implements user registration, login, friend management, and both **private** and **broadcast** messaging.

> 🎥 **Demo Video:** [Watch on YouTube](https://youtu.be/SV7l7AY5wbk)  

---

## ✨ Features

- Custom UDP application protocol (commands & responses)  
- Independent client and server with Java DatagramSocket  
- Clear MVC architecture for maintainability  
- Swing-based UI (client)  
- User authentication and session control  
- Friend requests (send, accept, remove)  
- Private and broadcast messages  
- View the last 10 received messages  

---

## 🗂️ Project Structure
src/
├─ clientViews/ # Swing GUI (client)
├─ controllers/ # ClientController, Serveur, SessionController
├─ models/ # User, Ami, Message
└─ utils/ # ConstUtils (constants and protocol)

## ⚙️ Requirements

- **Java JDK 17+**
- **Ant** or **NetBeans** (optional, `build.xml` provided)
- OS: Windows, Linux or macOS



📜 License

This project is licensed under the MIT License

