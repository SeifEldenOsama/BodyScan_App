# BodyScan App: AI for Medical Imaging

The **BodyScan App** is a comprehensive medical imaging application designed to assist in the preliminary diagnosis of several critical conditions using deep learning models. The project integrates multiple specialized AI models into a unified system, providing a robust tool for analyzing medical scans such as X-rays and MRIs.

## Key Features

The application is modular, featuring four distinct AI-powered detection modules, each focused on a specific medical condition:

| Module | Purpose | AI Model Files |
| :--- | :--- | :--- |
| **Chest X-ray Analysis** | Detects the presence of **Pneumonia** from chest X-ray images. | `chestxray.ipynb`, `api_chest.py` |
| **Brain Tumor Detection** | Identifies potential **brain tumors** from MRI or CT scans. | `brain-tumor-detection.ipynb`, `brain_api.py` |
| **Diabetic Retinopathy** | Analyzes retinal images to detect signs of **diabetic retinopathy**. | `diabetic.ipynb`, `diabetic.py` |
| **Fracture Detection** | Determines the presence of **bone fractures** in X-ray images. | `fracture or not.ipynb`, `fracture_api.py` |

## Architecture

The BodyScan App employs a client-server architecture, separating the user interface from the heavy-lifting AI processing:

1.  **Frontend Application (Client):** The user interface is built using **Java** (as indicated by the `.java` source files in the `src/` directory). This application handles user interaction, image selection, and displays the final results.
2.  **AI Backend (Server):** The core AI logic is exposed via a RESTful API built with **Python** and the **Flask** framework. Each detection module has its own dedicated API endpoint (e.g., `/predict` in `api_chest.py`) that handles image processing and model inference.
3.  **Deep Learning Models:** The models are implemented using **Keras/TensorFlow** and are loaded from `.h5` files. They are responsible for analyzing the input image and returning a prediction score and label.

## Project Structure

The repository is organized to separate the application code, AI models, and documentation:

| Directory/File | Description |
| :--- | :--- |
| `src/` | Contains the **Java source code** for the frontend application (e.g., `Main.java`, `ChestXRay.java`, `BrainTumorPage.java`). |
| `AI Models/` | Main directory for all AI-related assets. |
| `AI Models/APIs/` | Contains the **Python Flask API scripts** (`*.py`) for each detection module. |
| `AI Models/*.ipynb` | Contains the **Jupyter Notebooks** used for model training, experimentation, and data analysis for each module. |
| `presentation/` | Contains the project's final presentation, *BodyScan AI for Medical Imaging.pdf*. |
| `.project`, `.classpath` | Configuration files suggesting the use of an **Eclipse IDE** for the Java frontend development. |

## Setup and Usage

Setting up the BodyScan App requires configuring both the Python AI backend and the Java frontend.

### 1. AI Backend Setup (Python/Flask)

The backend must be running to serve predictions to the Java application.

**Prerequisites:**
*   Python 3.x
*   Pip

**Installation:**
You will need to install the necessary Python libraries, including Flask, Keras, and TensorFlow.

```bash
pip install flask numpy pillow tensorflow keras
```

**Running the API:**
Each API file (`api_chest.py`, `brain_api.py`, etc.) needs to be run separately or integrated into a single service gateway.

```bash
# Example: Running the Chest X-ray API
python "AI Models/APIs/api_chest.py"
```

### 2. Frontend Application Setup (Java)

The frontend is a Java application that communicates with the running Python APIs.

**Prerequisites:**
*   Java Development Kit (JDK)
*   An IDE like Eclipse (recommended due to project files)

**Steps:**
1.  Import the project into your Java IDE (e.g., Eclipse) using the provided `.project` and `.classpath` files.
2.  Ensure the application is configured to correctly call the local endpoints exposed by the Flask APIs.
3.  Run the `Main.java` file to launch the application interface.
