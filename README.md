# AR-Navigation
AR based navigation system for Adama science and technology university


# ASTU Smart Campus Navigation & Event Assistant

A smart Android application built using **Kotlin**, **Jetpack Compose**, **ARCore**, and **Firebase** to help students and visitors navigate Adama Science and Technology University (ASTU) campus, view real-time events, and interact with a chatbot for quick assistance.

---

## 🚀 Features

- 📍 **AR-Based Campus Navigation**
  - Uses **ARCore Geospatial API** to render real-world navigation
  - Guides users with AR anchors to buildings or event locations

- 🧭 **Firebase Integration**
  - Firebase Realtime Database for dynamic location and event data
  - Firebase Authentication with Email Verification
  - Admin panel to post and manage events or FAQs

- 💬 **AI Chatbot**
  - Hybrid chatbot using OpenAI API (via OpenRouter)
  - Answers general queries and Firebase FAQs

- 🔐 **User Authentication**
  - Signup/Login with email & password
  - Email verification before login
  - Guest mode available

- 🗂 **Admin Features**
  - Add/Edit/Delete FAQs from the app
  - Post and manage campus events

---

## 📸 Screenshots

> Add screenshots of:
- Home Screen
- ![image alt](https://github.com/crackasuper/AR-Navigation/blob/435188a6c7b0017b3e829e03c8f644fad210f5e6/Screenshot%20from%202024-12-15%2022-38-05.png)
- AR Navigation
- Chatbot
- Admin Panel

---

## 🛠 Tech Stack

| Component      | Technology                  |
|----------------|-----------------------------|
| Frontend       | Kotlin, Jetpack Compose     |
| AR Engine      | ARCore + Geospatial API     |
| Backend        | Firebase Realtime Database  |
| Auth           | Firebase Authentication     |
| AI Assistant   | OpenAI (via OpenRouter API) |
| UI Framework   | Jetpack Compose             |

---

## 🧪 Methodology

We followed the **Agile Software Development** methodology throughout the project lifecycle.  
Below is the Agile workflow used:

![Agile Diagram](./assets/agile_diagram.png)

---

## 📦 Project Structure



app/
├── ui/ # Composable Screens (Login, Signup, Home, etc.)
├── ar/ # ARCore and Geospatial logic
├── viewmodel/ # ViewModels (AuthViewModel, ChatBotViewModel)
├── data/ # Firebase and Repository code
├── chatbot/ # OpenAI/Firebase hybrid chatbot
└── MainActivity.kt

yaml
Copy
Edit

---

## 🔧 Setup Instructions

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/astu-smart-campus-app.git
Open in Android Studio

Add your Firebase google-services.json in the app/ directory

Replace API keys in ChatBotViewModel (OpenAI/OpenRouter)

Build and run on ARCore-supported Android device

📄 License
This project is licensed under the MIT License.
Feel free to use and improve it for academic or research purposes.

🙌 Authors
Sadem Hussen – LinkedIn | GitHub

vbnet
Copy
Edit
