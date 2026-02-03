# BodyScan App: AI-Powered Medical Imaging Diagnostics 

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Python](https://img.shields.io/badge/Python-3.8%2B-blue.svg)](https://www.python.org/)
[![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://www.oracle.com/java/)

The **BodyScan App** is a professional-grade medical imaging application designed to assist healthcare professionals in the preliminary diagnosis of critical conditions. By leveraging state-of-the-art deep learning models, the application provides a unified interface for analyzing various types of medical scans, including X-rays and MRIs.

---

## 🌟 Key Features

The application is built with a modular architecture, supporting four specialized diagnostic modules:

| Module | Diagnostic Focus | Modality |
| :--- | :--- | :--- |
| **Chest X-ray** | Pneumonia Detection | X-ray |
| **Brain Tumor** | Tumor Identification | MRI/CT |
| **Diabetic Retinopathy** | Retinal Analysis | Fundus Photography |
| **Fracture Detection** | Bone Fracture Identification | X-ray |

---

## 🏗️ System Architecture

The project follows a modern **Client-Server Architecture** to ensure scalability and separation of concerns:

1.  **Frontend (Java/JavaFX):** A cross-platform desktop application that provides an intuitive user interface for image uploading and result visualization.
2.  **Backend (Python/Flask):** A RESTful API service that handles complex image processing and deep learning inference using TensorFlow/Keras.
3.  **AI Engine:** Pre-trained Convolutional Neural Networks (CNNs) optimized for medical image classification.

---

## 📂 Project Structure

```text
BodyScan_App/
├── ai_models/          # Python Backend & AI Models
│   ├── app.py          # Unified Flask API Service
│   ├── requirements.txt # Python Dependencies
│   └── APIs/           # Legacy individual API scripts
├── src/                # Java Frontend Source Code
│   └── BodyScan/       # JavaFX Application Logic
├── presentation/       # Project Documentation & Slides
├── .gitignore          # Git exclusion rules
└── README.md           # Project Documentation
```

---

## 🚀 Getting Started

### 1. Backend Setup (Python)

Ensure you have Python 3.8+ installed.

```bash
# Navigate to the backend directory
cd ai_models

# Install dependencies
pip install -r requirements.txt

# Start the API service
python app.py
```

The backend will be available at `http://localhost:5000`. You can check the health status at `http://localhost:5000/health`.

### 2. Frontend Setup (Java)

**Prerequisites:**
- Java Development Kit (JDK) 11 or higher.
- JavaFX SDK.

**Running the App:**
1. Import the project into your preferred IDE (IntelliJ IDEA, Eclipse, or VS Code).
2. Ensure the JavaFX libraries are added to your project's build path.
3. Run `src/BodyScan/Main.java`.

---

## 🛠️ API Documentation

The backend exposes a unified endpoint for all diagnostic modules:

- **Endpoint:** `POST /predict/<model_type>`
- **URL Parameters:** `model_type` (one of: `chest`, `brain`, `diabetic`, `fracture`)
- **Body:** `multipart/form-data` with a `file` field containing the image.

**Example Response:**
```json
{
  "model": "chest",
  "prediction": "Pneumonia",
  "confidence": 98.5,
  "raw_score": 0.985
}
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
