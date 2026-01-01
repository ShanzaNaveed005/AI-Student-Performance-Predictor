# AI-Student-Performance-Predictor
Android app to predict student academic performance using AI
# 🎓 AI Student Performance Predictor

An intelligent academic guidance system that combines **On-Device Machine Learning (TensorFlow Lite)** and **Generative AI (Google Gemini 1.5 Flash)** to help students track and improve their performance.

---

## 🚀 Key Features

- **Performance Prediction (Offline AI):** Uses a custom-trained **TensorFlow Lite (TFLite)** regression model to predict academic outcomes based on attendance data.
- **Smart Study Timetable (Generative AI):** Leverages **Google Gemini 1.5 Flash** to generate personalized study schedules based on the student's weak subjects stored in Firebase.
- **AI Student Consultant:** A real-time chatbot powered by Gemini for academic counseling and instant query resolution.
- **Data Visualization:** Interactive bar charts using **MPAndroidChart** to visualize marks and progress.
- **Real-time Synchronization:** Powered by **Firebase Realtime Database** and **Firebase Auth** for secure and instant data management.

---

## 🛠️ Tech Stack

- **Frontend:** Java (Android Studio), XML, Material Design.
- **Backend:** Firebase Realtime Database, Firebase Authentication.
- **Machine Learning:** TensorFlow Lite (Regression Model).
- **Cloud AI:** Google Gemini 1.5 Flash API.
- **Libraries:** MPAndroidChart, Google Guava, Generative AI SDK.

---

## 🏗️ System Architecture

1. **Input:** Student enters attendance and marks.
2. **Local Processing:** TFLite model calculates the performance score locally on the device.
3. **Cloud Processing:** Data is synced to Firebase; Gemini API analyzes the data to generate a custom timetable.
4. **Output:** Visual graphs, AI-generated suggestions, and predictive scores are displayed to the user.

---

## 🔐 Setup & Installation
```bash git clone [https://github.com/ShanzaNaveed005/AIStudentPerformancePredictor.git](https://github.com/Shanza Naveed/AIStudentPerformancePredictor.git)

To run this project, follow these steps:

1. **Clone the repository:**
Setup Firebase:


Create a project on Firebase Console.

Add google-services.json to the app/ directory.

API Key Configuration:

Get an API Key from Google AI Studio.

Add the key to your local.properties file:

Properties

GEMINI_API_KEY=your_api_key_here
Build & Run: Open the project in Android Studio and sync Gradle files.

🛡️ Security Note
This project uses local.properties to manage API keys. Ensure that your local.properties is included in .gitignore to prevent leaking sensitive API credentials.

👤 Author
Shanza Naveed

Roll No: 232201035

Department of Computer Science, BSCS Program

Khan Institute of Computer Science and Information Technology (KICSIT)
   ```bash
   git clone [https://github.com/YourUsername/AIStudentPerformancePredictor.git](https://github.com/YourUsername/AIStudentPerformancePredictor.git)
